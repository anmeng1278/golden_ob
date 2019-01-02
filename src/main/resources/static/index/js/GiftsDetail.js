$(function () {
    // 判断底部金色严选提示语是否浮动
    var divHeight = $('.content').outerHeight();
    var wHeight = $(window).height();
    if (divHeight > wHeight) {
        $('.footer-tips').css({'margin': '0.6rem auto'});
    } else {
        $('.footer-tips').css({'position': 'fixed', 'left': '20%', 'bottom': '0.5rem'});
    }

    //活动规则弹层显示
    $('.coupon-rule-btn').on('click', function () {
        $(this).parents("div.coupon-item").next().show();
    });
    //活动规则弹层隐藏
    $('.four_close').on('click', function () {
        $('.IllustrateLayer').hide();
    });


});
