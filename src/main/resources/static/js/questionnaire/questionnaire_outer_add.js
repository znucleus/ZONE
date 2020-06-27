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
        "bootstrap": web_path + "/plugins/bootstrap/js/bootstrap.bundle.min"
    },
    // shimオプションの設定。モジュール間の依存関係を定義します。
    shim: {
        "tools": {
            deps: ["jquery"]
        },
        "bootstrap": {
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

// require(["module/name", ...], function(params){ ... });
require(["jquery", "requirejs-domready", "lodash", "tools", "bootstrap", "csrf"],
    function ($, domready, _, tools) {
        domready(function () {
            //This function is called once the DOM is ready.
            //It will be safe to query the DOM and manipulate
            //DOM nodes in this function.
            var ajax_url = {
                obtain_questionnaire_subjects: web_path + '/anyone/questionnaire_subjects',
                save: web_path + '/anyone/questionnaire/save',
                success: web_path + '/anyone/questionnaire/save/success'
            };

            var button_id = {
                save: {
                    id: '#save',
                    text: '提 交',
                    tip: '保存中...'
                }
            };

            var page_param = {
                questionnaireReleaseId: $('#questionnaireReleaseId').val()
            };

            init();

            function init() {
                // 拉取数据
                $.get(ajax_url.obtain_questionnaire_subjects + "/" + page_param.questionnaireReleaseId,
                    function (data) {
                        if (data.state) {
                            for (var i = 0; i < data.listResult.length; i++) {
                                var d = data.listResult[i];
                                $('#dataTable').append(generateHtml(d));
                            }
                        }
                    });
            }

            function generateHtml(d) {
                var html = '<div class="form-group subject" data-id="' + d.questionnaireSubjectId + '" data-null="' + d.isNull + '" data-type="' + d.subjectType + '">';
                html += '<label>' + d.sequenceNumber + '. (' + isNull(d) + ')' + d.content + '</label>';
                // 单选
                if (d.subjectType === 0) {
                    html += generateRadio(d);
                } else if (d.subjectType === 1) {
                    // 多选
                    html += generateCheckBox(d);
                } else if (d.subjectType === 2) {
                    // 填空
                    html += generateFill(d);
                }
                html += ' <div class="text-danger" id="' + d.questionnaireSubjectId + '_error"></div>';
                html += '</div>';
                return html;
            }

            function generateRadio(d) {
                var html = '';
                for (var r = 0; r < d.questionnaireOptions.length; r++) {
                    var rd = d.questionnaireOptions[r];
                    // 单纯选项
                    if (rd.optionType === 0) {
                        html += '<label class="rdiobox">';
                        html += '<input type="radio" name="' + d.questionnaireSubjectId + '" value="' + rd.optionKey + '"><span>' + rd.optionContent + '</span>';
                        html += '</label>';
                    } else if (rd.optionType === 1) {
                        // 选填
                        html += '<label class="rdiobox">';
                        html += '<input type="radio" name="' + d.questionnaireSubjectId + '" class="fill" value="' + rd.optionKey + '"><span>' + rd.optionContent + '</span>';
                        html += '<input style=" border:none;border-bottom: 1px solid #000;outline:none;box-shadow: none;"/>';
                        html += '</label>';
                    }
                }
                return html;
            }

            function generateCheckBox(d) {
                var html = '';
                for (var c = 0; c < d.questionnaireOptions.length; c++) {
                    var cd = d.questionnaireOptions[c];
                    // 单纯选项
                    if (cd.optionType === 0) {
                        html += '<label class="ckbox">';
                        html += '<input type="checkbox" name="' + d.questionnaireSubjectId + '" value="' + cd.optionKey + '"><span>' + cd.optionContent + '</span>';
                        html += '</label>';
                    } else if (cd.optionType === 1) {
                        // 选填
                        html += '<label class="ckbox">';
                        html += '<input type="checkbox" name="' + d.questionnaireSubjectId + '" class="fill" value="' + cd.optionKey + '"><span>' + cd.optionContent + '</span>';
                        html += '<input style=" border:none;border-bottom: 1px solid #000;outline:none;box-shadow: none;"/>';
                        html += '</label>';
                    }
                }
                return html;
            }

            function generateFill(d) {
                return '<input class="form-control" name="' + d.questionnaireSubjectId + '">'
            }

            function isNull(d) {
                var text = '选填';
                if (d.isNull === 0) {
                    text = '必填';
                }
                return text;
            }

            $(button_id.save.id).click(function () {
                checkParam();
            });

            function checkParam() {
                var hasError = false;
                // 获取题目选项
                var subjects = $('.subject');
                for (var j = 0; j < subjects.length; j++) {
                    var subject = $(subjects[j]);
                    var id = subject.attr('data-id');
                    var isNull = Number(subject.attr('data-null'));
                    var type = Number(subject.attr('data-type'));
                    // 必填
                    if (isNull === 0) {
                        if (type === 0 || type === 1) {
                            var ids = $('input[name="' + id + '"]:checked');
                            if (ids.length <= 0) {
                                $('#' + id + '_error').text('请选择一个选项');
                                hasError = true;
                            } else {
                                $('#' + id + '_error').text('');
                            }
                        } else if (type === 2) {
                            var content = _.trim($('input[name="' + id + '"]').val());
                            if (content.length <= 0) {
                                $('#' + id + '_error').text('请填写您的回答');
                                hasError = true;
                            } else {
                                $('#' + id + '_error').text('');
                            }
                        }
                    }
                }

                var others = $('input[class="fill"]:checked');
                for (var k = 0; k < others.length; k++) {
                    var otherId = $(others[k]).attr('name');
                    var otherContent = _.trim($(others[k]).next().next().val());
                    if (otherContent.length <= 0) {
                        $(others[k]).next().next().css('border-bottom-color', 'red');
                        $('#' + otherId + '_error').text('请填写您的其它回答');
                        hasError = true;
                    } else {
                        $(others[k]).next().next().css('border-bottom-color', '#000');
                        $('#' + otherId + '_error').text('');
                    }
                }

                if (!hasError) {
                    assembleParam();
                }
            }

            function assembleParam() {
                var params = [];
                // 获取题目选项
                var subjects = $('.subject');
                for (var j = 0; j < subjects.length; j++) {
                    var subject = $(subjects[j]);
                    var id = subject.attr('data-id');
                    var type = Number(subject.attr('data-type'));
                    if (type === 0 || type === 1) {
                        var ids = $('input[name="' + id + '"]:checked');
                        for (var a = 0; a < ids.length; a++) {
                            var ai = $(ids[a]);
                            if (ai.hasClass('fill')) {
                                params.push({
                                    questionnaireSubjectId: id,
                                    choiceKey: ai.val(),
                                    fillContent: _.trim(ai.next().next().val())
                                });
                            } else {
                                params.push({
                                    questionnaireSubjectId: id,
                                    choiceKey: ai.val(),
                                    fillContent: _.trim(ai.next().text())
                                });
                            }
                        }
                    } else if (type === 2) {
                        var content = _.trim($('input[name="' + id + '"]').val());
                        params.push({
                            questionnaireSubjectId: id,
                            choiceKey: '',
                            fillContent: content
                        });
                    }
                }

                sendAjax(params);
            }

            function sendAjax(params) {
                // 显示遮罩
                tools.buttonLoading(button_id.save.id, button_id.save.tip);
                $.post(ajax_url.save, {'data': JSON.stringify(params)}, function (data) {
                    // 去除遮罩
                    tools.buttonEndLoading(button_id.save.id, button_id.save.text);
                    var globalError = $('#globalError');
                    if (data.state) {
                        globalError.text('');
                        window.location.href = ajax_url.success;
                    } else {
                        globalError.text(data.msg);
                    }
                });
            }
        });
    });