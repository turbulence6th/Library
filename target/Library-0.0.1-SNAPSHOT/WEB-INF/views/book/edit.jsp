<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<div class="container">
	<form action="/books/${book.id}" method="POST">
  		<jsp:include page="book_form.jsp" flush="true" />
	</form>
</div>