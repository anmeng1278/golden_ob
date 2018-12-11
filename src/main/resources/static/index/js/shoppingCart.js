
$(function(){
	var totalprice = 0;
	//购买数量加的点击事件
	$(".purchase-num-add").click(function(){
		totalprice = 0;
		var n = $(this).parents('.purchase-num').find('.num').val();
		var num = parseInt(n)+1;
		if(num==0){ return;}
		if(num > 1){
			$(this).parents('.purchase-num').find('.purchase-num-reduce').show();
			$(this).parents('.purchase-num').find('.purchase-num-del').hide();
		}
		$(this).parents('.purchase-num').find('.num').val(num);
		
		var len = $('.goods-list-item .check-all-xz').length;
		for (var i=0;i < len; i++) {
			var price = $('.check-all-xz:eq('+ i +')').parents('.goods-list-item').find('.goods-dl-price em').text();
			var _num = $('.check-all-xz:eq('+ i +')').parents('.goods-list-item').find('.num').val();
			totalprice += parseFloat(price*_num);
		}
		$('.total-price em').text(totalprice);
	});
	
	//购买数量减的点击事件
	$(".purchase-num-reduce").click(function(){
		totalprice = 0;
		var n = $(this).parents('.purchase-num').find('.num').val();
		var num=parseInt(n)-1;
		if(num==1){
			$(this).parents('.purchase-num').find('.purchase-num-reduce').hide();
			$(this).parents('.purchase-num').find('.purchase-num-del').show();
		}
		if(num==0){return ;}
		$(this).parents('.purchase-num').find('.num').val(num);
		
		var len = $('.goods-list-item .check-all-xz').length;
		for (var i=0;i < len; i++) {
			var price = $('.check-all-xz:eq('+ i +')').parents('.goods-list-item').find('.goods-dl-price em').text();
			var _num = $('.check-all-xz:eq('+ i +')').parents('.goods-list-item').find('.num').val();
			totalprice += parseFloat(price*_num);
		}
		$('.total-price em').text(totalprice);
	});
	
	//删除商品-提示弹层显示
	$('.purchase-num-del').on('click',function(){
		$('.delete-layer').show();
	});
	//删除商品-提示弹层隐藏
	$('.delete-layer-cancel').on('click',function(){
		$('.delete-layer').hide();
	});
	
	//全选
	$('#checkAll').on('click',function(){
		totalprice = 0;
		if($(this).hasClass('check-all-xz')){
			$('.checkone,#checkAll').removeClass('check-all-xz');
			$('.total-price em').text(totalprice);
		}else{
			$('.checkone,#checkAll').addClass('check-all-xz');
			
			//计算总金额展示
			var len = $('.goods-list-item').length;
			for (var i=0;i < len; i++) {
				var price = $('.goods-list-item').eq(i).find('.goods-dl-price em').text();
				var num = $('.goods-list-item').eq(i).find('.num').val();
				totalprice += price*num;
			}
			$('.total-price em').text(totalprice);
		}
	});
	
	//单选
	$('.checkone').on('click',function(){
		var len = $('.goods-list-item').length;
		if($(this).hasClass('check-all-xz')){
			$(this).removeClass('check-all-xz');	//当前div删除选中状态
			var xzlen = $('.goods-list-item .check-all-xz').length;	//获取选中状态的个数
			//判断是否所有商品非选中状态
			if(len != xzlen){
				$('#checkAll').removeClass('check-all-xz');
			}
			
			//计算总金额展示
			var price = $(this).parents('.goods-list-item').find('.goods-dl-price em').text();
			var num = $(this).parents('.goods-list-item').find('.num').val();
			totalprice -= price*num;
			$('.total-price em').text(totalprice);
			
		}else{
			
			$(this).addClass('check-all-xz');	//当前div添加选中状态
			var xzlen = $('.goods-list-item .check-all-xz').length;	//获取选中状态的个数
			//判断是否所有商品全是选中状态
			if(len == xzlen){
				$('#checkAll').addClass('check-all-xz');
			}
			
			//计算总金额展示
			var price = $(this).parents('.goods-list-item').find('.goods-dl-price em').text();
			var num = $(this).parents('.goods-list-item').find('.num').val();
			totalprice += price*num;
			$('.total-price em').text(totalprice);
		}
	});
});
