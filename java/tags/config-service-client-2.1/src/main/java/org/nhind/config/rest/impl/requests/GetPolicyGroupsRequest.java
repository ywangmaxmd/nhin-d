package org.nhind.config.rest.impl.requests;

import org.apache.http.client.HttpClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.nhindirect.common.rest.AbstractGetRequest;
import org.nhindirect.common.rest.ServiceSecurityManager;
import org.nhindirect.common.rest.exceptions.ServiceException;
import org.nhindirect.config.model.CertPolicyGroup;

public class GetPolicyGroupsRequest extends AbstractGetRequest<CertPolicyGroup>
{
    public GetPolicyGroupsRequest(HttpClient httpClient, String certServerUrl,
            ObjectMapper jsonMapper, ServiceSecurityManager securityManager) 
    {
        super(httpClient, certServerUrl, jsonMapper, securityManager, true);
    }
 
    @Override
    protected String getRequestUri() throws ServiceException
    {
    	return serviceUrl + "certpolicy/groups";
    }
}
