<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<form action="/books/edit/${book.id}" method="POST">
 		<jsp:include page="book_form.jsp" flush="true" />
</form>
