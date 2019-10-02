/**
 * Created by lenovo on 2016-10-30.
 */
define(["jquery"], function ($) {
    return function (activeMenu) {
        $(".navigation-menu li").removeClass('active');

        $(".navigation-menu a").each(function () {
            var otherHref = $(this).attr('href').substring(1);
            if(activeMenu === otherHref){
                $(this).parent().addClass("active"); // add active to li of the current link
                $(this).parent().parent().parent().addClass("active"); // add active class to an anchor
                $(this).parent().parent().parent().parent().parent().addClass("active"); // add active class to an anchor
            }
        });
    }
});