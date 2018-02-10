package com.turbulence6th.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@ServerEndpoint(value = "/websocket", configurator = ServletAwareConfig.class)
public class WebSocket {
	
	private ServletContext context;
	
	private Map<String, Set<Session>> webSocketSessionMap;
	
	@SuppressWarnings("unchecked")
	@OnOpen
	public void handleOpen(Session session, EndpointConfig config) throws IOException {
		this.context = (ServletContext) config.getUserProperties().get("servletContext");
		this.webSocketSessionMap = (Map<String, Set<Session>>) this.context.getAttribute("webSocketSessionMap");
	}

	@OnMessage
	public void handleMessage(Session session, String message) {
		JsonObject jsonMessage = new JsonParser().parse(message).getAsJsonObject();
		String pathname = jsonMessage.get("pathname").getAsString();
		Set<Session> set = this.webSocketSessionMap.get(pathname);
		set.add(session);
	}

	@OnClose
	public void handleClose(Session session) {
		for(String pathname: this.webSocketSessionMap.keySet()) {
			Set<Session> sessionSet = this.webSocketSessionMap.get(pathname);
			sessionSet.remove(session);
		}
	}

	@OnError
	public void handleError(Throwable t) {
		t.printStackTrace();
	}

}
