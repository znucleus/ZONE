/**
 * Template Name: Uplon bootstrap 4 Admin Template
 * Author: Coderthemes
 * Module/App: Main Js
 */


!function ($) {
    "use strict";

    var Navbar = function () {
    };

    //navbar - topbar
    Navbar.prototype.init = function () {
        //toggle
        $('.navbar-toggle').on('click', function (event) {
            $(this).toggleClass('open');
            $('#navigation').slideToggle(400);
        });

        if ($('.navigation-menu>li').length > 1) {
            $('.navigation-menu>li').slice(-1).addClass('last-elements');
        }

        $('.navigation-menu li.has-submenu a[href="javascript:;"]').on('click', function (e) {

            $('.navigation-menu li.has-submenu a[href="javascript:;"]').each(function () {
                if ($(window).width() < 992) {
                    $(this).parent('li').removeClass('open').find('.submenu:first').removeClass('open');
                }
            });

            if ($(window).width() < 992) {
                e.preventDefault();
                $(this).parent('li').toggleClass('open').find('.submenu:first').toggleClass('open');
            }
        });

        $(".right-bar-toggle").click(function () {
            $(".right-bar").toggle();
            $('.wrapper').toggleClass('right-bar-enabled');
        });
    },
        //init
        $.Navbar = new Navbar, $.Navbar.Constructor = Navbar
}(window.jQuery),

//initializing
    function ($) {
        "use strict";
        $.Navbar.init()
    }(window.jQuery);


// === following js will activate the menu in left side bar based on url ====
$(document).ready(function () {
    $(".navigation-menu a").each(function () {
        var pageUrl = window.location.href.split(/[?#]/);
        var curUrl = this.href.split(/[?#]/);
        if (pageUrl.length > 1 && curUrl.length > 1) {
            if (curUrl[1] ===  pageUrl[1]) {
                $(this).parent().addClass("active"); // add active to li of the current link
                $(this).parent().parent().parent().addClass("active"); // add active class to an anchor
                $(this).parent().parent().parent().parent().parent().addClass("active"); // add active class to an anchor
            }
        }

    });
});

