<%@page import="com.gcit.lms.entity.BookCopy"%>
<%@page import="com.gcit.lms.service.LibrarianService"%>
<%
	Integer branchId = Integer.parseInt(request.getParameter("branchId"));
	Integer bookId = Integer.parseInt(request.getParameter("bookId"));
	LibrarianService service = new LibrarianService();
	BookCopy copy = service.getBookCopyByPks(branchId, bookId);
%>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-label="Close">
		<span aria-hidden="true">&times;</span>
	</button>
	<h4 class="modal-title" id="myModalLabel"> Update Number of Book Copies</h4>
</div>
<form action="editBookCopy" method="post">
	<div class="modal-body">
		<div class = "row">
		Enter New No of Copies: <input type="number" name="noOfCopies" id ="noOfCopies"
			value="<%=copy.getNoOfCopies()%>" type="number" min="1" step="1" required="required"><br /> 
			<input type="hidden" name="branchId" value=<%=copy.getBranch().getBranchId()%>>
			<input type="hidden" name="bookId" value=<%=copy.getBook().getBookId()%>>
			</div>	
	</div>
	<div class="modal-footer">
		<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		<button type="submit" class="btn btn-primary">Update NoOfCopies</button>
	</div>
</form>

