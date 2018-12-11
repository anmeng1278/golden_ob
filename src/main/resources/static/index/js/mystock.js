$(function(){
	//购买数量加的点击事件
	$(".purchase-num-add").click(function(){
		var n=$(this).prev().val();
		var num=parseInt(n)+1;
		var strok = Number($(this).parents('.strok-list-dl').find('.strok-dd-num span').text())+1;
		if(num==strok){ return;}
		$(this).prev().val(num);
		
	});
	
	//购买数量减的点击事件
	$(".purchase-num-reduce").click(function(){
		var n=$(this).next().val();
		var num=parseInt(n)-1;
		if(num==-1){return;}
		$(this).next().val(num);
	});
	
	//点击商品显示商品详情
	$('.strok-list-dl').on('click',function(){
		$(this).find('.goods-detail').show();
		$(this).find('.purchase-num').show();
		$('.footer').show();
	});
	
});