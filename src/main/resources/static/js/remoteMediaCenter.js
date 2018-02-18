function checkRunning(app){
	$("#"+app+"Controls").hide();
	$("#no"+app).hide();
	$.ajax( {
        url: '/media/'+app+'/isrunning',
        type : 'GET',
		contentType : 'application/json',
        success: function(result){
        	if(result === true){
        		$("#"+app+"Controls").show();
        	}else{
        		$("#no"+app).show();
        	}
        }
    });
}

function setLinkAction(app, protocol){
	$('#'+protocol+'go').on('click', function() {
		$.ajax({
			url : '/media/'+app+'/open',
			type : 'GET',
			contentType : 'application/json',
			data : {path : $('#'+protocol+'link').val()},
			success: function(result){
				checkRunning(app);
            }
		});
	});
}

function setButtonAction(app, id, action){
	$('#'+id).on('click', function() {
		$.ajax({
			url : '/media/'+app+'/'+action,
			type : 'GET',
			contentType : 'application/json',
			success: function(result){
				checkRunning(app);
			}
		});
	});
}