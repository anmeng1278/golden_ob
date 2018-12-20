$(function(){

    //选择日期
    $('#birthdate').date( function () {
        $("#birthdate").text(arguments[0]);
    });

});