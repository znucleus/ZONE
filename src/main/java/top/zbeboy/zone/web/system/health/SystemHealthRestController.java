package top.zbeboy.zone.web.system.health;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SystemHealthRestController {

    /**
     * 健康检查
     *
     * @return 健康检查
     */
    @GetMapping(value = "/anyone/health")
    public ResponseEntity<String> health() {
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }
}
