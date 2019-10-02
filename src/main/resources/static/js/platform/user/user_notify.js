//# sourceURL=user_notify.js
require(["jquery", "tools", "handlebars", "moment-with-locales", "bootstrap",
        "csrf", "jquery.simple-pagination"],
    function ($, tools, Handlebars, moment) {

        moment.locale('zh-cn');

        var ajax_url = {
            user_notify: web_path + '/user/data/notify',
            user_notify_read: web_path + '/user/notify/read'
        };

        var param = {
            pageNum: 0,
            length: 10,
            displayedPages: 3,
            orderColumnName: 'createDate',
            orderDir: 'desc',
            extraSearch: JSON.stringify({
                isSee: 0
            })
        };

        // 只在unRead请求后台更新状态
        var isReadModal = false;

        $('#userNotifyUnRead').click(function () {
            isReadModal = false;
            $('#userNotifyRead').removeClass('active');
            $(this).addClass('active');
            param.pageNum = 0;
            param.extraSearch = JSON.stringify({isSee: 0});
            init();
        });

        $('#userNotifyRead').click(function () {
            isReadModal = true;
            $('#userNotifyUnRead').removeClass('active');
            $(this).addClass('active');
            param.pageNum = 0;
            param.extraSearch = JSON.stringify({isSee: 1});
            init();
        });

        var tableData = $('#accordion');

        init();

        /**
         * 初始化数据
         */
        function init() {
            tools.dataLoading();
            $.get(ajax_url.user_notify, param, function (data) {
                tools.dataEndLoading();
                if(!isReadModal){
                    $('#unReadNotifyUnReadNum').text(data.page.totalSize);
                } else {
                    $('#unReadNotifyReadNum').text(data.page.totalSize);
                }

                createPage(data);
                listData(data);
            });
        }

        /**
         * 创建分页
         * @param data 数据
         */
        function createPage(data) {
            $('#pagination').pagination({
                pages: data.page.totalPages,
                displayedPages: data.page.displayedPages,
                hrefTextPrefix: '',
                prevText: '上一页',
                nextText: '下一页',
                cssStyle: '',
                listStyle: 'pagination',
                onPageClick: function (pageNumber, event) {
                    // Callback triggered when a page is clicked
                    // Page number is given as an optional parameter
                    nextPage(pageNumber);
                }
            });
        }

        /**
         * 下一页
         * @param pageNumber 当前页
         */
        function nextPage(pageNumber) {
            param.pageNum = pageNumber;
            tools.dataLoading();
            $.get(ajax_url.user_notify, param, function (data) {
                tools.dataEndLoading();
                listData(data);
            });
        }

        /**
         * 列表数据
         * @param data 数据
         */
        function listData(data) {
            var template = Handlebars.compile($("#user-notify-template").html());

            Handlebars.registerHelper('notify_title', function () {
                return new Handlebars.SafeString(Handlebars.escapeExpression(this.notifyTitle));
            });

            tableData.html(template(data));
        }

        tableData.delegate('.userNotify', "click", function () {
            if(!isReadModal){
                var id = $(this).attr('data-id');
                $.post(ajax_url.user_notify_read + '/' + id);
            }
        });
    });