//rem单位计算
(function () {
    var docEl = document.documentElement;
    var resize = 'orientationchange' in window ? 'orientationchange' : 'resize';
    var setRem = function () {
        var screenWidth = docEl.clientWidth || window.screen.width || 360;
        if(screenWidth>800){
            docEl.style.fontSize = (100 * screenWidth / 1920) + 'px';
        }else{
    	    docEl.style.fontSize = (100 * screenWidth / 800) + 'px';
            }
        };
 
        window.addEventListener('resize', setRem, false);
        setRem();
})();