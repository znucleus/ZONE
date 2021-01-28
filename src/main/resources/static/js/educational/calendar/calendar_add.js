//# sourceURL=calendar_add.js
require(["jquery", "tools", "moment-with-locales", "sweetalert2", "nav.active", "messenger", "select2-zh-CN", "jquery.address", "bootstrap-maxlength", "flatpickr-zh"],
    function ($, tools, moment, Swal, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            obtain_school_data: web_path + '/anyone/data/schools',
            obtain_college_data: web_path + '/anyone/data/college',
            save: web_path + '/web/educational/calendar/save',
            back: '/web/educational/calendar/list',
            page: '/web/menu/educational/calendar'
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
            academicYear: '#academicYear',
            term: '#term',
            startDate: '#startDate',
            endDate: '#endDate',
            holidayStartDate: '#holidayStartDate',
            holidayEndDate: '#holidayEndDate',
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
            academicYear: '',
            term: '',
            startDate: '',
            endDate: '',
            holidayStartDate: '',
            holidayEndDate: '',
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

            param.academicYear = $(param_id.academicYear).val();
            param.term = $(param_id.term).val();

            param.startDate = $(param_id.startDate).val();
            param.endDate = $(param_id.endDate).val();
            param.holidayStartDate = $(param_id.holidayStartDate).val();
            param.holidayEndDate = $(param_id.holidayEndDate).val();
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
            initAcademicYear();
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

        function initAcademicYear() {
            $(param_id.academicYear).html('<option value="" label="请选择学年"></option>');
            var date = new Date();
            var year = date.getFullYear();
            var yearArr = [];
            for (var i = year + 2; i >= year - 6; i--) {
                yearArr.push({
                    id: (i - 1) + '-' + (i),
                    text: (i - 1) + '-' + (i) + '学年'
                });
            }

            $(param_id.academicYear).select2({data: yearArr});
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

        $('.flatpickr').flatpickr({
            locale: "zh"
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

        $(param_id.academicYear).change(function () {
            var v = $(this).val();

            if (Number(v) > 0) {
                tools.validSelect2SuccessDom(param_id.academicYear);
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
                validAcademicYear();
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
                validAcademicYear();
            }
        }

        function validAcademicYear() {
            var academicYear = param.academicYear;
            if (academicYear === '') {
                tools.validSelect2ErrorDom(param_id.academicYear, '请选择学年');
            } else {
                tools.validSelect2SuccessDom(param_id.academicYear);
                validTerm();
            }
        }

        function validTerm() {
            var term = param.term;
            if (term === '') {
                tools.validErrorDom(param_id.term, '请选择学期');
            } else {
                tools.validSuccessDom(param_id.term);
                validStartDate();
            }
        }

        function validStartDate() {
            var startDate = param.startDate;
            if (startDate.length <= 0) {
                tools.validErrorDom(param_id.startDate, '请选择开学日期');
            } else {
                tools.validSuccessDom(param_id.startDate);
                validEndDate();
            }
        }

        function validEndDate() {
            var endDate = param.endDate;
            if (endDate.length <= 0) {
                tools.validErrorDom(param_id.endDate, '请选择结束日期');
            } else {
                var startDate = param.startDate;
                if (moment(endDate, 'YYYY-MM-DD').isSameOrAfter(moment(startDate, 'YYYY-MM-DD'))) {
                    tools.validSuccessDom(param_id.endDate);
                    validHolidayStartDate();
                } else {
                    tools.validErrorDom(param_id.endDate, '开始日期应小于或等于结束日期');
                }
            }
        }

        function validHolidayStartDate() {
            var holidayStartDate = param.holidayStartDate;
            if (holidayStartDate.length <= 0) {
                tools.validErrorDom(param_id.holidayStartDate, '请选择放假日期');
            } else {
                tools.validSuccessDom(param_id.holidayStartDate);
                validHolidayEndDate();
            }
        }

        function validHolidayEndDate() {
            var holidayEndDate = param.holidayEndDate;
            if (holidayEndDate.length <= 0) {
                tools.validErrorDom(param_id.holidayEndDate, '请选择收假日期');
            } else {
                var holidayStartDate = param.holidayStartDate;
                if (moment(holidayEndDate, 'YYYY-MM-DD').isSameOrAfter(moment(holidayStartDate, 'YYYY-MM-DD'))) {
                    tools.validSuccessDom(param_id.holidayEndDate);
                    validRemark();
                } else {
                    tools.validErrorDom(param_id.holidayEndDate, '放假日期应小于或等于收假日期');
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
                                $.address.value(ajax_url.back);
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