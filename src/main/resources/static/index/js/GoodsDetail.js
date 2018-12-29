$(function () {
    //banner图片轮播
    var swiper = new Swiper('.swiper-container', {
        pagination: {
            el: '.swiper-pagination',
            type: 'fraction',
        }
    });

    //点击立即购买按钮购买弹层显示
    $('.purchase-show-btn').on('click', function () {
        $('.purchase-layer').show();
    });
    //点击购买弹层背景按钮购买弹层隐藏
    $('.purchase-layer-bg').on('click', function () {
        $('.purchase-layer').hide();
    });

    //点击加入购物车按钮购买弹层显示
    $('.shopping-cart-btn').on('click', function () {
        $('.shopping-cart').show();
    });
    //加入购物车弹层背景按钮购买弹层隐藏
    $('.shopping-cart-bg').on('click', function () {
        $('.shopping-cart').hide();
    });

    //选规格的点击事件
    $('.purchase-layer-p span').on('click', function () {
        return;
        $(this).addClass('active').siblings().removeClass('active');
    });

    //购买数量加的点击事件
    $(document).on("click", ".purchase-num-add", function () {

        if ($(this).hasClass("disabled")) {
            return;
        }
        var stockCount = window.specStockCount || 0;
        var n = parseInt($(this).prev().val(), 10);
        if (n >= stockCount) {
            return;
        }
        var num = n + 1;
        if (num == 0) {
            return;
        }
        $(this).prev().val(num);

        if ($(this).parents("div.purchase-layer").length > 0) {
            if (typeof calculateOrder != "undefined") {
                calculateOrder();
            }
        }

    });

    $('.purchase-num .num').blur(function () {
        if (typeof calculateOrder != "undefined") {
            calculateOrder();
        }
    })

    //购买数量减的点击事件
    $(document).on("click", ".purchase-num-reduce", function () {

        if ($(this).hasClass("disabled")) {
            return;
        }
        var n = parseInt($(this).next().val(), 10);
        var num = n - 1;
        if (num == 0) {
            return
        }
        $(this).next().val(num);

        if ($(this).parents("div.purchase-layer").length > 0) {
            if (typeof calculateOrder != "undefined") {
                calculateOrder();
            }
        }

    });


    //选优惠券的点击事件
    $(".purchase-coupon li").click(function () {
        $(this).siblings().removeClass('active');
        if ($(this).hasClass("active")) {
            $(this).removeClass("active");
        } else {
            $(this).addClass("active")
        }

        if (typeof calculateOrder != "undefined") {
            calculateOrder();
        }

    });
})
