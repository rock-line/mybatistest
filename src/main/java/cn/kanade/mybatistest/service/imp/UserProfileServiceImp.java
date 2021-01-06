package cn.kanade.mybatistest.service.imp;

import cn.kanade.mybatistest.mapper.UserProfileMapper;
import cn.kanade.mybatistest.model.UserProfile;
import cn.kanade.mybatistest.service.BaseQueryService;
import cn.kanade.mybatistest.service.UserProfileService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserProfileServiceImp extends BaseQueryService implements UserProfileService {

    private final UserProfileMapper userProfileMapper;


    @Override
    public UserProfile findById(Integer id) {
        return userProfileMapper.selectById(id);
    }

    @Override
    public Page<UserProfile> query(String query, Page<UserProfile> page) {
        QueryWrapper<UserProfile> queryWrapper=createQueryWrapper(query,UserProfile.class);
        return userProfileMapper.selectPage(page,queryWrapper);
    }
}
