<%@page import="java.util.ArrayList"%>
<%@page import="com.gcit.lms.entity.Branch"%>
<%@page import="java.util.List"%>
<%@page import="com.gcit.lms.service.LibrarianService"%>

<%
	LibrarianService librarianService = new LibrarianService();
	List<Branch> branches = librarianService.getAllBranches();
%>
<%@include file="include.html"%>

<div class="container"> <!-- jumbotron -->
	<h4>Below is the list of all library branches.</h4>
	<p>Edit your branch info or select the branch to add book copies.</p>
	<table class="table">
		<tr>
			<th>No</th>
			<th>Branch Name</th>
			<th>Branch Address</th>
			<th>Edit</th>
			<th>View Books</th>
		</tr>
		<%
			for (Branch b : branches) {
		%>
		<tr>
			<td><%=branches.indexOf(b) + 1%></td>
			<td><%=b.getBranchName()%></td>
			<td><%=b.getBranchAddress()%></td>
			<td><button type="button" class="btn btn-sm btn-primary"
					data-toggle="modal" data-target="#editBranchModal"
					href="l_editbranch.jsp?branchId=<%=b.getBranchId()%>">Edit!</button></td>
			<td>
				<button type="button" class="btn btn-sm btn-success"
					onclick="javascript:location.href='getBookCopies?branchId=<%=b.getBranchId()%>'">Select!</button>
				<!-- button will call doGet because GET instead of POST is the default. -->
			</td>
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