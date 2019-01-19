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


    //购买数量加的点击事件
    $(document).on("click", ".purchase-num-add", function () {
        var n = $(this).prev().val();
        var num = parseInt(n) + 1;
        var strok = Number($(this).parents('.strok-list-dl').find('.strok-dd-num span').text()) + 1;
        if (num == strok) {
            return;
        }
        $(this).prev().val(num);

        disabledUseBtn();
    });

    //购买数量减的点击事件
    $(document).on("click", ".purchase-num-reduce", function () {
        var n = $(this).next().val();
        var num = parseInt(n) - 1;
        if (num <= 0) {
            $(this).parents(".strok-list-dl:first").find('.goods-detail').hide();
            $(this).parents(".strok-list-dl:first").find('.purchase-num').hide();
            num = 0;
        }
        $(this).next().val(num);

        disabledUseBtn();
    });

    //点击商品显示商品详情
    $("dl.strok-list-dl").click(function (e) {

        if ($(e.target).hasClass("purchase-num-reduce") || $(e.target).hasClass("purchase-num-add")) {
            return;
        }
        $(this).find('.goods-detail').show();
        $(this).find('.purchase-num').show();
        var num = parseInt($(this).find(":text[name='num']").val(), 10);
        if (num <= 0) {
            $(this).find(":text[name='num']").val(1);
        }
        $('.footer').show();

        disabledUseBtn();
    });


});


/**
 * 不同的种类商品，需要禁用使用按钮
 */
function disabledUseBtn() {

    $("a.use-btn").removeClass("disabled-use-btn");
    $("a.share-btn").removeClass("disabled-use-btn");

    var totalNum = 0;
    var propertyTypeIds = [];

    $("dl.strok-list-dl :text[name='num']").each(function () {
        var v = parseInt(this.value, 10);
        totalNum += v;
        if (v > 0) {
            var propertyTypeId = parseInt($(this).parents("dl.strok-list-dl").attr("data-property-type"), 10);
            if ($.inArray(propertyTypeId, propertyTypeIds) == -1) {
                propertyTypeIds.push(propertyTypeId);
            }
        }
    });

    if (propertyTypeIds.length > 1) {
        //多个商品属性不能使用
        $("a.use-btn").addClass("disabled-use-btn");
    } else if (propertyTypeIds.length == 1) {
        //活动码、会员卡，选择数量大于1时不允许使用
        if ($.inArray(propertyTypeIds[0], [2, 34, 46, 47, 41]) > -1 && totalNum > 1) {
            $("a.use-btn").addClass("disabled-use-btn");
        }
    }

    if ($.inArray(41, propertyTypeIds) > -1) {
        $("a.share-btn").addClass("disabled-use-btn");
    }

    $("span.selected-num").html(totalNum);

}

/**
 * 获取所选商品类型
 * @returns {Array}
 */
function getPropertyTypeIds(callback) {

    var propertyTypeIds = [];
    $("dl.strok-list-dl :text[name='num']").each(function () {
        var v = parseInt(this.value, 10);
        if (v > 0) {
            var propertyTypeId = parseInt($(this).parents("dl.strok-list-dl").attr("data-property-type"), 10);
            if ($.inArray(propertyTypeId, propertyTypeIds) == -1) {
                propertyTypeIds.push(propertyTypeId);
            }
            callback && callback($(this).parents("dl.strok-list-dl"));
        }
    });
    return propertyTypeIds;
}
