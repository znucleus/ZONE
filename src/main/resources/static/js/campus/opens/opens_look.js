require(["jquery", "handlebars", "nav.active", "quill", "jquery.address"],
    function ($, Handlebars, navActive, Quill) {

        /*
         ajax url.
         */
        var ajax_url = {
            nav: '/web/menu/campus/opens'
        };

        // 刷新时选中菜单
        navActive(ajax_url.nav);

        /*
         参数id
         */
        var param_id = {
            content: '#content',
            jsonContent: '#jsonContent'
        };


        // 初始化内容与感想富文本框
        var quill = new Quill(param_id.content, {
            placeholder: '开学内容',
            theme: 'bubble'
        });

        init();

        function init() {
            quill.enable(false);
            quill.setContents(JSON.parse($(param_id.jsonContent).val()));
        }
    });