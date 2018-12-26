if (typeof shareConfig == "undefined") {
    var url = location.protocol + "//" + location.host + virtualPath;
    shareConfig = {
        title: "空铁管家",
        desc: "服务覆盖全国100个城市，300万商旅人士的出行首选！",
        link: url,
        imgUrl: "http://img.jsjinfo.cn/3b43f3ee6d6e7309515811cfab50cd68"
    };
}

wx.ready(function () {

    var url = location.protocol + "//" + location.host;
    console.log(shareConfig)
    if (shareConfig.link) {
        if (shareConfig.link.indexOf(location.host) == -1) {
            shareConfig.link = url + shareConfig.link;
        }
    } else {
        shareConfig.link = url;
    }

    shareConfig.desc = shareConfig.desc || "服务覆盖全国100个城市，300万商旅人士的出行首选！";
    shareConfig.title = shareConfig.title || "空铁管家";
    shareConfig.imgUrl = shareConfig.imgUrl || "http://img.jsjinfo.cn/3b43f3ee6d6e7309515811cfab50cd68"

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