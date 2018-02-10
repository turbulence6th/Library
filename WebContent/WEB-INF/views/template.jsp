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
		
		<link rel="stylesheet" type="text/css" href="/stylesheets/bootstrap.min.css"></style>
		<link rel="stylesheet" type="text/css" href="/stylesheets/bootstrap-theme.min.css"></style>
		<link rel="stylesheet" type="text/css" href="/stylesheets/library.css"></style>
		
		<script type="text/javascript" src="/javascripts/jquery-3.2.0.min.js"></script>
		<script type="text/javascript" src="/javascripts/bootstrap.min.js"></script>
	</head>
	<body>
		<jsp:include page="${page}" flush="true" />
	</body>
</html>