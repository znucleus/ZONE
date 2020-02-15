//# sourceURL=internship_journal_add.js
require(["jquery", "tools", "handlebars", "sweetalert2", "nav.active", "moment-with-locales", "quill", "KaTeX", "messenger", "jquery.address", "flatpickr-zh"],
    function ($, tools, Handlebars, Swal, navActive, moment, Quill, KaTeX) {

        window.katex = KaTeX;
        moment.locale('zh-cn');

        /*
         ajax url.
         */
        var ajax_url = {
            save: web_path + '/web/internship/journal/save',
            page: '/web/menu/internship/journal'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
       返回
       */
        $('#journalList').click(function () {
            window.history.go(-1);
        });

        /*
         参数id
         */
        var param_id = {
            internshipJournalContent: '#internshipJournalContent',
            internshipJournalHtml: '#internshipJournalHtml',
            internshipJournalContentText: '#internshipJournalContentText',
            internshipJournalDate: '#internshipJournalDate'
        };

        var button_id = {
            save: {
                id: '#save',
                text: '保存',
                tip: '保存中...'
            }
        };

        /*
         参数
         */
        var param = {
            internshipJournalContent: '',
            internshipJournalDate: ''
        };

        // 初始化内容与感想富文本框
        var quill = new Quill(param_id.internshipJournalContent, {
            modules: {
                formula: true,
                syntax: true,
                toolbar: '#toolbar-container'
            },
            placeholder: '内容与感想',
            theme: 'snow'
        });

        /**
         * 初始化参数
         */
        function initParam() {
            param.internshipJournalContent = quill.getText(0, quill.getLength());
            param.internshipJournalDate = $(param_id.internshipJournalDate).val();
            $(param_id.internshipJournalHtml).val(JSON.stringify(quill.getContents()));
            $(param_id.internshipJournalContentText).val(param.internshipJournalContent);
        }

        $(param_id.internshipJournalDate).flatpickr({
            "locale": "zh",
            defaultDate: moment().format("YYYY-MM-DD")
        });

        /*
         保存数据
         */
        $(button_id.save.id).click(function () {
            initParam();
            validInternshipJournalContent();
        });

        /**
         * 检验内容与感想
         */
        function validInternshipJournalContent() {
            var internshipJournalContent = param.internshipJournalContent;
            if (internshipJournalContent.length <= 1) {
                Messenger().post({
                    message: '内容与感想不能为空',
                    type: 'error',
                    showCloseButton: true
                });
            } else {
                validInternshipJournalDate();
            }
        }

        function validInternshipJournalDate() {
            var internshipJournalDate = param.internshipJournalDate;
            if (internshipJournalDate.length <= 0) {
                Messenger().post({
                    message: '请选择日志日期',
                    type: 'error',
                    showCloseButton: true
                });
            } else {
                sendAjax();
            }
        }

        /**
         * 发送数据到后台
         */
        function sendAjax() {
            tools.buttonLoading(button_id.save.id, button_id.save.tip);
            $.ajax({
                type: 'POST',
                url: ajax_url.save,
                data: $('#app_form').serialize(),
                success: function (data) {
                    tools.buttonEndLoading(button_id.save.id, button_id.save.text);
                    if (data.state) {
                        Swal.fire({
                            title: data.msg,
                            type: "success",
                            confirmButtonText: "确定",
                            preConfirm: function () {
                                window.history.go(-1);
                            }
                        });
                    } else {
                        Swal.fire('保存失败', data.msg, 'error');
                    }
                },
                error: function (XMLHttpRequest) {
                    tools.buttonEndLoading(button_id.save.id, button_id.save.text);
                    Messenger().post({
                        message: 'Request error : ' + XMLHttpRequest.status + " " + XMLHttpRequest.statusText,
                        type: 'error',
                        showCloseButton: true
                    });
                }
            });
        }

    });