function createOrder(data, callback) {

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