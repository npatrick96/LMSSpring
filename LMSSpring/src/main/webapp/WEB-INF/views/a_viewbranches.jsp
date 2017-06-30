<%@include file="include.html"%>
<%@page import="com.gcit.lms.entity.Branch"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.gcit.lms.entity.Book"%>
<%@page import="java.util.List"%>
<%@page import="com.gcit.lms.service.AdminService"%>

<%
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
<div class="container"> <!-- jumbotron -->
	<h4>Welcome to GCIT Library Management System</h4>
	<p>Below are all Library Branches in our System.</p>
	${message}
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
			<th>Edit</th>
			<th>Delete</th>
		</tr>
		<%
			for (Branch p : branches) {
		%>
		<tr>
			<td><%=branches.indexOf(p) + 1%></td>
			<td><%=p.getBranchName()%></td>
			<td><%=p.getBranchAddress()%></td>
			<td><button type="button" class="btn btn-sm btn-primary"
					data-toggle="modal" data-target="#editBranchModal"
					href="a_editbranch.jsp?branchId=<%=p.getBranchId()%>">Edit!</button></td>
			<td><button type="button" class="btn btn-sm btn-danger"
					onclick="javascript:location.href='deleteBranch?branchId=<%=p.getBranchId()%>'">Delete!</button></td>
					
		</tr>
		<%
			}
		%>
	</table>
</div>

<div class="modal fade bs-example-modal-lg" tabindex="-1"
	id="editBranchModal" role="dialog" aria-labelledby="myLargeModalLabel">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content"></div>
	</div>
</div>

<script>
	$(document).ready(function() {
		// codes works on all bootstrap modal windows in application
		$('.modal').on('hidden.bs.modal', function(e) {
			$(this).removeData();
		});
	});
</script>