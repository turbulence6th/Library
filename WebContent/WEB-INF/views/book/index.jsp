<%@ page import="java.util.List"%>
<%@ page import="com.turbulence6th.model.Book"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<div class="container">      
  	<table class="table table-hover">
    	<thead>
      		<tr>
      			<th>Id</th>
        		<th>Name</th>
        		<th>Author</th>
        		<th>Publish Date</th>
      		</tr>
    	</thead>
    	<tbody>
	    	<% 
	    		List<Book> books = (List<Book>) request.getAttribute("books");
	
				for(Book book: books) { %>
					<tr>
						<td><%= book.getId() %></td>
						<td><%= book.getName() %></td>
						<td><%= book.getAuthor() %></td>
						<td><%= book.getPublishDate() %></td>
					</tr>
				<% }
			%>
    	</tbody>
  	</table>
</div>

