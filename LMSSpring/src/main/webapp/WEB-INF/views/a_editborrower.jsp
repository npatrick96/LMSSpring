<%@page import="com.gcit.lms.entity.Borrower"%>
<%@page import="com.gcit.lms.service.AdminService"%>
<%
	Integer cardNo = Integer.parseInt(request.getParameter("borrowerId"));
	AdminService service = new AdminService();
	Borrower borrower = service.getBorrowerByPK(cardNo);
%>

<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-label="Close">
		<span aria-hidden="true">&times;</span>
	</button>
	<h4 class="modal-title" id="myModalLabel">Edit Borrower Details</h4>
</div>
<form action="editBorrower" method="post">
	<div class="modal-body">
		<div class = "row">
		Edit Borrower Title: <input type="text" name="borrowerName"
			value="<%=borrower.getName()%>"><br /> 
			<input type="hidden" name="borrowerId" value=<%=borrower.getCardNo()%>>
			</div>
			
			
		<div class = "row">
		Edit Borrower Address: <input type="text" name="borrowerAddress"
			value="<%=borrower.getAddress()%>"><br /> 
			<input type="hidden" name="borrowerId" value=<%=borrower.getCardNo()%>>
			</div>
			
			
		<div class = "row">
		Edit Borrower Phone: <input type="text" name="borrowerPhone"
			value="<%=borrower.getPhone()%>"><br /> 
			<input type="hidden" name="borrowerId" value=<%=borrower.getCardNo()%>>
			</div>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		<button type="submit" class="btn btn-primary">Edit Borrower</button>
	</div>
</form>