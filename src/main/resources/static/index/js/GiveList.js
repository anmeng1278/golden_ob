$(function () {
    $("ul.nav-list-ul li").click(function () {

        $("ul.nav-list-ul li").removeClass("active");
        $(this).addClass('active');

        var index = $(this).index();
        setTimeout(function () {
            $('div.give-item').hide();
            $('div.give-item').eq(index).show();
        }, 0);


    });
})
