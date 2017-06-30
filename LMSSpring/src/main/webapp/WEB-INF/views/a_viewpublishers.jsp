<%@include file="include.html"%>
<%@page import="com.gcit.lms.entity.Publisher"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.gcit.lms.entity.Book"%>
<%@page import="java.util.List"%>
<%@page import="com.gcit.lms.service.AdminService"%>

<%
	AdminService adminService = new AdminService();
	List<Publisher> publishers = new ArrayList<>();
	String searchS = request.getParameter("searchString");
	if (searchS == null){
		searchS = "";
	}
    Integer publishersCount = adminService.getPublishersCount(searchS);
	int pages = 0;
	if(publishersCount%10> 0){
		pages = publishersCount/10+1;
	}else{
		pages = publishersCount/10;
	}
	
	Integer pageNo= (Integer) request.getAttribute("pageNo");
	if (pageNo == null){
		pageNo = 1;
	}
	
	if(request.getAttribute("publishers")!=null){
		publishers = (List<Publisher>)(request.getAttribute("publishers"));
	}else{
		publishers = adminService.getAllPublishers(1, ""); 
	}
	
	
	
%>
<div class="container"> <!-- jumbotron -->
	<h3>Welcome to GCIT Library Management System</h3>
	<h6>Below is the list of all Publishers in our Library System.</h6>
	${message}
	<nav aria-label="Page navigation">
		<ul class="pagination">
			<li><a href="#" aria-label="Previous"> <span
					aria-hidden="true">&laquo;</span>
			</a></li>
			<%for(int i=1; i<=pages; i++){ 
				String empty = ""; %>
				<li><a href="pagePublishers?pageNo=<%=i%>&searchString=<%=empty%>"><%=i%></a></li>
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
			<th>Books</th>
			<th>Edit</th>
			<th>Delete</th>
		</tr>
		<%
			for (Publisher p : publishers) {
		%>
		<tr>
			<td><%=publishers.indexOf(p) + 1%></td>
			<td><%=p.getPublisherName()%></td>
			<td><%=p.getPublisherAddress()%></td>
			<td><%=p.getPublisherPhone()%></td>
			<td>
				<%
					for (Book b : p.getBooks()) {
							out.println("'"+b.getTitle() + "'");
						}
				%>
			</td>
			<td><button type="button" class="btn btn-sm btn-primary"
					data-toggle="modal" data-target="#editPublisherModal"
					href="a_editpublisher.jsp?publisherId=<%=p.getPublisherId()%>">Edit!</button></td>
			<td><button type="button" class="btn btn-sm btn-danger"
					onclick="javascript:location.href='deletePublisher?publisherId=<%=p.getPublisherId()%>'">Delete!</button></td>
					
		</tr>
		<%
			}
		%>
	</table>
</div>

<div class="modal fade bs-example-modal-lg" tabindex="-1"
	id="editPublisherModal" role="dialog" aria-labelledby="myLargeModalLabel">
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