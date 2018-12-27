$(function () {
    //banner轮播
    var swiper = new Swiper('.swiper-container', {
        loop: true,
        autoplay: {
            delay: 2500,
            disableOnInteraction: false,
        },
    });

    //导航条显示、隐藏
    $(window).scroll(function () {
        var top = $('.banner').outerHeight();
        if ($(window).scrollTop() > top) {
            $(".nav-wrap-div").addClass('nav-wrap-fix');
        }
        else {
            $(".nav-wrap-div").removeClass('nav-wrap-fix');
        }
    });

    //内容信息导航锚点
    $('.nav-wrap').navScroll({
        mobileDropdown: true,
        mobileBreakpoint: 200,
        scrollSpy: true
    });

});