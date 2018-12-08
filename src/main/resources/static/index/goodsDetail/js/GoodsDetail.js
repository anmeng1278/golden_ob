$(function(){
	//banner图片轮播
	var swiper = new Swiper('.swiper-container', {
      pagination: {
        el: '.swiper-pagination',
        type: 'fraction',
      }
    });
    
    //点击立即购买按钮购买弹层显示
    $('.purchase-show-btn').on('click',function(){
    	$('.purchase-layer').show();
    });
    //点击购买弹层背景按钮购买弹层隐藏
    $('.purchase-layer-bg').on('click',function(){
    	$('.purchase-layer').hide();
    });

    //点击加入购物车按钮购买弹层显示
    $('.shopping-cart-btn').on('click',function(){
        $('.shopping-cart').show();
    });
    //加入购物车弹层背景按钮购买弹层隐藏
    $('.shopping-cart-bg').on('click',function(){
        $('.shopping-cart').hide();
    });

    //选规格的点击事件
    $('.purchase-layer-p span').on('click',function(){
    	$(this).addClass('active').siblings().removeClass('active');
    });
    
    //购买数量加的点击事件
	$(".purchase-num-add").click(function(){
		var n=$(this).prev().val();
		var num=parseInt(n)+1;
		if(num==0){ return;}
		$(this).prev().val(num);
	});
	
	//购买数量减的点击事件
	$(".purchase-num-reduce").click(function(){
		var n=$(this).next().val();
		var num=parseInt(n)-1;
		if(num==0){ return}
		$(this).next().val(num);
	});
	
    //选优惠券的点击事件
    $('.purchase-coupon li').on('click',function(){
    	$(this).addClass('active').siblings().removeClass('active');
    });
})
