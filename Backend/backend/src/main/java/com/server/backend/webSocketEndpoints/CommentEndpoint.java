package com.server.backend.webSocketEndpoints;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.backend.User;
import com.server.backend.UserRepository;
import com.server.backend.services.CommentMessageHandler;
import com.server.backend.webSocketEndpoints.Encoders.FrontEndEventCommentEncoder;

import FrontEndObjects.FrontEndEventComment;
import com.server.backend.staticHelpers.userHelper;
/**
 * Sends user next comment in their queue when they query /comment/{userId}
 * Inspired by https://www.baeldung.com/java-websockets
 *
 * @author bhendel
 *
 */
@ServerEndpoint(
		value = "/comment/{userId}",
		encoders = { FrontEndEventCommentEncoder.class }
)
@Component
public class CommentEndpoint {
	
	private int userId;

	@OnOpen
    public void onOpen(Session session,
    		@PathParam("userId") String userId) throws IOException {
    
		this.userId = Integer.parseInt(userId);
    	
    	while(true)
    	{
    		FrontEndEventComment toSend = new FrontEndEventComment(CommentMessageHandler.getInstance().receiveComment(this.userId));
        	
        	try {
    			session.getBasicRemote().sendObject(toSend);
    			TimeUnit.SECONDS.sleep(2);
    		} catch (Exception e) {

    		}
        	
    	}
    }
 
    @OnMessage
    public void onMessage(Session session, String incoming) throws IOException {
        
    }
    
    @OnClose
    public void onClose(Session session) throws IOException{
    	CommentMessageHandler.getInstance().closeUserQueue(this.userId);
    }
    
}
