/**
 * Created by lenovo on 2016-10-30.
 */
define(["jquery"], function ($) {
    return function (activeMenu) {
        $(".br-sideleft-menu ul").css('display','none');
        $(".br-sideleft-menu a").each(function () {
            var otherHref = $(this).attr('href').substring(1);
            if(activeMenu === otherHref){
                $(this).addClass('active'); // add active to li of the current link
                $(this).parent().parent().css('display','block').prev().addClass('active'); // add active class to an anchor
                $(this).parent().parent().parent().parent().parent().addClass("active"); // add active class to an anchor
            } else {
                $(this).removeClass('active');
            }
        });
    }
});