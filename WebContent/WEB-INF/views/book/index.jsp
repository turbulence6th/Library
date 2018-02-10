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
        		<th>Delete<th>
      		</tr>
    	</thead>
    	<tbody>
	    	<% 
	    		@SuppressWarnings("unchecked")
	    		List<Book> books = (List<Book>) request.getAttribute("books");
	
				for(Book book: books) { %>
					<tr>
						<td><%= book.getId() %></td>
						<td><%= book.getName() %></td>
						<td><%= book.getAuthor() %></td>
						<td><%= book.getPublishDate() %></td>
						<td>
							<button class="btn btn-danger deleteBook" data-id="<%= book.getId() %>">
								<span class="glyphicon glyphicon-trash"></span>
							</button>
						</td>
					</tr>
				<% }
			%>
    	</tbody>
  	</table>
</div>

<script type="text/javascript">
	$(function () {
		$('.deleteBook').click(function(){
			var id = $(this).data().id;
			var that = this;
			$.ajax({	
				url: '/books/' + id,
				type: 'DELETE',
			   	data: {
					format: 'json'
				},
				success: function(data) {
					if(data.success) {
						$(that).parent().parent().remove();
					}
				}, 
				error: function() {
				   
				}
			});
		});
	});
</script>