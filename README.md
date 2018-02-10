<h1>Web Content</h1>

<ul class="list-group">
	<li class="list-group-item">List all books: <a href="https://library-turbulence6th.herokuapp.com/books">/books</a></li>
  	<li class="list-group-item">Add a new book: <a href="https://library-turbulence6th.herokuapp.com/books/new">/books/new</a></li>
  	<li class="list-group-item">
  		Edit a book: <a href="https://library-turbulence6th.herokuapp.com/books/%BOOK_ID%">/books/%book_id%</a>
  		<ul>
  			<li><b>book_id <i>(PATH_PARAM)</i>:</b> id of the book</li>
  		</ul>
  	</li>
  	<li class="list-group-item">Delete a book: <a href="https://library-turbulence6th.herokuapp.com/books">/books</a></li>
</ul>

<h1>Api</h1>
<ul class="list-group">
	<li class="list-group-item">
		List all books: GET <a href="https://library-turbulence6th.herokuapp.com/api/books">/books</a><br>
		<code class="prettyprint">
			[
		        {
		        	"id": <i>(int)</i> id_of_the_book,
		        	"name": <i>(String)</i> "name_of_the_book",
		        	"author": <i>(String)</i> "author_of_the_book",
		        	"publishDate": <i>(Object)</i> {
		        		"year": <i>(int)</i> year,
		        		"month": <i>(int)</i> month,
		        		"day": <i>(int)</i> day
		     		}
		        }, ...
		    ]
		</code>
	</li>
  	<li class="list-group-item">
  		Add a new book: POST <a href="https://library-turbulence6th.herokuapp.com/api/books/new">/books/new</a>
  		<ul>
  			<li><b>book[name] <i>(REQ_PARAM)</i>:</b> name of the book</li>
  			<li><b>book[author] <i>(REQ_PARAM)</i></b> author of the book</li>
  			<li><b>book[publishDate] <i>(REQ_PARAM)(yyyy/mm/dd)</i>: </b> publish date of the book</li>
  		</ul>
		<code>
			{
	        	"success": <i>(boolean)</i> success_status,
	        	"errors": <i>(Object)</i> {
	        		"name": <i>(Array[String])</i> ["name_error", ...],
	        		"author": <i>(Array[String])</i> ["author_error", ...],
	        		"publishDate": <i>(Array[String])</i> ["publish_date_error", ...]
	        	}
		    }
		</code>
  	</li>
  	<li class="list-group-item">
  		Edit a book: PUT <a href="https://library-turbulence6th.herokuapp.com/api/books/%book_id%">/books/%book_id%</a>
  		<ul>
  			<li><b>book_id <i>(PATH_PARAM)</i>:</b> id of the book</li>
  			<li><b>book[name] <i>(REQ_PARAM)</i>:</b> name of the book</li>
  			<li><b>book[author] <i>(REQ_PARAM)</i></b> author of the book</li>
  			<li><b>book[publishDate] <i>(REQ_PARAM)(yyyy/mm/dd)</i>: </b> publish date of the book</li>
  		</ul>
  		<code>
			{
	        	"success": <i>(boolean)</i>  success_status,
	        	"errors": <i>(Object)</i>  {
	        		"name": <i>(Array[String])</i> ["name_error", ...],
	        		"author": <i>(Array[String])</i> ["author_error", ...],
	        		"publishDate": <i>(Array[String])</i> ["publish_date_error", ...]
	        	},
			"message": <i>(String)</i> "error_message"
		    }
		</code>
  	</li>
  	<li class="list-group-item">
  		Delete a book: DELETE <a href="https://library-turbulence6th.herokuapp.com/api/books/%book_id%">/books</a>
  		<ul>
  			<li><b>book_id <i>(PATH_PARAM)</i>:</b> id of the book</li>
  		</ul>
  		<code>
			{
	        	"success": <i>(boolean)</i> success_status,
			"message": <i>(String)</i> "error_message"
		    }
		</code>
  	</li>
</ul>

<b>Served at: </b><a href="https://library-turbulence6th.herokuapp.com/">https://library-turbulence6th.herokuapp.com/</a>
