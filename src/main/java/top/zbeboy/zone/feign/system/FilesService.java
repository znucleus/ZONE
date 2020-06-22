package top.zbeboy.zone.feign.system;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import top.zbeboy.zbase.domain.tables.pojos.Files;
import top.zbeboy.zone.hystrix.system.FilesHystrixClientFallbackFactory;

@FeignClient(value = "base-server", fallback = FilesHystrixClientFallbackFactory.class)
public interface FilesService {

    /**
     * 获取文件
     *
     * @param id 主键
     * @return 数据
     */
    @GetMapping("/base/system/files/{id}")
    Files findById(@PathVariable("id") String id);

    /**
     * 保存
     *
     * @param files 数据
     */
    @PostMapping("/base/system/files/save")
    void save(@RequestBody Files files);

    /**
     * 更新
     *
     * @param files 数据
     */
    @PostMapping("/base/system/files/update")
    void update(@RequestBody Files files);

    /**
     * 删除
     *
     * @param files 数据
     */
    @PostMapping("/base/system/files/deletes")
    void delete(@RequestBody Files files);

    /**
     * 删除
     *
     * @param id 主键
     */
    @PostMapping("/base/system/files/delete")
    void deleteById(@RequestParam("id") String id);
}
