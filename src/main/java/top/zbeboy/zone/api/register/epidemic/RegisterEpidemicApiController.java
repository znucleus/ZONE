package top.zbeboy.zone.api.register.epidemic;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.register.RegisterEpidemicService;
import top.zbeboy.zbase.tools.web.util.AjaxUtil;
import top.zbeboy.zone.web.util.SessionUtil;
import top.zbeboy.zbase.vo.register.epidemic.EpidemicRegisterDataAddVo;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@RestController
public class RegisterEpidemicApiController {

    @Resource
    private RegisterEpidemicService registerEpidemicService;

    /**
     * 登记
     *
     * @param epidemicRegisterDataAddVo 数据
     * @param bindingResult             检验
     * @return true or false
     */
    @PostMapping("/api/register/epidemic/data/save")
    public ResponseEntity<Map<String, Object>> dataSave(@Valid EpidemicRegisterDataAddVo epidemicRegisterDataAddVo, BindingResult bindingResult, Principal principal) {
        Users users = SessionUtil.getUserFromOauth(principal);
        epidemicRegisterDataAddVo.setUsername(users.getUsername());
        epidemicRegisterDataAddVo.setChannelName(Workbook.channel.API.name());
        AjaxUtil<Map<String, Object>> ajaxUtil = registerEpidemicService.dataSave(epidemicRegisterDataAddVo);
        return new ResponseEntity<>(ajaxUtil.send(), HttpStatus.OK);
    }
}
