/**
 * Created by zbeboy on 2019-05-15.
 */
define(["jquery"], function ($) {

        return {
            validSuccessDom: function (validId) {
                $(validId).removeClass('is-invalid');
                $(validId).next().text('');
            },
            validErrorDom: function (validId, msg) {
                $(validId).addClass('is-invalid');
                $(validId).next().text(msg);
            },
            validSelect2SuccessDom: function (validId) {
                $(validId).next().children().first().children('.select2-selection').css('border-color', '');
                $(validId).next().next().hide().text('');
            },
            validSelect2ErrorDom: function (validId, msg) {
                $(validId).next().children().first().children('.select2-selection').css('border-color', '#dc3545');
                $(validId).next().next().show().text(msg);
            },
            validCustomerSuccessDom: function (errorId) {
                $(errorId).text("");
                $(errorId).prev().children(':input').css('border-color', '');
            },
            validCustomerErrorDom: function (errorId, msg) {
                $(errorId).css({'font-size': '80%', 'color': '#dc3545'}).text(msg);
                $(errorId).prev().children(':input').css('border-color', '#dc3545');
            },
            validCustomerSingleSuccessDom: function (validId) {
                $(validId).parents('.form-group').children('.invalid-feedback').hide().text('');
            },
            validCustomerSingleErrorDom: function (validId, msg) {
                $(validId).parents('.form-group').children('.invalid-feedback').show().text(msg);
            },
            buttonLoading: function (btnId, msg) {
                $(btnId).prop('disabled', true).text(msg);
            },
            buttonEndLoading: function (btnId, msg) {
                $(btnId).prop('disabled', false).text(msg);
            },
            dataLoading: function () {
                // 显示遮罩
                $('#page-wrapper').showLoading();
            },
            dataEndLoading: function () {
                // 去除遮罩
                $('#page-wrapper').hideLoading();
            },
            dataLocalLoading: function (id) {
                // 显示遮罩
                $(id).showLoading();
            },
            dataLocalEndLoading: function (id) {
                // 去除遮罩
                $(id).hideLoading();
            },
            toSize: function (size) {
                var str = "";
                if (size < 1024) {
                    str = size + "B";
                } else if (size >= 1024 && size < 1024 * 1024) {
                    str = (size / 1024).toFixed(2) + "KB";
                } else if (size >= 1024 * 1024 && size < 1024 * 1024 * 1024) {
                    str = (size / (1024 * 1024)).toFixed(2) + "MB";
                } else {
                    str = (size / (1024 * 1024 * 1024)).toFixed(2) + "GB";
                }

                return str;
            },
            regex: {
                username: /^[a-zA-Z0-9]{1,20}$/,
                email: /^\w+((-\w+)|(\.\w+))*@[A-Za-z0-9]+(([.\-])[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/,
                studentNumber: /^[0-9]{13}$/,
                staffNumber: /^[0-9]+$/,
                mobile: /^1[0-9]{10}$/,
                password: /^[a-zA-Z0-9]\w{5,17}$/,
                idCard: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/,
                dormitoryNumber: /^\d{2}-\d{3}$/,
                year: /^\d{4}$/,
                multiNumber: /^\d+$/
            },
            moment: {
                releaseTime: 'YYYY.MM.DD HH:mm:ss',
                defaultTime:'YYYY-MM-DD HH:mm:ss'
            },
            weekDay: function(day){
                var v = '';
                switch (day) {
                    case 1:
                        v = '星期一';
                        break;
                    case 2:
                        v = '星期二';
                        break;
                    case 3:
                        v = '星期三';
                        break;
                    case 4:
                        v = '星期四';
                        break;
                    case 5:
                        v = '星期五';
                        break;
                    case 6:
                        v = '星期六';
                        break;
                    case 7:
                        v = '星期天';
                        break;
                }
                return v;
            }
        };
    }
);