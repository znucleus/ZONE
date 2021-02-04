//# sourceURL=building_add.js
require(["jquery", "lodash", "tools", "sweetalert2", "nav.active", "messenger", "jquery.address", "bootstrap-maxlength", "select2-zh-CN"],
    function ($, _, tools, Swal, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            obtain_school_data: web_path + '/anyone/data/school',
            obtain_college_data: web_path + '/anyone/data/college',
            obtain_building_classifies_data: web_path + '/users/data/building-classifies',
            save: web_path + '/web/data/building/save',
            check_name: web_path + '/web/data/building/check-add-name',
            page: '/web/menu/data/building'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
            school: '#school',
            college: '#college',
            buildingClassify: '#buildingClassify',
            buildingName: '#buildingName',
            coordinate: '#coordinate'
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
            buildingClassifyId: '',
            buildingName: '',
            coordinate: '',
            buildingIsDel: ''
        };

        var page_param = {
            collegeId: $('#collegeId').val()
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
            param.buildingClassifyId = $(param_id.buildingClassify).val();
            param.buildingName = _.trim($(param_id.buildingName).val());
            param.coordinate = _.trim($(param_id.coordinate).val());
            var buildingIsDel = $('input[name="buildingIsDel"]:checked').val();
            param.buildingIsDel = _.isUndefined(buildingIsDel) ? 0 : buildingIsDel;
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
                initBuildingClassifies();
                initSelect2();
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

        function initBuildingClassifies() {
            $.get(ajax_url.obtain_building_classifies_data, function (data) {
                $(param_id.buildingClassify).select2({
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
            $(param_id.buildingName).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });

            $(param_id.coordinate).maxlength({
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

        $(param_id.buildingClassify).change(function () {
            var v = $(this).val();

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.buildingClassify);
            }
        });

        $(param_id.buildingName).blur(function () {
            initParam();
            var buildingName = param.buildingName;
            if (buildingName.length <= 0 || buildingName.length > 30) {
                tools.validErrorDom(param_id.buildingName, '楼名30个字符以内');
            } else {
                $.get(ajax_url.check_name, param, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.buildingName);
                    } else {
                        tools.validErrorDom(param_id.buildingName, data.msg);
                    }
                });
            }
        });

        $(param_id.coordinate).blur(function () {
            initParam();
            var coordinate = param.coordinate;
            if (coordinate.length > 1000) {
                tools.validErrorDom(param_id.coordinate, '坐标1000个字符以内');
            } else {
                tools.validSuccessDom(param_id.coordinate);
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
                validBuildingClassifyId();
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
                validBuildingClassifyId();
            }
        }

        /**
         * 检验楼类型
         */
        function validBuildingClassifyId() {
            var buildingClassifyId = param.buildingClassifyId;
            if (Number(buildingClassifyId) <= 0) {
                tools.validSelect2ErrorDom(param_id.buildingClassify, '请选择类型');
            } else {
                tools.validSelect2SuccessDom(param_id.buildingClassify);
                validBuildingName();
            }
        }

        /**
         * 添加时检验并提交数据
         */
        function validBuildingName() {
            var buildingName = param.buildingName;
            if (buildingName.length <= 0 || buildingName.length > 30) {
                tools.validErrorDom(param_id.buildingName, '楼名30个字符以内');
            } else {
                $.get(ajax_url.check_name, param, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.buildingName);
                        validCoordinate();
                    } else {
                        tools.validErrorDom(param_id.buildingName, data.msg);
                    }
                });
            }
        }

        function validCoordinate() {
            var coordinate = param.coordinate;
            if (coordinate.length > 1000) {
                tools.validErrorDom(param_id.coordinate, '坐标1000个字符以内');
            } else {
                tools.validSuccessDom(param_id.coordinate);
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