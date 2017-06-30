

<%@page import="java.util.List"%>
<%@include file="include.html"%>



<div class="container jumbotron">

	<h4>Add New Publisher Below</h4>


	<form action="addPublisher" method="post">
	
		<div class="row">
			<span style="width: 250px;">Publisher Name</span>
		</div>
		<div class="row">
			<input name="publisherName" class="input-lg" type="text"
				placeholder="Publisher Name" style="width: 250px;">
		</div>
		
		<div class="row">
			<span style="width: 250px;">Publisher Address</span>
		</div>
		<div class="row">
			<input name="publisherAddress" class="input-lg" type="text"
				placeholder="Publisher Address" style="width: 250px;">
		</div>
		
		<div class="row">
			<span style="width: 250px;">Publisher Phone</span>
		</div>
		<div class="row">
			<input name="publisherPhone" class="input-lg" type="text"
				placeholder="Publisher Phone" style="width: 250px;">
		</div>
		

		<div class="col-xs-12" style="height: 5px;"></div>
		<button type="submit" class="btn-primary btn btn-lg" style="width: 250px;">Add
			Publisher!</button>
		<div class="col-xs-12" style="height: 5px;"></div>

	</form>

</div>
