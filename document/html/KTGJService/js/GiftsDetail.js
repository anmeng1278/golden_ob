$(function(){
	// 判断底部金色严选提示语是否浮动
	var divHeight = $('.content').outerHeight();
	var wHeight = $(window).height();
	if(divHeight > wHeight){
		$('.footer-tips').css({'margin':'0.6rem auto'});
	}else{
		$('.footer-tips').css({'position': 'fixed', 'left': '20%', 'bottom': '0.5rem'});
	}
	
	//点击立即使用关注二维码弹层显示
	$('.coupon-tips').on('click',function(){
		$('.QR-code-layer').show();
	});
	//关注二维码弹层隐藏
	$('.QR-code-layer-bg').on('click',function(){
		$('.QR-code-layer').hide();
	});
});
