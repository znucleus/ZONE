//# sourceURL=internship_change_company_history.js
require(["jquery", "tools", "handlebars", "nav.active", "jquery.address", "css!" + web_path + "/plugins/timeline/timeline.min.css"],
    function ($, tools, Handlebars, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            data: web_path + '/web/internship/statistical/record/company/data',
            page: '/web/menu/internship/statistical'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        var page_param = {
            paramInternshipReleaseId: $('#paramInternshipReleaseId').val(),
            paramStudentId: $('#paramStudentId').val()
        };

        /**
         * 列表数据
         * @param data 数据
         */
        function listData(data) {
            var template = Handlebars.compile($("#timeline-template").html());
            var count = 0;

            Handlebars.registerHelper('timeline_state_css', function () {
                return new Handlebars.SafeString(Handlebars.escapeExpression('info'));
            });

            Handlebars.registerHelper('icon', function () {
                return new Handlebars.SafeString(Handlebars.escapeExpression('fa fa-pencil'));
            });

            Handlebars.registerHelper('time', function () {
                return new Handlebars.SafeString(Handlebars.escapeExpression(this.changeTimeStr));
            });

            Handlebars.registerHelper('inverted', function () {
                var value = '';
                if (count % 2 === 0) {
                    value = Handlebars.escapeExpression('timeline-inverted');
                } else {
                    value = Handlebars.escapeExpression('');
                }
                count++;
                return new Handlebars.SafeString(value);
            });
            $('#timeData').html(template(data));
        }

        init();

        /**
         * 初始化数据
         */
        function init() {
            tools.dataLoading();
            $.get(ajax_url.data + '/' + page_param.paramInternshipReleaseId + "/" + page_param.paramStudentId, function (data) {
                tools.dataEndLoading();
                listData(data);
            });
        }

    });