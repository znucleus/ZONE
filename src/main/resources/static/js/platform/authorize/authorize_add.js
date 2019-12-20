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
        save: web_path + '/web/platform/authorize/save',
        page: '/web/menu/platform/authorize'
    };

    // 刷新时选中菜单
    navActive(ajax_url.page);

    /*
     参数id
     */
    var param_id = {
        school: '#school',
        college: '#college',
        department: '#department',
        science: '#science',
        grade: '#grade',
        organize: '#organize',
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
        authorizeTypeId: '',
        roleId: '',
        duration: '',
        validDate: '',
        organizeId: '',
        reason: ''
    };

    /**
     * 初始化参数
     */
    function initParam() {
        param.authorizeTypeId = $(param_id.authorizeTypeId).val();
        param.roleId = $(param_id.roleId).val();
        param.duration = $(param_id.duration).val();
        param.validDate = $(param_id.validDate).val() + ":00";

        var organize = $('.organizeId');
        if (organize && organize.length > 0) {
            param.organizeId = _.trim($(organize[0]).text());
        }
        param.reason = _.trim($(param_id.reason).val());
    }

    $(param_id.validDate).flatpickr({
        "locale": "zh",
        enableTime: true,
        dateFormat: "Y-m-d H:i"
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
        initCollegeRole();
        initMaxLength();
        initSchool();
    }

    function initAuthorizeType() {
        $.get(ajax_url.obtain_authorize_type_data, function (data) {
            $(param_id.authorizeType).select2({
                data: data.results
            });
        });
    }

    function initCollegeRole() {
        $.get(ajax_url.obtain_college_role_data, function (data) {
            $(param_id.role).select2({
                data: data.results
            });
        });
    }

    function initSchool() {
        $.get(ajax_url.obtain_school_data, function (data) {
            $(param_id.school).select2({
                data: data.results,
                dropdownParent: $("#organizeModal")
            });
        });
    }

    function initCollege(schoolId) {
        if (Number(schoolId) > 0) {
            $.get(ajax_url.obtain_college_data, {schoolId: schoolId}, function (data) {
                $(param_id.college).html('<option label="请选择院"></option>');
                $(param_id.college).select2({
                    data: data.results,
                    dropdownParent: $("#organizeModal")
                });
            });
        } else {
            $(param_id.college).html('<option label="请选择院"></option>');
        }
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

    $(param_id.school).change(function () {
        var v = $(this).val();
        initCollege(v);
        initDepartment(0);
        initScience(0);
        initGrade(0);
        initOrganize(0);

        if (Number(v) > 0) {
            tools.validSelect2SuccessDom(param_id.school);
        }
    });

    $(param_id.college).change(function () {
        var v = $(this).val();
        initDepartment(v);
        initScience(0);
        initGrade(0);
        initOrganize(0);

        if (Number(v) > 0) {
            tools.validSelect2SuccessDom(param_id.college);
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
        var template = Handlebars.compile($("#organize_data").html());
        console.log($(param_id.organize).text());
        var context =
            {
                listResult: [
                    {
                        "organizeId": $(param_id.organize).val(),
                        "organizeName": $(param_id.organize).text()
                    }
                ]
            };

        organizeData.html(template(context));
    });

    organizeData.delegate('.delOrganize', "click", function () {
        $(this).parent().parent().remove();
    });

    /*
     保存数据
     */
    $(button_id.save.id).click(function () {
        initParam();
        add();
    });

    function add() {
        if (Number(page_param.collegeId) === 0) {
            validSchool();
        } else {
            validRoleName();
        }
    }

    function validSchool() {
        var schoolId = param.school;
        if (Number(schoolId) <= 0) {
            tools.validSelect2ErrorDom(param_id.school, '请选择学校');
        } else {
            tools.validSelect2SuccessDom(param_id.school);
            validCollege();
        }
    }

    function validCollege() {
        var collegeId = param.college;
        if (Number(collegeId) <= 0) {
            tools.validSelect2ErrorDom(param_id.college, '请选择院');
        } else {
            tools.validSelect2SuccessDom(param_id.college);
            validRoleName();
        }
    }

    /**
     * 添加时检验并提交数据
     */
    function validRoleName() {
        var roleName = param.roleName;
        if (roleName.length <= 0 || roleName.length > 50) {
            tools.validErrorDom(param_id.roleName, '角色名50个字符以内');
        } else {
            $.post(ajax_url.check_name, param, function (data) {
                if (data.state) {
                    tools.validSuccessDom(param_id.roleName);
                    sendAjax();
                } else {
                    tools.validErrorDom(param_id.roleName, data.msg);
                }
            });
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

    /**
     * 初始化tree view
     */
    function initTreeView(collegeId) {
        if (collegeId > 0) {
            $.get(ajax_url.application_json_data, {collegeId: collegeId}, function (data) {
                if (data.listResult != null) {
                    treeViewData(data.listResult);
                }
            });
        }
    }

    function treeViewData(data) {
        treeviewId.treeview({
            data: data,
            showIcon: false,
            showCheckbox: true,
            checkedIcon: 'fa fa-check-circle-o',
            collapseIcon: 'fa fa-minus',
            emptyIcon: 'fa',
            expandIcon: 'fa fa-plus',
            nodeIcon: 'fa fa-stop',
            selectedIcon: 'fa fa-stop',
            uncheckedIcon: 'fa fa-circle-o',
            onNodeChecked: function (event, node) {
                checkAllParentNode(node);
                checkAllChildrenNode(node);
            },
            onNodeUnchecked: function (event, node) {
                uncheckAllChildrenNode(node);
                getAllParent(node);// 若任何子节点选中则取消选中该父节点
            }
        });
    }

    /**
     * 选中所有父节点
     * @param node
     */
    function checkAllParentNode(node) {
        if (node.hasOwnProperty('parentId') && node.parentId !== undefined) {
            var parentNode = treeviewId.treeview('getParent', node);
            checkAllParentNode(parentNode);
        }
        treeviewId.treeview('checkNode', [node.nodeId, {silent: true}]);
    }

    /**
     * 取消所有子节点的选中
     * @param node
     */
    function uncheckAllChildrenNode(node) {
        if (node.hasOwnProperty('nodes') && node.nodes != null) {
            var n = node.nodes;
            for (var i = 0; i < n.length; i++) {
                uncheckAllChildrenNode(n[i]);
            }
        }
        treeviewId.treeview('uncheckNode', [node.nodeId, {silent: true}]);
    }

    /**
     * 选中所有子节点
     * @param node
     */
    function checkAllChildrenNode(node) {
        if (node.hasOwnProperty('nodes') && node.nodes != null) {
            var n = node.nodes;
            for (var i = 0; i < n.length; i++) {
                checkAllChildrenNode(n[i]);
            }
        }
        treeviewId.treeview('checkNode', [node.nodeId, {silent: true}]);
    }

    var childrenArr = [];

    function getAllChildren(node) {
        if (node.hasOwnProperty('nodes') && node.nodes != null) {
            var n = node.nodes;
            for (var i = 0; i < n.length; i++) {
                getAllChildren(n[i]);
            }
        }
        childrenArr.push(node);
    }

    function getAllParent(node) {
        if (node.hasOwnProperty('parentId') && node.parentId !== undefined) {
            var parentNode = treeviewId.treeview('getParent', node);
            childrenArr = [];
            getAllChildren(parentNode);
            var parentNodeIsChecked = false;
            for (var i = 0; i < childrenArr.length; i++) {
                if (childrenArr[i].nodeId !== parentNode.nodeId && childrenArr[i].state.checked) {
                    parentNodeIsChecked = true;
                }
            }
            if (!parentNodeIsChecked) {
                treeviewId.treeview('uncheckNode', [parentNode.nodeId, {silent: true}]);
                getAllParent(parentNode);
            }

        }
    }

    /**
     * 获取所有选中节点的dataId
     * @returns {string}
     */
    function getAllCheckedData() {
        var applicationIds = '';
        var checkeds = treeviewId.treeview('getChecked');
        var temp = [];
        for (var i = 0; i < checkeds.length; i++) {
            temp.push(checkeds[i].dataId);
        }
        if (temp.length > 0) {
            applicationIds = temp.join(",");
        }
        return applicationIds;
    }

});