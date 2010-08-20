/* 
Copyright (c) 2010, NHIN Direct Project
All rights reserved.

Authors:
   Umesh Madan     umeshma@microsoft.com
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

package org.nhindirect.gateway.smtp.module;

import org.nhindirect.gateway.smtp.config.SmtpAgentConfig;
import org.nhindirect.gateway.smtp.provider.XMLSmtpAgentConfigProvider;
import org.nhindirect.stagent.NHINDAgent;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;

public class SmtpAgentConfigModule extends AbstractModule 
{
	private final String configurationFile;
	private final Provider<SmtpAgentConfig> smtpAgentprovider;
	private final Provider<NHINDAgent> agentProvider;
	
	public static SmtpAgentConfigModule create(String configurationFile,  Provider<SmtpAgentConfig> smtpAgentprovider, Provider<NHINDAgent> agentProvider)
	{
		return new SmtpAgentConfigModule(configurationFile, smtpAgentprovider, agentProvider);
	}
	
	private SmtpAgentConfigModule(String configurationFile, Provider<SmtpAgentConfig> smtpAgentprovider, Provider<NHINDAgent> agentProvider)
	{
		this.configurationFile = configurationFile;
		this.smtpAgentprovider = smtpAgentprovider;
		this.agentProvider = agentProvider;
	}
	
	protected void configure()
	{	
		Provider<SmtpAgentConfig> provider = smtpAgentprovider;
		
		if (provider == null) // use the default XML configuration 
			provider = new XMLSmtpAgentConfigProvider(configurationFile, agentProvider);

		bind(SmtpAgentConfig.class).toProvider(provider);
	}
}
