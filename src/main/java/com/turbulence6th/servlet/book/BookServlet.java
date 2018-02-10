package com.turbulence6th.servlet.book;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.turbulence6th.model.Book;
import com.turbulence6th.repository.BookRepository;
import com.turbulence6th.servlet.AbstractServlet;

public abstract class BookServlet extends AbstractServlet {

	private static final long serialVersionUID = -2399002301004833284L;
	
	protected BookRepository bookRepository;
	
	protected Book requestBook(HttpServletRequest request) {
		String[] pathParts = request.getRequestURI().split("/");
		
		/**
		 * BookShowServlet
		 */
		if(request.getMethod().equals("GET") && pathParts.length == 4) {
			Long id = Long.valueOf(pathParts[3]);
			Book book = this.bookRepository.findById(id);
			return book;
		}
		
		/**
		 * BookNewServlet
		 */
		else if(request.getMethod().equals("POST") && pathParts.length == 4) {
			String name = request.getParameter("book[name]").trim().replaceAll(" +", " ");
	        String author = request.getParameter("book[author]").trim().replaceAll(" +", " ");
	        String publishDate = request.getParameter("book[publishDate]");
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	        return new Book(name, author, LocalDate.parse(publishDate, formatter));
		}

		/**
		 * BookEditServlet
		 */
		else if(request.getMethod().equals("GET") && pathParts.length == 5) {
			Long id = Long.valueOf(pathParts[4]);
			Book book = this.bookRepository.findById(id);
			return book;
		}
		
		/**
		 * BookEditServlet
		 */
		else if (request.getMethod().equals("POST") && pathParts.length == 5) {
			Long id = Long.valueOf(pathParts[4]);
			Book book = this.bookRepository.findById(id);

			String name = request.getParameter("book[name]").trim().replaceAll(" +", " ");
			String author = request.getParameter("book[author]").trim().replaceAll(" +", " ");
			String publishDate = request.getParameter("book[publishDate]");

			book.setName(name);
			book.setAuthor(author);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			book.setPublishDate(LocalDate.parse(publishDate, formatter));

			return book;
		}

		return null;
	}
	
	@Override
	public void init() throws ServletException {
		super.init();
		this.bookRepository = (BookRepository) this.context.getAttribute("bookRepository");
	}
	
	@Override
	protected String view() {
		return "/book";
	}

}
