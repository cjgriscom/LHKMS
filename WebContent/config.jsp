<%@page import="java.util.Enumeration"%>
<%@page import="edu.letu.lvkms.Util"%>
<%@page import="edu.letu.lvkms.WebInterface"%>
<%@page import="edu.letu.lvkms.DatabaseLifecycle"%>
<%@page import="java.util.function.Consumer"%>
<%@page import="com.quirkygaming.propertylib.MutableProperty"%>
<%@page import="edu.letu.lvkms.Database"%>
<%@page import="edu.letu.lvkms.db.ServerConf"%>
<%@page import="edu.letu.lvkms.db.UserList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<% 

if ("POST".equalsIgnoreCase(request.getMethod()) && request.getParameter("request") != null) {

	final MutableProperty<String> errMsg = MutableProperty.newProperty("");
	final MutableProperty<Boolean> valid = MutableProperty.newProperty(false);
	final MutableProperty<String> resetPassword = MutableProperty.newProperty("");
	final String req = request.getParameter("request");
	
	if (req.equals("configure")) {
		if (DatabaseLifecycle.serverConf().access().isConfigured()) {
			errMsg.set("The server is already configured.");
		} else {
			final String username = request.getParameter("setusername");
			final String password = request.getParameter("setpassword");
			final String password2 = request.getParameter("setpassword2");
			if (username != null && password != null && password2 != null) {
				if (username.isEmpty() || password.isEmpty() || password2.isEmpty()) {
					errMsg.set("Fill in all fields before submitting.");
				} else if (!password.equals(password2)) {
					errMsg.set("The passwords do not match.");
				} else {
					DatabaseLifecycle.userList().commit(new Consumer<UserList>() {public void accept(final UserList userList) {
						userList.clear();
						userList.addSysadmin(username, password.toCharArray());
					}});

					DatabaseLifecycle.serverConf().commit(new Consumer<ServerConf>() {public void accept(final ServerConf serverConf) {
						serverConf.setConfigured();
					}});
				}
			}
		}
	} else if (req.equals("login")) {

		final String username = request.getParameter("username");
		final char[] password = request.getParameter("password").toCharArray();
		
		if (username != null && password != null) {
			DatabaseLifecycle.userList().access(new Consumer<UserList>() {public void accept(final UserList userList) {
				
				if (userList.userExists(username)) {
					if (userList.passwordMatches(username, password)) {
						valid.set(true);
					} else {
						errMsg.set("The password does not match.");
					}
				} else {
					errMsg.set("User not recognized.");
				}
				
				
			}});
			
			if (valid.get()) 
				session.setAttribute("user", username);
			

			Util.wipe(password); // Zero out password
		} else {
			errMsg.set("Please fill in all fields.");
		}
		
		
	} else if (req.equals("resetPassword")) {
		if (WebInterface.validateJSPSession(request.getSession(), errMsg, false, false)) {
			resetPassword.set("?resetPassword=true"); // Redirect to reset page
			final String username = (String) session.getAttribute("user");
			
			final char[] password = request.getParameter("password").toCharArray();
			final String newPassword = request.getParameter("setpassword");
			final String newPassword2 = request.getParameter("setpassword2");
			
			final HttpSession finalSession = session;
			
			if (username != null && password != null && newPassword != null && newPassword2 != null && !newPassword.isEmpty()) {
				DatabaseLifecycle.userList().commit(new Consumer<UserList>() {public void accept(final UserList userList) {
					
					if (userList.userExists(username)) {
						if (userList.passwordMatches(username, password)) {
							if (!newPassword.equals(newPassword2)) {
								errMsg.set("The passwords do not match.");
							} else {
								userList.setHashedPassword(username, newPassword.toCharArray());
								
								// Log out user
								finalSession.removeAttribute("user");
								resetPassword.set(""); // Redirect to index
								errMsg.set("Now log in with your new password...");
							}
						} else {
							errMsg.set("The current password was incorrect.");
						}
					} else {
						errMsg.set("User not recognized.");
					}
					
					
				}});

				Util.wipe(password); // Zero out password
			} else {
				errMsg.set("Please fill in all fields.");
			}
		} else {
			errMsg.set("You are not logged in.");
		}
	} else if (req.equals("logout")) {
		session.removeAttribute("user");
		response.sendRedirect("config.jsp");
		return;
	    
	}
	String errorAppend;
	if (resetPassword.get().isEmpty()) errorAppend = "?error=";
	else errorAppend = "&error=";
	response.sendRedirect("config.jsp" + resetPassword.get() + (errMsg.equals("") ? "" : errorAppend + Util.sanitizeURL(errMsg.get())));
	return;
}

%> 


<!DOCTYPE html>
<html>

<% 

if (!DatabaseLifecycle.serverConf().access().isConfigured()) {
	// Domain code is not set; display initial configuration form
	
%>
<head>
	<link rel="stylesheet" type="text/css" href="conf.css">
	<title>LVKMS Initial Configuration</title>
</head>

<body>
	<div class = "front">
	<div class = "components">
	<br>
	<h1>Configure LVKMS</h1>
    <br>
    
	<div class="buttons" style="text-align:center;margin: 0 auto;">
	<span style="color:red;">
    <%    
    if (request.getParameter("error") != null) out.println(request.getParameter("error"));
    else out.println("Define an initial administrator user.");
    
    %>
    </span>
	<form method="post">
	<input name="request" type="hidden" value="configure"/>
	<br><br>
	<input name="setusername" placeholder="Administrator Username" style="width:40%; height:30px;"/>
	<br><br>
	<input name="setpassword" type="password" placeholder="Administrator Password" style="width:40%; height:30px;"/>
	<br><br>
	<input name="setpassword2" type="password" placeholder="Confirm Password" style="width:40%; height:30px;"/>
	<br><br>
	<button type="submit" style="width:40%; height:30px;">Begin</button>
	</form>
	</div>
	<br><br><br><br><br><br><br><br><br>
	</div>
	</div>
</body>

<%
	
} else if (!WebInterface.sessionValid(session)) {
%>

<head>
	<link rel="stylesheet" type="text/css" href="conf.css">
	<title>LVKMS Login</title>
</head>

<body>
	<div class = "front">
	<div class = "components">
	<br>
	<h1>LVKMS Login</h1>
    <br>
    
	<div class="buttons" style="text-align:center;margin: 0 auto;">
	<span style="color:red;">
    <%    
    if (request.getParameter("error") != null) out.println(request.getParameter("error"));
    
    %>
    </span>
	<form method="post">
	<input name="request" type="hidden" value="login"/>
	<input name="username" placeholder="Username" style="width:40%; height:30px;"/>
	<br><br>
	<input name="password" type="password" placeholder="Password" style="width:40%; height:30px;"/>
	<br><br>
	<button type="submit" class="button" style="width:30%; height:30px;">Log In</button>
	</form>
	</div>
	<br><br><br><br><br><br><br><br><br>
	</div>
	</div>
</body>


<% } else if (WebInterface.hasTempPassword(session) || request.getParameter("resetPassword") != null) { 
%>

<head>
	<link rel="stylesheet" type="text/css" href="conf.css">
	<title>LVKMS - Reset Password</title>
</head>

<body>
	<div class = "front">
	<div class = "components">
	<br>
	<h1>LVKMS - Reset Password</h1>
    <br>
    
	<div class="buttons" style="text-align:center;margin: 0 auto;">
	<span style="color:red;">
    <%    
    if (request.getParameter("error") != null) out.println(request.getParameter("error"));
    
    %>
    </span>
	<form method="post">
	<input name="request" type="hidden" value="resetPassword"/>
	<input name="password" type="password" placeholder="Current Password" style="width:40%; height:30px;"/>
	<br><br>
	<input name="setpassword" type="password" placeholder="New Password" style="width:40%; height:30px;"/>
	<br><br>
	<input name="setpassword2" type="password" placeholder="Confirm Password" style="width:40%; height:30px;"/>
	<br><br>
	<button type="submit" class="button" style="width:30%; height:30px;">Reset Password</button>
	</form>
	<form method="get">
	<br>
	<button type="submit" class="button" style="width:30%; height:30px;">Cancel</button>
	</form>
	</div>
	<br><br><br><br><br><br><br><br><br>
	</div>
	</div>
</body>


<% } else { 
%>

<head>
	<link rel="stylesheet" type="text/css" href="conf.css">
	<script src="http://ajax.aspnetcdn.com/ajax/jQuery/jquery-1.6.2.min.js"></script>
	<script src="scripts.js"></script>

	<%if (request.getParameter("error") != null) {%>
		<script>
			alert("<%out.print(request.getParameter("error"));%>");
		</script>
	<%}%>

	<title>LVKMS Administration Page</title>
</head>
<body>
<h1>Configuration Home (TODO)</h1>
<div class="box" style="width:49%;">
	<form id="resetForm" method="post">
 		<input name="request" type="hidden" value="logout"/>
 		
 		<a href="index.jsp?resetPassword=true" class="button" style="width:49%;float:left">  Reset Password  </a>
		<a class="tinyspace">&nbsp;</a>
		<a onClick="document.getElementById('resetForm').submit();" class="button" style="width:49%;float:left">  Logout: <%out.println(session.getAttribute("user"));%>  </a>
		<a class="tinyspace">&nbsp;</a>
 	</form>
		 
		
	</div>
</body>


<% } %>

</html>
