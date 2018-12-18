if (typeof shareConfig == "undefined") {
    shareConfig = {
        title: document && document.title,
        desc: "",
        link: location.href,
        //TODO 上线修改
        imgUrl: "https://hezy-static.oss-cn-shanghai.aliyuncs.com/test/product/oncecard_cover.png"
    };
}

wx.ready(function () {

    if (shareConfig.link) {
        if (shareConfig.link.indexOf(location.host) == -1) {
            var url = location.protocol + "//" + location.host;
            shareConfig.link = url + shareConfig.link;
        }
    }

    shareConfig.desc = shareConfig.desc || "未设置分享描述";

    wx.onMenuShareAppMessage({
        title: shareConfig.title, // 分享标题
        desc: shareConfig.desc, // 分享描述
        link: shareConfig.link, // 分享链接
        imgUrl: shareConfig.imgUrl, // 分享图标
        type: '', // 分享类型,music、video或link，不填默认为link
        dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
        success: function (resp) {
            // 用户确认分享后执行的回调函数
            if (typeof shareSuccess != "undefined") {
                shareSuccess();
            }
        },
        cancel: function () {
            // 用户取消分享后执行的回调函数
        }
    });


    wx.onMenuShareTimeline({
        title: shareConfig.title, // 分享标题
        link: shareConfig.link, // 分享链接
        imgUrl: shareConfig.imgUrl, // 分享图标
        success: function (resp) {

            alert(JSON.stringify(resp));

            // 用户确认分享后执行的回调函数
            if (typeof shareSuccess != "undefined") {
                shareSuccess();
            }
        },
        cancel: function () {
            // 用户取消分享后执行的回调函数
        }
    });

});