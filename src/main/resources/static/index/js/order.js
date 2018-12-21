function createOrder(data) {

    TX.CORE.p({
        url: "/product/createOrder",
        data: data,
        success: function (resp) {

            var url = resp.baseResp.url;
            if (resp.baseResp.success) {

                var successUrl = resp.datas.successUrl;
                TX.MSG.msg(resp.baseResp.message, {time: 1500}, function () {

                    if (resp.datas.resp.amount > 0) {

                        if (typeof wx == "undefined") {
                            TX.MSG.msg("当前环境无法调用微信客户端！", {time: 1500}, function () {
                            });
                            return;
                        }

                        var appId = resp.datas.pay.responseBody.appId;
                        var timestamp = resp.datas.pay.responseBody.timeStamp;
                        var nonceStr = resp.datas.pay.responseBody.nonceStr;
                        var package = resp.datas.pay.responseBody._package;
                        var signType = resp.datas.pay.responseBody.signType;
                        var paySign = resp.datas.pay.responseBody.paySign;

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
                    } else {
                        //无需支付，跳转到订单列表页面
                        location.href = url;
                    }
                });
            } else {
                TX.MSG.msg(resp.baseResp.message, {icon: 2});
            }
        }
    });


}