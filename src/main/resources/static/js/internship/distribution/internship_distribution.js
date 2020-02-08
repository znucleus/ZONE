//# sourceURL=internship_distribution.js
require(["jquery", "lodash", "tools", "sweetalert2", "nav.active", "handlebars", "messenger", "jquery.address", "bootstrap-duallistbox"],
    function ($, _, tools, Swal, navActive, Handlebars) {

        /*
         ajax url.
         */
        var ajax_url = {
            obtain_organize_data: web_path + '/web/internship/teacher_distribution/organizes',
            obtain_staff_data: web_path + '/web/internship/teacher_distribution/staff',
            save: web_path + '/web/internship/teacher_distribution/distribution/save',
            page: '/web/menu/internship/teacher_distribution',
            back: '/web/internship/teacher_distribution/list'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
            organize: '#organize',
            staff: '#staff'
        };

        var button_id = {
            save: {
                id: '#save',
                text: '确认分配',
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
            organizeId: '',
            staffId: '',
            internshipReleaseId: page_param.paramInternshipReleaseId
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.organizeId = $(param_id.organize).val().join(',');
            param.staffId = $(param_id.staff).val().join(',');
        }

        init();

        /**
         * 初始化数据
         */
        function init() {
            $.get(ajax_url.obtain_organize_data + '/' + page_param.paramInternshipReleaseId, function (data) {
                organizeData(data);
            });

            $.get(ajax_url.obtain_staff_data + '/' + page_param.paramInternshipReleaseId, function (data) {
                staffData(data);
            });
        }

        /**
         * 班级数据
         * @param data
         */
        function organizeData(data) {
            var template = Handlebars.compile($("#data-template").html());
            $(param_id.organize).html(template(data));
            initOrganizeDualListbox();
        }

        /**
         * 教职工数据
         * @param data
         */
        function staffData(data) {
            var template = Handlebars.compile($("#data-template").html());
            $(param_id.staff).html(template(data));
            initStaffDualListbox();
        }

        /**
         * 初始化班级双列表
         */
        function initOrganizeDualListbox() {
            $(param_id.organize).bootstrapDualListbox();
        }

        /**
         * 初始化教职工双列表
         */
        function initStaffDualListbox() {
            $(param_id.staff).bootstrapDualListbox();
        }

        /*
         保存
         */
        $(button_id.save.id).click(function () {
            initParam();
            validOrganizeId();
        });

        /**
         * 检验班级id
         */
        function validOrganizeId() {
            var organizeId = param.organizeId;
            if (organizeId == null || organizeId === '' || organizeId.length <= 0) {
                tools.validCustomerSingleErrorDom(param_id.organize, '请选择班级');
            } else {
                tools.validCustomerSingleSuccessDom(param_id.organize);
                validStaffId();
            }
        }

        /**
         * 检验
         */
        function validStaffId() {
            var staffId = param.staffId;
            if (staffId == null || staffId === '' || staffId.length <= 0) {
                tools.validCustomerSingleErrorDom(param_id.staff, '请选择教师');
            } else {
                tools.validCustomerSingleSuccessDom(param_id.staff);
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