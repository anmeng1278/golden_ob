var seckillObj = {
    //项目根路径
    contextPath: "",
    url: {
        getSystemTime: function() {
            return seckillObj.contextPath + "/getSystemTime";
        },
        seckill: function() {
            return seckillObj.contextPath + "seckill";
        },
        getSeckillOrder: function() {
            return seckillObj.contextPath + "getSeckillOrder";
        }
    },
    fun: {
        initDetail: function(productId, beginTime, endTime, productKey) {
            $.ajax({
                url: seckillObj.url.getSystemTime(),
                dataType: JSON,
                success: function(data) {
                    if (data < beginTime) {
                        $("#seckillBox").countdown(beginTime,
                            function(event) {
                                var str = event.strftime("距离开始时间还有    %D天 %H小时 %M分钟 %S秒");
                                $("#seckillBox").html(str);
                                $("#seckillBox").css("color", "red");
                                $("#seckillBox").css("color", "red")
                            });
                    } else if (data > beginTime) {
                        $("#seckillBox").html("秒杀已结束")
                    } else {
                        //进行秒杀
                        seckillObj.fun.seckill(productId, productKey);
                    }
                },
                error: function f() {
                    alert("网络错误");
                }
            })
        },
        seckill:function (productId,productKey) {
            $("#seckillBox").html("<input type='button' id='seckillBtn' value='立即秒杀'> ");

            $("seckillBtn").bind("onclick",function () {
                alert("点击了秒杀按钮")
                $(this).prop("disabled",true);
                $.ajax({
                    url:seckillObj.url.seckill(),
                    type:JSON,
                    data:{
                        productId:productId,
                        productKey:productKey
                    },
                    success: function (resp) {
                        if (resp.baseResp.success) {
                            //成功获得秒杀订单
                            seckillObj.fun.getSeckillOrder(productKey);
                        }
                    },
                    error:function(){
                        alert("网络错误")
                    }

                })
            })
        },
        getSeckillOrder:function (productKey) {
            $.ajax({
                url:seckillObj.url.getSeckillOrder(),
                dataType:"json",
                data:{
                    productKey:productKey
                },
                type:"get",
                success:function(data){
                    if(data.errorCode=="0"){
                        $("#seckillBox").html("下单成功请在45分钟内进行支付！");
                        return false;
                    }
                    window.setTimeout(seckillObj.fun.getSeckillOrder(productKey),3000);
                },
                error:function(){
                    alert("网络错误！")
                }
            })

        }
    }
}
