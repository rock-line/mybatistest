package cn.kanade.mybatistest.controller;

import cn.hutool.json.JSONUtil;
import cn.kanade.mybatistest.service.UserProfileService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping(value = "/{id}")
    public String findById(@PathVariable Integer id) {
        return JSONUtil.toJsonStr(userProfileService.findById(id));
    }

    @GetMapping(value = "/query")
    public String findByAll(String query,int page,int size) {
        return JSONUtil.toJsonStr(userProfileService.query(query,new Page<>(page,size)));
    }
}
