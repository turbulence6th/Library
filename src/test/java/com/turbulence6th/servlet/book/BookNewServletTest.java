package com.turbulence6th.servlet.book;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.Mockito;

import com.turbulence6th.Assert;
import com.turbulence6th.Globals;
import com.turbulence6th.annotation.Test;
import com.turbulence6th.model.Book;

public class BookNewServletTest {

	@Test("Test New Book")
	public void testNewBook() throws ServletException, IOException {

		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

		Mockito.when(request.getMethod()).thenReturn("POST");
		Mockito.when(request.getRequestURI()).thenReturn("/pages/books/new");

		RequestDispatcher dispatcher = Mockito.mock(RequestDispatcher.class);
		Mockito.doNothing().when(dispatcher).forward(request, response);
		Mockito.when(request.getRequestDispatcher(Mockito.any(String.class))).thenReturn(dispatcher);

		String bookName = "Little Fires Everywhere";
		String bookAuthor = "Celeste Ng";
		String bookPublishDate = "2017/12/09";

		Mockito.when(request.getParameter("book[name]")).thenReturn(bookName);
		Mockito.when(request.getParameter("book[author]")).thenReturn(bookAuthor);
		Mockito.when(request.getParameter("book[publishDate]")).thenReturn(bookPublishDate);

		BookNewServlet servlet = Mockito.spy(new BookNewServlet());
		Mockito.doReturn(Globals.context).when(servlet).getServletContext();
		servlet.init();
		servlet.doPost(request, response);

		Book book = Globals.bookRepository.findBy("name", bookName, "author", bookAuthor);

		Assert.assertNotNull(book);

	}

}
