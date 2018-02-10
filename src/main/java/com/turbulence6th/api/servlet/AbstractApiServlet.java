package com.turbulence6th.api.servlet;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;

import com.google.gson.Gson;

public abstract class AbstractApiServlet extends HttpServlet {

	private static final long serialVersionUID = 8169820268448014911L;

	protected ServletContext context;
	
	protected Properties applicationProperties;
	
	protected Gson gson;
	
	protected Map<String, Set<Session>> webSocketSessionMap;
	
	@SuppressWarnings("unchecked")
	@Override
	public void init() throws ServletException {
		super.init();
		this.context = this.getServletContext();
		this.applicationProperties = (Properties) this.context.getAttribute("applicationProperties");
		this.gson = (Gson) this.context.getAttribute("gson");
		this.webSocketSessionMap = (Map<String, Set<Session>>) this.context.getAttribute("webSocketSessionMap");
	}
	
	protected void print(HttpServletResponse response, Object object) throws IOException {
		response.setContentType("application/json");
		response.getWriter().println(gson.toJson(object));
	}
}
