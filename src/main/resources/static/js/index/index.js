requirejs.config({
    // shimオプションの設定。モジュール間の依存関係を定義します。
    shim: {
        "app": {
            deps: ["particles"]
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
require(["jquery", "modernizr.custom.86080", "app"], function ($) {
    var ajax_url = {
        login: web_path + '/login'
    };

    $('#login').click(function () {
        window.location.href = ajax_url.login;
    });

    console.log('%c ██████    █      █   █████', 'color:blue');
    console.log('%c      █    █      █   █   ', 'color:blue');
    console.log('%c    █      ████████   █████', 'color:red');
    console.log('%c  █        █      █   █', 'color:red');
    console.log('%c ██████    █      █   █████', 'color:green');
    console.log('%c Welcome to Z.核.', 'color:red');
});
