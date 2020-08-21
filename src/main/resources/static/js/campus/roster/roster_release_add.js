//# sourceURL=roster_release_add.js
require(["jquery", "tools", "moment-with-locales", "sweetalert2", "nav.active", "messenger", "select2-zh-CN", "jquery.address", "bootstrap-maxlength", "flatpickr-zh"],
    function ($, tools, moment, Swal, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            obtain_school_data: web_path + '/anyone/data/school',
            obtain_college_data: web_path + '/anyone/data/college',
            save: web_path + '/web/campus/roster/save',
            page: '/web/menu/campus/roster'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        var page_param = {
            collegeId: $('#collegeId').val()
        };

        /*
         参数id
         */
        var param_id = {
            school: '#school',
            college: '#college',
            grade: '#grade',
            startTime: '#startTime',
            endTime: '#endTime',
            remark: '#remark'
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
            grade: '',
            title: '',
            startTime: '',
            endTime: '',
            remark: ''
        };

        /**
         * 初始化参数
         */
        function initParam() {
            if (Number(page_param.collegeId) === 0) {
                param.schoolId = $(param_id.school).val();
                param.collegeId = $(param_id.college).val();
            } else {
                param.collegeId = page_param.collegeId;
            }

            param.grade = $(param_id.grade).val();

            param.title = $(param_id.school).find("option:selected").text() + $(param_id.college).find("option:selected").text() + $(param_id.grade).find("option:selected").text() + '花名册';

            var startTime = $(param_id.startTime).val();
            if (startTime.length > 0) {
                param.startTime = startTime + ":00";
            } else {
                param.startTime = startTime;
            }

            var endTime = $(param_id.endTime).val();
            if (endTime.length > 0) {
                param.endTime = endTime + ":00";
            } else {
                param.endTime = endTime;
            }
            param.remark = $(param_id.remark).val();
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
                $(param_id.school).parent().css('display', '');
                $(param_id.college).parent().css('display', '');
            }
            initGrade();
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

        function initGrade() {
            $(param_id.grade).html('<option label="请选择年级"></option>');
            var date = new Date();
            var year = date.getFullYear();
            var yearArr = [];
            for (var i = year + 1; i >= year - 4; i--) {
                yearArr.push({
                    id: i,
                    text: i + '级'
                });
            }

            $(param_id.grade).select2({data: yearArr});
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
            $(param_id.remark).maxlength({
                alwaysShow: true,
                threshold: 10,
                warningClass: "text-success",
                limitReachedClass: "text-danger"
            });
        }

        $(param_id.startTime).flatpickr({
            locale: "zh",
            enableTime: true,
            dateFormat: "Y-m-d H:i"
        });

        $(param_id.endTime).flatpickr({
            locale: "zh",
            enableTime: true,
            dateFormat: "Y-m-d H:i"
        });

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

        $(param_id.grade).change(function () {
            var v = $(this).val();

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.grade);
            }
        });

        $(param_id.remark).change(function () {
            var v = $(this).val();

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.remark);
            }
        });

        $(param_id.remark).blur(function () {
            initParam();
            var remark = param.remark;
            if (remark.length > 200) {
                tools.validErrorDom(param_id.remark, '备注200个字符以内');
            } else {
                tools.validSuccessDom(param_id.remark);
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
                validGradeId();
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
                validGradeId();
            }
        }

        function validGradeId() {
            var grade = param.grade;
            if (Number(grade) <= 0) {
                tools.validSelect2ErrorDom(param_id.grade, '请选择年级');
            } else {
                tools.validSelect2SuccessDom(param_id.grade);
                validStartTime();
            }
        }

        function validStartTime() {
            var startTime = param.startTime;
            if (startTime.length <= 0) {
                tools.validErrorDom(param_id.startTime, '请选择开始填写时间');
            } else {
                tools.validSuccessDom(param_id.startTime);
                validEndTime();
            }
        }

        function validEndTime() {
            var endTime = param.endTime;
            if (endTime.length <= 0) {
                tools.validErrorDom(param_id.endTime, '请选择结束填写时间');
            } else {
                var startTime = param.startTime;
                if (moment(endTime, 'YYYY-MM-DD HH:mm:ss').isSameOrAfter(moment(startTime, 'YYYY-MM-DD HH:mm:ss'))) {
                    tools.validSuccessDom(param_id.endTime);
                    validRemark();
                } else {
                    tools.validErrorDom(param_id.endTime, '开始填写时间应大于或等于结束填写时间');
                }
            }
        }

        function validRemark() {
            var remark = param.remark;
            if (remark.length > 200) {
                tools.validErrorDom(param_id.remark, '备注200个字符以内');
            } else {
                tools.validSuccessDom(param_id.remark);
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