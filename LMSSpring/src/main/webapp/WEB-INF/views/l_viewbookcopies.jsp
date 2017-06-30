<%@include file="include.html"%>

<%@page import="java.util.ArrayList"%>
<%@page import="com.gcit.lms.entity.BookCopy"%>
<%@page import="com.gcit.lms.entity.Branch"%>
<%@page import="java.util.List"%>
<%@page import="com.gcit.lms.service.LibrarianService"%>

<%
	/*
	
	AdminService adminService = new AdminService();
	List<Book> books = new ArrayList<>();
	String searchS = request.getParameter("searchString");
	if (searchS == null){
		searchS = "";
	}
	Integer booksCount = adminService.getBooksCount(searchS);
	int pages = 0;
	if(booksCount%10> 0){
		pages = booksCount/10+1;
	}else{
		pages = booksCount/10;
	}
	
	Integer pageNo= (Integer) request.getAttribute("pageNo");
	if (pageNo == null){
		pageNo = 1;
	}
	
	if(request.getAttribute("books")!=null){
		books = (List<Book>)(request.getAttribute("books"));
	}else{
		books = adminService.getAllBooks(1, "")	; 
	}
	
	 */

	/* 
	Branch branch = new Branch();
	branch.setBranchId(Integer.parseInt(request.getParameter("branchId")));
	LibrarianService librarianService = new LibrarianService();
	List<BookCopy> bookcopies = librarianService.getAllBookCopiesOwnedBy(branch);
	 */

	LibrarianService librarianService = new LibrarianService();
	List<BookCopy> copies = new ArrayList<>();

	Branch branch = new Branch();
	if (request.getParameter("branchId") != null) {
		branch = librarianService.getBranchByPk(Integer
				.parseInt(request.getParameter("branchId")));
	}

	if (request.getAttribute("copies") != null) {
		copies = (List<BookCopy>) (request.getAttribute("copies"));
	} else {
		copies = librarianService.getAllBookCopiesOwnedBy(branch);
	}
%>


<div class="container"> <!-- jumbotron -->
	<h4>Welcome to GCIT Library Management System.</h4>
	<p>Below is the list of books Owned by <%= branch.getBranchName() %>.</p>
	<!-- <%//request.getAttribute("message");%> -->
	${message}
	<table class="table">
		<tr>
			<th>No</th>
			<th>Book Name</th>
			<th>Number of Copies</th>
			<th>Edit No of Copies</th>
			<th>Delete</th>
		</tr>
		<%
			for (BookCopy b : copies) {
		%>
		<tr>
			<td><%=copies.indexOf(b) + 1%></td>
			<td><%=b.getBook().getTitle()%></td>
			<td><%=b.getNoOfCopies()%></td>
			<td><button type="button" class="btn btn-sm btn-primary"
					data-toggle="modal" data-target="#editNumberOfCopiesModal"
					href="l_editbookcopy.jsp?bookId=<%=b.getBook().getBookId()%>&branchId=<%=b.getBranch().getBranchId()%>">Change No of Copies!</button></td>
			<td><button type="button" class="btn btn-sm btn-danger"
					onclick="javascript:location.href='deleteBookCopy?bookId=<%=b.getBook().getBookId()%>&branchId=<%=b.getBranch().getBranchId()%>'">Delete!</button></td>
		</tr>
		<%
			}
		%>
	</table>
</div>


<div class="modal fade bs-example-modal-lg" tabindex="-1"
	id="editNumberOfCopiesModal" role="dialog" aria-labelledby="myLargeModalLabel">
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