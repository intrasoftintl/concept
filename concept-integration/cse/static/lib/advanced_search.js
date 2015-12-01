function executeBasicSearchQuery(param) {
	myUrl = '/search_basic?search_param='+param;
	console.log('myUrl -> ' + myUrl);
	var response;
	jQuery.ajax({url: myUrl, async : false, success: function(result){
		response = result;
	}});
	console.log("Response : " + response);
	return response;
}

function paginateItems(hits,idx) {
	len = hits['hits']['total']
	console.log(len);
	console.log(hits['hits']['hits']);
	$('.result').remove();
	for(i = 0; i < len; i++) {
		item = hits['hits']['hits'][i]['_source'];
		//$('#search_results').append('<p class="result"><span style="color:red">'+item['uuid']+'</span></p>');
		$('#search_results').append('<div class="result" id="result_'+i+'">');
		$('#result_'+i).append('<div class="thumbnail" id="thumbnail_'+i+'">');
		$('#thumbnail_'+i).append('<img class="thumbnail" src="'+item['thumbnail-url']+'">');
		
		$('#result_'+i).append('<div class="result_field" id="result_field_'+i+'">');
		$('#result_field_'+i).append('<p><b>Title:</b> '+item['title']);
		$('#result_field_'+i).append('<p><b>Description:</b> '+item['description']);
	}
	console.log($('#search_results'));
}