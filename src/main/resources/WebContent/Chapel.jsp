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
	background-image: url("C:\Users\baugh\OneDrive\Software Engineering 2\eclipse-workspace\Kiosk\WebContent\background.png");
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
    <!-- TradingView Widget BEGIN -->
<div id="tv-medium-widget-3b2bf"></div>
<script type="text/javascript" src="https://s3.tradingview.com/tv.js"></script>
<script type="text/javascript">
new TradingView.MediumWidget({
  "container_id": "tv-medium-widget-3b2bf",
  "symbols": [
    [
      "Apple",
      "AAPL "
    ],
    [
      "Google",
      "GOOGL"
    ],
    [
      "Microsoft",
      "MSFT"
    ],
    [
      "GOLD",
      "TVC:GOLD|max"
    ],
    [
      "SILVER",
      "TVC:SILVER|max"
    ],
    [
      "S&P 500",
      "INDEX:SPX|max"
    ],
    [
      "DJI",
      "DJ:DJI|max"
    ]
  ],
  "greyText": "Quotes by",
  "gridLineColor": "#e9e9ea",
  "fontColor": "#83888D",
  "underLineColor": "#dbeffb",
  "trendLineColor": "#4bafe9",
  "width": "100%",
  "height": "100%",
  "locale": "en"
});
</script>
<!-- TradingView Widget END -->
</div>



<aside>
	<ul>
    <font size="3">
    <li><a href="MainPage.jsp" style="color:white">Main page</a></li>
    <li><a href="index.jsp" style="color:white">Slide Show 1</a></li>
    <li><a href="slideshow2.jsp" style="color:white">Slide Show 2</a></li>
    <li><a href="WordDoc.jsp" style="color:white">Student Projects</a></li>
    <li><a href="Chapel.jsp" style="color:white">Chapel Livestream</a></li>
    <li><a href="#" style="color:white">Settings</a></li>
  </ul>
</aside>

<footer>Stock Ticker <div style="color:white" id="time"></div></footer>

</body>








