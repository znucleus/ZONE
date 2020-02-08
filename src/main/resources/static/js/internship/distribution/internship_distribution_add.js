//# sourceURL=internship_distribution_add.js
require(["jquery", "lodash", "tools", "sweetalert2", "nav.active", "messenger", "jquery.address", "select2-zh-CN"],
    function ($, _, tools, Swal, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            save: web_path + '/web/internship/teacher_distribution/save',
            check_student: web_path + '/web/internship/teacher_distribution/check/add/student',
            obtain_staff_data: web_path + '/web/internship/teacher_distribution/staff',
            page: '/web/menu/internship/teacher_distribution',
            back:'/web/internship/teacher_distribution/list'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
            studentUsername: '#studentUsername',
            studentNumber: '#studentNumber',
            staff: '#staff'
        };

        var button_id = {
            save: {
                id: '#save',
                text: '保存',
                tip: '保存中...'
            }
        };

        var page_param = {
            paramInternshipReleaseId: $('#paramInternshipReleaseId').val()
        };

        /*
         参数
         */
        var param = {
            studentUsername: '',
            studentNumber: '',
            staffId: ''
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.studentUsername = _.trim($(param_id.studentUsername).val());
            param.studentNumber = _.trim($(param_id.studentNumber).val());
            param.staffId = $(param_id.staff).val();
        }

        init();

        /**
         * 初始化数据
         */
        function init() {
            initStaff();
            initSelect2();
        }

        function initStaff() {
            $.get(ajax_url.obtain_staff_data + '/' + page_param.paramInternshipReleaseId, function (data) {
                $(param_id.staff).select2({
                    data: data.results
                });
            });
        }

        function initSelect2() {
            $('.select2-show-search').select2({
                language: "zh-CN"
            });
        }

        $(param_id.staff).change(function () {
            var v = $(this).val();

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.staff);
            }
        });

        /*
         保存数据
         */
        $(button_id.save.id).click(function () {
            initParam();
            validStudent();
        });

        /**
         * 检验学生信息
         */
        function validStudent() {
            var studentUsername = param.studentUsername;
            var studentNumber = param.studentNumber;
            if (studentUsername.length <= 0 && studentNumber.length <= 0) {
                tools.validCustomerSingleErrorDom(param_id.studentNumber, '请至少填写一项学生信息');
            } else {
                var student = "";
                var type = -1;
                if (studentUsername.length > 0) {
                    student = studentUsername;
                    type = 0;
                }

                if (studentNumber.length > 0) {
                    student = studentNumber;
                    type = 1;
                }

                $.post(ajax_url.check_student, {
                    id: page_param.paramInternshipReleaseId, student: student, type: type
                }, function (data) {
                    if (data.state) {
                        tools.validCustomerSingleSuccessDom(param_id.studentNumber);
                        validStaff();
                    } else {
                        tools.validCustomerSingleErrorDom(param_id.studentNumber, data.msg);
                    }
                });
            }
        }

        function validStaff() {
            var staffId = param.staffId;
            if (Number(staffId) <= 0) {
                tools.validSelect2ErrorDom(param_id.staff, '请选择教职工');
            } else {
                tools.validSelect2SuccessDom(param_id.staff);
                sendAjax();
            }
        }

        /**
         * 发送数据到后台
         */
        function sendAjax() {
            var student = "";
            var type = -1;
            var studentUsername = param.studentUsername;
            var studentNumber = param.studentNumber;
            if (studentUsername.length > 0) {
                student = studentUsername;
                type = 0;
            }

            if (studentNumber.length > 0) {
                student = studentNumber;
                type = 1;
            }
            var params = {
                student: student,
                staffId: param.staffId,
                type: type,
                id: page_param.paramInternshipReleaseId
            };

            tools.buttonLoading(button_id.save.id, button_id.save.tip);
            $.ajax({
                type: 'POST',
                url: ajax_url.save,
                data: params,
                success: function (data) {
                    tools.buttonEndLoading(button_id.save.id, button_id.save.text);
                    if (data.state) {
                        Swal.fire({
                            title: data.msg,
                            type: "success",
                            confirmButtonText: "确定",
                            preConfirm: function () {
                                $.address.value(ajax_url.back + '/' + page_param.paramInternshipReleaseId);
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