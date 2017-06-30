<%@page import="com.gcit.lms.entity.BookLoan"%>
<%@page import="com.gcit.lms.service.AdminService"%>
<%
	String dateout = request.getParameter("dateOut");
	//System.out.println(dateout);
	
	//System.out.println(dateout);
	Integer cardNo  =  Integer.parseInt(request.getParameter("cardNo"));
	Integer bookId  =  Integer.parseInt(request.getParameter("bookId"));
	Integer branchId  =  Integer.parseInt(request.getParameter("branchId"));
	
	//dateout = dateout.substring(0, dateout.length()-2);
	//System.out.println(dateout);
	AdminService service = new AdminService();
	BookLoan loan = service.getBookLoanBy4Pks(bookId, branchId, cardNo, dateout);
 
	
	String duedate  = loan.getDueDate();
	if (duedate==null){
		duedate = "";
	}
	if (duedate.length()>10){
		duedate = duedate.substring(0, 10);
	}
%>

<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-label="Close">
		<span aria-hidden="true">&times;</span>
	</button>
	<h4 class="modal-title" id="myModalLabel">Edit Book Loan Due Date</h4>
</div>
<form action="editBookLoan" method="post">
	<div class="modal-body">
		<div class="row">
			Enter New Due Date (YYYY-MM-DD): <input type="text" name="newDueDate"
				value="<%=duedate%>"><br />
			<input type="hidden" name="dateOut" value=<%=request.getParameter("dateOut")%>>
			<input type="hidden" name="cardNo" value=<%=cardNo%>>
			<input type="hidden" name="bookId" value=<%=bookId%>>
			<input type="hidden" name="branchId" value=<%=branchId%>>
		</div>
		<div class="row"></div>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		<button type="submit" class="btn btn-primary">Override Due
			Date</button>
	</div>
</form>