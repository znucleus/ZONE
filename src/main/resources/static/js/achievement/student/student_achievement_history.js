//# sourceURL=student_achievement_history.js
require(["jquery", "tools", "handlebars", "nav.active", "messenger"],
    function ($, tools, Handlebars, navActive) {

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                semester: web_path + '/web/achievement/student/query/history/new/semester',
                data: web_path + '/web/achievement/student/query/history/new/data',
                page: '/web/menu/achievement/student/query'
            };
        }

        navActive(getAjaxUrl().page);

        var page_param = {
            studentNumber: $('#paramStudentNumber').val()
        }

        init();

        function init() {
            initSemester();
        }

        var semesterList = [];
        var semesterId = "";

        function initSemester() {
            $.get(getAjaxUrl().semester, page_param, function (data) {
                if (data.state) {
                    semesterList = data.listResult;
                    semesterData(data);
                    initAchievement();
                } else {
                    Messenger().post({
                        message: data.msg,
                        type: 'error',
                        showCloseButton: true
                    });
                }
            });
        }

        var semesterTableElement = $('#semesterData');

        function semesterData(data) {
            var template = Handlebars.compile($("#semester_template").html());
            semesterTableElement.append(template(data));
        }

        semesterTableElement.delegate('.semester', "click", function () {
            $('.semester').removeClass('active');
            $(this).addClass('active');
            semesterId = $(this).attr('data-id');
            if (semesterId !== '') {
                $('#semesterInfo').css('display', '');
                $.each(semesterList, function (i, v) {
                    if (v.semesterId === semesterId) {
                        $('#departmentName').text(v.departmentName);
                        $('#organizeName').text(v.organizeName);
                        $('#studentNumber').text(v.studentNumber);
                        $('#realName').text(v.realName);
                    }
                });
            } else {
                $('#semesterInfo').css('display', 'none');
            }
            initAchievement();
        });

        var achievementList = [];

        function initAchievement() {
            var param = {
                orderColumnName: 'semester',
                orderDir: 'desc',
                extraSearch: JSON.stringify({
                    semesterId: semesterId,
                    studentNumber: page_param.studentNumber,
                })
            };
            tools.dataLocalLoading('#dataTable');
            $.get(getAjaxUrl().data + '/', param, function (data) {
                tools.dataLocalEndLoading('#dataTable');
                if (data.state) {
                    achievementList = data.listResult;
                    search();
                } else {
                    Messenger().post({
                        message: data.msg,
                        type: 'error',
                        showCloseButton: true
                    });
                }
            });
        }

        var tableElement = $('#dataTable');

        /**
         * 列表数据
         * @param data 数据
         */
        function listData(data) {
            var template = Handlebars.compile($("#data_template").html());
            Handlebars.registerHelper("addOne", function (index) {
                //返回+1之后的结果
                return index + 1;
            });
            tableElement.html(template(data));
        }

        $('#refresh').click(function () {
            initAchievement();
        });

        $("#search").keyup(function () {
            search();
        });

        function search() {
            var v = $('#search').val();
            var filterData = {listResult: []};
            if (v !== '') {
                $.each(achievementList, function (i, n) {
                    if (n.courseName.indexOf(v) > -1) {
                        filterData.listResult.push(n);
                    }
                });
            } else {
                filterData.listResult = achievementList;
            }
            listData(filterData);
        }
    });