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
            $(".nav-wrap").addClass('nav-wrap-fix');
        }
        else {
            $(".nav-wrap").removeClass('nav-wrap-fix');
        }
    });

    //内容信息导航锚点
    $('.nav-wrap').navScroll({
        mobileDropdown: true,
        mobileBreakpoint: 200,
        scrollSpy: true
    });

    //限时秒杀的倒计时
    window.setTimer = null;
    var chazhi = window.secKillTime || 0;
    //差值计算
    //例子(模拟)
    //执行函数部分
    countFunc(chazhi);
    window.setTimer = setInterval(function () {
        chazhi = chazhi - 1000;
        countFunc(chazhi);
    }, 1000);

});

//计算时间
function countFunc(leftTime) {
    if (leftTime >= 0) {
        var hours = parseInt(leftTime / 1000 / 60 / 60 % 24, 10); //计算剩余的小时
        var minutes = parseInt(leftTime / 1000 / 60 % 60, 10); //计算剩余的分钟
        var seconds = parseInt(leftTime / 1000 % 60, 10); //计算剩余的秒数
        hours = checkTime(hours);
        minutes = checkTime(minutes);
        seconds = checkTime(seconds);
        $(".joinh").html(hours);
        $(".joinm").html(minutes);
        $(".joins").html(seconds);
    } else {
        window.setTimer && clearInterval(window.setTimer);
        $(".joinh").html("00");
        $(".joinm").html("00");
        $(".joins").html("00");
    }
}

//将0-9的数字前面加上0，例1变为01
function checkTime(i) {
    if (i < 10) {
        i = "0" + i;
    }
    return i;
}