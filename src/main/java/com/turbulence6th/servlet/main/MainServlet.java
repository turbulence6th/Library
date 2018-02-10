package com.turbulence6th.servlet.main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.turbulence6th.servlet.AbstractServlet;

@WebServlet("/")
public class MainServlet extends AbstractServlet {

	private static final long serialVersionUID = 793116294545564282L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.forward(request, response);
	}

	@Override
	protected String view() {
		return "/main/main.jsp";
	}

}
