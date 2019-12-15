//# sourceURL=role_add.js
require(["jquery", "lodash", "tools", "sweetalert2", "handlebars", "nav.active", "messenger", "bootstrap-treeview", "jquery.address",
    "bootstrap-maxlength"], function ($, _, tools, Swal, Handlebars, navActive) {

    /*
     ajax url.
     */
    var ajax_url = {
        obtain_school_data: web_path + '/anyone/data/school',
        obtain_college_data: web_path + '/anyone/data/college',
        application_json_data: web_path + '/web/platform/role/application/json',
        save: web_path + '/web/platform/role/save',
        check_name: web_path + '/web/platform/role/check/add/name',
        back: '/web/menu/platform/role'
    };

    // 刷新时选中菜单
    navActive(ajax_url.back);

    /*
     参数id
     */
    var param_id = {
        schoolId: '#select_school',
        collegeId: '#select_college',
        roleName: '#roleName'
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
        schoolId: '',
        collegeId: '',
        roleName: '',
        applicationIds: ''
    };

    var page_param = {
        collegeId: $('#collegeId').val()
    };

    /**
     * 初始化参数
     */
    function initParam() {
        param.schoolId = $(param_id.schoolId).val();
        param.collegeId = $(param_id.collegeId).val();
        param.roleName = _.replace(_.trim($(param_id.roleName).val()), ' ', '');
        param.applicationIds = getAllCheckedData();
    }

    var treeviewId = $('#treeview-checkable');

    /*
     初始化数据
     */
    init();

    /**
     * 初始化界面
     */
    function init() {
        if (Number(page_param.collegeId) === 0) {
            initSchool();
            treeViewData([]);
        } else {
            initTreeView(page_param.collegeId);
        }

        initMaxLength();
    }

    function initSchool() {
        $.get(ajax_url.obtain_school_data, function (data) {
            $(param_id.school).select2({
                data: data.results
            });
        });
    }

    function initCollege(schoolId) {
        if (Number(schoolId) > 0) {
            $.get(ajax_url.obtain_college_data, {schoolId: schoolId}, function (data) {
                $(param_id.college).html('<option label="请选择院"></option>');
                $(param_id.college).select2({data: data.results});
            });
        } else {
            $(param_id.college).html('<option label="请选择院"></option>');
        }
    }

    /**
     * 初始化Input max length
     */
    function initMaxLength() {
        $(param_id.roleName).maxlength({
            alwaysShow: true,
            threshold: 10,
            warningClass: "text-success",
            limitReachedClass: "text-danger"
        });
    }

    $(param_id.school).change(function () {
        var v = $(this).val();
        initCollege(v);

        if (Number(v) > 0) {
            tools.validSelect2SuccessDom(param_id.school);
        }
    });

    $(param_id.college).change(function () {
        var v = $(this).val();
        if (Number(v) > 0) {
            tools.validSelect2SuccessDom(param_id.college);
        }
    });

    /*
     即时检验角色名
     */
    $(param_id.roleName).blur(function () {
        initParam();
        var roleName = param.roleName;
        if (roleName.length <= 0 || roleName.length > 50) {
            tools.validErrorDom(param_id.roleName, '角色名50个字符以内');
        } else {
            $.post(ajax_url.check_name, param, function (data) {
                if (data.state) {
                    tools.validSuccessDom(param_id.roleName);
                } else {
                    tools.validErrorDom(param_id.roleName, data.msg);
                }
            });
        }
    });


    /*
     保存数据
     */
    $(button_id.save.id).click(function () {
        initParam();
        add();
    });

    /*
     添加询问
     */
    function add() {
        if (Number(page_param.collegeId) === 0) {
            validSchool();
        } else {
            validRoleName();
        }
    }

    function validSchool() {
        var schoolId = param.schoolId;
        if (Number(schoolId) <= 0) {
            tools.validErrorDom(param_id.schoolId, '请选择学校');
        } else {
            tools.validSuccessDom(param_id.schoolId);
            validCollege();
        }
    }

    function validCollege() {
        var collegeId = param.collegeId;
        if (Number(collegeId) <= 0) {
            tools.validErrorDom(param_id.collegeId, '请选择院');
        } else {
            tools.validSuccessDom(param_id.collegeId);
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
            data: $('#app_form').serialize(),
            success: function (data) {
                tools.buttonEndLoading(button_id.save.id, button_id.save.text);
                if (data.state) {
                    Swal.fire({
                        title: data.msg,
                        type: "success",
                        confirmButtonText: "确定",
                        preConfirm: function () {
                            $.address.value(ajax_url.back);
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
            $.get(web_path + ajax_url.application_json_data, {collegeId: collegeId}, function (data) {
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