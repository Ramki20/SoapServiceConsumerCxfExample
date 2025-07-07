package com.example.soap.config;

import com.example.soap.client.generated.AuthorizationSharedService;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.xml.ws.BindingProvider;
import java.util.Map;

@Configuration
public class SoapClientConfig {

    @Autowired
    private SoapProperties soapProperties;

    @Bean
    public AuthorizationSharedService authorizationSharedService(Bus cxfBus) {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(AuthorizationSharedService.class);
        factory.setAddress(soapProperties.getUrl());
        factory.setBus(cxfBus);

        AuthorizationSharedService client = (AuthorizationSharedService) factory.create();

        // Configure timeout and other HTTP properties
        configureClient(client);

        return client;
    }

    private void configureClient(AuthorizationSharedService client) {
        // Configure endpoint address
        BindingProvider bindingProvider = (BindingProvider) client;
        Map<String, Object> requestContext = bindingProvider.getRequestContext();
        requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, soapProperties.getUrl());

        // Configure HTTP timeout
        org.apache.cxf.endpoint.Client cxfClient = org.apache.cxf.frontend.ClientProxy.getClient(client);
        HTTPConduit httpConduit = (HTTPConduit) cxfClient.getConduit();
        
        HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
        httpClientPolicy.setConnectionTimeout(soapProperties.getConnectionTimeout());
        httpClientPolicy.setReceiveTimeout(soapProperties.getReceiveTimeout());
        httpClientPolicy.setAllowChunking(soapProperties.isAllowChunking());
        
        httpConduit.setClient(httpClientPolicy);
    }
}