//# sourceURL=timetable_release_blank.js
require(["jquery", "tools", "sweetalert2", "nav.active", "messenger", "jquery.address", "bootstrap-maxlength"],
    function ($, tools, Swal, navActive) {
        /*
        ajax url.
        */
        var ajax_url = {
            save: web_path + '/web/campus/timetable/save',
            page: '/web/menu/campus/timetable'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
        参数id
        */
        var param_id = {
            title: '#title',
            startYear: '#startYear',
            endYear: '#endYear',
            term: '#term'
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
            title: '',
            startYear: '',
            endYear: '',
            term: ''
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.title = $(param_id.title).val();
            param.startYear = $(param_id.startYear).val();
            param.endYear = $(param_id.endYear).val();
            param.term = $(param_id.term).val();
        }

        init();

        function init() {
            initMaxLength();
            initStartYear();
            initEndYear();
        }

        /**
         * 初始化Input max length
         */
        function initMaxLength() {
            $(param_id.title).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });
        }

        function initStartYear() {
            var date = new Date();
            var year = date.getFullYear();
            for (var i = year + 2; i >= year - 4; i--) {
                $(param_id.startYear).append('<option value="' + i + '">' + i + '</option>');
            }
        }

        function initEndYear() {
            var date = new Date();
            var year = date.getFullYear();
            for (var i = year + 3; i >= year - 3; i--) {
                $(param_id.endYear).append('<option value="' + i + '">' + i + '</option>');
            }
        }

        $(param_id.title).blur(function () {
            initParam();
            var title = param.title;
            if (title.length <= 0 || title.length > 100) {
                tools.validErrorDom(param_id.title, '标题100个字符以内');
            } else {
                tools.validSuccessDom(param_id.title);
            }
        });

        /*
        保存数据
        */
        $(button_id.save.id).click(function () {
            initParam();
            validTitle();
        });

        function validTitle() {
            var title = param.title;
            if (title.length <= 0 || title.length > 100) {
                tools.validErrorDom(param_id.title, '标题100个字符以内');
            } else {
                tools.validSuccessDom(param_id.title);
                validStartYear();
            }
        }

        function validStartYear() {
            var startYear = param.startYear;
            if (startYear.length <= 0) {
                tools.validErrorDom(param_id.startYear, '请选择开始学年');
            } else {
                tools.validSuccessDom(param_id.startYear);
                validEndYear();
            }
        }

        function validEndYear() {
            var endYear = param.endYear;
            if (endYear.length <= 0) {
                tools.validErrorDom(param_id.endYear, '请选择结束学年');
            } else {
                tools.validSuccessDom(param_id.endYear);
                validTerm();
            }
        }

        function validTerm() {
            var term = param.term;
            if (term === '') {
                tools.validErrorDom(param_id.term, '请选择学期');
            } else {
                tools.validSuccessDom(param_id.term);
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
                data: param,
                success: function (data) {
                    tools.buttonEndLoading(button_id.save.id, button_id.save.text);
                    if (data.state) {
                        Swal.fire({
                            title: data.msg,
                            type: "success",
                            confirmButtonText: "确定",
                            preConfirm: function () {
                                $.address.value(ajax_url.page);
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