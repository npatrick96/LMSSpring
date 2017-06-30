<%@page import="com.gcit.lms.entity.Author"%>
<%@page import="com.gcit.lms.entity.Genre"%>
<%@page import="com.gcit.lms.entity.Publisher"%>
<%@page import="java.util.List"%>
<%@page import="com.gcit.lms.service.AdminService"%>
<%@include file="include.html"%>
<%
	AdminService service = new AdminService();
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
</script>

<div class="container jumbotron">

	<h4>Enter New Book Details Below</h4>


	<form action="addBook" method="post">
		<div class="row">
			<span style="width: 250px;">Book Title</span>
		</div>
		<div class="row">
			<input name="title" class="input-md" style="width: 250px;"
				type="text" placeholder="Book Title">

		</div>
		
		<div class="row">
			<div class="col-md-2"></div>
			<div class="col-md-12">
				<span style="width: 250px;">Book Authors</span>
				<div class = "row">
				<select name="authorId" multiple="multiple" size="5"
					style="width: 250px;" data-placeholder="Choose Authors..."
					class="chosen-select">
					<%
						for (Author a : authors) {
					%>
					<option value="<%=a.getAuthorId()%>"><%=a.getAuthorName()%></option>
					<%
						}
					%>
				</select> </div> 
				<span style="width: 250px;">Book Genres</span>
				<div class = "row">
				<select name="genreId" multiple="multiple" size="5"
					style="width: 250px;" data-placeholder="Choose Genres..."
					class="chosen-select">
					<%
						for (Genre g : genres) {
					%>
					<option value="<%=g.getGenreId()%>"><%=g.getGenreName()%></option>
					<%
						}
					%>
				</select> </div> 
				<span style="width: 250px;">Book Publisher</span>
				<div class = "row">
				<select name="publisherId" size="5" style="width: 250px;"
					data-placeholder="Choose Publisher..." class="chosen-select">
					<%
						for (Publisher p : publishers) {
					%>
					<option value="<%=p.getPublisherId()%>"><%=p.getPublisherName()%></option>
					<%
						}
					%>
				</select> </div>
			</div>
		</div>
		<div class="col-xs-12" style="height: 5px;"></div>
		<button type="submit" style="width: 250px;" class="btn-primary btn btn-lg">Add
			Book!</button>
		<div class="col-xs-12" style="height: 5px;"></div>

	</form>

</div>


