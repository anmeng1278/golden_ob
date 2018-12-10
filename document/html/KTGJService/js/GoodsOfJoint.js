$(function(){
	//购买数量加的点击事件
	$(".purchase-num-add").click(function(){
		var n=$(this).prev().val();
		var price = $('#totalprice').val();
		var num=parseInt(n)+1;
		if(num==0){ return;}
		var totalprice = num * price;
		$('.shopping-price em').text(totalprice)
		$(this).prev().val(num);
	});
	
	//购买数量减的点击事件
	$(".purchase-num-reduce").click(function(){
		var n=$(this).next().val();
		var price = $('#totalprice').val();
		var num=parseInt(n)-1;
		if(num==0){ return}
		var totalprice = num * price;
		$('.shopping-price em').text(totalprice)
		$(this).next().val(num);
	});
});