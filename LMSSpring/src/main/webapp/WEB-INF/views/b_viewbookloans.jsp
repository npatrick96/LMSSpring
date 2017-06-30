<%@include file="include.html"%>
<%@page import="com.gcit.lms.entity.BookLoan"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.gcit.lms.entity.Book"%>
<%@page import="com.gcit.lms.entity.Borrower"%>
<%@page import="java.util.List"%>
<%@page import="com.gcit.lms.service.AdminService"%>

<%
	AdminService adminService = new AdminService();
	List<BookLoan> bookloans = new ArrayList<>();
	
	Integer cardNo = Integer.parseInt(request.getParameter("cardNo")) ; 
	//System.out.println(cardNo);
	Borrower borrower = adminService.getBorrowerByPK(cardNo);
	
    Integer bookloansCount = adminService.getDueBookLoansCount(cardNo);
    //System.out.println(bookloansCount);
	int pages = 0;
	if(bookloansCount%10> 0){
		pages = bookloansCount/10+1;
	}else{
		pages = bookloansCount/10;
	}
	
	Integer pageNo= (Integer) request.getAttribute("pageNo");
	if (pageNo == null){
		pageNo = 1;
	}
	
	if(request.getAttribute("bookloans")!=null){
		bookloans = (List<BookLoan>)(request.getAttribute("bookloans"));
	}else{
		bookloans = adminService.getAllDueBookLoans(1, cardNo); 
	}
	
	
	
%>
<div class="container"> <!-- jumbotron class="container" -->
	${message}
	<h4>Hello, <%=borrower.getName()%>. Welcome to GCIT Library Management System</h4>
	<p>Below is the list of all books you borrowed from the library.</p>
	<nav aria-label="Page navigation">
		<ul class="pagination">
			<li><a href="#" aria-label="Previous"> <span
					aria-hidden="true">&laquo;</span>
			</a></li>
			<%for(int i=1; i<=pages; i++){ 
				String empty = ""; %>
				<li><a href="pageBookLoans?pageNo=<%=i%>&searchString=<%=empty%>"><%=i%></a></li>
			<%} %>
			<li><a href="#" aria-label="Next"> <span aria-hidden="true">&raquo;</span>
			</a></li>
		</ul>
	</nav>
	<table class="table">
		<tr>
			<th>No</th>
			<th>Book</th>
			<th>Branch</th>
			<th>Date Out</th>
			<th>Due Date</th>
			<th>Return</th>
		</tr>
		<%
			for (BookLoan p : bookloans) {
				String dateOut  = p.getDateOut();
				//System.out.println(dateOut);
				dateOut = dateOut.replaceAll(" ", "T");
				//System.out.println(dateOut);
				String dateout = p.getDateOut();
				if (dateout != null){
					dateout = dateout.substring(0, 10);
				}
				String datein = p.getDateIn();
				if (datein != null){
					datein = datein.substring(0, 10);
				}
				String duedate = p.getDueDate();
				if (duedate != null){
					duedate = duedate.substring(0, 10);
				}
		%>
		<tr>
			<td><%=bookloans.indexOf(p) + 1%></td>
			<td><%=p.getBook().getTitle()%></td>
			<td><%=p.getBranch().getBranchName()%></td>
			<td><%=dateout %></td>
			<td><%=duedate %></td>
			<td><button type="button" class="btn btn-sm btn-primary"
					onclick="javascript:location.href='returnBook?bookId=<%=p.getBook().getBookId()%>&dateOut=<%=dateOut %>&branchId=<%=p.getBranch().getBranchId()%>&cardNo=<%=cardNo%>'">Return!</button></td>
				
				<!-- based on three PKs or use the dateout thing??? -->
				<!-- take care of servlet -->
				
					
		</tr>
		<%
			}
		%>
	</table>
</div>

<div class="modal fade bs-example-modal-lg" tabindex="-1"
	id="editBookLoanModal" role="dialog" aria-labelledby="myLargeModalLabel">
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