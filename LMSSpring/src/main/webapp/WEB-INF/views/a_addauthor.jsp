
<%@page import="com.gcit.lms.entity.Book"%>
<%@page import="java.util.List"%>
<%@page import="com.gcit.lms.service.AdminService"%>
<%@include file="include.html"%>
<%
	AdminService service = new AdminService();
	List<Book> books = service.getAllBooks();
%>

<script>
	$(function() {
		$('.chosen-select').chosen();
		$('.chosen-select-deselect').chosen({
			allow_single_deselect : true
		});
	});
</script>


<div class="container jumbotron">

	<h4>Add New Author Below</h4>


	<form action="addAuthor" method="post">
		<div class="row">
			<span style="width: 250px;">Author Name</span>
		</div>
		<div class="row">
			<input name="authorName" class="input-md" type="text" placeholder="Author Name" style="width: 250px;">
		
		</div>
		<div class="row">
			<span style="width: 250px;">Books by Author</span>
		</div>
		<div class="row">
			<div class="col-md-2"></div>
			<div class="col-md-12">

				<select name="bookId"  multiple="multiple" size="5" data-placeholder="Choose Books..." style="width: 250px;" class ="chosen-select">
					
					<%
						for (Book b : books) {
					%>
					<option value="<%=b.getBookId()%>"><%=b.getTitle()%></option>
					<%
						}
					%>
				</select>
			</div>
		</div>
		
		<div class="col-xs-12" style="height: 5px;"></div>

		<button type="submit" style="width: 250px;" class="btn-primary btn btn-lg">Add
			Author!</button>
		<div class="col-xs-12" style="height: 5px;"></div>

	</form>

</div>
