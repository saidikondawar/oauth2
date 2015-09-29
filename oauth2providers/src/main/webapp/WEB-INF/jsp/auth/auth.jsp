<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="net.oauth.v2.*" %>
<%@ page import="java.util.*" %>
<%@ page import="com.multi.oauth2.provider.vo.*" %>
<%
	boolean isloggined = (Boolean)request.getAttribute("isloginned");
	RequestAuthVO rVO = (RequestAuthVO)request.getAttribute("requestAuthVO");
	ClientVO cVO = (ClientVO)request.getAttribute("clientVO");
	//csv to an array
	String[] scopes = rVO.getScope().split(",");
	String currentUrl = "auth?" + request.getQueryString();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Authorization</title>
<script type="text/javascript">
function allow() {
	document.getElementById("f1").submit();
}

function deny() {
	document.getElementById("isallow").value = "false";
	document.getElementById("f1").submit();
}
</script>
</head>
<body>
	<h1>Consumer </h1>
	<hr>
		<b>Client Name : <%=cVO.getClient_name() %></b>
	<hr>
	<h3>Scopes :</h3>
	<ul>
<% for (int i=0; i < scopes.length; i++) { %>
		<li><%=OAuth2Scope.getScopeMsg(scopes[i]) %></li>		
<% } %>
	</ul>
	<h1>Are you sure you want to approve the above rights?</h1>
	<hr>
	<form id="f1" method="post" action="<%=currentUrl %>">
		<input type="hidden" id="isallow" name="isallow" value="true" />
<% if (!isloggined) { 	%>
		User Name : <input type="text" id="userid" name="userid" /><br>
		Password : <input type="text" id="password" name="password" /><br>
<% } else { %>
		<h3>Allow User?</h3>
<% } %>
		<input type="button" value="Allow" onclick="allow()" />
		<input type="button" value="Deny" onclick="deny()" />
	</form>
</body>
</html>