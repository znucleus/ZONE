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
            buttonLoading: function (btnId, msg) {
                $(btnId).prop('disabled', true).text(msg);
            },
            buttonEndLoading: function (btnId, msg) {
                $(btnId).prop('disabled', false).text(msg);
            },
            dataLoading: function () {
                // 显示遮罩
                $('#loading-view').showLoading();
            },
            dataEndLoading: function () {
                // 去除遮罩
                $('#loading-view').hideLoading();
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
            }
        };
    }
);