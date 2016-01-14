<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>
<!-- CSS -->
<link rel="stylesheet"
	href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/assets/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/assets/font-awesome/css/font-awesome.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/assets/css/form-elements.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/assets/css/style.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/dist/sweetalert.css">
</head>
<body>
    <!-- Top content -->
        <div class="top-content">
            <div class="inner-bg">
                <div class="container">
                    <div class="row">
                        <div class="col-sm-6 col-sm-offset-3 form-box">
                        	<div class="form-top">
                        		<div class="form-top-left">
                        			<h3>AKN API</h3>
                            		<p>Enter your username and password to log in:</p>
                        		</div>
                        		<div class="form-top-right">
                        			<i class="fa fa-key"></i>
                        		</div>
                            </div>
                            <div class="form-bottom">
			                    <form role="form" id="frmLogin" action="${pageContext.request.contextPath}/login" method="POST" class="login-form">
			                    	<div class="form-group">
			                    		<label class="sr-only" for="form-username">Username</label>
			                        	<input type="text" name="username" placeholder="Username..." class="form-username form-control" id="form-username">
			                        </div>
			                        <div class="form-group">
			                        	<label class="sr-only" for="form-password">Password</label>
			                        	<input type="password" name="password" placeholder="Password..." class="form-password form-control" id="form-password">
			                        </div>
			                        <button type="submit" class="btn">Login</button>
			                    </form>
		                    </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/assets/bootstrap/js/bootstrap.min.js"></script>

	<script src="${pageContext.request.contextPath}/resources/dist/sweetalert-dev.js"></script>
	<script type="text/javascript">
		$(function() {
			$("#frmLogin")
					.submit(function(e) {
								e.preventDefault();
								$
										.ajax({
											url : "${pageContext.request.contextPath}/login",
											type : "POST",
											data : $("#frmLogin").serialize(),
											success : function(data) {
												if (data == "Bad credentials") {
													swal({   title: "Login Fail Please Check again !",   text: "It will close auto in 2 seconds.",   timer: 2000,   showConfirmButton: false });
												} else {
													location.href = "${pageContext.request.contextPath}/"
															+ data;
												}

											},
											error : function(data) {
												console.log(data);
											}
										});
							});
		});
	</script>
</body>
</html>


