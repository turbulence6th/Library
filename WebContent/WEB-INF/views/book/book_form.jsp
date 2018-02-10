<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<div class="form-group">
	<label for="name">Name:</label>
	<input class="form-control" id="name" name="book[name]" value="${book.name}">
</div>
<div class="form-group">
	<label for="author">Author:</label>
	<input class="form-control" id="author" name="book[author]" value="${book.author}">
</div>
<button type="submit" class="btn btn-default">Submit</button>