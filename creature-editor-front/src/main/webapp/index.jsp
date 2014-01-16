<!-- Use this for the 'form' tag   -->
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!--  This is the JSTL tag -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page session="false"%>
<html lang="en">
<head>
<title>Match Report</title>
<link rel="stylesheet"
	href="<c:url value='/resources/blueprint/screen.css'/>" type="text/css"
	media="screen, projection" />
<link rel="stylesheet"
	href="<c:url value='/resources/blueprint/print.css'/>" type="text/css"
	media="print" />
<!--[if lt IE 8]>
    <link rel="stylesheet" href="<c:url value='/resources/blueprint/ie.css' />" type="text/css" media="screen, projection" />
  	<![endif]-->

<link rel="stylesheet"
	href="<c:url value='/resources/mystyle.css'/>" type="text/css"
	media="screen, projection" />

<script type="text/javascript"
	src="<c:url value='/resources/jquery-1.9.1.js' /> "></script>
<script type="text/javascript"
	src="<c:url value='/resources/polling.js' /> "></script>
	
<script type="text/javascript">

	$(document).ready(function() {
	
		var startUrl = "matchupdate/begin";
		var pollUrl = "matchupdate/deferred";
		
		var poll = new Poll();
		poll.start(startUrl,pollUrl);
	});
	
</script>

</head>

<body>
	<div class="container">
		<div id='first_row' class="span-22 prepend-2">
			<div style='float: left;'><img src="<c:url value='/resources/images/classic.png'/>"/></div>
			<div>
			<h2 class="update">Match Updates</h2>
			</div>
			<hr />
		</div>
		<%-- Place updates in here --%>
		<div class="span-4  prepend-2">
			<p class="update">Time:</p>
		</div>
		<div class="span-3 border">
			<p id='time' class="update"></p>
		</div>
		<div class="span-13 append-2 last" id='update-div'>
			<p id='message' class="update">The game has not yet started</p>
		</div>
	</div>
</body>

</html>

