$(function(){
	$('.nav-list-ul li').on('click',function(){
		var index = $(this).index();
		$(this).addClass('active').siblings().removeClass('active');
		$('.give-item').eq(index).show().siblings().hide();
	});
})
