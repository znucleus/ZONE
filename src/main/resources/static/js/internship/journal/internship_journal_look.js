require(["jquery", "handlebars", "nav.active", "quill", "jquery.address", "jquery.print"],
    function ($, Handlebars, navActive, Quill) {

        /*
         ajax url.
         */
        var ajax_url = {
            nav: '/web/menu/internship/journal'
        };

        // 刷新时选中菜单
        navActive(ajax_url.nav);

        /*
         参数id
         */
        var param_id = {
            internshipJournalContent: '#internshipJournalContent',
            internshipJournalHtml: '#internshipJournalHtml'
        };

        /*
         返回
         */
        $('#journalList').click(function () {
            window.history.go(-1);
        });

        /*
         打印
         */
        $('#print_area').click(function () {
            $('#print_content').print({
                globalStyles: true,
                mediaPrint: false,
                stylesheet: null,
                noPrintSelector: ".no-print",
                iframe: true,
                append: null,
                prepend: "<h2 class='text-center'>实习日(周)志<h2/>",
                manuallyCopyFormValues: true,
                deferred: $.Deferred(),
                timeout: 750,
                title: null,
                doctype: '<!doctype html>'
            });
        });

        // 初始化内容与感想富文本框
        var quill = new Quill(param_id.internshipJournalContent, {
            placeholder: '内容与感想',
            theme: 'bubble'
        });

        init();

        function init() {
            quill.enable(false);
            quill.setContents(JSON.parse($(param_id.internshipJournalHtml).val()));
        }
    });