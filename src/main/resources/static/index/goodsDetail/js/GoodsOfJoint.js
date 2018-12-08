$(function(){
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
});