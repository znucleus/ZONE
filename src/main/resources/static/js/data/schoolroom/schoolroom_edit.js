//# sourceURL=schoolroom_add.js
require(["jquery", "lodash", "tools", "sweetalert2", "nav.active", "messenger", "jquery.address", "bootstrap-maxlength", "select2-zh-CN"],
    function ($, _, tools, Swal, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            obtain_school_data: web_path + '/anyone/data/schools',
            obtain_college_data: web_path + '/anyone/data/college',
            obtain_building_data: web_path + '/users/data/building',
            update: web_path + '/web/data/schoolroom/update',
            check_code: web_path + '/web/data/schoolroom/check/edit/code',
            page: '/web/menu/data/schoolroom'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
            school: '#school',
            college: '#college',
            building: '#building',
            buildingCode: '#buildingCode'
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
            buildingId: '',
            schoolroomId: '',
            buildingCode: '',
            schoolroomIsDel: ''
        };

        var page_param = {
            paramSchoolId: $('#paramSchoolId').val(),
            paramCollegeId: $('#paramCollegeId').val(),
            collegeId: $('#collegeId').val(),
            paramBuildingId: $('#paramBuildingId').val(),
            paramSchoolroomId: $('#paramSchoolroomId').val()
        };

        var init_configure = {
            init_school: false,
            init_college: false,
            init_building: false
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.schoolId = $(param_id.school).val();
            if (Number(page_param.collegeId) === 0) {
                param.collegeId = $(param_id.college).val();
            } else {
                param.collegeId = page_param.collegeId;
            }
            param.buildingId = $(param_id.building).val();
            param.schoolroomId = page_param.paramSchoolroomId;
            param.buildingCode = _.trim($(param_id.buildingCode).val());
            var schoolroomIsDel = $('input[name="schoolroomIsDel"]:checked').val();
            param.schoolroomIsDel = _.isUndefined(schoolroomIsDel) ? 0 : schoolroomIsDel;
        }

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
                initSelect2();
            } else {
                initBuilding(page_param.collegeId);
            }
            initMaxLength();
        }

        function initSchool() {
            $.get(ajax_url.obtain_school_data, function (data) {
                var sl = $(param_id.school).select2({
                    data: data.results
                });

                if (!init_configure.init_school) {
                    sl.val(page_param.paramSchoolId).trigger("change");
                    init_configure.init_school = true;
                }
            });
        }

        function initCollege(schoolId) {
            if (Number(schoolId) > 0) {
                $.get(ajax_url.obtain_college_data, {schoolId: schoolId}, function (data) {
                    $(param_id.college).html('<option label="请选择院"></option>');
                    var sl = $(param_id.college).select2({data: data.results});
                    if (!init_configure.init_college) {
                        sl.val(page_param.paramCollegeId).trigger("change");
                        init_configure.init_college = true;
                    }
                });
            } else {
                $(param_id.college).html('<option label="请选择院"></option>');
            }
        }

        function initBuilding(collegeId) {
            if (Number(collegeId) > 0) {
                $.get(ajax_url.obtain_building_data, {collegeId: collegeId}, function (data) {
                    $(param_id.building).html('<option label="请选择楼"></option>');
                    var sl = $(param_id.building).select2({data: data.results});
                    if (!init_configure.init_building) {
                        sl.val(page_param.paramBuildingId).trigger("change");
                        init_configure.init_building = true;
                    }
                });
            } else {
                $(param_id.building).html('<option label="请选择楼"></option>');
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
            $(param_id.buildingCode).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });
        }

        $(param_id.school).change(function () {
            var v = $(this).val();
            initCollege(v);
            initBuilding(0);

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.school);
            }
        });

        $(param_id.college).change(function () {
            var v = $(this).val();
            initBuilding(v);

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.college);
            }
        });

        $(param_id.building).change(function () {
            var v = $(this).val();

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.building);
            }
        });

        $(param_id.buildingCode).blur(function () {
            initParam();
            var buildingCode = param.buildingCode;
            if (buildingCode.length <= 0 || buildingCode.length > 10) {
                tools.validErrorDom(param_id.buildingCode, '教室10个字符以内');
            } else {
                $.post(ajax_url.check_code, param, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.buildingCode);
                    } else {
                        tools.validErrorDom(param_id.buildingCode, data.msg);
                    }
                });
            }
        });

        /*
         保存数据
         */
        $(button_id.save.id).click(function () {
            initParam();
            if (Number(page_param.collegeId) === 0) {
                validSchoolId();
            } else {
                validBuildingCode();
            }

        });

        /**
         * 检验学校id
         */
        function validSchoolId() {
            var schoolId = param.schoolId;
            if (Number(schoolId) <= 0) {
                tools.validSelect2ErrorDom(param_id.school, '请选择学校');
            } else {
                tools.validSelect2SuccessDom(param_id.school);
                validCollegeId();
            }
        }

        /**
         * 检验院id
         */
        function validCollegeId() {
            var collegeId = param.collegeId;
            if (Number(collegeId) <= 0) {
                tools.validSelect2ErrorDom(param_id.college, '请选择院');
            } else {
                tools.validSelect2SuccessDom(param_id.college);
                validBuildingId();
            }
        }

        function validBuildingId() {
            var buildingId = param.buildingId;
            if (Number(buildingId) <= 0) {
                tools.validSelect2ErrorDom(param_id.building, '请选择教室');
            } else {
                tools.validSelect2SuccessDom(param_id.building);
                validBuildingCode();
            }
        }

        /**
         * 添加时检验并提交数据
         */
        function validBuildingCode() {
            var buildingCode = param.buildingCode;
            if (buildingCode.length <= 0 || buildingCode.length > 30) {
                tools.validErrorDom(param_id.buildingCode, '教室10个字符以内');
            } else {
                $.post(ajax_url.check_code, param, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.buildingCode);
                        sendAjax();
                    } else {
                        tools.validErrorDom(param_id.buildingCode, data.msg);
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