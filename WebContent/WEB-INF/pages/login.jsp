<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>
</head>
<body>
<h1>CUSTOMIZATION LOGIN</h1>
<h1>${message}</h1>

<h1>Login Page</h1>

<c:if test="${param.error != null }">
	<p> Invalid username or password!</p>
</c:if>

<form action="${pageContext.request.contextPath}/login" method="POST">
	
	<input type="text" name="usernameKSHRD" placeholder="Username"/> <br/>
	
	<input type="text" name="passwordKSHRD" placeholder="password" /> <br/>
	
<%-- 	<input type="text" name="${_csrf.parameterName}" value="${_csrf.token}"/>. --%>

	<input type="submit" />
</form>
</body>
</html>


                        