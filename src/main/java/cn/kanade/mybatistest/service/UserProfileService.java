package cn.kanade.mybatistest.service;

import cn.kanade.mybatistest.model.UserProfile;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface UserProfileService {

    UserProfile findById(Integer id);

    Page<UserProfile> query(String query,Page<UserProfile> page);

}
