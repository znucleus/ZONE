//# sourceURL=internship_regulate_add.js
require(["jquery", "tools", "handlebars", "sweetalert2", "nav.active", "moment-with-locales", "messenger", "jquery.address",
        "flatpickr-zh", "select2-zh-CN", "bootstrap-maxlength"],
    function ($, tools, Handlebars, Swal, navActive, moment) {

        /*
         ajax url.
         */
        var ajax_url = {
            save: web_path + '/web/internship/regulate/save',
            obtain_student_data: web_path + '/web/internship/regulate/student',
            student_info: web_path + '/web/internship/regulate/student/info',
            page: '/web/menu/internship/regulate'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
            student: '#student',
            internshipReleaseId: '#internshipReleaseId',
            studentName: '#studentName',
            studentNumber: '#studentNumber',
            studentTel: '#studentTel',
            internshipContent: '#internshipContent',
            internshipProgress: '#internshipProgress',
            reportDate: '#reportDate',
            reportWay: '#reportWay'
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
            studentId: '',
            internshipReleaseId: '',
            studentName: '',
            studentNumber: '',
            studentTel: '',
            internshipContent: '',
            internshipProgress: '',
            reportWay: ''
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.studentId = $(param_id.student).val();
            param.internshipReleaseId = $(param_id.internshipReleaseId).val();
            param.studentName = $(param_id.studentName).val();
            param.studentNumber = $(param_id.studentNumber).val();
            param.studentTel = $(param_id.studentTel).val();
            param.internshipContent = $(param_id.internshipContent).val();
            param.internshipProgress = $(param_id.internshipProgress).val();
            param.reportWay = $(param_id.reportWay).val();
        }

        init();

        function init() {
            initParam();
            initStudent();
            initSelect2();
            initMaxLength();
        }

        function initStudent() {
            tools.dataLoading();
            $.get(ajax_url.obtain_student_data + "/" + param.internshipReleaseId, function (data) {
                tools.dataEndLoading();
                $(param_id.student).select2({
                    data: data.results
                });
            });
        }

        function initSelect2() {
            $('.select2-show-search').select2({
                language: "zh-CN"
            });
        }

        /**
         * 初始化Input max length
         */
        function initMaxLength() {
            $(param_id.internshipContent).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });

            $(param_id.internshipProgress).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });

            $(param_id.reportWay).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });
        }

        $(param_id.student).on('select2:select', function (e) {
            $.post(ajax_url.student_info, {
                id: param.internshipReleaseId,
                studentId: e.currentTarget.value
            }, function (data) {
                if (data.state) {
                    $(param_id.studentName).val(data.student.studentName);
                    $(param_id.studentNumber).val(data.student.studentNumber);
                    $(param_id.studentTel).val(data.student.mobile);
                } else {
                    Messenger().post({
                        message: data.msg,
                        type: 'error',
                        showCloseButton: true
                    });
                }
            });
        });

        $(param_id.reportDate).flatpickr({
            "locale": "zh",
            defaultDate: moment().format("YYYY-MM-DD")
        });

        /*
         返回
         */
        $('#regulateList').click(function () {
            window.history.go(-1);
        });

        $(param_id.internshipContent).blur(function () {
            initParam();
            var internshipContent = param.internshipContent;
            if (internshipContent.length <= 0 || internshipContent.length > 200) {
                tools.validErrorDom(param_id.internshipContent, '实习内容200个字符以内');
            } else {
                tools.validSuccessDom(param_id.internshipContent);
            }
        });

        $(param_id.internshipProgress).blur(function () {
            initParam();
            var internshipProgress = param.internshipProgress;
            if (internshipProgress.length <= 0 || internshipProgress.length > 200) {
                tools.validErrorDom(param_id.internshipProgress, '实习进展200个字符以内');
            } else {
                tools.validSuccessDom(param_id.internshipProgress);
            }
        });

        $(param_id.reportWay).blur(function () {
            initParam();
            var reportWay = param.reportWay;
            if (reportWay.length <= 0 || reportWay.length > 200) {
                tools.validErrorDom(param_id.reportWay, '汇报途径20个字符以内');
            } else {
                tools.validSuccessDom(param_id.reportWay);
            }
        });

        /*
         保存数据
         */
        $(button_id.save.id).click(function () {
            initParam();
            validStudentId();
        });

        /**
         * 检验学生id
         */
        function validStudentId() {
            var studentId = param.studentId;
            if (studentId === '' || Number(studentId) <= 0) {
                Messenger().post({
                    message: '请选择学生',
                    type: 'error',
                    showCloseButton: true
                });
                tools.validSelect2ErrorDom(param_id.student, '请选择学生');
            } else {
                tools.validSelect2SuccessDom(param_id.student);
                validInternshipContent();
            }
        }

        /**
         * 检验实习内容
         */
        function validInternshipContent() {
            var internshipContent = param.internshipContent;
            if (internshipContent.length <= 0 || internshipContent.length > 200) {
                Messenger().post({
                    message: '实习内容200个字符以内',
                    type: 'error',
                    showCloseButton: true
                });
                tools.validErrorDom(param_id.internshipContent, '实习内容200个字符以内');
            } else {
                tools.validSuccessDom(param_id.internshipContent);
                validInternshipProgress();
            }
        }

        /**
         * 检验实习进展
         */
        function validInternshipProgress() {
            var internshipProgress = param.internshipProgress;
            if (internshipProgress.length <= 0 || internshipProgress.length > 200) {
                Messenger().post({
                    message: '实习进展200个字符以内',
                    type: 'error',
                    showCloseButton: true
                });
                tools.validErrorDom(param_id.internshipProgress, '实习进展200个字符以内');
            } else {
                tools.validSuccessDom(param_id.internshipProgress);
                validReportWay();
            }
        }

        /**
         * 检验汇报途径
         */
        function validReportWay() {
            var reportWay = param.reportWay;
            if (reportWay.length <= 0 || reportWay.length > 200) {
                Messenger().post({
                    message: '汇报途径20个字符以内',
                    type: 'error',
                    showCloseButton: true
                });
                tools.validErrorDom(param_id.reportWay, '汇报途径20个字符以内');
            } else {
                tools.validSuccessDom(param_id.reportWay);
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
                    Swal.fire(data.state ? '保存成功' : '保存失败', data.msg, data.state ? 'success' : 'error');
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