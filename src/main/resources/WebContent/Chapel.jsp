<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<title>Auto Height in jQuery</title>
<script type="text/javascript" src="jquery-1.6.2.min.js"></script>
<script type="text/javascript">
$(function(){
    var height = $("#content").height();
    $(".tab_container").height(height);
    var height2 = $("#content_slides").height();
    $(".iframe_container").height(height2);
});

function startTime() {
    var today = new Date();
    var h = today.getHours();
    var m = today.getMinutes();
    var s = today.getSeconds();
    m = checkTime(m);
    s = checkTime(s);
    document.getElementById('time').innerHTML =
    h + ":" + m + ":" + s;
    var t = setTimeout(startTime, 500);
}
function checkTime(i) {
    if (i < 10) {i = "0" + i};  // add zero in front of numbers < 10
    return i;
}

</script>
<style type="text/css">

body {
	background-color: #3063b5;
} 

header, footer {
    padding: 1em;
    color: white;
    background-color: #302bc6;
    clear: left;
    text-align: left;
}

nav {
    float: right;
    max-width: 160px;
    margin: 0;
    padding: 1em;
}

nav ul {
    list-style-type: none;
    padding: 0;
}
   
nav ul a {
    text-decoration: none;
}

aside {
	float: right;
	margin: 0 1.5%;
    width: 10%;    
}

#websiteLogo {
	float: left;
}

#wrapper #content {
    border: 1px ridge #999;
    height: auto;
    min-height: auto;
    width: auto;
    margin-top: auto;
    margin-right: auto;
    margin-bottom: auto;
    margin-left: auto;
    float: left;
}
.tab_container {
    clear: both;
    width: 100%;
    border-right: 1px ridge #999;
    height: 100%;
    border: 1px solid green;
}
#wrapper #content #content_slides {
    border: 1px ridge #999;
    height: auto;
    min-height: auto;
    width: auto;
    margin-top: auto;
    margin-right: auto;
    margin-bottom: auto;
    margin-left: auto;
    float: left;
}
.iframe_container {
    clear: both;
    width: 100%;
    border-right: 1px ridge #999;
    height: 100%;
    border: 1px solid green;
}
</style>
</head>
<body onload="startTime()">

<header>
   
   
   <img id="websiteLogo" alt="LETU Logo" src="//drive.google.com/uc?id=0Bz4D-ILXJag0ZFdEUGFnTEJxS3M"/>
   
   <h1>School of Business</h1>
   
   
</header>
<hr />
<nav>
  
</nav>

<div id="wrapper">
    <div id="content" class="tab_container">
        <div style="width:100%;height:95%;overflow:hidden;" >
        <iframe src="https://docs.google.com/presentation/d/e/2PACX-1vT_1_s-Z_CnqPai8YSvZStxwz0gEsyUwxZGb3QFxJN7njyOR594cQcZTulQuJKKvrSkh0eYvVO5vZpE/embed?start=true&loop=true&delayms=3000"
         frameborder="0" width="960" height="569" allowfullscreen="true" mozallowfullscreen="true" webkitallowfullscreen="true"></iframe>
        <!--<iframe src="https://docs.google.com/presentation/d/e/2PACX-1vRYaSEFJhJibDZ__KUn0Rn_VttvgEge9RpZ-XC753ZOgihALxtL5o3UonkD10-Qs2v0oPy-KfWgt--T/embed?start=true&loop=true&delayms=3000" 
          id="content_slides" class="iframe_container"
          frameborder="0" width="100%" height="100%" 
          allowfullscreen="true" mozallowfullscreen="true" webkitallowfullscreen="true"></iframe>-->
        </div>
    </div>
</div>



<aside>
	<ul>
    <font size="3">
    <li><a href="http://localhost:8080/Kiosk/MainPage.jsp" style="color:white">Main page</a></li>
    <li><a href="http://localhost:8080/Kiosk/index.jsp" style="color:white">Slide Show 1</a></li>
    <li><a href="http://localhost:8080/Kiosk/slideshow2.jsp" style="color:white">Slide Show 2</a></li>
    <li><a href="http://localhost:8080/Kiosk/WordDoc.jsp" style="color:white">Student Projects</a></li>
    <li><a href="http://localhost:8080/Kiosk/Chapel.jsp" style="color:white">Chapel Livestream</a></li>
    <li><a href="#" style="color:white">Settings</a></li>
  </ul>
</aside>

<footer>Stock Ticker <div style="color:white" id="time"></div></footer>

</body>








