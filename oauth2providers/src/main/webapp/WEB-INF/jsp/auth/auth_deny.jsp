<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<script type="text/javascript">
window.onload = function() {
	document.getElementById("gomain").onclick = function() {
		console.log('aaa');
		location.href="../index.jsp";
	};
	
};
</script>
</head>
<body>
<h1>The user has not authorized the access of client</h1>
<br><br>
To go to the main page of this service, please click on the button below.<br><br>
<button id="gomain">To main page</button> 
</body>
</html>