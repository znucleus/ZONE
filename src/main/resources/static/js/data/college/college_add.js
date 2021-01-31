//# sourceURL=college_add.js
require(["jquery", "lodash", "tools", "sweetalert2", "nav.active", "messenger", "jquery.address", "bootstrap-maxlength", "select2-zh-CN"],
    function ($, _, tools, Swal, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            obtain_school_data: web_path + '/anyone/data/school',
            save: web_path + '/web/data/college/save',
            check_name: web_path + '/web/data/college/check/add/name',
            check_code: web_path + '/web/data/college/check/add/code',
            page: '/web/menu/data/college'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
            school: '#school',
            collegeName: '#collegeName',
            collegeCode: '#collegeCode',
            collegeAddress: '#collegeAddress'
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
            collegeName: '',
            collegeCode: '',
            collegeAddress: ''
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.schoolId = $(param_id.school).val();
            param.collegeName = _.trim($(param_id.collegeName).val());
            param.collegeCode = _.trim($(param_id.collegeCode).val());
            param.collegeAddress = _.trim($(param_id.collegeAddress).val());
        }

        init();

        /**
         * 初始化
         */
        function init() {
            initSchool();
            initSelect2();
            initMaxLength();
        }

        function initSchool() {
            $.get(ajax_url.obtain_school_data, function (data) {
                $(param_id.school).select2({
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
            $(param_id.collegeName).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });

            $(param_id.collegeCode).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });

            $(param_id.collegeAddress).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });
        }

        $(param_id.school).change(function () {
            var v = $(this).val();

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.school);
            }
        });

        /*
         即时检验院名
         */
        $(param_id.collegeName).blur(function () {
            initParam();
            var collegeName = param.collegeName;
            if (collegeName.length <= 0 || collegeName.length > 200) {
                tools.validErrorDom(param_id.collegeName, '院名200个字符以内');
            } else {
                $.post(ajax_url.check_name, param, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.collegeName);
                    } else {
                        tools.validErrorDom(param_id.collegeName, data.msg);
                    }
                });
            }
        });

        /*
         即时检验院代码
         */
        $(param_id.collegeCode).blur(function () {
            initParam();
            var collegeCode = param.collegeCode;
            if (collegeCode.length <= 0 || collegeCode.length > 20) {
                tools.validErrorDom(param_id.collegeCode, '院代码20个字符以内');
            } else {
                $.post(ajax_url.check_code, param, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.collegeCode);
                    } else {
                        tools.validErrorDom(param_id.collegeCode, data.msg);
                    }
                });
            }
        });


        /*
         保存数据
         */
        $(button_id.save.id).click(function () {
            initParam();
            validSchoolId();
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
                validCollegeName();
            }
        }

        /**
         * 添加时检验并提交数据
         */
        function validCollegeName() {
            var collegeName = param.collegeName;
            if (collegeName.length <= 0 || collegeName.length > 200) {
                tools.validErrorDom(param_id.collegeName, '院名200个字符以内');
            } else {
                $.post(ajax_url.check_name, param, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.collegeName);
                        validCollegeCode();
                    } else {
                        tools.validErrorDom(param_id.collegeName, data.msg);
                    }
                });
            }
        }

        /**
         * 添加时检验并提交数据
         */
        function validCollegeCode() {
            var collegeCode = param.collegeCode;
            if (collegeCode.length <= 0 || collegeCode.length > 20) {
                tools.validErrorDom(param_id.collegeCode, '院代码20个字符以内');
            } else {
                $.post(ajax_url.check_code, param, function (data) {
                    if (data.state) {
                        tools.validSuccessDom(param_id.collegeCode);
                        validCollegeAddress();
                    } else {
                        tools.validErrorDom(param_id.collegeCode, data.msg);
                    }
                });
            }
        }

        function validCollegeAddress() {
            var collegeAddress = param.collegeAddress;
            if (collegeAddress.length <= 0 || collegeAddress.length > 500) {
                tools.validErrorDom(param_id.collegeAddress, '院地址500个字符以内');
            } else {
                tools.validSuccessDom(param_id.collegeAddress);
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
                data: $('#app_form').serialize(),
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