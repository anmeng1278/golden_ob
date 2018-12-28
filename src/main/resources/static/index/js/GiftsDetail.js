$(function(){
	// 判断底部金色严选提示语是否浮动
	var divHeight = $('.content').outerHeight();
	var wHeight = $(window).height();
	if(divHeight > wHeight){
		$('.footer-tips').css({'margin':'0.6rem auto'});
	}else{
		$('.footer-tips').css({'position': 'fixed', 'left': '20%', 'bottom': '0.5rem'});
	}
	

});
