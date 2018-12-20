$(function(){
	//选项卡
    $('.city-list-ul li').click(function () {
        var index = $(this).index();
        $(this).addClass('city-cur').siblings().removeClass('city-cur');
        $('.city-list-item').eq(index).show().siblings().hide();
        $("html,body").animate({
            scrollTop: "0px"
        }, 300);
    });
    
    //点击搜索显示
    $('.search-btn').click(function () {
        $('.search-layer').show()
    });
    
    //点击取消搜索隐藏
    $('#cancelBtn').click(function () {
        $('.search-layer').hide()
    });
    
	
	//右侧点击 定位滑动
    $(".slider-nav a,.slider-nav2 a").click(function () {
        $("html,body").animate({
            scrollTop: $($(this).attr("href")).offset().top - 60 + "px"
        }, 300);
        return false;
    });

    $("#SelfCity").click(function () {
        $("div.content-city").show();
        $("div.content-form").hide();
    });

    //初始化贵宾厅数据
    var airports = [];
    $("span[data-airport-id]").each(function () {
        var code = $(this).attr("data-airport-code");
        var airportId = $(this).attr("data-airport-id");
        var ifhot = $(this).attr("data-ifhot");
        var airportName = $(this).html();
        airports.push({id: airportId, name: airportName, code: code, ifhot: ifhot});
    });

    function getAirports(key) {
        if (!key || $.trim(key).length == 0) {
            var items = [];
            var res = JSLINQ(airports).where(function () {
                return this.ifhot == "true";
            }).select(function () {
                return this;
            })
            return res.items;
        } else {
            var res = JSLINQ(airports).where(function () {
                return this.name.indexOf(key) > -1 || this.code.toLowerCase().indexOf(key) > -1;
            }).select(function () {
                return this;
            });
            return res.items;
        }
    }

    function createHtml(items, key) {
        $("div.search_list ul li").remove();
        if (!items || items.length == 0) {
            return;
        }
        var html = [];
        $.each(items, function (i, item) {
            html.push("<li data-airport-id='" + item.id + "' data-airport-code='" + item.code + "'>" + item.name + "</li>")
        });
        $("div.search_list ul").append(html.join(""));
    }

    //搜索框事件
    $("#txbName").keyup(function () {
        var items = getAirports(this.value);
        createHtml(items);
    });

    //点击服务网点
    $(document).on("click", "*[data-airport-id]", function(){

        var airportName = $(this).html();
        var airportCode = $(this).attr("data-airport-code");

        $("div.content-city").hide();
        $("div.content-form").show();

        $("#SelfCity").html(airportName);
        $(":hidden[name='airportName']").val(airportName);
        $(":hidden[name='airportCode']").val(airportCode);

    });

    //网点关闭
    $("div.content-city img.close-btn").click(function () {
        $("div.content-city").hide();
        $("div.content-form").show();
    });

});