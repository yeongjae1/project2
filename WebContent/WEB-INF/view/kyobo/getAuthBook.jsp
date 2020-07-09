<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="/js/jquery-3.4.1.min.js"></script>
<script type="text/javascript">
	$(window).on("load", function() {
		getRank();
	});
	
	function getRank() {
		
		$.ajax({
			url : "/kyobo/getAuthBook.do",
			type : "post",
			dataType : "JSON",
			contentType : "application/json; charset=UTF-8",
			success : function(json) {
				
				var auth_rank = "";
				
				for (var i = 0; i < json.length; i++){
					auth_rank += (json[i].rank + "위| ");
					auth_rank += (json[i].auth + " | ");
					auth_rank += (json[i].book_cnt + "권  <br> ");
				}
				$('#book_rank').html(book_rank);
			}
		})
		
	}
</script>
<title>많은책</title>
</head>
<body>
	<h1></h1>
	<hr />
	<div id="book_rank"></div>
	<br />
	<hr />
</body>
</html>