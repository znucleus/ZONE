package top.zbeboy.zone.service.export;

import org.apache.poi.ss.usermodel.Row;
import top.zbeboy.zbase.bean.campus.roster.RosterDataBean;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.ExportUtil;

import java.util.List;

public class RosterDataExport extends ExportUtil<RosterDataBean> {

    // 序号
    private double sequence = 0;

    /**
     * 初始化数据
     *
     * @param data 数据
     */
    public RosterDataExport(List<RosterDataBean> data) {
        super(data);
    }

    @Override
    public void createHeader(Row row) {
        row.createCell(0).setCellValue("序号");
        row.createCell(1).setCellValue("学号");
        row.createCell(2).setCellValue("姓名");
        row.createCell(3).setCellValue("姓名拼音");
        row.createCell(4).setCellValue("性别");
        row.createCell(5).setCellValue("出生年月");
        row.createCell(6).setCellValue("身份证号");
        row.createCell(7).setCellValue("民族");
        row.createCell(8).setCellValue("政治面貌");
        row.createCell(9).setCellValue("班级名称");
        row.createCell(10).setCellValue("省份");
        row.createCell(11).setCellValue("籍贯");
        row.createCell(12).setCellValue("所属地区");
        row.createCell(13).setCellValue("乘车区间");
        row.createCell(14).setCellValue("家长姓名");
        row.createCell(15).setCellValue("家长联系电话");
        row.createCell(16).setCellValue("家长联系地址");
        row.createCell(17).setCellValue("邮政编码");
        row.createCell(18).setCellValue("本人联系方式");
        row.createCell(19).setCellValue("邮箱");
        row.createCell(20).setCellValue("考生类别");
        row.createCell(21).setCellValue("是否残疾人");
        row.createCell(22).setCellValue("残疾人编号");
        row.createCell(23).setCellValue("是否进行兵役登记");
        row.createCell(24).setCellValue("是否办理生源地贷款");
        row.createCell(25).setCellValue("大学任职情况");
        row.createCell(26).setCellValue("是否为贫困生");
        row.createCell(27).setCellValue("贫困生类型");
        row.createCell(28).setCellValue("是否外宿");
        row.createCell(29).setCellValue("宿舍号");
        row.createCell(30).setCellValue("外宿类型");
        row.createCell(31).setCellValue("外宿详细地址具体到门牌号");
        row.createCell(32).setCellValue("入团时间");
        row.createCell(33).setCellValue("是否注册志愿者");
        row.createCell(34).setCellValue("团籍档案是否完整");
        row.createCell(35).setCellValue("递交入党申请书时间");
        row.createCell(36).setCellValue("列积极分子时间");
        row.createCell(37).setCellValue("列预备党员时间");
        row.createCell(38).setCellValue("转正时间（入党时间）");
    }

    @Override
    public void createCell(Row row, RosterDataBean t) {
        sequence++;
        row.createCell(0).setCellValue(sequence);
        row.createCell(1).setCellValue(t.getStudentNumber());
        row.createCell(2).setCellValue(t.getRealName());
        row.createCell(3).setCellValue(t.getNamePinyin());
        row.createCell(4).setCellValue(t.getSex());
        row.createCell(5).setCellValue(null != t.getBirthday() ? DateTimeUtil.defaultFormatSqlDate(t.getBirthday()) : "");
        row.createCell(6).setCellValue(t.getIdCard());
        row.createCell(7).setCellValue(t.getNationName());
        row.createCell(8).setCellValue(t.getPoliticalLandscapeName());
        row.createCell(9).setCellValue(t.getOrganizeName());
        row.createCell(10).setCellValue(t.getProvince());
        row.createCell(11).setCellValue(t.getNativePlace());
        row.createCell(12).setCellValue(t.getRegion());
        row.createCell(13).setCellValue(t.getBusSection());
        row.createCell(14).setCellValue(t.getParentName());
        row.createCell(15).setCellValue(t.getParentContactPhone());
        row.createCell(16).setCellValue(t.getParentContactAddress());
        row.createCell(17).setCellValue(t.getZipCode());
        row.createCell(18).setCellValue(t.getPhoneNumber());
        row.createCell(19).setCellValue(t.getEmail());
        row.createCell(20).setCellValue(getCandidatesType(t.getCandidatesType()));
        row.createCell(21).setCellValue(convert(t.getIsDeformedMan()));
        row.createCell(22).setCellValue(t.getDeformedManCode());
        row.createCell(23).setCellValue(convert(t.getIsMilitaryServiceRegistration()));
        row.createCell(24).setCellValue(convert(t.getIsProvideLoan()));
        row.createCell(25).setCellValue(t.getUniversityPosition());
        row.createCell(26).setCellValue(convert(t.getIsPoorStudents()));
        row.createCell(27).setCellValue(getPoorStudentsType(t.getPoorStudentsType()));
        row.createCell(28).setCellValue(convert(t.getIsStayOutside()));
        row.createCell(29).setCellValue(t.getDormitoryNumber());
        row.createCell(30).setCellValue(getStayOutsideType(t.getStayOutsideType()));
        row.createCell(31).setCellValue(t.getStayOutsideAddress());
        row.createCell(32).setCellValue(null != t.getLeagueMemberJoinDate() ? DateTimeUtil.defaultFormatSqlDate(t.getLeagueMemberJoinDate()) : "");
        row.createCell(33).setCellValue(convert(t.getIsRegisteredVolunteers()));
        row.createCell(34).setCellValue(convert(t.getIsOkLeagueMembership()));
        row.createCell(35).setCellValue(null != t.getApplyPartyMembershipDate() ? DateTimeUtil.defaultFormatSqlDate(t.getApplyPartyMembershipDate()) : "");
        row.createCell(36).setCellValue(null != t.getBecomeActivistsDate() ? DateTimeUtil.defaultFormatSqlDate(t.getBecomeActivistsDate()) : "");
        row.createCell(37).setCellValue(null != t.getBecomeProbationaryPartyMemberDate() ? DateTimeUtil.defaultFormatSqlDate(t.getBecomeProbationaryPartyMemberDate()) : "");
        row.createCell(38).setCellValue(null != t.getJoiningPartyDate() ? DateTimeUtil.defaultFormatSqlDate(t.getJoiningPartyDate()) : "");
    }

    private String getCandidatesType(Integer candidatesType) {
        String v = "";
        if (null != candidatesType) {
            if (candidatesType == 0) {
                v = "城镇应届";
            } else if (candidatesType == 1) {
                v = "城市往届";
            } else if (candidatesType == 2) {
                v = "农村应届";
            } else if (candidatesType == 3) {
                v = "农村往届";
            }
        }

        return v;
    }

    private String getPoorStudentsType(Integer poorStudentsType) {
        String v = "";
        if (null != poorStudentsType) {
            if (poorStudentsType == 0) {
                v = "一般困难";
            } else if (poorStudentsType == 1) {
                v = "贫困";
            } else if (poorStudentsType == 3) {
                v = "特困";
            } else if (poorStudentsType == 4) {
                v = "建档立卡户";
            }
        }

        return v;
    }

    private String getStayOutsideType(Integer stayOutsideType) {
        String v = "";
        if (null != stayOutsideType) {
            if (stayOutsideType == 0) {
                v = "住自家";
            } else if (stayOutsideType == 1) {
                v = "亲戚家";
            } else if (stayOutsideType == 2) {
                v = "同学家";
            } else if (stayOutsideType == 3) {
                v = "租房";
            }
        }

        return v;
    }

    private String convert(Byte b) {
        String v = "";
        if (null != b) {
            if (b == 0) {
                v = "否";
            } else if (b == 1) {
                v = "是";
            }
        }

        return v;
    }
}
