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
	Integer cardNo = 1;
	Integer branchId = 1;
	if (request.getParameter("branchId") != null) {
		branchId = Integer.parseInt(request.getParameter("branchId"));
		branch = librarianService.getBranchByPk(branchId);
	}
	
	if (request.getParameter("cardNo") != null) {
		cardNo = Integer.parseInt(request.getParameter("cardNo"));
	}

	if (request.getAttribute("copies") != null) {
		copies = (List<BookCopy>) (request.getAttribute("copies"));
	} else {
		copies = librarianService.getAllBookCopiesOwnedBy(branch);
	}
%>


<div class="container">
	<!-- jumbotron -->
	<h4>Welcome to GCIT Library Management System.</h4>
	<p>
		Below is the list of books available at
		<%=branch.getBranchName()%>.
	</p>
	<!-- <%//request.getAttribute("message");%> -->
	${message}
	<table class="table">
		<tr>
			<th>No</th>
			<th>Book Name</th>
			<th>Number of Copies</th>
			<th>Pick Book</th>
		</tr>
		<%
			for (BookCopy b : copies) {
		%>
		<tr>
			<td><%=copies.indexOf(b) + 1%></td>
			<td><%=b.getBook().getTitle()%></td>
			<td><%=b.getNoOfCopies()%></td>
			<td><button type="button" class="btn btn-sm btn-primary"
					onclick="javascript:location.href='checkOutBook?bookId=<%=b.getBook().getBookId()%>&branchId=<%=branchId%>&cardNo=<%=cardNo%>'">
					Borrow 1 Copy!</button></td>
		</tr>
		<%
			}
		%>
	</table>
</div>

