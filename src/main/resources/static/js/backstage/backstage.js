requirejs.config({
    map: {
        '*': {
            'css': web_path + '/webjars/require-css/css.min.js' // or whatever the path to require-css is
        }
    },
    // pathsオプションの設定。"module/name": "path"を指定します。拡張子（.js）は指定しません。
    paths: {
        "csrf": web_path + "/js/util/csrf",
        "tools": web_path + "/js/util/tools",
        "bootstrap": web_path + "/plugins/bootstrap/js/bootstrap.bundle.min",
        "bracket": web_path + "/plugins/bracket/js/bracket.min",
        "perfect-scrollbar.jquery": web_path + "/plugins/perfect-scrollbar/js/perfect-scrollbar.jquery.min",
        "peity": web_path + "/plugins/peity/jquery.peity.min"
    },
    // shimオプションの設定。モジュール間の依存関係を定義します。
    shim: {
        "tools": {
            deps: ["jquery"]
        },
        "bootstrap": {
            deps: ["jquery"]
        },
        "peity":{
            deps: ["jquery"]
        },
        "perfect-scrollbar.jquery":{
            deps: ["jquery"]
        },
        "bracket": {
            deps: ["jquery", "perfect-scrollbar.jquery", "peity"]
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
require(["jquery", "requirejs-domready", "bootstrap", "bracket"],
    function ($, domready) {
        domready(function () {

        });
    });