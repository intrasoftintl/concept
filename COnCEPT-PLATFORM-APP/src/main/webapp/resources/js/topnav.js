$.ajax({
	url: "topnav.html",
	dataType: "html",
	cache: false
})
.done(function(html) {
	//$("#topnav").append(html);
	document.body.insertAdjacentHTML('afterbegin', html);
});


$(".communication").click(function(){
    $(".chatbar").slideToggle();
});

setTimeout(function(){
	$(document).ready(function(){
		
		if (/192.168.3.5/.test(self.location.href)){
			$('.pm').addClass('active');
			console.log('pm');
		}
		if (/ba/.test(self.location.href)){
			$('.ba').addClass('active');
			console.log('ba');
		}
		if (/ms/.test(self.location.href)){
			$('.ms').addClass('active');
			console.log('ms');
		}
		if (/mb/.test(self.location.href)){
			$('.mb').addClass('active');
			console.log('mb');
		}
		if (/sketching/.test(self.location.href)){
			$('.sk').addClass('active');
			console.log('sk');
		}
		if (/vs/.test(self.location.href)){
			$('.vs').addClass('active');
			console.log('vs');
		}
		if (/wordpress/.test(self.location.href)){
			$('.wp').addClass('active');
			console.log('wp');
		}
	})
}, 400);