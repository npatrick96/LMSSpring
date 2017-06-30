<%@include file="include.html"%>
<%@page import="com.gcit.lms.entity.Branch"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.gcit.lms.entity.Book"%>
<%@page import="java.util.List"%>
<%@page import="com.gcit.lms.service.AdminService"%>

<%

	String cardNo = request.getParameter("cardNo"); 
	//System.out.println(cardNo);
	AdminService adminService = new AdminService();
	List<Branch> branches = new ArrayList<>();
	String searchS = request.getParameter("searchString");
	if (searchS == null){
		searchS = "";
	}
    Integer branchesCount = adminService.getBranchsCount(searchS);
	int pages = 0;
	if(branchesCount%10> 0){
		pages = branchesCount/10+1;
	}else{
		pages = branchesCount/10;
	}
	
	Integer pageNo= (Integer) request.getAttribute("pageNo");
	if (pageNo == null){
		pageNo = 1;
	}
	
	if(request.getAttribute("branches")!=null){
		branches = (List<Branch>)(request.getAttribute("branches"));
	}else{
		branches = adminService.getAllBranchs(1, ""); 
	}
	
	
	
%>
<div class="container">
	<!-- jumbotron -->
	<h4>Welcome to GCIT Library Management System</h4>
	<p>Pick one branch from the list below from which you want to
		borrow a book.</p>
	<nav aria-label="Page navigation">
		<ul class="pagination">
			<li><a href="#" aria-label="Previous"> <span
					aria-hidden="true">&laquo;</span>
			</a></li>
			<%for(int i=1; i<=pages; i++){ 
				String empty = ""; %>
			<li><a href="pageBranches?pageNo=<%=i%>&searchString=<%=empty%>"><%=i%></a></li>
			<%} %>
			<li><a href="#" aria-label="Next"> <span aria-hidden="true">&raquo;</span>
			</a></li>
		</ul>
	</nav>
	<table class="table">
		<tr>
			<th>No</th>
			<th>Name</th>
			<th>Address</th>
			<th>Select</th>
		</tr>
		<%
			for (Branch p : branches) {
		%>
		<tr>
			<td><%=branches.indexOf(p) + 1%></td>
			<td><%=p.getBranchName()%></td>
			<td><%=p.getBranchAddress()%></td>
			<td><a href="b_viewbooksavailable.jsp?branchId=<%=p.getBranchId()%>&cardNo=<%=cardNo%>">
					<button type="button" class="btn btn-sm btn-primary">Select!</button>
			</a></td>

		</tr>
		<%
			}
		%>
	</table>
</div>

