<%@include file="include.html"%>
<%@page import="com.gcit.lms.entity.Borrower"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.gcit.lms.entity.Book"%>
<%@page import="java.util.List"%>
<%@page import="com.gcit.lms.service.AdminService"%>

<%
	AdminService adminService = new AdminService();
	List<Borrower> borrowers = new ArrayList<>();
	String searchS = request.getParameter("searchString");
	if (searchS == null){
		searchS = "";
	}
    Integer borrowersCount = adminService.getBorrowersCount(searchS);
	int pages = 0;
	if(borrowersCount%10> 0){
		pages = borrowersCount/10+1;
	}else{
		pages = borrowersCount/10;
	}
	
	Integer pageNo= (Integer) request.getAttribute("pageNo");
	if (pageNo == null){
		pageNo = 1;
	}
	
	if(request.getAttribute("borrowers")!=null){
		borrowers = (List<Borrower>)(request.getAttribute("borrowers"));
	}else{
		borrowers = adminService.getAllBorrowers(1, ""); 
	}
	
	
	
%>
<div class="container"> <!-- jumbotron -->
	<h3>Welcome to GCIT Library Management System</h3>
	<h6>Below is the list of all Borrowers in our Library System.</h6>
	${message}
	<nav aria-label="Page navigation">
		<ul class="pagination">
			<li><a href="#" aria-label="Previous"> <span
					aria-hidden="true">&laquo;</span>
			</a></li>
			<%for(int i=1; i<=pages; i++){ 
				String empty = ""; %>
				<li><a href="pageBorrowers?pageNo=<%=i%>&searchString=<%=empty%>"><%=i%></a></li>
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
			<th>Phone</th>
			<th>Edit</th>
			<th>Delete</th>
		</tr>
		<%
			for (Borrower b : borrowers) {
		%>
		<tr>
			<td><%=borrowers.indexOf(b) + 1%></td>
			<td><%=b.getName()%></td>
			<td><%=b.getAddress()%></td>
			<td><%=b.getPhone()%></td>
			<td><button type="button" class="btn btn-sm btn-primary"
					data-toggle="modal" data-target="#editBorrowerModal"
					href="a_editborrower.jsp?borrowerId=<%=b.getCardNo()%>">Edit!</button></td>
			<td><button type="button" class="btn btn-sm btn-danger"
					onclick="javascript:location.href='deleteBorrower?borrowerId=<%=b.getCardNo()%>'">Delete!</button></td>
					
		</tr>
		<%
			}
		%>
	</table>
</div>

<div class="modal fade bs-example-modal-lg" tabindex="-1"
	id="editBorrowerModal" role="dialog" aria-labelledby="myLargeModalLabel">
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