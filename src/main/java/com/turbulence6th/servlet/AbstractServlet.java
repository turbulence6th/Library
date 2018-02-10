package com.turbulence6th.servlet;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;

import com.google.gson.Gson;

public abstract class AbstractServlet extends HttpServlet {
	
	private static final long serialVersionUID = -4853114904937995071L;
	
	protected ServletContext context;
	
	protected Properties applicationProperties;
	
	protected Gson gson;
	
	protected Map<String, Set<Session>> webSocketSessionMap;
	
	protected abstract String view();
	
	protected String title() {
		return "Library";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init() throws ServletException {
		super.init();
		this.context = this.getServletContext();
		this.applicationProperties = (Properties) this.context.getAttribute("applicationProperties");
		this.gson = (Gson) this.context.getAttribute("gson");
		this.webSocketSessionMap = (Map<String, Set<Session>>) this.context.getAttribute("webSocketSessionMap");
	}
	
	protected void forward(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("title", title());
		request.setAttribute("page", "/WEB-INF/views" + view());
		request.getRequestDispatcher("/WEB-INF/views/template.jsp").forward(request, response);
	}
	
}
