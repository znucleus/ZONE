requirejs.config({
    map: {
        '*': {
            'css': web_path + '/webjars/require-css/css.min.js' // or whatever the path to require-css is
        }
    },
    // pathsオプションの設定。"module/name": "path"を指定します。拡張子（.js）は指定しません。
    paths: {
        "csrf": web_path + "/js/util/csrf",
        "tools": web_path + "/js/util/tools",
        "bootstrap": web_path + "/plugins/bootstrap/js/bootstrap.bundle.min",
        "moment": web_path + "/plugins/moment/moment.min",
        "moment-with-locales": web_path + "/plugins/moment/moment-with-locales.min",
        "sweetalert2": web_path + "/plugins/sweetalert2/sweetalert2.all.min",
        "select2": web_path + "/plugins/select2/js/select2.full.min",
        "select2-zh-CN": web_path + "/plugins/select2/js/i18n/zh-CN.min",
        "bootstrap-inputmask": web_path + "/plugins/bootstrap-inputmask/bootstrap-inputmask.min",
        "flatpickr": web_path + "/plugins/flatpickr/js/flatpickr.min",
        "flatpickr-zh": web_path + "/plugins/flatpickr/l10n/zh.min",
        "jquery-loading": web_path + "/plugins/jquery-loading/jquery.loading.min"
    },
    // shimオプションの設定。モジュール間の依存関係を定義します。
    shim: {
        "tools": {
            deps: ["jquery"]
        },
        "bootstrap": {
            deps: ["jquery"]
        },
        "bootstrap-inputmask": {
            deps: ["jquery"]
        },
        "sweetalert2": {
            deps: ["jquery", "css!" + web_path + "/plugins/sweetalert2/sweetalert2.min"]
        },
        "select2-zh-CN": {
            deps: ["jquery", "select2"]
        },
        "flatpickr": {
            deps: ["jquery", "css!" + web_path + "/plugins/flatpickr/css/flatpickr.min"]
        },
        "flatpickr-zh": {
            deps: ["flatpickr"]
        },
        "messenger": {
            deps: ["jquery"]
        },
        "jquery-loading": {
            deps: ["jquery"]
        }
    }
});

/*
 捕获全局错误
 */
requirejs.onError = function (err) {
    console.log(err.requireType);
    if (err.requireType === 'timeout') {
        console.log('modules: ' + err.requireModules);
    }
    throw err;
};

require(["jquery", "tools", "sweetalert2", "moment-with-locales", "bootstrap", "messenger",
        "csrf", "select2-zh-CN", "flatpickr-zh", "bootstrap-inputmask", "jquery-loading"],
    function ($, tools, Swal, moment) {
        Messenger.options = {
            extraClasses: 'messenger-fixed messenger-on-bottom messenger-on-right',
            theme: 'flat'
        };

        moment.locale('zh-cn');

        var ajax_url = {
            obtain_department_data: web_path + '/anyone/data/department',
            obtain_science_data: web_path + '/anyone/data/science',
            obtain_organize_data: web_path + '/anyone/data/organize',
            obtain_nation_data: web_path + '/anyone/data/nation',
            obtain_political_landscape_data: web_path + '/anyone/data/politics',
            check_student_number: web_path + '/anyone/campus/roster/check/student/number',
            convert_name: web_path + '/anyone/campus/roster/convert_name',
            save: web_path + '/anyone/campus/roster/data/save',
            success: web_path + '/anyone/campus/roster/data/save/success'
        };

        var param_id = {
            studentNumber: '#studentNumber',
            realName: '#realName',
            namePinyin: '#namePinyin',
            sex: '#sex',
            birthday: '#birthday',
            idCard: '#idCard',
            politicalLandscapeId: '#politicalLandscape',
            nationId: '#nation',
            organizeId: '#organize',
            province: '#province',
            nativePlace: '#nativePlace',
            region: '#region',
            busSection: '#busSection',
            parentName: '#parentName',
            parentContactPhone: '#parentContactPhone',
            parentContactAddress: '#parentContactAddress',
            zipCode: '#zipCode',
            phoneNumber: '#phoneNumber',
            email: '#email',
            candidatesType: '#candidatesType',
            isDeformedMan: '#isDeformedMan',
            deformedManCode: '#deformedManCode',
            isMilitaryServiceRegistration: '#isMilitaryServiceRegistration',
            isProvideLoan: '#isProvideLoan',
            universityPosition: '#universityPosition',
            isPoorStudents: '#isPoorStudents',
            poorStudentsType: '#poorStudentsType',
            isStayOutside: '#isStayOutside',
            dormitoryNumber: '#dormitoryNumber',
            stayOutsideType: '#stayOutsideType',
            stayOutsideAddress: '#stayOutsideAddress',
            leagueMemberJoinDate: '#leagueMemberJoinDate',
            isRegisteredVolunteers: '#isRegisteredVolunteers',
            isOkLeagueMembership: '#isOkLeagueMembership',
            applyPartyMembershipDate: '#applyPartyMembershipDate',
            becomeActivistsDate: '#becomeActivistsDate',
            becomeProbationaryPartyMemberDate: '#becomeProbationaryPartyMemberDate',
            joiningPartyDate: '#joiningPartyDate'
        };

        var button_id = {
            save: {
                tip: '保存中...',
                text: '保存',
                id: '#save'
            }
        };

        var page_param = {
            rosterReleaseId: $('#rosterReleaseId').val(),
            collegeId: $('#collegeId').val(),
            grade: $('#grade').val()
        };

        var param = {
            rosterReleaseId: '',
            studentNumber: '',
            realName: '',
            namePinyin: '',
            sex: '',
            birthday: '',
            idCard: '',
            politicalLandscapeId: '',
            nationId: '',
            organizeId: '',
            province: '',
            nativePlace: '',
            region: '',
            busSection: '',
            parentName: '',
            parentContactPhone: '',
            parentContactAddress: '',
            zipCode: '',
            phoneNumber: '',
            email: '',
            candidatesType: '',
            isDeformedMan: '',
            deformedManCode: '',
            isMilitaryServiceRegistration: '',
            isProvideLoan: '',
            universityPosition: '',
            isPoorStudents: '',
            poorStudentsType: '',
            isStayOutside: '',
            dormitoryNumber: '',
            stayOutsideType: '',
            stayOutsideAddress: '',
            leagueMemberJoinDate: '',
            isRegisteredVolunteers: '',
            isOkLeagueMembership: '',
            applyPartyMembershipDate: '',
            becomeActivistsDate: '',
            becomeProbationaryPartyMemberDate: '',
            joiningPartyDate: ''
        };

        function initParam() {
            param.rosterReleaseId = page_param.rosterReleaseId;
            param.studentNumber = tools.trim($(param_id.studentNumber).val());
            param.realName = tools.trim($(param_id.realName).val());
            param.namePinyin = tools.trim($(param_id.namePinyin).val());
            param.sex = $(param_id.sex).val();
            param.birthday = $(param_id.birthday).val();
            param.idCard = tools.trim($(param_id.idCard).val());
            param.politicalLandscapeId = $(param_id.politicalLandscapeId).val();
            param.nationId = $(param_id.nationId).val();
            param.organizeId = $(param_id.organizeId).val();
            param.province = tools.trim($(param_id.province).val());
            param.nativePlace = tools.trim($(param_id.nativePlace).val());
            param.region = tools.trim($(param_id.region).val());
            param.busSection = tools.trim($(param_id.busSection).val());
            param.parentName = tools.trim($(param_id.parentName).val());
            param.parentContactPhone = tools.trim($(param_id.parentContactPhone).val());
            param.parentContactAddress = tools.trim($(param_id.parentContactAddress).val());
            param.zipCode = tools.trim($(param_id.zipCode).val());
            param.phoneNumber = tools.trim($(param_id.phoneNumber).val());
            param.email = tools.trim($(param_id.email).val());
            param.candidatesType = $(param_id.candidatesType).val();
            param.isDeformedMan = $(param_id.isDeformedMan).val();
            param.deformedManCode = tools.trim($(param_id.deformedManCode).val());
            param.isMilitaryServiceRegistration = $(param_id.isMilitaryServiceRegistration).val();
            param.isProvideLoan = $(param_id.isProvideLoan).val();
            param.universityPosition = tools.trim($(param_id.universityPosition).val());
            param.isPoorStudents = $(param_id.isPoorStudents).val();
            param.poorStudentsType = $(param_id.poorStudentsType).val();
            param.isStayOutside = $(param_id.isStayOutside).val();
            param.dormitoryNumber = tools.trim($(param_id.dormitoryNumber).val());
            param.stayOutsideType = $(param_id.stayOutsideType).val();
            param.stayOutsideAddress = tools.trim($(param_id.stayOutsideAddress).val());
            param.leagueMemberJoinDate = $(param_id.leagueMemberJoinDate).val();
            param.isRegisteredVolunteers = $(param_id.isRegisteredVolunteers).val();
            param.isOkLeagueMembership = $(param_id.isOkLeagueMembership).val();
            param.applyPartyMembershipDate = $(param_id.applyPartyMembershipDate).val();
            param.becomeActivistsDate = $(param_id.becomeActivistsDate).val();
            param.becomeProbationaryPartyMemberDate = $(param_id.becomeProbationaryPartyMemberDate).val();
            param.joiningPartyDate = $(param_id.joiningPartyDate).val();
        }

        $(param_id.studentNumber).blur(function () {
            initParam();
            var studentNumber = param.studentNumber;
            if (studentNumber !== '') {
                var regex = tools.regex.studentNumber;
                if (!regex.test(studentNumber)) {
                    tools.validErrorDom(param_id.studentNumber, '学号13位数字');
                    return;
                }

                $.post(ajax_url.check_student_number, {studentNumber: studentNumber}, function (data) {
                    if (data.state) {
                        tools.validErrorDom(param_id.studentNumber, "该学号已登记，若需要修改资料，请登录后修改。");
                    } else {
                        tools.validSuccessDom(param_id.studentNumber);
                    }
                });
            } else {
                tools.validSuccessDom(param_id.studentNumber);
            }
        });

        $(param_id.realName).blur(function () {
            initParam();
            var realName = param.realName;
            if (realName !== '') {
                tools.dataLoading();
                $.get(ajax_url.convert_name, {realName: realName}, function (data) {
                    tools.dataEndLoading();
                    if (data.state) {
                        $(param_id.namePinyin).val(data.pinyin);
                    }
                });
            }
        });

        init();

        function init() {
            initIdCard();
            initNation();
            initPoliticalLandscapeId();
            initOrganize();
            initSelect2();
        }

        function initSelect2() {
            $('.select2-show-search').select2({
                language: "zh-CN"
            });
        }

        function initIdCard() {
            var v = $(param_id.idCard).val();
            if (tools.regex.idCard.test(v)) {
                $(param_id.birthday).val(v.substring(6, 10) + "-" + v.substring(10, 12) + "-" + v.substring(12, 14));
                if (Number(v.substring(16, 17)) % 2 === 0) {
                    $(param_id.sex).val('女');
                } else {
                    $(param_id.sex).val('男');
                }
            }
        }

        function initOrganize() {
            $.get(ajax_url.obtain_organize_data, {
                grade: page_param.grade,
                collegeId: page_param.collegeId
            }, function (data) {
                $(param_id.organizeId).html('<option label="请选择班级"></option>');
                $(param_id.organizeId).select2({data: data.results});
            });
        }

        function initNation() {
            $.get(ajax_url.obtain_nation_data, function (data) {
                $(param_id.nationId).select2({
                    data: data.results
                });
            });
        }

        function initPoliticalLandscapeId() {
            $.get(ajax_url.obtain_political_landscape_data, function (data) {
                $(param_id.politicalLandscapeId).html('<option label="请选择政治面貌"></option>');
                $.each(data.results, function (i, n) {
                    $(param_id.politicalLandscapeId).append('<option value="' + n.id + '">' + n.text + '</option>');
                });
            });
        }

        $(param_id.birthday).flatpickr({
            "locale": "zh",
            defaultDate: tools.trim($(param_id.birthday).val()) !== '' ? moment(tools.trim($(param_id.birthday).val()), "YYYY-MM-DD").toDate() : moment((new Date().getFullYear() - 26) + "-01-07", "YYYY-MM-DD").toDate()
        });

        $('.flatpickr').flatpickr({
            "locale": "zh",
            wrap: true
        });

        $(param_id.idCard).blur(function () {
            initIdCard();
        });

        $(param_id.isDeformedMan).change(function () {
            var v = $(this).val();
            if (Number(v) === 1) {
                $(param_id.deformedManCode).parent().css('display', '');
            } else {
                $(param_id.deformedManCode).parent().css('display', 'none');
                $(param_id.deformedManCode).val('');
            }
        });

        $(param_id.isPoorStudents).change(function () {
            var v = $(this).val();
            if (Number(v) === 1) {
                $(param_id.poorStudentsType).parent().css('display', '');
            } else {
                $(param_id.poorStudentsType).parent().css('display', 'none');
                $(param_id.poorStudentsType).val('');
            }
        });

        $(param_id.isStayOutside).change(function () {
            var v = $(this).val();
            if (Number(v) === 1) {
                $(param_id.stayOutsideType).parent().css('display', '');
                $(param_id.stayOutsideAddress).parent().css('display', '');
                $(param_id.dormitoryNumber).parent().css('display', 'none');
                $(param_id.dormitoryNumber).val('');
            } else {
                $(param_id.stayOutsideType).parent().css('display', 'none');
                $(param_id.stayOutsideAddress).parent().css('display', 'none');
                $(param_id.dormitoryNumber).parent().css('display', '');
                $(param_id.stayOutsideType).val('');
                $(param_id.stayOutsideAddress).val('');
            }
        });

        $(button_id.save.id).click(function () {
            initParam();
            validStudentNumber();
        });

        function validStudentNumber() {
            var studentNumber = param.studentNumber;
            if (studentNumber !== '') {
                var regex = tools.regex.studentNumber;
                if (!regex.test(studentNumber)) {
                    tools.validErrorDom(param_id.studentNumber, '学号13位数字');
                    errorTip('学号13位数字');
                    return;
                }

                $.post(ajax_url.check_student_number, {studentNumber: studentNumber}, function (data) {
                    if (data.state) {
                        tools.validErrorDom(param_id.studentNumber, "该学号已填写，若需要修改资料，请登录后修改。");
                        errorTip('该学号已填写，若需要修改资料，请登录后修改。');
                    } else {
                        tools.validSuccessDom(param_id.studentNumber);
                        validRealName();
                    }
                });
            } else {
                tools.validErrorDom(param_id.studentNumber, "请填写学号");
                errorTip('请填写学号');
            }
        }

        function validRealName() {
            var realName = param.realName;
            if (realName.length <= 0 || realName.length > 15) {
                tools.validErrorDom(param_id.realName, '姓名15个字符以内');
                errorTip('姓名15个字符以内');
            } else {
                tools.validSuccessDom(param_id.realName);
                validNamePinyin();
            }
        }

        function validNamePinyin() {
            var namePinyin = param.namePinyin;
            if (namePinyin.length <= 0 || namePinyin.length > 20) {
                tools.validErrorDom(param_id.namePinyin, '姓名拼音20个字符以内');
                errorTip('姓名拼音20个字符以内');
            } else {
                tools.validSuccessDom(param_id.namePinyin);
                validIdCard();
            }
        }

        function validIdCard() {
            var idCard = param.idCard;
            if (idCard.length <= 0) {
                tools.validErrorDom(param_id.idCard, '请填写身份证号');
                errorTip('请填写身份证号');
            } else {
                if (!tools.regex.idCard.test(idCard)) {
                    tools.validErrorDom(param_id.idCard, '身份证号格式不正确');
                    errorTip('身份证号格式不正确');
                } else {
                    tools.validSuccessDom(param_id.idCard);
                    validSex();
                }
            }

        }

        function validSex() {
            var sex = param.sex;
            if (sex.length <= 0) {
                tools.validErrorDom(param_id.sex, '请选择性别');
                errorTip('请选择性别');
            } else {
                tools.validSuccessDom(param_id.sex);
                validBirthday();
            }
        }

        function validBirthday() {
            var birthday = param.birthday;
            if (birthday.length <= 0) {
                tools.validErrorDom(param_id.birthday, '请选择生日');
                errorTip('请选择生日');
            } else {
                tools.validSuccessDom(param_id.birthday);
                validPoliticalLandscape();
            }
        }

        function validPoliticalLandscape() {
            var politicalLandscapeId = param.politicalLandscapeId;
            if (Number(politicalLandscapeId) <= 0) {
                tools.validErrorDom(param_id.politicalLandscapeId, '请选择政治面貌');
                errorTip('请选择政治面貌');
            } else {
                tools.validSuccessDom(param_id.politicalLandscapeId);
                validNation();
            }
        }

        function validNation() {
            var nationId = param.nationId;
            if (Number(nationId) <= 0) {
                tools.validSelect2ErrorDom(param_id.nationId, '请选择民族');
                errorTip('请选择民族');
            } else {
                tools.validSelect2SuccessDom(param_id.nationId);
                validOrganize();
            }
        }

        function validOrganize() {
            var organizeId = param.organizeId;
            if (Number(organizeId) <= 0) {
                tools.validSelect2ErrorDom(param_id.organizeId, '请选择班级');
                errorTip('请选择班级');
            } else {
                tools.validSelect2SuccessDom(param_id.organizeId);
                validProvince();
            }
        }

        function validProvince() {
            var province = param.province;
            if (province.length <= 0 || province.length > 20) {
                tools.validErrorDom(param_id.province, '省份20个字符以内');
                errorTip('省份20个字符以内');
            } else {
                tools.validSuccessDom(param_id.province);
                validNativePlace();
            }
        }

        function validNativePlace() {
            var nativePlace = param.nativePlace;
            if (nativePlace.length <= 0 || nativePlace.length > 120) {
                tools.validErrorDom(param_id.nativePlace, '籍贯120个字符以内');
                errorTip('籍贯120个字符以内');
            } else {
                tools.validSuccessDom(param_id.nativePlace);
                validRegion();
            }
        }

        function validRegion() {
            var region = param.region;
            if (region.length <= 0 || region.length > 50) {
                tools.validErrorDom(param_id.region, '所属地区50个字符以内');
                errorTip('所属地区50个字符以内');
            } else {
                tools.validSuccessDom(param_id.region);
                validBusSection();
            }
        }

        function validBusSection() {
            var busSection = param.busSection;
            if (busSection.length <= 0 || busSection.length > 150) {
                tools.validCustomerSingleErrorDom(param_id.busSection, '乘车区间150个字符以内');
                errorTip('乘车区间150个字符以内');
            } else {
                tools.validCustomerSingleSuccessDom(param_id.busSection);
                validParentName();
            }
        }

        function validParentName() {
            var parentName = param.parentName;
            if (parentName.length <= 0 || parentName.length > 10) {
                tools.validErrorDom(param_id.parentName, '家长姓名10个字符以内');
                errorTip('家长姓名10个字符以内');
            } else {
                tools.validSuccessDom(param_id.parentName);
                validParentContactPhone();
            }
        }

        function validParentContactPhone() {
            var parentContactPhone = param.parentContactPhone;
            if (parentContactPhone.length <= 0) {
                tools.validErrorDom(param_id.parentContactPhone, '请填写家长联系电话');
                errorTip('请填写家长联系电话');
            } else {
                if (!tools.regex.mobile.test(parentContactPhone)) {
                    tools.validErrorDom(param_id.parentContactPhone, '家长联系电话格式不正确');
                    errorTip('家长联系电话格式不正确');
                } else {
                    tools.validSuccessDom(param_id.parentContactPhone);
                    validParentContactAddress();
                }
            }
        }

        function validParentContactAddress() {
            var parentContactAddress = param.parentContactAddress;
            if (parentContactAddress.length <= 0 || parentContactAddress.length > 200) {
                tools.validErrorDom(param_id.parentContactAddress, '家长联系地址200个字符以内');
                errorTip('家长联系地址200个字符以内');
            } else {
                tools.validSuccessDom(param_id.parentContactAddress);
                validZipCode();
            }
        }

        function validZipCode() {
            var zipCode = param.zipCode;
            if (zipCode.length <= 0) {
                tools.validErrorDom(param_id.zipCode, '请填写邮政编码');
                errorTip('请填写邮政编码');
            } else {
                if (!tools.regex.zipCode.test(zipCode)) {
                    tools.validErrorDom(param_id.zipCode, '邮政编码格式不正确');
                    errorTip('邮政编码格式不正确');
                } else {
                    tools.validSuccessDom(param_id.zipCode);
                    validPhoneNumber();
                }
            }
        }

        function validPhoneNumber() {
            var phoneNumber = param.phoneNumber;
            if (phoneNumber.length <= 0) {
                tools.validErrorDom(param_id.phoneNumber, '请填写本人联系电话');
                errorTip('请填写本人联系电话');
            } else {
                if (!tools.regex.mobile.test(phoneNumber)) {
                    tools.validErrorDom(param_id.phoneNumber, '本人联系电话格式不正确');
                    errorTip('本人联系电话格式不正确');
                } else {
                    tools.validSuccessDom(param_id.phoneNumber);
                    validEmail();
                }
            }
        }

        function validEmail() {
            var email = param.email;
            if (email.length <= 0) {
                tools.validErrorDom(param_id.email, '请填写邮箱');
                errorTip('请填写邮箱');
            } else {
                if (!tools.regex.email.test(email)) {
                    tools.validErrorDom(param_id.email, '邮箱格式不正确');
                    errorTip('邮箱格式不正确');
                } else {
                    tools.validSuccessDom(param_id.email);
                    validDeformedManCode();
                }
            }
        }

        function validDeformedManCode() {
            var isDeformedMan = param.isDeformedMan;
            var deformedManCode = param.deformedManCode;
            if (Number(isDeformedMan) === 1) {
                if (deformedManCode.length <= 0 || deformedManCode.length > 100) {
                    tools.validErrorDom(param_id.deformedManCode, '残疾人编号100个字符以内');
                    errorTip('残疾人编号100个字符以内');
                } else {
                    tools.validSuccessDom(param_id.deformedManCode);
                    validPoorStudentsType();
                }
            } else {
                tools.validSuccessDom(param_id.deformedManCode);
                validPoorStudentsType();
            }
        }

        function validPoorStudentsType() {
            var isPoorStudents = param.isPoorStudents;
            var poorStudentsType = param.poorStudentsType;
            if (Number(isPoorStudents) === 1) {
                if (poorStudentsType === '') {
                    tools.validErrorDom(param_id.poorStudentsType, '请选择贫困生类型');
                    errorTip('请选择贫困生类型');
                } else {
                    tools.validSuccessDom(param_id.poorStudentsType);
                    validDormitoryNumber();
                }
            } else {
                tools.validSuccessDom(param_id.poorStudentsType);
                validDormitoryNumber();
            }
        }

        function validDormitoryNumber() {
            var isStayOutside = param.isStayOutside;
            var dormitoryNumber = param.dormitoryNumber;
            if (Number(isStayOutside) === 0) {
                if (dormitoryNumber.length <= 0) {
                    tools.validErrorDom(param_id.dormitoryNumber, '请填写宿舍号');
                    errorTip('请填写宿舍号');
                } else {
                    if (!tools.regex.dormitoryNumber.test(dormitoryNumber)) {
                        tools.validErrorDom(param_id.dormitoryNumber, '宿舍号格式不正确');
                        errorTip('宿舍号格式不正确');
                    } else {
                        tools.validSuccessDom(param_id.dormitoryNumber);
                        validStayOutsideType();
                    }
                }
            } else {
                tools.validSuccessDom(param_id.dormitoryNumber);
                validStayOutsideType();
            }
        }

        function validStayOutsideType() {
            var isStayOutside = param.isStayOutside;
            var stayOutsideType = param.stayOutsideType;
            if (Number(isStayOutside) === 1) {
                if (stayOutsideType === '') {
                    tools.validErrorDom(param_id.stayOutsideType, '请选择外宿类型');
                    errorTip('请选择外宿类型');
                } else {
                    tools.validSuccessDom(param_id.stayOutsideType);
                    validStayOutsideAddress();
                }
            } else {
                tools.validSuccessDom(param_id.stayOutsideType);
                validStayOutsideAddress();
            }
        }

        function validStayOutsideAddress() {
            var isStayOutside = param.isStayOutside;
            var stayOutsideAddress = param.stayOutsideAddress;
            if (Number(isStayOutside) === 1) {
                if (stayOutsideAddress.length <= 0 || stayOutsideAddress.length > 200) {
                    tools.validErrorDom(param_id.stayOutsideAddress, '外宿详细地址200个字符以内');
                    errorTip('外宿详细地址200个字符以内');
                } else {
                    tools.validSuccessDom(param_id.stayOutsideAddress);
                    lastInitParam();
                }
            } else {
                tools.validSuccessDom(param_id.stayOutsideAddress);
                lastInitParam();
            }
        }

        function errorTip(msg) {
            Messenger().post({
                message: msg,
                type: 'error',
                showCloseButton: true
            });
        }

        function lastInitParam() {
            param.busSection = "昆明-" + param.busSection;
            sendAjax();
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
                        window.location.href = ajax_url.success;
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