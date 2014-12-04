/**
 * this file handle events todo application
 */

$(document).ready(function(){
		$.ajax({
		
			url: "@controllers.routes.Application.tasks()",
		  type: "GET",
		  dataType: "json",
		  success: function(data){
		    	  
		    var ul = $("#todo-list");
		    $(data).each(function() {
		      var id = (this.id);
		      var name = (this.name);
		      var output = 
 			    	  "<li class = 'liTag'>" +
 				    	  "<div class = 'view'>" +
 				    		   "<input id = 'todo_"+ id +"' class = 'toggle' type = 'checkbox'>" +
 				    		   "<label class = 'list'>"+name+"</label> <a id = 'todo_"+ id +"' class = 'deleted'></a>" +
 				    		"</div>" +
 				    		"<input class='edit' type='text' value='"+name+"'>" +
 				    	"</li>";
 			    	
		      $(ul).append(output);	
		    });
	 		},
	 		error: function(e, msg) {   
	 		 alert('Opps');
			}
		});
		
		$('.toggle').prop('checked', false);
		$('#toggle-all').prop('checked', false);
		$("body").on("change","#toggle-all",function(event) {  //on click
 			var todo_count = $(".todo-count");
 			
 			if(this.checked) { // check select status
 				$("#clear-completed").css("display", "block");
 				$("#footer").css("display", "block");
 				$('#todo-list .liTag .view .deleted').css("display","block");
 		    
 				$('.toggle').each(function() { //loop through each checkbox
 		      this.checked = true;  //select all checkboxes with class "checkbox1"          
 		      $("#clear-completed").css("display", "block");
 		      $(todo_count).text($("li :checkbox:checked").length+" item selected");
 		    });
 		 	} else {
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
 			    	data: jsonData,
 			    	success: function(id){
						var output = 
 			    			"<li class = 'liTag'>" +
 				    		  "<div class = 'view'>" +
 				    		     "<input id = 'todo_"+ id +"' class = 'toggle' type = 'checkbox'>" +
 				    		     "<label class = 'list'>"+name+"</label> <a id = 'todo_"+ id +"' class = 'deleted'></a>" +
 				    		  "</div>" +
 				    		  "<input class='edit' type='text' value='"+name+"'>" +
 				    		"</li>";
 			    	
 			    		$(ul).append(output);	
				
 	 	 			},
 	 	 			error: function(e, msg) {   	
 	 	 				alert('Opps');
 	 				}
 	 			});
 	 				
 			}
 		});
		
		// Handle Checkbox click event
 		$("body").on("change", ".toggle", function() {
 			var todo_count = $(".todo-count");
 			var len = $("li :checkbox").length;
 			if($(this).is(':checked')){		
 			  if($("li :checkbox:checked").length == len) {
 			   $('#toggle-all').prop('checked', true);
 			  }
 				var id = $(this).attr('id');
 				$("#clear-completed").css("display", "block");
 				$(this).parent().find('a').css("display", "block");
 		
 			} else {
 				$(this).parent().find('a').css("display", "none");
 				$('#toggle-all').prop('checked', false);
 			}
 			
 			$(todo_count).text($("li :checkbox:checked").length+" item selected");
 		});
 		
 		// Handle when click delete button 
 		$("body").on("click",".deleted", function(){
 			var id_todo = $(this).attr('id');
 			var id = id_todo.substr(id_todo.indexOf('_')+1);
 			var todo_count = $(".todo-count");
 			var todo_count_text =	$(todo_count).text();
 			var todo_count1= todo_count_text.substr(0, todo_count_text.indexOf(' ')); 
 			var count = todo_count1 - '1';
 			var jsonData = {'check': id};

 			$(todo_count).text(count+" item selected");
 			
 			$.ajax({
 				
 				url: "@controllers.routes.Application.delete()",
 				type: "DELETE",
 				data : jsonData,
 				success: function(data){
 				  
 					var Litag = $('#todo_'+id).closest('li.liTag');
 					$(Litag).remove();
 				},
 				error: function(e, msg) {   
 				 
 					alert('Opps');
 				}
 			}); 
 		});
 		
 		//Handle to show input edit text when click label tag
 		$("body").on("dblclick",".liTag div label",function(event){
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
 				var value = $(this).val();	
 				var jsonData = {'id': id ,'name' : value};
 				
 				$.ajax({
 					url: "@controllers.routes.Application.editTask()",
 					type: "PUT",
 					data: jsonData,
 					success: function(){
 						var view = $('#todo_'+id).closest('.view');
 						var edit = $('#todo_'+id).parent().parent().find('.edit');
 						var list = $('#todo_'+id).parent().find('.list');
 						$(view).show();
 		 				$(edit).hide();
 		 				$(list).text(value);
 		 				
 					},
	 				error: function(e, msg) {   
	 					alert('Opps');
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
 		  var len = $("li :checkbox:checked").length;
 		 
 		  var arrayData = {};
 		  arrayData.check = [];
 		  var check ;
 		  var todo_count = $(".todo-count");
 			var id = $(this).attr('id');
 			if($('#toggle-all').is(":checked")){
 			  check = "true";
 			}
 			else {
 			  
 			  check = "false";
   			
   			$("input:checkbox").each(function(){
   				
   				if($(this).is(":checked")){
   					if((this).id != "toggle-all"){
   						var	id_todo = ($(this).attr("id"));
   						var id = id_todo.substr(id_todo.indexOf('_')+1);
   						arrayData.check.push(id);
   					}
   				}
   				
   			});
 			}
 			
 			if(len >= 1){
	 			$.ajax({
	 				url: "@controllers.routes.Application.delete()",
	 				type: "DELETE",
	 				data: {"id_all" : arrayData.check, 'check': check},
	 				success: function(check){
	 				  
	 				  if("false" == check){
  	 					var i;
  	 		 			for(i = 0; i < arrayData.check.length; i++){
  	 		 				var id = "#todo_"+arrayData.check[i];
  	 		 				var checkbox = $("#todo_"+arrayData.check[i]);
  	 		 				var li = $(checkbox).closest("li.liTag");
  	 		 				$(li).remove();
  	 		 				$(todo_count).text($("li :checkbox:checked").length+" item selected");
  	 		 				$('#toggle-all').prop('checked', false);
  	 		 			}
	 				  }
	 				  else{
	 				    
	 				    $("#todo-list").empty();
	 				    $('#toggle-all').prop('checked', false);
	 				    $(todo_count).text($("li :checkbox:checked").length+" item selected");
	 				  }
	 					
	 				},
	 				error: function(e, msg) {   
	 				  
	 					alert('Opps');
	 				}
	 			});
 			}
 			else {
 			 alert('You must choose at least todo');
 			}
 		});
});