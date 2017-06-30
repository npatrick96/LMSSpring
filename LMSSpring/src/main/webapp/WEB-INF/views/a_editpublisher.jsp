<%@page import="com.gcit.lms.entity.Publisher"%>
<%@page import="com.gcit.lms.service.AdminService"%>
<%
	Integer pubId = Integer.parseInt(request.getParameter("publisherId"));
	AdminService service = new AdminService();
	Publisher publisher = service.getPublisherByPK(pubId);
%>

<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-label="Close">
		<span aria-hidden="true">&times;</span>
	</button>
	<h4 class="modal-title" id="myModalLabel">Edit Publisher Details</h4>
</div>
<form action="editPublisher" method="post">
	<div class="modal-body">
		<div class = "row">
		Edit Publisher Title: <input type="text" name="publisherName"
			value="<%=publisher.getPublisherName()%>"><br /> 
			<input type="hidden" name="publisherId" value=<%=publisher.getPublisherId()%>>
			</div>
			
			
		<div class = "row">
		Edit Publisher Address: <input type="text" name="publisherAddress"
			value="<%=publisher.getPublisherAddress()%>"><br /> 
			<input type="hidden" name="publisherId" value=<%=publisher.getPublisherId()%>>
			</div>
			
			
		<div class = "row">
		Edit Publisher Phone: <input type="text" name="publisherPhone"
			value="<%=publisher.getPublisherPhone()%>"><br /> 
			<input type="hidden" name="publisherId" value=<%=publisher.getPublisherId()%>>
			</div>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		<button type="submit" class="btn btn-primary">Edit Publisher</button>
	</div>
</form>