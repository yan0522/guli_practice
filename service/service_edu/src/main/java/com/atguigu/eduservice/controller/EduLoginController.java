package com.atguigu.eduservice.controller;

import com.atguigu.commonutils.R;
import org.springframework.web.bind.annotation.*;

/**
 * TODO
 *
 * @ClassName: EduLoginController
 * @author: yan
 * @since: 2021/4/3 15:16
 */
@RestController
@RequestMapping("/eduservice/user")
public class EduLoginController {

    //临时模拟的登录
    //TODO
    @PostMapping("login")
    public R login() {
        return R.ok().data("token","admin");
    }

    //临时info
    //TODO
    @GetMapping("info")
    public R info() {
        return R.ok().data("roles","[admin]")
                .data("name","admin")
                .data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }
}
