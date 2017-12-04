	$.ajax({
        url : "/stock?sym={sym}",
        dataType: "text",
        success : function (data) {
            $("#statusbar{i}").html(data);
        }
    });
	