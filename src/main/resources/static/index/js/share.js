$(function(){
	$('.head-nav li').on('click',function(){
		var index = $(this).index();
		$(this).addClass('head-nav-cur').siblings().removeClass('head-nav-cur');
		$('.head-wrap-item').eq(index).show().siblings().hide();
	});
})
