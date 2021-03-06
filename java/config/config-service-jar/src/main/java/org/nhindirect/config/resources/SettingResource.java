/* 
Copyright (c) 2010, NHIN Direct Project
All rights reserved.

Authors:
   Greg Meyer      gm2552@cerner.com
 
Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer 
in the documentation and/or other materials provided with the distribution.  Neither the name of the The NHIN Direct Project (nhindirect.org). 
nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS 
BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE 
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
THE POSSIBILITY OF SUCH DAMAGE.
*/

package org.nhindirect.config.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nhindirect.config.model.Setting;
import org.nhindirect.config.resources.util.EntityModelConversion;
import org.nhindirect.config.store.dao.SettingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.inject.Singleton;

/**
 * JAX-RS resource for managing settings resources in the configuration service.
 * <p>
 * Although not required, this class is instantiated using the Jersey SpringServlet and dependencies are defined in the Sprint context XML file.
 * @author Greg Meyer
 * @since 2.0
 */
@Component
@Path("setting/")
@Singleton
public class SettingResource extends ProtectedResource
{	
    private static final Log log = LogFactory.getLog(SettingResource.class);
   
    /**
     * Settings DAO is defined in the context XML file an injected by Spring
     */
    protected SettingDao settingDao;
    
    /**
     * Constructor
     */
    public SettingResource()
    {
		
	}
    
    /**
     * Sets the settings Dao.  Auto populated by Spring
     * @param settingDao Settings Dao
     */
    @Autowired
    public void setSettingDao(SettingDao settingDao) 
    {
        this.settingDao = settingDao;
    }
    
    /**
     * Gets all settings in the system.
     * @return A JSON representation of a collection of all settings in the system.  Returns a status of 204 if no settings exist.
     */
    @Produces(MediaType.APPLICATION_JSON)       
    @GET
    public Response getAllSettings()
    {
    	Collection<org.nhindirect.config.store.Setting> retSettings;
    	
    	try
    	{
    		retSettings = settingDao.getAll();
    		if (retSettings.isEmpty())
    			return Response.status(Status.NO_CONTENT).cacheControl(noCache).build();
    	}
    	catch (Exception e)
    	{
    		log.error("Error looking up settings.", e);
    		return Response.serverError().cacheControl(noCache).build();
    	}
    	
    	final Collection<Setting> modelSettings = new ArrayList<Setting>();
    	for (org.nhindirect.config.store.Setting setting: retSettings)
    	{
    		modelSettings.add(EntityModelConversion.toModelSetting(setting));
    	}
    	
		final GenericEntity<Collection<Setting>> entity = new GenericEntity<Collection<Setting>>(modelSettings) {};
		
		return Response.ok(entity).cacheControl(noCache).build();      	
    }
    
    /**
     * Gets a setting by name.
     * @param name The name of the setting to retrieve.
     * @return A JSON representation of the setting.  Returns a status of 404 if a setting with the given name does not exist.
     */
    @Produces(MediaType.APPLICATION_JSON)       
    @Path("{name}")
    @GET
    public Response getSettingByName(@PathParam("name") String name)
    {    	
    	try
    	{
    		final Collection<org.nhindirect.config.store.Setting> retSettings = settingDao.getByNames(Arrays.asList(name));
    		if (retSettings.isEmpty())
    			return Response.status(Status.NOT_FOUND).cacheControl(noCache).build();
    		
    		final Setting modelSetting = EntityModelConversion.toModelSetting(retSettings.iterator().next());
    		
    		return Response.ok(modelSetting).cacheControl(noCache).build();  	
    	}
    	catch (Exception e)
    	{
    		log.error("Error looking up setting.", e);
    		return Response.serverError().cacheControl(noCache).build();
    	}
    }  
        
    /**
     * Adds a setting to the system.
     * @param uriInfo Injected URI context used for building the location URI.
     * @param name The name of the setting to add.
     * @param value The value of the setting.
     * @return Status of 201 if the setting was created or a status of 409 if a setting with the same name
     * already exists.
     */
    @PUT
    @Path("{name}/{value}")    
    public Response addSetting(@Context UriInfo uriInfo, @PathParam("name") String name, @PathParam("value") String value)
    {    	
    	// check to see if it already exists
    	try
    	{
    		final Collection<org.nhindirect.config.store.Setting> retSettings = settingDao.getByNames(Arrays.asList(name));
    		if (!retSettings.isEmpty())
    			return Response.status(Status.CONFLICT).cacheControl(noCache).build();	
    	}
    	catch (Exception e)
    	{
    		log.error("Error looking up setting.", e);
    		return Response.serverError().cacheControl(noCache).build();
    	}
    	
    	try
    	{
    		settingDao.add(name, value);
    		
    		final UriBuilder newLocBuilder = uriInfo.getBaseUriBuilder();
    		final URI newLoc = newLocBuilder.path("setting/" + name).build();
    		
    		return Response.created(newLoc).cacheControl(noCache).build();
    	}
    	catch (Exception e)
    	{
    		log.error("Error adding setting.", e);
    		return Response.serverError().cacheControl(noCache).build();
    	}
    }
    
    /**
     * Updates the value of a setting.
     * @param name The name of the setting to update.
     * @param value The new value of the setting.
     * @return Status of 204 if the value of the setting was updated or a status of 404 if a setting with the given name
     * does not exist.
     */
    @Path("{name}/{value}")
    @POST
    public Response updateSetting(@PathParam("name") String name, @PathParam("value") String value)
    {    	
    	// make sure it exists
    	try
    	{
    		final Collection<org.nhindirect.config.store.Setting> retSettings = settingDao.getByNames(Arrays.asList(name));
    		if (retSettings.isEmpty())
    			return Response.status(Status.NOT_FOUND).cacheControl(noCache).build();	
    	}
    	catch (Exception e)
    	{
    		log.error("Error looking up setting.", e);
    		return Response.serverError().cacheControl(noCache).build();
    	}
    	
    	try
    	{
    		settingDao.update(name, value);
    		
    		return Response.noContent().cacheControl(noCache).build();
    	}
    	catch (Exception e)
    	{
    		log.error("Error updating setting.", e);
    		return Response.serverError().cacheControl(noCache).build();
    	}
    } 
    
    
    /**
     * Deletes a setting in the system by name.
     * @param name The name of the setting to delete.
     * @return Status of 200 if the setting was deleted or a status of 204 if a setting with the given name does not exist.
     */
    @DELETE
    @Path("{name}")   
    public Response removeSettingByName(@PathParam("name") String name)
    {
    	// check to see if it already exists
    	try
    	{
    		final Collection<org.nhindirect.config.store.Setting> retSettings = settingDao.getByNames(Arrays.asList(name));
    		if (retSettings.isEmpty())
    			return Response.status(Status.NOT_FOUND).cacheControl(noCache).build();	
    	}
    	catch (Exception e)
    	{
    		log.error("Error looking up setting.", e);
    		return Response.serverError().cacheControl(noCache).build();
    	}
    	
    	try
    	{
    		settingDao.delete(Arrays.asList(name));
    		
    		return Response.ok().cacheControl(noCache).build();
    	}
    	catch (Exception e)
    	{
    		log.error("Error removing setting by name.", e);
    		return Response.serverError().cacheControl(noCache).build();
    	}
    }       
}
