<%@page import="com.gcit.lms.entity.Book"%>
<%@page import="com.gcit.lms.entity.Author"%>
<%@page import="com.gcit.lms.service.AdminService"%>
<%@page import="com.gcit.lms.entity.Genre"%>
<%@page import="com.gcit.lms.entity.Publisher"%>
<%@page import="java.util.List"%>
<script src="./template_files/chosen.jquery.js"></script>
<link href="./template_files/bootstrap-chosen.css" rel="stylesheet">
<%
	Integer bookId = Integer.parseInt(request.getParameter("bookId"));
	AdminService service = new AdminService();
	Book book = service.getBookByPK(bookId);
	
	List<Author> authors = service.getAllAuthors();
	List<Publisher> publishers = service.getAllPublishers();
	List<Genre> genres = service.getAllGenres();
%>

<script>
	$(function() {
		$('.chosen-select').chosen();
		$('.chosen-select-deselect').chosen({
			allow_single_deselect : true
		});
	});
	
	$('#editBookModal').on('shown.bs.modal', function () {
		  $('.chosen-select', this).chosen();
		});
	$('#editBookModal').on('shown.bs.modal', function () {
		  $('.chosen-select', this).chosen('destroy').chosen();
		});
	
</script>

<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-label="Close">
		<span aria-hidden="true">&times;</span>
	</button>
	<h4 class="modal-title" id="myModalLabel">Edit Book Details</h4>
</div>
<form action="editBook" method="post">
	<!-- <div class="modal-body"> -->
	<div class="col-md-12">
		<div class="row">
			<span style="width: 250px;">  Edit Book Title</span><input type="text" name="title"
				value="<%=book.getTitle()%>" style="width: 250px;"><br /> <input
				type="hidden" name="bookId" value=<%=book.getBookId()%>>
		</div>
		<div class="row">
			<span style="width: 250px;">  Edit Book Authors</span><select name="authorId" multiple="multiple"
				data-placeholder="Choose Authors..."
				class="chosen-select" style="width: 250px;">
				<%
						for (Author a :authors) {
							if (book.getAuthors().contains(a)){
					%>
				<option value="<%=a.getAuthorId()%>" selected="selected"><%=a.getAuthorName()%></option>
				<%
							}else{
					%>
				<option value="<%=a.getAuthorId()%>"><%=a.getAuthorName()%></option>
				<%
							}
						}
					%>
			</select>
		</div>
		<div class="row">
			<span style="width: 250px;">  Edit Book Genres</span><select name="genreId" multiple="multiple"
				style="width: 250px;" data-placeholder="Choose Genres..."
				class="chosen-select">
				<%
						for (Genre a :genres) {
							if (book.getGenres().contains(a)){
					%>
				<option value="<%=a.getGenreId()%>" selected="selected"><%=a.getGenreName()%></option>
				<%
							}else{
					%>
				<option value="<%=a.getGenreId()%>"><%=a.getGenreName()%></option>
				<%
							}
						}
					%>
			</select>
		</div>
		<div class="row">
			<span style="width: 250px;">  Edit Book Publisher</span><select id="pubSelect" name="publisherId" style="width: 250px;"
				data-placeholder="Choose Publisher ..." class="chosen-select"> <!--  class="chosen-select-deselect" -->
				<option selected="selected" value=0>Choose Publisher ...</option> <!-- disabled -->
				
				<%
						for (Publisher a :publishers) {
							if ( book.getPublisher()!=null && book.getPublisher().equals(a)){
					%>
				<option value="<%=a.getPublisherId()%>" selected="selected"><%=a.getPublisherName()%></option>
				<%
							}else{
					%>
				<option value="<%=a.getPublisherId()%>"><%=a.getPublisherName()%></option>
				<%
						}
					}
					%>
			</select>
		</div>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		<button type="submit" class="btn btn-primary">Edit Book</button>
	</div>
</form>