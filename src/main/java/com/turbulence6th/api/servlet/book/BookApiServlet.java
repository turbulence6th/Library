package com.turbulence6th.api.servlet.book;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.turbulence6th.api.servlet.AbstractApiServlet;
import com.turbulence6th.model.Book;
import com.turbulence6th.repository.BookRepository;

public abstract class BookApiServlet extends AbstractApiServlet {

	private static final long serialVersionUID = 4859402327371805154L;

	protected BookRepository bookRepository;

	@Override
	public void init() throws ServletException {
		super.init();
		this.bookRepository = (BookRepository) this.context.getAttribute("bookRepository");
	}

	protected Book requestBook(HttpServletRequest request) {
		String[] pathParts = request.getRequestURI().split("/");

		/**
		 * BookShowApiServlet
		 */
		if (request.getMethod().equals("GET") && pathParts.length == 5) {
			Long id = Long.valueOf(pathParts[4]);
			Book book = this.bookRepository.findById(id);
			return book;
		}

		/**
		 * BookNewApiServlet
		 */
		else if (request.getMethod().equals("POST")) {
			String name = request.getParameter("book[name]").trim().replaceAll(" +", " ");
			String author = request.getParameter("book[author]").trim().replaceAll(" +", " ");
			String publishDate = request.getParameter("book[publishDate]");
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			return new Book(name, author, LocalDate.parse(publishDate, formatter));
		}

		/**
		 * BookEditApiServlet
		 */
		else if (request.getMethod().equals("PUT") && pathParts.length == 6) {
			Long id = Long.valueOf(pathParts[5]);
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
		
		/**
		 * BookDeleteApiServlet
		 */
		else if(request.getMethod().equals("DELETE") && pathParts.length == 6) {
			Long id = Long.valueOf(pathParts[5]);
			Book book = this.bookRepository.findById(id);
			return book;
		}

		return null;
	}

}
