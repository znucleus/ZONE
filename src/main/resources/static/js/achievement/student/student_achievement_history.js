//# sourceURL=student_achievement_history.js
require(["jquery", "lodash", "tools", "handlebars", "sweetalert2", "nav.active", "tablesaw", "messenger"],
    function ($, _, tools, Handlebars, Swal, navActive) {

        /*
         ajax url
         */
        function getAjaxUrl() {
            return {
                data: web_path + '/web/achievement/student/query/history/data',
                page: '/web/menu/achievement/student/query'
            };
        }

        navActive(getAjaxUrl().page);

        var page_param = {
            studentNumber: $('#studentNumber').val()
        }

        init();

        function init() {
            initData();
        }

        var achievementData = [];
        function initData() {
            $.get(getAjaxUrl().data, page_param, function (data) {
                Messenger().post({
                    message: data.msg,
                    type: data.state ? 'success' : 'error',
                    showCloseButton: true
                });
                if (data.state) {
                    achievementData = data.listResult;
                    search();
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
            $('#dataTable > tbody').html(template(data));
            $('#totalSize').text(data.listResult.length);
            tableElement.tablesaw().data("tablesaw").refresh();
        }

        $('#refresh').click(function () {
            init();
        });

        $("#search").keyup(function(){
            search();
        });

        function search(){
            var v = $('#search').val();
            var filterData = {listResult:[]};
            if(v !== ''){
                $.each(achievementData,function (i, n){
                    if(n.courseName.indexOf(v) > -1 ||
                        n.achievement.indexOf(v) > -1 ||
                        n.schoolYear.indexOf(v) > -1 ||
                        n.semester.indexOf(v) > -1 ||
                        n.organizeName.indexOf(v) > -1 ||
                        n.courseCode.indexOf(v) > -1 ||
                        n.courseType.indexOf(v) > -1 ||
                        n.totalHours.indexOf(v) > -1 ||
                        n.courseNature.indexOf(v) > -1 ||
                        n.assessmentMethod.indexOf(v) > -1 ||
                        n.registrationMethod.indexOf(v) > -1 ||
                        n.creditsDue.indexOf(v) > -1 ||
                        n.creditsObtained.indexOf(v) > -1 ||
                        n.examType.indexOf(v) > -1 ||
                        n.turn.indexOf(v) > -1 ||
                        n.examDate.indexOf(v) > -1 ||
                        n.remark.indexOf(v) > -1 ||
                        n.createDateStr.indexOf(v) > -1){
                        filterData.listResult.push(n);
                    }
                });
            } else {
                filterData.listResult = achievementData;
            }

            listData(filterData);
        }
    });