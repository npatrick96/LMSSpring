<%@page import="com.gcit.lms.entity.Author"%>
<%@page import="com.gcit.lms.service.AdminService"%>
<%
	Integer authorId = Integer.parseInt(request.getParameter("authorId"));
	AdminService service = new AdminService();
	Author author = service.getAuthorByPK(authorId);
%>

<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-label="Close">
		<span aria-hidden="true">&times;</span>
	</button>
	<h4 class="modal-title" id="myModalLabel">Edit Author Details</h4>
</div>
<form action="editAuthor" method="post">
	<div class="modal-body">

		Edit Author Name: <input type="text" name="authorName"
			value="<%=author.getAuthorName()%>"><br /> 
			<input type="hidden" name="authorId" value=<%=author.getAuthorId()%>>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		<button type="submit" class="btn btn-primary">Edit Author</button>
	</div>
</form>