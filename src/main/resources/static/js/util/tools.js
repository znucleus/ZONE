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
                $(validId).removeClass('is-invalid').parents('.form-group').children('.invalid-feedback').hide().text('');
            },
            validCustomerSingleErrorDom: function (validId, msg) {
                $(validId).addClass('is-invalid').parents('.form-group').children('.invalid-feedback').show().text(msg);
            },
            buttonLoading: function (btnId, msg) {
                $(btnId).prop('disabled', true).text(msg);
            },
            buttonEndLoading: function (btnId, msg) {
                $(btnId).prop('disabled', false).text(msg);
            },
            dataLoading: function () {
                // 显示遮罩
                $('#page-wrapper').loading({
                    stoppable: true
                });
            },
            dataEndLoading: function () {
                // 去除遮罩
                $('#page-wrapper').loading('stop');
            },
            dataLocalLoading: function (id) {
                // 显示遮罩
                $(id).loading({
                    stoppable: true
                });
            },
            dataLocalEndLoading: function (id) {
                // 去除遮罩
                $(id).loading('stop');
            },
            toSize: function (size) {
                var str;
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
                password: /^(?=.*([a-zA-Z].*))(?=.*[0-9].*)[a-zA-Z0-9-_*/+.~=!@#$%^&()]{6,20}$/,
                idCard: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/,
                dormitoryNumber: /^\d{2}-\d{3}$/,
                year: /^\d{4}$/,
                multiNumber: /^\d+$/,
                zipCode: /^[1-9][0-9]{5}$/
            },
            moment: {
                releaseTime: 'YYYY.MM.DD HH:mm:ss',
                defaultTime: 'YYYY-MM-DD HH:mm:ss'
            },
            weekDay: function (day) {
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
            },
            trim: function (str) {
                return str.replace(/(^\s*)|(\s*$)/g, "");
            },
            addHandler: function (element, type, handler) {
                if (element.addEventListener)
                    element.addEventListener(type, handler, false);
                else if (element.attachEvent)
                    element.attachEvent("on" + type, handler);
                else
                    element["on" + type] = handler;
            },
            removeHandler: function (element, type, handler) {
                if (element.removeEventListener)
                    element.removeEventListener(type, handler, false);
                else if (element.detachEvent)
                    element.detachEvent("on" + type, handler);
                else
                    element["on" + type] = handler;
            },
            /**
             * 监听触摸的方向
             * @param target            要绑定监听的目标元素
             * @param isPreventDefault  是否屏蔽掉触摸滑动的默认行为（例如页面的上下滚动，缩放等）
             * @param upCallback        向上滑动的监听回调（若不关心，可以不传，或传false）
             * @param rightCallback     向右滑动的监听回调（若不关心，可以不传，或传false）
             * @param downCallback      向下滑动的监听回调（若不关心，可以不传，或传false）
             * @param leftCallback      向左滑动的监听回调（若不关心，可以不传，或传false）
             */
            listenTouchDirection: function (target, isPreventDefault, upCallback, rightCallback, downCallback, leftCallback) {
                this.addHandler(target, "touchstart", handleTouchEvent);
                this.addHandler(target, "touchend", handleTouchEvent);
                this.addHandler(target, "touchmove", handleTouchEvent);
                var startX;
                var startY;

                function handleTouchEvent(event) {
                    switch (event.type) {
                        case "touchstart":
                            startX = event.touches[0].pageX;
                            startY = event.touches[0].pageY;
                            break;
                        case "touchend":
                            var spanX = event.changedTouches[0].pageX - startX;
                            var spanY = event.changedTouches[0].pageY - startY;

                            if (Math.abs(spanX) > Math.abs(spanY)) {      //认定为水平方向滑动
                                if (spanX > 30) {         //向右
                                    if (rightCallback)
                                        rightCallback();
                                } else if (spanX < -30) { //向左
                                    if (leftCallback)
                                        leftCallback();
                                }
                            } else {                                    //认定为垂直方向滑动
                                if (spanY > 30) {         //向下
                                    if (downCallback)
                                        downCallback();
                                } else if (spanY < -30) {//向上
                                    if (upCallback)
                                        upCallback();
                                }
                            }

                            break;
                        case "touchmove":
                            //阻止默认行为
                            if (isPreventDefault)
                                event.preventDefault();
                            break;
                    }
                }
            }
        };
    }
);