//# sourceURL=internship_journal_statistical.js
require(["jquery", "tools", "handlebars", "nav.active", "jquery.address", "messenger", "tablesaw"],
    function ($, tools, Handlebars, navActive) {

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/internship/journal/statistical/data',
                page: '/web/menu/internship/journal'
            };
        }

        // 刷新时选中菜单
        navActive(getAjaxUrl().page);

        /*
       返回
       */
        $('#journalList').click(function () {
            window.history.go(-1);
        });

        var page_param = {
            internshipReleaseId: $('#internshipReleaseId').val(),
            staffId: $('#staffId').val()
        };


        var tableElement = $('#dataTable');

        initJournalCount();

        /**
         * 初始化小组内个人日志统计
         */
        function initJournalCount() {
            tools.dataLoading();
            $.get(getAjaxUrl().data + '/' + page_param.internshipReleaseId + "/" + page_param.staffId, function (data) {
                tools.dataEndLoading();
                if (data.state) {
                    listData(data);
                } else {
                    Messenger().post({
                        message: data.msg,
                        type: 'error',
                        showCloseButton: true
                    });
                }
            });
        }

        /**
         * 列表数据
         * @param data 数据
         */
        function listData(data) {
            var template = Handlebars.compile($("#journal-template").html());
            Handlebars.registerHelper("addOne", function (index) {
                //返回+1之后的结果
                return index + 1;
            });
            Handlebars.registerHelper('student_number', function () {
                var studentNumber = this.studentNumber ? this.studentNumber : '无数据';
                return new Handlebars.SafeString(Handlebars.escapeExpression(studentNumber));
            });
            Handlebars.registerHelper('journal_num', function () {
                var journalNum = this.journalNum ? this.journalNum : 0;
                return new Handlebars.SafeString(Handlebars.escapeExpression(journalNum));
            });
            $('#dataTable > tbody').html(template(data));
            tableElement.tablesaw().data("tablesaw").refresh();
        }


        $('#refresh').click(function () {
            initJournalCount();
        });
    });