<%@include file="include.html"%>
<% 
String cardNo = request.getParameter("cardNo"); 
System.out.println(cardNo);
%>

<div class="jumbotron container">
<p>Hello ${message}, please pick an action!</p>

<a href="b_pickbranch.jsp?cardNo=<%= cardNo %>">Checkout a Book</a><br/>
<a href="b_viewbookloans.jsp?cardNo=<%= cardNo %>">Return a Book</a><br/>

</div>