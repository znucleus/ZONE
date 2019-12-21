//# sourceURL=authorize_add.js
require(["jquery", "lodash", "tools", "sweetalert2", "handlebars", "nav.active", "messenger", "jquery.address",
    "bootstrap-maxlength", "flatpickr-zh", "select2-zh-CN"], function ($, _, tools, Swal, Handlebars, navActive) {

    /*
     ajax url.
     */
    var ajax_url = {
        obtain_school_data: web_path + '/anyone/data/school',
        obtain_college_data: web_path + '/anyone/data/college',
        obtain_department_data: web_path + '/anyone/data/department',
        obtain_science_data: web_path + '/anyone/data/science',
        obtain_grade_data: web_path + '/anyone/data/grade',
        obtain_organize_data: web_path + '/anyone/data/organize',
        obtain_authorize_type_data: web_path + '/web/platform/authorize/type',
        obtain_college_role_data: web_path + '/web/platform/authorize/role',
        check_username: web_path + '/web/platform/authorize/check/add/username',
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
        username: '#username',
        authorizeType: '#authorizeType',
        role: '#role',
        duration: '#duration',
        validDate: '#validDate',
        organizeId: '#organizeId',
        reason: '#reason'
    };

    var button_id = {
        save: {
            id: '#save',
            text: '保存',
            tip: '保存中...'
        },
        okOrganize: {
            id: '#okOrganize',
            text: '确定',
            tip: '确定中...'
        }
    };

    /*
     参数
     */
    var param = {
        username: '',
        authorizeTypeId: '',
        roleId: '',
        duration: '',
        validDate: '',
        organizeId: '',
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
        param.username = $(param_id.username) ? $(param_id.username).val() : '';
        param.authorizeTypeId = $(param_id.authorizeType).val();
        param.roleId = $(param_id.role).val();
        param.duration = $(param_id.duration).val();

        var validDate = $(param_id.validDate).val();
        if (validDate !== '') {
            param.validDate = $(param_id.validDate).val() + ":00";
        } else {
            param.validDate = '';
        }

        var organize = $('.organizeId');
        if (organize && organize.length > 0) {
            param.organizeId = _.trim($(organize[0]).text());
        } else {
            param.organizeId = '';
        }
        param.reason = _.trim($(param_id.reason).val());
        param.collegeId = page_param.collegeId;
    }

    $(param_id.validDate).flatpickr({
        "locale": "zh",
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
                    dropdownParent: $("#organizeModal")
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
                    dropdownParent: $("#organizeModal")
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
                    dropdownParent: $("#organizeModal")
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
                    dropdownParent: $("#organizeModal")
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

    $(param_id.username).blur(function () {
        initParam();
        var username = param.username;
        if (username !== '') {
            $.post(ajax_url.check_username, param, function (data) {
                if (data.state) {
                    tools.validSuccessDom(param_id.username);
                    if (Number(page_param.collegeId) === 0) {
                        initDepartment(data.collegeId);
                        initCollegeRole(data.collegeId);
                        page_param.collegeId = data.collegeId;
                    }
                } else {
                    tools.validErrorDom(param_id.username, data.msg);
                }
            });
        } else {
            tools.validErrorDom(param_id.username, '请填写申请账号');
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


    var organizeData = $('#organizeData');
    $(button_id.okOrganize.id).click(function () {
        $('#organizeModal').modal('hide');
        var organize = $(param_id.organize).val();
        if (Number(organize) > 0) {
            tools.validCustomerSingleSuccessDom(param_id.organizeId);
            var template = Handlebars.compile($("#organize_data").html());
            var context =
                {
                    listResult: [
                        {
                            "organizeId": organize,
                            "organizeName": $(param_id.organize).text()
                        }
                    ]
                };

            organizeData.html(template(context));
        }
    });

    organizeData.delegate('.delOrganize', "click", function () {
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
        var username = param.username;
        if(!_.isUndefined(username)){
            if (username !== '') {
                $.post(ajax_url.check_username, param, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.username);
                        validAuthorizeType();
                    } else {
                        tools.validErrorDom(param_id.username, data.msg);
                    }
                });
            } else {
                tools.validErrorDom(param_id.username, '请填写申请账号');
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
            validOrganize();
        }
    }

    function validOrganize() {
        var organizeId = param.organizeId;
        if (Number(organizeId) <= 0) {
            tools.validCustomerSingleErrorDom(param_id.organizeId, '请选择班级');
        } else {
            tools.validCustomerSingleSuccessDom(param_id.organizeId);
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
                Messenger().post({
                    message: 'Request error : ' + XMLHttpRequest.status + " " + XMLHttpRequest.statusText,
                    type: 'error',
                    showCloseButton: true
                });
            }
        });
    }

});