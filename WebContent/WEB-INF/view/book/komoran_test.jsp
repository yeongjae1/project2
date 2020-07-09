<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%
List<String> pList = (List<String>) request.getAttribute("pList");
List<Integer> iList = (List<Integer>) request.getAttribute("iList");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
#chartdiv {
   width: 100%;
   height: 500px;
}
</style>
<script src="https://www.amcharts.com/lib/4/core.js"></script>
<script src="https://www.amcharts.com/lib/4/charts.js"></script>
<script src="https://www.amcharts.com/lib/4/plugins/wordCloud.js"></script>
<script src="https://www.amcharts.com/lib/4/themes/frozen.js"></script>
<script src="https://www.amcharts.com/lib/4/themes/animated.js"></script>

<script src="/js/jquery-3.5.1.min.js"></script>

<script type="text/javascript">
   
   
   var str =  [];
   <%for(int i = 0 ; i<pList.size();i++){%>
      str.push({"word":"<%=pList.get(i)%>","count" : "<%=iList.get(i)%>"});
   <%}%>
   
   am4core.ready(function() {
      // Themes begin
      am4core.useTheme(am4themes_frozen);
      am4core.useTheme(am4themes_animated);
        // Themes end
      var chart = am4core.create("chartdiv", am4plugins_wordCloud.WordCloud);
      var series = chart.series.push(new am4plugins_wordCloud.WordCloudSeries());
         series.randomness = 0.1;
         series.labels.template.tooltipText = "{word}: {value}";
         series.fontFamily = "Courier New";
         series.data = str;
         series.dataFields.word = "word";
         series.dataFields.value = "count";
         series.colors = new am4core.ColorSet();
         series.colors.passOptions = {}; // makes it loop
   });
</script>
</head>
<body>
<div id="chartdiv"></div>
</body>
</html>