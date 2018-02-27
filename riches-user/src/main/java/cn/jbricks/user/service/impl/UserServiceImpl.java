package cn.jbricks.user.service.impl;

import cn.jbricks.user.mapper.UserMapper;
import cn.jbricks.user.model.User;
import cn.jbricks.user.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: haoting.wang
 * @Date: Created in 下午3:51 2018/2/26
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    public User getUserById(String id) {

        return userMapper.getUserById(id);

    }
}
