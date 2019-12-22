//# sourceURL=role_see.js
require(["jquery", "nav.active", "bootstrap-treeview", "jquery.address"],
    function ($, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            application_json_data: web_path + '/web/platform/role/application/json',
            role_application_data: web_path + '/web/platform/role/application/data',
            page: '/web/menu/platform/role'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        var page_param = {
            collegeId: $('#collegeId').val(),
            roleId: $('#roleId').val()
        };

        /*
         初始化数据
         */
        init();

        /**
         * 初始化界面
         */
        function init() {
            initTreeView(page_param.collegeId);
        }

        var treeviewId = $('#treeview-checkable');

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

            selectedApplication();
        }

        /**
         * 选中应用
         */
        function selectedApplication() {
            $.post(ajax_url.role_application_data, {roleId: page_param.roleId}, function (data) {
                var list = data.listResult;
                if (list.length > 0) {
                    var unCheckeds = treeviewId.treeview('getUnchecked');
                    for (var i = 0; i < list.length; i++) {
                        for (var j = 0; j < unCheckeds.length; j++) {
                            if (list[i].applicationId === unCheckeds[j].dataId) {
                                treeviewId.treeview('checkNode', [unCheckeds[j], {silent: true}]);
                                break;
                            }
                        }
                    }
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

    });