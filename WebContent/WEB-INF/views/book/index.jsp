<%@ page import="java.util.List"%>
<%@ page import="com.turbulence6th.model.Book"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
  
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
    <tbody id="booksTbody">
	    <% 
	    	@SuppressWarnings("unchecked")
	    	List<Book> books = (List<Book>) request.getAttribute("books");
	
			for(Book book: books) { %>
				<tr id="bookRow-<%= book.getId() %>">
					<td><%= book.getId() %></td>
					<td><%= book.getName() %></td>
					<td><%= book.getAuthor() %></td>
					<td><%= book.getPublishDate() %></td>
					<td>
						<button class="btn btn-danger" onclick="sendDeleteRequest(<%= book.getId() %>)">
							<span class="glyphicon glyphicon-trash"></span>
						</button>
					</td>
				</tr>
			<% }
		%>
    </tbody>
</table>

<script type="text/javascript">
	
	function removeBook(id) {
		if($('#bookRow-' + id).length) {
			$('#bookRow-' + id).remove();
		}
	}
	
	function sendDeleteRequest(id) {
		if($('#bookRow-' + id).length) {
			$.ajax({	
				url: '/api/books/delete/' + id,
				type: 'DELETE',
			   	data: {
					format: 'json'
				},
				success: function(data) {
					if(data.success) {
						removeBook(id);
					}
				}, 
				error: function() {
				   
				}
			});
		}
	}
	
	function getPublishDate(publishDate) {
		var year = publishDate.year;
		var month = publishDate.month;
		var day = publishDate.day;
		return year + "-" + (month<10?"0"+month:month) + "-" + (day<10?"0"+day:day);
	}
	
	websocket.onopen = (() => websocket.send(JSON.stringify({
		pathname: window.location.pathname
	})));
	
	websocket.addEventListener("message", message => {
		var data = JSON.parse(message.data);
		if(data.addBook) {
			var book = data.book;
			var tr = $('<tr id="bookRow-' + book.id + '"></tr>');
			tr.append($('<td>' + book.id + '</td>'));
			tr.append($('<td>' + book.name + '</td>'));
			tr.append($('<td>' + book.author + '</td>'));
			tr.append($('<td>' + getPublishDate(book.publishDate) + '</td>'));
			
			var buttonTd = $('<td></td>');
			var button = $('<button class="btn btn-danger" onclick="sendDeleteRequest(' + book.id +')">'
				+ '<span class="glyphicon glyphicon-trash"></span></button>');
			
			buttonTd.append(button);
			tr.append(buttonTd);
			
			$('#booksTbody').append(tr);
		}
		
		else if(data.updateBook) {
			var book = data.book;
			$('#bookRow-' + book.id + ' td:nth-child(2)').text(book.name);
			$('#bookRow-' + book.id + ' td:nth-child(3)').text(book.author);
			$('#bookRow-' + book.id + ' td:nth-child(4)').text(getPublishDate(book.publishDate));
		}
		
		else if(data.removeBook) {
			removeBook(data.id);
		}
	});
</script>