$(function(){
	//选择城市
	$("#city").click(function (e) {
		SelCity(this,e);
	});

	//选择日期
	$('#birthdate').date( function () {
        $("#birthdate").val(arguments[0]);
   });
	
	//导航tab切换
	$('.nav-list-ul li').on('click',function(){
		var index = $(this).index();
		$(this).addClass('active').siblings().removeClass('active');
		$('.nav-wrap-item').eq(index).show().siblings().hide();
	});
	
});