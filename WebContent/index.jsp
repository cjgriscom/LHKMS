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
</script>
<style type="text/css">
#wrapper #content {
    border: 1px ridge #999;
    height: auto;
    min-height: 1000px;
    width: 1100px;
    margin-top: 40px;
    margin-right: auto;
    margin-bottom: 30px;
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
    min-height: 1000px;
    width: 1100px;
    margin-top: 40px;
    margin-right: auto;
    margin-bottom: 30px;
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
<body>
<div id="wrapper">
    <div id="content" class="tab_container">
        <div style="width:100%;height:95%;overflow:hidden;" >
        <iframe src="https://docs.google.com/presentation/d/e/2PACX-1vRYaSEFJhJibDZ__KUn0Rn_VttvgEge9RpZ-XC753ZOgihALxtL5o3UonkD10-Qs2v0oPy-KfWgt--T/embed?start=true&loop=true&delayms=3000" 
          id="content_slides" class="iframe_container"
          frameborder="0" width="100%" height="100%" 
          allowfullscreen="true" mozallowfullscreen="true" webkitallowfullscreen="true"></iframe>
        </div>
    </div>
</div>
</body>