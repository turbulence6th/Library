package com.turbulence6th.api.servlet;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public abstract class AbstractApiServlet extends HttpServlet {

	private static final long serialVersionUID = 8169820268448014911L;

	protected ServletContext context;
	
	protected Properties applicationProperties;
	
	protected Gson gson;
	
	@Override
	public void init() throws ServletException {
		super.init();
		this.context = this.getServletContext();
		this.applicationProperties = (Properties) this.context.getAttribute("applicationProperties");
		this.gson = (Gson) this.context.getAttribute("gson");
	}
	
	protected void print(HttpServletResponse response, Object object) throws IOException {
		response.setContentType("application/json");
		response.getWriter().println(gson.toJson(object));
	}
}
