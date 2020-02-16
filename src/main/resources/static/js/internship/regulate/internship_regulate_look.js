require(["jquery", "nav.active"],
    function ($, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            page: '/web/menu/internship/regulate'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
         返回
         */
        $('#regulateList').click(function () {
            window.history.go(-1);
        });

    });