//# sourceURL=authorize_add.js
require(["jquery", "lodash", "tools", "sweetalert2", "handlebars", "nav.active", "messenger", "jquery.address",
    "bootstrap-maxlength", "flatpickr-zh", "select2-zh-CN"], function ($, _, tools, Swal, Handlebars, navActive) {

    /*
     ajax url.
     */
    var ajax_url = {
        obtain_school_data: web_path + '/anyone/data/schools',
        obtain_college_data: web_path + '/anyone/data/college',
        obtain_department_data: web_path + '/anyone/data/department',
        obtain_science_data: web_path + '/anyone/data/science',
        obtain_grade_data: web_path + '/anyone/data/grade',
        obtain_organize_data: web_path + '/anyone/data/organize',
        obtain_authorize_type_data: web_path + '/web/platform/authorize/type',
        obtain_college_role_data: web_path + '/web/platform/authorize/role',
        check_username: web_path + '/web/platform/authorize/check/username',
        save: web_path + '/web/platform/authorize/save',
        page: '/web/menu/platform/authorize'
    };

    // 刷新时选中菜单
    navActive(ajax_url.page);

    /*
     参数id
     */
    var param_id = {
        department: '#department',
        science: '#science',
        grade: '#grade',
        organize: '#organize',
        targetUsername: '#targetUsername',
        authorizeType: '#authorizeType',
        role: '#role',
        duration: '#duration',
        validDate: '#validDate',
        dataScope: '#dataScope',
        dataId: '#dataId',
        reason: '#reason'
    };

    var button_id = {
        save: {
            id: '#save',
            text: '保存',
            tip: '保存中...'
        },
        okData: {
            id: '#okData',
            text: '确定',
            tip: '确定中...'
        }
    };

    /*
     参数
     */
    var param = {
        targetUsername: '',
        authorizeTypeId: '',
        roleId: '',
        duration: '',
        validDate: '',
        dataScope: '',
        dataId: '',
        reason: '',
        collegeId: ''
    };

    var page_param = {
        collegeId: $('#collegeId').val()
    };

    /**
     * 初始化参数
     */
    function initParam() {
        param.targetUsername = $(param_id.targetUsername).val();
        param.authorizeTypeId = $(param_id.authorizeType).val();
        param.roleId = $(param_id.role).val();
        param.duration = $(param_id.duration).val();

        var validDate = $(param_id.validDate).val();
        if (validDate !== '') {
            param.validDate = $(param_id.validDate).val() + ":00";
        } else {
            param.validDate = '';
        }

        param.dataScope = $(param_id.dataScope).val();
        var dataId = $('.dataId');
        if (dataId && dataId.length > 0) {
            param.dataId = _.trim($(dataId[0]).text());
        } else {
            param.dataId = '';
        }
        param.reason = _.trim($(param_id.reason).val());
        param.collegeId = page_param.collegeId;
    }

    $(param_id.validDate).flatpickr({
        locale: "zh",
        enableTime: true,
        dateFormat: "Y-m-d H:i",
        onClose: function (selectedDates, dateStr, instance) {
            if (dateStr !== '') {
                tools.validSuccessDom(param_id.validDate);
            } else {
                tools.validErrorDom(param_id.validDate, '请选择生效时间');
            }
        }
    });

    /*
     初始化数据
     */
    init();

    /**
     * 初始化界面
     */
    function init() {
        initSelect2();
        initAuthorizeType();
        initMaxLength();
        if (Number(page_param.collegeId) > 0) {
            initDepartment(page_param.collegeId);
            initCollegeRole(page_param.collegeId);
        }

    }

    function initAuthorizeType() {
        $.get(ajax_url.obtain_authorize_type_data, function (data) {
            $(param_id.authorizeType).select2({
                data: data.results
            });
        });
    }

    function initCollegeRole(collegeId) {
        $.get(ajax_url.obtain_college_role_data + '/' + collegeId, function (data) {
            $(param_id.role).select2({
                data: data.results
            });
        });
    }

    function initDepartment(collegeId) {
        if (Number(collegeId) > 0) {
            $.get(ajax_url.obtain_department_data, {collegeId: collegeId}, function (data) {
                $(param_id.department).html('<option label="请选择系"></option>');
                $(param_id.department).select2({
                    data: data.results,
                    dropdownParent: $("#dataModal")
                });
            });
        } else {
            $(param_id.department).html('<option label="请选择系"></option>');
        }
    }

    function initScience(departmentId) {
        if (Number(departmentId) > 0) {
            $.get(ajax_url.obtain_science_data, {departmentId: departmentId}, function (data) {
                $(param_id.science).html('<option label="请选择专业"></option>');
                $(param_id.science).select2({
                    data: data.results,
                    dropdownParent: $("#dataModal")
                });
            });
        } else {
            $(param_id.science).html('<option label="请选择专业"></option>');
        }
    }

    function initGrade(scienceId) {
        if (Number(scienceId) > 0) {
            $.get(ajax_url.obtain_grade_data, {scienceId: scienceId}, function (data) {
                $(param_id.grade).html('<option label="请选择年级"></option>');
                $(param_id.grade).select2({
                    data: data.results,
                    dropdownParent: $("#dataModal")
                });
            });
        } else {
            $(param_id.grade).html('<option label="请选择年级"></option>');
        }
    }

    function initOrganize(gradeId) {
        if (Number(gradeId) > 0) {
            $.get(ajax_url.obtain_organize_data, {gradeId: gradeId}, function (data) {
                $(param_id.organize).html('<option label="请选择班级"></option>');
                $(param_id.organize).select2({
                    data: data.results,
                    dropdownParent: $("#dataModal")
                });
            });
        } else {
            $(param_id.organize).html('<option label="请选择班级"></option>');
        }
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
        $(param_id.reason).maxlength({
            alwaysShow: true,
            threshold: 10,
            warningClass: "text-success",
            limitReachedClass: "text-danger"
        });
    }

    $(param_id.targetUsername).blur(function () {
        initParam();
        var targetUsername = param.targetUsername;
        if (targetUsername !== '') {
            $.post(ajax_url.check_username, param, function (data) {
                if (data.state) {
                    tools.validSuccessDom(param_id.targetUsername);
                    if (Number(page_param.collegeId) === 0) {
                        initDepartment(data.collegeId);
                        initCollegeRole(data.collegeId);
                        page_param.collegeId = data.collegeId;
                    }
                } else {
                    tools.validErrorDom(param_id.targetUsername, data.msg);
                }
            });
        } else {
            tools.validErrorDom(param_id.targetUsername, '请填写申请账号');
        }
    });

    $(param_id.authorizeType).change(function () {
        var v = $(this).val();

        if (Number(v) > 0) {
            tools.validSelect2SuccessDom(param_id.authorizeType);
        }
    });

    $(param_id.role).change(function () {
        var v = $(this).val();

        if (v !== '') {
            tools.validSelect2SuccessDom(param_id.role);
        }
    });

    var selectData = $('#selectData');
    $(param_id.dataScope).change(function () {
        var v = $(this).val();
        selectData.empty();

        $(param_id.department).val('').trigger('change');
        $(param_id.science).val('').trigger('change');
        $(param_id.grade).val('').trigger('change');
        $(param_id.organize).val('').trigger('change');

        if (Number(v) === 1) {
            $(param_id.department).prop('disabled', false);
            $(param_id.science).prop('disabled', true);
            $(param_id.grade).prop('disabled', true);
            $(param_id.organize).prop('disabled', true);
        } else if (Number(v) === 2) {
            $(param_id.department).prop('disabled', false);
            $(param_id.science).prop('disabled', false);
            $(param_id.grade).prop('disabled', true);
            $(param_id.organize).prop('disabled', true);
        } else if (Number(v) === 3) {
            $(param_id.department).prop('disabled', false);
            $(param_id.science).prop('disabled', false);
            $(param_id.grade).prop('disabled', false);
            $(param_id.organize).prop('disabled', true);
        } else if (Number(v) === 4) {
            $(param_id.department).prop('disabled', false);
            $(param_id.science).prop('disabled', false);
            $(param_id.grade).prop('disabled', false);
            $(param_id.organize).prop('disabled', false);
        } else {
            $(param_id.department).prop('disabled', true);
            $(param_id.science).prop('disabled', true);
            $(param_id.grade).prop('disabled', true);
            $(param_id.organize).prop('disabled', true);
        }
    });

    $(param_id.reason).blur(function () {
        initParam();
        var reason = param.reason;
        if (reason !== '') {
            tools.validSuccessDom(param_id.reason);
        } else {
            tools.validErrorDom(param_id.reason, '请填写申请原因');
        }
    });

    $(param_id.department).change(function () {
        var v = $(this).val();
        initScience(v);
        initGrade(0);
        initOrganize(0);

        if (Number(v) > 0) {
            tools.validSelect2SuccessDom(param_id.department);
        }
    });

    $(param_id.science).change(function () {
        var v = $(this).val();
        initGrade(v);
        initOrganize(0);

        if (Number(v) > 0) {
            tools.validSelect2SuccessDom(param_id.science);
        }
    });

    $(param_id.grade).change(function () {
        var v = $(this).val();
        initOrganize(v);

        if (Number(v) > 0) {
            tools.validSelect2SuccessDom(param_id.grade);
        }
    });

    $(param_id.organize).change(function () {
        var v = $(this).val();

        if (Number(v) > 0) {
            tools.validSelect2SuccessDom(param_id.organize);
        }
    });

    $(button_id.okData.id).click(function () {
        $('#dataModal').modal('hide');
        initParam();
        var dataScope = Number(param.dataScope);

        var dataId = '';
        var dataName = '';
        if (dataScope === 1) {
            dataId = $(param_id.department).find(':selected').val();
            dataName = $(param_id.department).find(':selected').text();
        } else if (dataScope === 2) {
            dataId = $(param_id.science).find(':selected').val();
            dataName = $(param_id.science).find(':selected').text();
        } else if (dataScope === 3) {
            dataId = $(param_id.grade).find(':selected').val();
            dataName = $(param_id.grade).find(':selected').text();
        } else if (dataScope === 4) {
            dataId = $(param_id.organize).find(':selected').val();
            dataName = $(param_id.organize).find(':selected').text();
        }

        if (Number(dataId) > 0) {
            tools.validCustomerSingleSuccessDom(param_id.dataId);
            var template = Handlebars.compile($("#data_template").html());
            var context =
                {
                    listResult: [
                        {
                            "dataId": dataId,
                            "dataName": dataName
                        }
                    ]
                };

            selectData.html(template(context));
        }
    });

    selectData.delegate('.del', "click", function () {
        $(this).parent().parent().remove();
    });

    /*
     保存数据
     */
    $(button_id.save.id).click(function () {
        initParam();
        validUsername();
    });

    function validUsername() {
        var targetUsername = param.targetUsername;
        if (!_.isUndefined(targetUsername)) {
            if (targetUsername !== '') {
                $.post(ajax_url.check_username, param, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.targetUsername);
                        validAuthorizeType();
                    } else {
                        tools.validErrorDom(param_id.targetUsername, data.msg);
                    }
                });
            } else {
                tools.validErrorDom(param_id.targetUsername, '请填写申请账号');
            }
        } else {
            validAuthorizeType();
        }
    }

    function validAuthorizeType() {
        var authorizeTypeId = param.authorizeTypeId;
        if (Number(authorizeTypeId) <= 0) {
            tools.validSelect2ErrorDom(param_id.authorizeType, '请选择权限类型');
        } else {
            tools.validSelect2SuccessDom(param_id.authorizeType);
            validRole();
        }
    }

    function validRole() {
        var roleId = param.roleId;
        if (roleId === '') {
            tools.validSelect2ErrorDom(param_id.role, '请选择角色');
        } else {
            tools.validSelect2SuccessDom(param_id.role);
            validDuration();
        }
    }

    function validDuration() {
        var duration = param.duration;
        if (Number(duration) <= 0) {
            tools.validErrorDom(param_id.duration, '请选择时长');
        } else {
            tools.validSuccessDom(param_id.duration);
            validValidDate();
        }
    }

    function validValidDate() {
        var validDate = param.validDate;
        if (validDate === '') {
            tools.validErrorDom(param_id.validDate, '请选择生效时间');
        } else {
            tools.validSuccessDom(param_id.validDate);
            validDataScope();
        }
    }

    function validDataScope() {
        var dataScope = param.dataScope;
        if (Number(dataScope) <= 0) {
            tools.validErrorDom(param_id.dataScope, '请选择数据域');
        } else {
            tools.validSuccessDom(param_id.dataScope);
            validDataId();
        }
    }

    function validDataId() {
        var dataId = param.dataId;
        if (Number(dataId) <= 0) {
            tools.validCustomerSingleErrorDom(param_id.dataId, '请选择数据');
        } else {
            tools.validCustomerSingleSuccessDom(param_id.dataId);
            validReason();
        }
    }

    function validReason() {
        var reason = param.reason;
        if (reason.length <= 0 || reason.length > 200) {
            tools.validCustomerSingleErrorDom(param_id.reason, '申请原因200个字符以内');
        } else {
            tools.validCustomerSingleSuccessDom(param_id.reason);
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