<html>
<script type="text/javascript" src="./js/jquery.min.js"></script>
<script type="text/javascript" src="./js/jquery.easyui.min.js"></script>
<script type="text/javascript">
	 $(document).ready(function(){
	 	var jsonData = {teacher_id:"test"};
	     var downloadAjax = function(url){
             $.ajax({
                 url : url,
                 type : 'get',
                 dataType: "json",
                 contentType : "application/json; charset=utf-8",
                 data : JSON.stringify(jsonData)
             }).success(function(data){
                  console.log(data);
             });
         };
	     $('#downloadButton').on('click', function(){
	                    var url = "/testbank/admin/teachers/ID";
	                    downloadAjax(url);
	     });
	});
</script>
<body>
	
	<table id="table">
		<thead>
			<tr>
				<td>ID</td>
				<td>username</td>
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>
	<button id="downloadButton" type="button">Click</button>
<h2>Hello World!</h2>
</body>
</html>
