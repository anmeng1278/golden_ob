TX.INIT(function () {

    TX.CORE.p({
        url: "/config/wechat",
        disabledLoading: true,
        success: function (resp) {
            if (!resp.baseResp.success) {
                return;
            }
            wx.config({
                debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                appId: resp.datas.appId, // 必填，公众号的唯一标识
                timestamp: resp.datas.timestamp, // 必填，生成签名的时间戳
                nonceStr: resp.datas.noncestr, // 必填，生成签名的随机串
                signature: resp.datas.signature,// 必填，签名，见附录1
                jsApiList: [
                    'updateAppMessageShareData',
                    'updateTimelineShareData',
                    'onMenuShareAppMessage',
                    'onMenuShareTimeline'
                ] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
            });
        }
    })

    window.onpageshow = function (event) {

        if (ob.unRefresh) {
            return;
        }

        var needRefresh = sessionStorage.getItem("need-refresh");
        var lo = location.href;
        if (needRefresh && needRefresh != lo && needRefresh != document.referrer) {
            sessionStorage.removeItem("need-refresh");
            history.go(0);
        }
        sessionStorage.setItem("need-refresh", lo);
    }
    $(document).on("click", "div.share-layer", function () {
        $(this).hide();
    });
    $("div.share-layer").click(function () {
        $(this).hide();
    });

});

//分享功能
ob.share = function () {

    if (ob.mini) {
        TX.MSG.msg("请点击右上角【•••】“转发”按钮，<br />分享到朋友。<br />让大家一起来试试手气吧", {time: 5000}, function () {
        });
        return;
    }

    if ($("div.share-layer").length == 0) {
        var html = [];
        html.push('<div class="share-layer">');
        html.push('       <div class="share-layer-bg"></div>');
        html.push('        <div class="share-layer-con">');
        html.push('         <span class="share-layer-sanjiao"></span>');
        html.push('         <p class="share-layer-p">点击右上角【•••】<br />分享到朋友，朋友圈。<br />让大家一起来试试手气吧</p>');
        html.push('     </div>');
        html.push('</div>');
        $(html.join('')).appendTo($(document.body));
    }

    $("div.share-layer").show().click(function () {
        $(this).hide();
    });

};

//小程序配置
ob.conf = {
    //浏览器
    browser: "/pages/browser/index",
    //支付页面
    pay: "/pages/pay/index"
};