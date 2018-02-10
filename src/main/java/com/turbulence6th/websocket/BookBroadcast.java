package com.turbulence6th.websocket;

import java.io.IOException;
import java.util.Set;

import javax.websocket.Session;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.turbulence6th.model.Book;

public interface BookBroadcast {

	default void broadcastAdd(Set<Session> sessions, Gson gson, Book book) throws IOException {
		JsonObject socketResponse = new JsonObject();
		socketResponse.addProperty("addBook", true);
		socketResponse.add("book", gson.toJsonTree(book));
		for(Session session: sessions) {
			session.getAsyncRemote().sendText(socketResponse.toString());
		}
	}
	
	default void broadcastUpdate(Set<Session> sessions, Gson gson, Book book) throws IOException {
		JsonObject socketResponse = new JsonObject();
		socketResponse.addProperty("updateBook", true);
		socketResponse.add("book", gson.toJsonTree(book));
		for(Session session: sessions) {
			session.getAsyncRemote().sendText(socketResponse.toString());
		}
	}
	
	default void broadcastDelete(Set<Session> sessions, Long id) throws IOException {
		JsonObject socketResponse = new JsonObject();
		socketResponse.addProperty("removeBook", true);
		socketResponse.addProperty("id", id);
		for(Session session: sessions) {
			session.getAsyncRemote().sendText(socketResponse.toString());
		}
		
	}
	
}
