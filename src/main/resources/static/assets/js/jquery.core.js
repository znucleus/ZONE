/**
 * Template Name: Uplon bootstrap 4 Admin Template
 * Author: Coderthemes
 * Module/App: Core js
 */

define(["jquery", "waves", "jquery.nicescroll", "bootstrap-switch"], function ($, Waves) {
    $('[data-toggle="tooltip"]').tooltip();
    $('[data-toggle="popover"]').popover();

    Waves.displayEffect();

    $(".nicescroll").niceScroll({
        cursorcolor: '#98a6ad',
        cursorwidth: '6px',
        cursorborderradius: '5px'
    });

    $("[data-plugin='bootstrap-switch']").bootstrapSwitch();
});