//# sourceURL=software_exam_query.js
require(["jquery", "lodash", "tools", "handlebars", "sweetalert2", "nav.active", "select2-zh-CN", "messenger", "jquery.address"],
    function ($, _, tools, Handlebars, Swal, navActive) {

        /*
         ajax url.
         */
        var ajax_url = {
            query: web_path + '/web/achievement/software/query/data',
            exam_date: web_path + '/web/achievement/software/query/exam-date',
            page: '/web/menu/achievement/software/query'
        };

        navActive(ajax_url.page);

        /*
         参数id
         */
        var param_id = {
            selectType: '#selectType',
            stage: '#stage',
            zjhm: '#zjhm',
            xm: '#xm',
            jym: '#jym'
        };

        var button_id = {
            query: {
                id: '#query',
                text: '查询',
                tip: '查询中...'
            }
        };

        /*
         参数
         */
        var param = {
            selectType: '',
            stage: '',
            zjhm: '',
            xm: '',
            jym: ''
        };

        /**
         * 初始化参数
         */
        function initParam() {
            param.selectType = $(param_id.selectType).val();
            param.stage = _.trim($(param_id.stage).val());
            param.zjhm = _.trim($(param_id.zjhm).val());
            param.xm = _.trim($(param_id.xm).val());
            param.jym = _.trim($(param_id.jym).val());
        }

        var zjhmText = '准考证号';
        $(param_id.selectType).change(function () {
            var v = Number($(this).val());
            if (v === 0) {
                zjhmText = '准考证号';
                $(param_id.zjhm).prev().text(zjhmText);
                $(param_id.zjhm).attr('placeholder', zjhmText);
            } else {
                zjhmText = '证件号';
                $(param_id.zjhm).prev().text(zjhmText);
                $(param_id.zjhm).attr('placeholder', zjhmText);
            }
        });

        $('#captcha').click(function (){
            changeCaptcha();
        });

        function changeCaptcha(){
            $('#captcha').attr('src',web_path + '/web/achievement/software/query/captcha?v=' + Math.random());
        }

        init();

        function init() {
            initExamDate();
        }

        function initExamDate() {
            $.get(ajax_url.exam_date, function (data) {
                $.each(data.listResult, function (i, v) {
                    $(param_id.stage).append($('<option>').val(v).text(v));
                });
                changeCaptcha();
            });
        }

        $(button_id.query.id).click(function () {
            initParam();
            validStage();
        });

        $('#back').click(function (){
            $('#RESULT_KSSJ').text('');
            $('#RESULT_ZGMC').text('');
            $('#RESULT_ZKZH').text('');
            $('#RESULT_ZJH').text('');
            $('#RESULT_XM').text('');
            $('#RESULT_SWCJ').text('');
            $('#RESULT_XWCJ').text('');
            $('#result').css('display','none');
            $('#queryForm').css('display','');
        });

        function validStage() {
            var stage = param.stage;
            if (stage !== '') {
                tools.validSuccessDom(param_id.stage);
                validZjhm();
            } else {
                tools.validErrorDom(param_id.stage, '请选择考试时间');
            }
        }

        function validZjhm() {
            var zjhm = param.zjhm;
            if (zjhm !== '') {
                tools.validSuccessDom(param_id.zjhm);
                validXm();
            } else {
                tools.validErrorDom(param_id.zjhm, '请填写' + zjhmText);
            }
        }

        function validXm() {
            var xm = param.xm;
            if (xm !== '') {
                tools.validSuccessDom(param_id.xm);
                validJym();
            } else {
                tools.validErrorDom(param_id.xm, '请填写姓名');
            }
        }

        function validJym() {
            var jym = param.jym;
            if (jym !== '') {
                tools.validSuccessDom(param_id.jym);
                sendAjax();
            } else {
                tools.validErrorDom(param_id.jym, '请填写检验码');
            }
        }

        function sendAjax() {
            $('#queryError').text('');
            tools.buttonLoading(button_id.query.id, button_id.query.tip);
            $.post(ajax_url.query, {
                captcha: param.jym,
                stage: param.stage,
                xm: param.xm,
                zjhm: param.zjhm,
                selectType: param.selectType
            }, function (data) {
                // 去除遮罩
                tools.buttonEndLoading(button_id.query.id, button_id.query.text);
                if (data.state) {
                    $('#RESULT_KSSJ').text(data.mapResult.data.KSSJ);
                    $('#RESULT_ZGMC').text(data.mapResult.data.ZGMC);
                    $('#RESULT_ZKZH').text(data.mapResult.data.ZKZH);
                    $('#RESULT_ZJH').text(data.mapResult.data.ZJH);
                    $('#RESULT_XM').text(data.mapResult.data.XM);
                    $('#RESULT_SWCJ').text(data.mapResult.data.SWCJ);
                    $('#RESULT_XWCJ').text(data.mapResult.data.XWCJ);
                    $('#result').css('display','');
                    $('#queryForm').css('display','none');
                } else {
                    $('#queryError').text(data.msg);
                }
            });
        }

    });