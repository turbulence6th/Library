<%@page import="java.io.Writer"%>
<%@page import="java.io.IOException"%>
<%@ page import="java.util.List"%>
<%@ page import="com.turbulence6th.model.Book"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<% Book book = request.getAttribute("book") != null ? (Book) request.getAttribute("book") : null; %>

<%!
	public void writeErrors(Book book, String field, JspWriter out) {
		if(book != null && book.getErrors() != null) {
			List<String> nameErrors = book.getErrors().get(field);
			if(nameErrors != null) {
				for(String error: nameErrors) { %>
					<%! 
						try {
							out.println("<div class=\"form-control label label-danger\">" + error + "</div>");
						} catch(IOException e) {
							e.printStackTrace();
						}
					%>
				<%! }
			}
		}
	}
%>

<div class="form-group">
	<label for="name">Name *</label>
	<input class="form-control" id="name" name="book[name]" value="${book.name}"/>
	<% writeErrors(book, "name", out); %>
</div>

<div class="form-group">
	<label for="author">Author *</label>
	<input class="form-control" id="author" name="book[author]" value="${book.author}"/>
	<% writeErrors(book, "author", out); %>
</div>
<div class="form-group" style="position: relative">
	<label for="publishDate">Publish Date *</label>
    <input type='text' class="form-control" id="publishDate" name="book[publishDate]" value="${book.publishDate}"/>
    <% writeErrors(book, "publishDate", out); %>
</div>

<button type="submit" class="btn btn-default">Submit</button>

<script type="text/javascript">
	$(function () {
		$('#publishDate').datetimepicker({
			format: 'YYYY/MM/DD'
		});
 	});
</script>