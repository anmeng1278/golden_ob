$(function(){
	//选项卡
    $('.city-list-ul li').click(function () {
        var index = $(this).index();
        $(this).addClass('city-cur').siblings().removeClass('city-cur');
        $('.city-list-item').eq(index).show().siblings().hide();
        $("html,body").animate({
            scrollTop: "0px"
        }, 300);
    });
    
    //点击搜索显示
    $('.search-btn').click(function () {
        $('.search-layer').show()
    });
    
    //点击取消搜索隐藏
    $('#cancelBtn').click(function () {
        $('.search-layer').hide()
    });
    
	
	//右侧点击 定位滑动
    $(".slider-nav a,.slider-nav2 a").click(function () {
        $("html,body").animate({
            scrollTop: $($(this).attr("href")).offset().top - 60 + "px"
        }, 300);
        return false;
    });
});