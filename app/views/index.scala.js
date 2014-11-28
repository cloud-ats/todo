/**
 * this file handle events todo application
 */

$(document).ready(function(){
		console.log('load page');
		$('.toggle').prop('checked', false);
		$("body").on("change","#toggle-all",function(event) {  //on click
 			var todo_count = $(".todo-count");
 			
 			if(this.checked) { // check select status
 				$("#clear-completed").css("display", "block");
 				$("#footer").css("display", "block");
 				$('#todo-list .liTag .view .deleted').css("display","block");
 		    	$('.toggle').each(function() { //loop through each checkbox
 		          	this.checked = true;  //select all checkboxes with class "checkbox1"          
 		         	// $(this).parent().find('#todo-list .liTag .view .deleted').css("display","block");
 		        	 $("#clear-completed").css("display", "block");
 		        	$(todo_count).text($("li :checkbox:checked").length+" item selected");
 		    	});
 		 	}else{
 		 		$(todo_count).text("0"+" item selected");
 		 		$('#todo-list .liTag .view .deleted').css("display","none");
 		      	$('.toggle').each(function() { //loop through each checkbox
 		            this.checked = false; //deselect all checkboxes with class "checkbox1"                      
 		          	 $("#clear-completed").css("display", "block");
 		      	});        
 		     }
 			
 		});
		$("body").on("keypress","#new-todo",function(event) {
 			
 			if ( event.which == 13 && $(this).val() != '') { 			
 				var name = $(this).val();	
 				var input = $(this);
 				var ul = $("#todo-list");
 				var todo_count = $(".todo-count");	
 			    $(input).val("");   		
 				var jsonData = {'name': name};
 				$.ajax({

 					url: "@controllers.routes.Application.newTask()",
 			    	type: "POST",
 			    	//dataType: 'json',
 			    	data: jsonData,
 			    	success: function(id){
					console.log(id);
						var output = 
 			    			"<li class = 'liTag'>" +
 				    		  "<div class = 'view'>" +
 				    		     "<input id = 'todo_"+ id +"' class = 'toggle' type = 'checkbox'>" +
 				    		     "<label class = 'list'>"+name+"</label> <a id = 'todo_"+ id +"' class = 'deleted'></a>" +
 				    		  "</div>" +
 				    		  "<input class='edit' type='text' value="+name+">" +
 				    		"</li>";
 			    	
 			    		$(ul).append(output);	
				
 	 	 			}

 	 			});
 	 				
 			}
 		}); 	
		// Handle Checkbox click event
 		$("body").on("change", ".toggle", function() {
 			
 			var todo_count = $(".todo-count");
 			if($(this).is(':checked')){		
 				var id = $(this).attr('id');
 				$("#clear-completed").css("display", "block");
 				$(this).parent().find('a').css("display", "block");
 		
 			}
 			else
 			{
 				$(this).parent().find('a').css("display", "none");
 			}
 			$(todo_count).text($("li :checkbox:checked").length+" item selected");
 		});
 	// Handle when click delete button 
 		$("body").on("click",".deleted", function(){
 			//console.log($(this).parent().attr('id'));
 			var id_todo = $(this).attr('id');
 			var id = id_todo.substr(id_todo.indexOf('_')+1);
 			$(this).parent().parent().hide();
 			var todo_count = $(".todo-count");
 			var todo_count_text =	$(todo_count).text();
 			var todo_count1= todo_count_text.substr(0, todo_count_text.indexOf(' ')); 
 			var count = todo_count1 - '1';
 			//console.log(count);
 			console.log(id_todo);
 			console.log(id);
 			$(todo_count).text(count+" item selected");
 			var jsonData = {'id': id};
 			$.ajax({
 				
 				url: "@controllers.routes.Application.deleteAll()",
 				type: "DELETE",
 				data : jsonData,
 				success: function(){
 					console.log(id);
 				}
 			}); 
 			
 			
 		});
 		
 		//Handle to show input edit text when click label tag
 		$("body").on("click",".liTag div label",function(event){
 			
 			$(this).parent().hide();
 			var input = $(this).parent().parent().find('.edit');
 			$(input).show();
 			$(input).focus();
 			
 		});
 		
 		// Handle when enter after input todos data
 		$("body").on("keypress","li .edit",function(event){
 				
 			if ( event.which == 13 ) {
 				
 				var id_todo = $(this).parent().find('.view input').attr('id');
 				var id = id_todo.substr(id_todo.indexOf('_')+1);
 				$(this).hide();
 				$(this).parent().find('.view').show();		
 				var value = $(this).val();
 				$(this).parent().find('.view .list').text(value);
 				var jsonData = {'id': id ,'name' : value};
 				$.ajax({
 					url: "@controllers.routes.Application.editTask()",
 					type: "PUT",
 					data: jsonData,
 					success: function(){
 					
 					}
 				});
 				
 			}
 		});
 	// Handle when click outside edit input text 
 		$("body").on("blur","li .edit",function(){
 			$(this).hide();
 			$(this).parent().find('.view').show();
 			
 		});
 		$("body").on("click","#clear-completed",function(){
 	 		
 			var id = $(this).attr('id');
 			$(this).parent().parent().find('#main #toggle-all').css("display","none");
 			var arrayData = {};
 			arrayData.check = [];
 			$("input:checkbox").each(function(){
 				
 				if($(this).is(":checked")){
 					if((this).id != "toggle-all"){
 						var	id_todo = ($(this).attr("id"));
 						var id = id_todo.substr(id_todo.indexOf('_')+1);
 						arrayData.check.push(id);
 					}
 				}
 				
 			});
 			var i;
 			for(i = 0; i < arrayData.check.length; i++){
 				var id = "#todo_"+arrayData.check[i];
 				var checkbox = $("#todo_"+arrayData.check[i]);
 				var li = $(checkbox).closest("li.liTag");
 				$(li).remove();
 			
 			}
 			if(arrayData.check.length == 0){
 			//	$("label[for='toggle-all']").hide();
 				//$('#toggle-all').hide();
 			}
 			//alert(arrayData.check);
 		 	if(arrayData.check.length >=1){
	 			$.ajax({
	 				url: "@controllers.routes.Application.deleteAll()",
	 				type: "DELETE",
	 				data: {"id_all" : arrayData.check}
	 			});
 			}
 			else {
 				alert("you must choose at least one checkbox");
 			} 
 		});
});