//# sourceURL=internship_distribution_edit.js
require(["jquery", "tools", "sweetalert2", "nav.active", "messenger", "jquery.address", "select2-zh-CN"],
    function ($, tools, Swal, navActive) {
        /*
         ajax url.
         */
        var ajax_url = {
            update: web_path + '/web/internship/teacher-distribution/update',
            obtain_staff_data: web_path + '/web/internship/teacher-distribution/staff',
            page: '/web/menu/internship/teacher-distribution',
            back: '/web/internship/teacher-distribution/list'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
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
            paramInternshipReleaseId: $('#paramInternshipReleaseId').val(),
            paramStudentId: $('#paramStudentId').val(),
            paramStaffId: $('#paramStaffId').val()
        };

        /*
         参数
         */
        var param = {
            studentId: page_param.paramStudentId,
            staffId: '',
            id: page_param.paramInternshipReleaseId
        };

        var init_configure = {
            init_staff: false
        };

        /**
         * 初始化参数
         */
        function initParam() {
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
                var sl = $(param_id.staff).select2({
                    data: data.results
                });

                if (!init_configure.init_staff) {
                    sl.val(page_param.paramStaffId).trigger("change");
                    init_configure.init_staff = true;
                }
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
            validStaff();
        });

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
            tools.buttonLoading(button_id.save.id, button_id.save.tip);
            $.ajax({
                type: 'POST',
                url: ajax_url.update,
                data: param,
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