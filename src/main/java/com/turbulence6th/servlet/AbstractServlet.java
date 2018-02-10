package com.turbulence6th.servlet;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractServlet extends HttpServlet {
	
	private static final long serialVersionUID = -4853114904937995071L;
	
	protected ServletContext context;
	
	protected Properties applicationProperties;
	
	abstract protected String view();
	
	protected String title() {
		return "Library";
	}

	@Override
	public void init() throws ServletException {
		super.init();
		this.context = this.getServletContext();
		this.applicationProperties = (Properties) this.context.getAttribute("applicationProperties");
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.forward(request, response);
	}
	
	private void forward(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("title", title());
		request.setAttribute("page", "/WEB-INF/views" + view());
		request.getRequestDispatcher("/WEB-INF/views/template.jsp").forward(request, response);
	}
	
}
