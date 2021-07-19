//# sourceURL=examination_detail_edit.js
require(["jquery", "lodash", "tools", "handlebars", "sweetalert2", "nav.active", "messenger", "flatpickr-zh"],
    function ($, _, tools, Handlebars, Swal, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            update: web_path + '/web/educational/examination/detail/update',
            page: '/web/menu/educational/examination',
            back: '/web/educational/examination/look'
        };

        // 刷新时选中菜单
        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
            serialNumber: '#serialNumber',
            courseNumber: '#courseNumber',
            courseName: '#courseName',
            organizeName: '#organizeName',
            peopleNumber: '#peopleNumber',
            serialNumberRange: '#serialNumberRange',
            examWeek: '#examWeek',
            examDate: '#examDate',
            weekday: '#weekday',
            examTime: '#examTime',
            examClassroom: '#examClassroom',
            invigilator: '#invigilator',
            chiefExaminer: '#chiefExaminer',
            mobiles: '#mobiles'
        };

        var button_id = {
            save: {
                id: '#save',
                text: '保存',
                tip: '保存中...'
            }
        };

        var page_param = {
            examinationNoticeReleaseId: $('#examinationNoticeReleaseId').val(),
            examinationNoticeDetailId: $('#examinationNoticeDetailId').val(),
            weekday: $('#paramWeekday').val()
        };

        /*
         参数
         */
        var param = {
            serialNumber: '',
            courseNumber: '',
            courseName: '',
            organizeName: '',
            peopleNumber: '',
            serialNumberRange: '',
            examWeek: '',
            examDate: '',
            weekday: '',
            examTime: '',
            examClassroom: '',
            invigilator: '',
            chiefExaminer: '',
            mobiles: '',
            examinationNoticeReleaseId: '',
            examinationNoticeDetailId: ''
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.serialNumber = $(param_id.serialNumber).val();
            param.courseNumber = _.trim($(param_id.courseNumber).val());
            param.courseName = _.trim($(param_id.courseName).val());
            param.organizeName = _.trim($(param_id.organizeName).val());
            param.peopleNumber = $(param_id.peopleNumber).val();
            param.serialNumberRange = _.trim($(param_id.serialNumberRange).val());
            param.examWeek = $(param_id.examWeek).val();
            param.examDate = _.trim($(param_id.examDate).val());
            param.weekday = $(param_id.weekday).val();
            param.examTime = _.trim($(param_id.examTime).val());
            param.examClassroom = _.trim($(param_id.examClassroom).val());
            param.invigilator = $(param_id.invigilator).val();
            param.chiefExaminer = $(param_id.chiefExaminer).val();
            param.mobiles = _.trim($(param_id.mobiles).val());
            param.examinationNoticeReleaseId = page_param.examinationNoticeReleaseId;
            param.examinationNoticeDetailId = page_param.examinationNoticeDetailId;
        }

        init();

        function init() {
            $(param_id.weekday).val(page_param.weekday);
        }

        $(param_id.serialNumber).blur(function () {
            initParam();
            var serialNumber = param.serialNumber;
            if (serialNumber.length <= 0) {
                tools.validErrorDom(param_id.serialNumber, '请填写序号');
            } else {
                tools.validSuccessDom(param_id.serialNumber);
            }
        });

        $(param_id.courseName).blur(function () {
            initParam();
            var courseName = param.courseName;
            if (courseName.length <= 0) {
                tools.validErrorDom(param_id.courseName, '请填写课程');
            } else {
                tools.validSuccessDom(param_id.courseName);
            }
        });

        $(param_id.examDate).flatpickr({
            "locale": "zh"
        });

        $(param_id.examTime).blur(function () {
            initParam();
            var examTime = param.examTime;
            if (examTime.length <= 0) {
                tools.validErrorDom(param_id.examTime, '请填写考试时间');
            } else {
                tools.validSuccessDom(param_id.examTime);
            }
        });

        $(param_id.examClassroom).blur(function () {
            initParam();
            var examClassroom = param.examClassroom;
            if (examClassroom.length <= 0) {
                tools.validErrorDom(param_id.examClassroom, '请填写考试教室');
            } else {
                tools.validSuccessDom(param_id.examClassroom);
            }
        });

        /*
         保存数据
         */
        $(button_id.save.id).click(function () {
            initParam();
            validSerialNumber();
        });

        function validSerialNumber() {
            var serialNumber = param.serialNumber;
            if (serialNumber.length <= 0) {
                tools.validErrorDom(param_id.serialNumber, '请填写序号');
            } else {
                tools.validSuccessDom(param_id.serialNumber);
                validCourseName();
            }
        }

        function validCourseName() {
            var courseName = param.courseName;
            if (courseName.length <= 0) {
                tools.validErrorDom(param_id.courseName, '请填写课程');
            } else {
                tools.validSuccessDom(param_id.courseName);
                validExamDate();
            }
        }

        function validExamDate() {
            var examDate = param.examDate;
            if (examDate.length <= 0) {
                tools.validErrorDom(param_id.examDate, '请填写考试日期');
            } else {
                tools.validSuccessDom(param_id.examDate);
                validExamTime();
            }
        }

        function validExamTime() {
            var examTime = param.examTime;
            if (examTime.length <= 0) {
                tools.validErrorDom(param_id.examTime, '请填写考试时间');
            } else {
                tools.validSuccessDom(param_id.examTime);
                validExamClassroom();
            }
        }

        function validExamClassroom() {
            var examClassroom = param.examClassroom;
            if (examClassroom.length <= 0) {
                tools.validErrorDom(param_id.examClassroom, '请填写考试教室');
            } else {
                tools.validSuccessDom(param_id.examClassroom);
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
                                $.address.value(ajax_url.back + "/" + page_param.examinationNoticeReleaseId);
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