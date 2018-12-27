$(function(){
	//选择城市
    $(document).on("click", "#city", function(e){
		SelCity(this,e);
	});

	//选择日期
	$('#birthdate').date( function () {
        $("#birthdate").text(arguments[0]);
   });
	
	//导航tab切换
    $(document).on("click", ".nav-list-ul li", function(){
		var index = $(this).index();
		$(this).addClass('active').siblings().removeClass('active');
		$('.nav-wrap-item').eq(index).show().siblings().hide();
	});
	
});