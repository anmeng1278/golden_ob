$(function(){
	//倒计时
	var setTimer = null;
	var chazhi = 0;
	//差值计算
	//例子(模拟)
	chazhi = 15 * 60000;
	//执行函数部分
	countFunc(chazhi);
	setTimer = setInterval(function() {
		chazhi = chazhi - 1000;
		countFunc(chazhi);
	}, 1000);
})
//计算时间
function countFunc(leftTime) {
	if(leftTime >= 0) {
		var minutes = parseInt(leftTime / 1000 / 60 % 60, 10); //计算剩余的分钟 
		var seconds = parseInt(leftTime / 1000 % 60, 10); //计算剩余的秒数 
		minutes = checkTime(minutes);
		seconds = checkTime(seconds);
		$(".joinm").html(minutes);
		$(".joins").html(seconds);
	} else {
		clearInterval(setTimer);
		$(".joinm").html("00");
		$(".joins").html("00");
	}
}
//将0-9的数字前面加上0，例1变为01
function checkTime(i) { 
	if(i < 10) {
		i = "0" + i;
	}
	return i;
}