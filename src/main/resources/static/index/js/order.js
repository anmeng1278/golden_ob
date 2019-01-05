function createOrder(data, callback) {

    //{\"appId\":\"wx555b169abb4d62ce\",\"timeStamp\":\"1546596919\",\"nonceStr\":\"nsupowk0oknvji2lhn7ncfh8trh1tggv\",\"package\":\"prepay_id=wx041015200353636bc361b7940149570223\",\"signType\":\"MD5\",\"paySign\":\"22AAED9B6A767C20008DC1A551306FDF\"}
    // var timestamp = "1546596919";
    // var nonceStr = "nsupowk0oknvji2lhn7ncfh8trh1tggv";
    // var package = encodeURIComponent("prepay_id=wx041015200353636bc361b7940149570223");
    // var signType = "MD5";
    // var paySign = "22AAED9B6A767C20008DC1A551306FDF";
    //
    // var url = '/pages/obpay/index?timeStamp=' + timestamp + '&nonceStr=' + nonceStr + '&_package=' + package + '&signType=' + signType + '&paySign=' + paySign;
    //
    // wx.miniProgram.navigateTo({
    //     url: url,
    //     success: function (resp) {
    //         // 打开成功
    //         // alert(JSON.stringify(resp));
    //     },
    //     fail: function (resp) {
    //         // alert(JSON.stringify(resp));
    //     },
    //     complete: function (resp) {
    //         // alert(JSON.stringify(resp));
    //     }
    // })
    //
    // return;

    // if (ob.mini && data.activityTypeId != 40) {
    //     TX.MSG.msg("小程序暂不支持商品购买，<br />请到“空铁管家”微信公众号上操作。", {time: 3000}, function () {
    //     });
    //     return;
    // }

    TX.CORE.p({
        url: "/product/createOrder",
        data: data,
        success: function (resp) {

            if (!resp.baseResp.success) {
                TX.MSG.msg(resp.baseResp.message, {icon: 2});
                return;
            }
            //订单列表
            var url = resp.baseResp.url;
            //活动类型
            var activityType = resp.datas.activityType;
            //支付成功
            var successUrl = resp.datas.successUrl;

            if (activityType == 20) {
                //秒杀订单
                if (resp.datas.resp.orderId > 0) {
                    TX.MSG.msg("秒杀成功", {time: 1500}, function () {
                        if (resp.datas.resp.amount > 0) {
                            //秒杀订单需要支付
                            callPay(resp, successUrl, url);
                        } else {
                            //无需支付,跳到支付成功页面
                            location.href = successUrl;
                        }
                    });
                } else {
                    TX.MSG.msg("秒杀成功，请到订单列表页面完成支付。", {time: 1500}, function () {
                        //跳转到订单列表页面
                        location.href = url;
                    });
                }
            } else {
                TX.MSG.msg(resp.baseResp.message, {time: 1500}, function () {
                    if (resp.datas.resp.amount > 0) {
                        //普通订单需要支付
                        callPay(resp, successUrl, url);
                    } else {
                        //无需支付,跳到支付成功页面
                        location.href = successUrl;
                    }
                });
            }

            //调起微信支付
            function callPay(resp, successUrl, url) {

                var timestamp = resp.datas.pay.responseBody.timeStamp;
                var nonceStr = resp.datas.pay.responseBody.nonceStr;
                var package = resp.datas.pay.responseBody._package;
                var signType = resp.datas.pay.responseBody.signType;
                var paySign = resp.datas.pay.responseBody.paySign;
                var appId = resp.datas.pay.responseBody.appId;

                if (ob.mini == true) {

                    successUrl = encodeURIComponent(ob.host + successUrl);
                    url = encodeURIComponent(ob.host + url);

                    package = encodeURIComponent(package);

                    var miniUrl = '/pages/obpay/index?source=2&timeStamp=' + timestamp + '&nonceStr=' + nonceStr + '&_package=' + package + '&signType=' + signType + '&paySign=' + paySign;
                    miniUrl += "&successUrl=" + successUrl;
                    miniUrl += "&failUrl=" + url;

                    wx.miniProgram.navigateTo({
                        url: miniUrl,
                        success: function (resp) {
                            // 打开成功
                            // alert(JSON.stringify(resp));
                        },
                        fail: function (resp) {
                            // alert(JSON.stringify(resp));
                        },
                        complete: function (resp) {
                            // alert(JSON.stringify(resp));
                        }
                    })

                    return;
                }

                //调起微信支付
                wx.chooseWXPay({
                    appId: appId,
                    timestamp: timestamp,
                    nonceStr: nonceStr,
                    package: package,
                    signType: signType,
                    paySign: paySign,
                    success: function (resp) {
                        location.href = successUrl;
                    },
                    fail: function (resp) {
                        TX.MSG.msg("对不起，支付失败了！", {time: 1500}, function () {
                            location.href = url;
                        });
                    },
                    cancel: function (resp) {
                        TX.MSG.msg("您已取消支付！", {time: 1500}, function () {
                            location.href = url;
                        });
                    }
                });
            }

        }
    });

}

function calculateProductOrder(data, callback) {
    TX.CORE.p({
        url: "/product/calculateOrder",
        data: data,
        success: function (resp) {
            callback && callback(resp);
        }
    });
}