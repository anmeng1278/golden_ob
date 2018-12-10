$(function(){
	//选择城市显示
	$('#city,#SelfCity').on('click',function(){
		$('.city-layer').show();
	});
	
	//选择城市弹层隐藏
	$('.close-btn').on('click',function(){
		$('.city-layer').hide();
	});
	
	//导航tab切换
	$('.nav-list-ul li').on('click',function(){
		var index = $(this).index();
		$(this).addClass('active').siblings().removeClass('active');
		$('.nav-wrap-item').eq(index).show().siblings().hide();
	});
});