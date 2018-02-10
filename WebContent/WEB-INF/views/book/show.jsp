<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<form class="form-horizontal">
	<div class="form-group">
		<h1>Book</h1>
	</div>
	<div class="form-group">
    	<label class="control-label" for="name">Id</label>
      	<p class="form-control-static">${book.id}</p>
  	</div>
	<div class="form-group">
    	<label class="control-label" for="name">Name</label>
      	<p class="form-control-static">${book.name}</p>
  	</div>
  	<div class="form-group">
    	<label class="control-label" for="name">Author</label>
    	<p class="form-control-static">${book.author}</p>
  	</div>
  	<div class="form-group">
    	<label class="control-label" for="name">Publish Date</label>
      	<p class="form-control-static">${book.publishDate}</p>
  	</div>
</form>