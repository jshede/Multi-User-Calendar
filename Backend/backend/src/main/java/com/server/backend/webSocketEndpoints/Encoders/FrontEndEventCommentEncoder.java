package com.server.backend.webSocketEndpoints.Encoders;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import FrontEndObjects.FrontEndEventComment;

public class FrontEndEventCommentEncoder implements Encoder.Text<FrontEndEventComment>
{

	@Override
	public void init(EndpointConfig endpointConfig) {
		
	}

	@Override
	public void destroy() {
		
	}

	/**
	 * Encodes a front end event comment into a JSON string
	 * @Param object The front end event comment to be encoded
	 */
	@Override
	public String encode(FrontEndEventComment object) throws EncodeException {
		JsonObject jsonObject = Json.createObjectBuilder()
				.add("comment", object.comment)
				.add("eventId", object.eventId)
				//.add("time", object.time) // if this is null everything explodes
				.add("id", object.id)
				.add("isPrivate", object.isPrivate)
				.add("parentId", object.parentId)
				.add("userId", object.userId)
				.build();
		
		return jsonObject.toString();
	}

}
