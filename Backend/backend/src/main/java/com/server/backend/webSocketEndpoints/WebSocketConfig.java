package com.server.backend.webSocketEndpoints;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/*
 * Taken from https://git.linux.iastate.edu/vamsi/WebSockets/tree/master/Server/WebSocketServer/src/main/java/websocket
 * */
@Configuration
public class WebSocketConfig {

	@Bean  
    public ServerEndpointExporter serverEndpointExporter(){  
        return new ServerEndpointExporter();  
    }  

}
