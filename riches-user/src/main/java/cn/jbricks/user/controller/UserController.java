package cn.jbricks.user.controller;

import cn.jbricks.mvc.model.Result;
import cn.jbricks.user.model.User;
import cn.jbricks.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: haoting.wang
 * @Date: Created in 下午3:22 2018/2/26
 */
@RestController
@RequestMapping("user")
public class UserController {


    @Resource
    private UserService userService;

    @RequestMapping("/getUserById")
    public Result<User> getUserById(String id){

        User user = userService.getUserById(id);
        return Result.valueOfSuccess(user);
    }
}
