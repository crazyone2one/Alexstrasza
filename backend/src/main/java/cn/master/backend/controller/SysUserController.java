package cn.master.backend.controller;

import cn.master.backend.entity.SysUser;
import cn.master.backend.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 11's papa
 * @since 2022-12-27
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class SysUserController {
    final SysUserService sysUserService;
    @PostMapping("/register")
    public SysUser registerUser(HttpServletRequest httpServletRequest, @RequestBody SysUser sysUser) {
        SysUser user = sysUserService.addUser(httpServletRequest, sysUser);
        return user;
    }
}
