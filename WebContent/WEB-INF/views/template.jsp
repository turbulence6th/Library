<%@ page import="java.io.IOException"%>
<%@ page import="java.nio.file.Files"%>
<%@ page import="java.nio.file.PathMatcher"%>
<%@ page import="java.nio.file.Path"%>
<%@ page import="java.nio.file.Paths"%>
<%@ page import="java.nio.file.PathMatcher"%>
<%@ page import="java.nio.file.FileSystems"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>${title}</title>
		
		<link rel="stylesheet" type="text/css" href="/stylesheets/bootstrap/bootstrap.min.css"></link>
		<link rel="stylesheet" type="text/css" href="/stylesheets/bootstrap/bootstrap-theme.min.css"></link>
		<link rel="stylesheet" type="text/css" href="/stylesheets/bootstrap/date-picker/bootstrap.date-picker.min.css"></link>
		
		<link rel="stylesheet" type="text/css" href="/stylesheets/pretty/pretty.css"></link>
		
		<link rel="stylesheet" type="text/css" href="/stylesheets/library.css"></link>
		
		<script type="text/javascript" src="/javascripts/jquery/jquery-3.2.0.min.js"></script>
		<script type="text/javascript" src="/javascripts/bootstrap/moment.js"></script>
		<script type="text/javascript" src="/javascripts/bootstrap/transition.js"></script>
		<script type="text/javascript" src="/javascripts/bootstrap/collapse.js"></script>
		
		<script type="text/javascript" src="/javascripts/bootstrap/bootstrap.min.js"></script>
		<script type="text/javascript" src="/javascripts/bootstrap/date-picker/bootstrap.date-picker.min.js"></script>
		
		<script type="text/javascript" src="/javascripts/pretty/pretty.js"></script>
		
		<script type="text/javascript" src="/javascripts/websocket/websocket.js"></script>
		
	</head>
	<body>
		<div class="container">
			<jsp:include page="${page}" flush="true" />
		</div>
	</body>
</html>