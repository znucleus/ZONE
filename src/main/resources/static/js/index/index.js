/**
 * Created by zbeboy on 2019-10-07.
 */
requirejs.config({
    map: {
        '*': {
            'css': web_path + '/webjars/require-css/css.min.js' // or whatever the path to require-css is
        }
    },
    // pathsオプションの設定。"module/name": "path"を指定します。拡張子（.js）は指定しません。
    paths: {
        "wow": web_path + "/plugins/wow/wow.min",
        "vegas": web_path + "/plugins/vegas/vegas.min",
        "moment": web_path + "/plugins/moment/moment.min",
        "bootstrap": web_path + "/plugins/bootstrap/js/bootstrap.bundle.min"
    },
    // shimオプションの設定。モジュール間の依存関係を定義します。
    shim: {
        "wow": {
            deps: ["jquery", "css!" + web_path + "/plugins/animate/animate.min"]
        },
        "vegas": {
            deps: ["jquery", "css!" + web_path + "/plugins/vegas/vegas.min"]
        },
        "bootstrap": {
            deps: ["jquery"]
        }
    }
});

/*
 捕获全局错误
 */
requirejs.onError = function (err) {
    console.log(err.requireType);
    if (err.requireType === 'timeout') {
        console.log('modules: ' + err.requireModules);
    }
    throw err;
};

// require(["module/name", ...], function(params){ ... });
require(["jquery", "bootstrap", "wow", "vegas"], function ($) {
    // Preloader
    $('.preloader').fadeOut(1000);

    var body = $('body');

    // qq social
    $('.fa-qq').popover({
        trigger: 'focus',
        html: true,
        animation: true,
        content: '<img src="' + web_path + '/images/social-qq.png" style="height: 150px;width: 150px;" alt="QQ群" />',
        placement: 'top'
    });

    // weixin social
    $('.fa-weixin').popover({
        trigger: 'focus',
        html: true,
        animation: true,
        content: '<img src="' + web_path + '/images/social-weixin.jpg" style="height: 150px;width: 150px;" alt="微信公众号" />',
        placement: 'top'
    });

    // weixin small social
    $('.fa-scribd').popover({
        trigger: 'focus',
        html: true,
        animation: true,
        content: '<img src="' + web_path + '/images/social-weixins.jpg" style="height: 150px;width: 150px;" alt="微信小程序" />',
        placement: 'top'
    });

    // pic
    body.vegas({
        slides: [
            {src: web_path + '/images/slide-1.jpg'},
            {src: web_path + '/images/slide-2.jpg'}
        ],
        timer: false,
        transition: ['zoomOut']
    });

    new WOW({mobile: false}).init();

    console.log('%c ██████    █      █   █████', 'color:blue');
    console.log('%c      █    █      █   █   ', 'color:blue');
    console.log('%c    █      ████████   █████', 'color:red');
    console.log('%c  █        █      █   █', 'color:red');
    console.log('%c ██████    █      █   █████', 'color:green');
    console.log('%c Welcome to Z.核.', 'color:red');
});