/**
 * Created by lenovo on 2016/10/25.
 */
define(["jquery", "tools", "jquery.showLoading", "messenger"], function ($, tools) {

    var loadingMessage;

    /**
     * load js
     * @param jsPath js path.
     */
    function loadJs(jsPath) {
        $.getScript(jsPath)
            .done(function (script, textStatus) {
                console.log(textStatus);
            })
            .fail(function (jqxhr, settings, exception) {
                loadingMessage.update({
                    message: 'Loading js fail !',
                    type: 'error',
                    showCloseButton: true
                });
            });
    }

    return function (url, targetId, web_path) {
        tools.dataEndLoading();
        loadingMessage = Messenger().post({
            message: 'Loading ...',
            type: 'info',
            id: 'loadingMessage',
            showCloseButton: true
        });
        tools.dataLoading();
        $.ajax({
            type: 'GET',
            url: web_path + url,
            success: function (data) {
                $(targetId).html($(data).html());
                var scripts = $('.dy_script');
                for (var i = 0; i < scripts.length; i++) {
                    loadJs(web_path + $(scripts[i]).val());
                }
                tools.dataEndLoading();
                loadingMessage.update({
                    message: 'Loading finish , enjoy you life !',
                    type: 'success',
                    showCloseButton: true
                });
            },
            error: function (XMLHttpRequest) {
                tools.dataEndLoading();
                loadingMessage.update({
                    message: 'Loading html error ! ' + XMLHttpRequest.status + " " + XMLHttpRequest.statusText,
                    type: 'error',
                    showCloseButton: true
                });
            }
        });
    };
});