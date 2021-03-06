
package org.nhindirect.wsdl.edge.ws;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.7-b01-
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "nhindirectWSEdgeService", targetNamespace = "http://nhindirect.org/wsdl/edge/ws", wsdlLocation = "file:/Users/ppyette/dev/NHIN-Direct/connectathon/nhin-d/java/direct-edge-ws/src/main/webapp/WEB-INF/wsdl/directprojectEdgeProtocol.wsdl")
public class NhindirectWSEdgeService
    extends Service
{

    private final static URL NHINDIRECTWSEDGESERVICE_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(org.nhindirect.wsdl.edge.ws.NhindirectWSEdgeService.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = org.nhindirect.wsdl.edge.ws.NhindirectWSEdgeService.class.getResource(".");
            url = new URL(baseUrl, "file:/Users/ppyette/dev/NHIN-Direct/connectathon/nhin-d/java/direct-edge-ws/src/main/webapp/WEB-INF/wsdl/directprojectEdgeProtocol.wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'file:/Users/ppyette/dev/NHIN-Direct/connectathon/nhin-d/java/direct-edge-ws/src/main/webapp/WEB-INF/wsdl/directprojectEdgeProtocol.wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        NHINDIRECTWSEDGESERVICE_WSDL_LOCATION = url;
    }

    public NhindirectWSEdgeService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public NhindirectWSEdgeService() {
        super(NHINDIRECTWSEDGESERVICE_WSDL_LOCATION, new QName("http://nhindirect.org/wsdl/edge/ws", "nhindirectWSEdgeService"));
    }

    /**
     * 
     * @return
     *     returns NhindirectWSEdgePort
     */
    @WebEndpoint(name = "nhindirectWSEdgeSOAPPort")
    public NhindirectWSEdgePort getNhindirectWSEdgeSOAPPort() {
        return super.getPort(new QName("http://nhindirect.org/wsdl/edge/ws", "nhindirectWSEdgeSOAPPort"), NhindirectWSEdgePort.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns NhindirectWSEdgePort
     */
    @WebEndpoint(name = "nhindirectWSEdgeSOAPPort")
    public NhindirectWSEdgePort getNhindirectWSEdgeSOAPPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://nhindirect.org/wsdl/edge/ws", "nhindirectWSEdgeSOAPPort"), NhindirectWSEdgePort.class, features);
    }

}
