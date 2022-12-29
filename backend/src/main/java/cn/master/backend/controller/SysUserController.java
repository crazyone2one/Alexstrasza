package cn.master.backend.controller;

import cn.master.backend.config.ResponseInfo;
import cn.master.backend.entity.SysUser;
import cn.master.backend.request.AuthenticateRequest;
import cn.master.backend.security.JwtUtils;
import cn.master.backend.security.SecurityUser;
import cn.master.backend.service.SysUserService;
import cn.master.backend.service.impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * 前端控制器
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
    final AuthenticationManager authenticationManager;
    final UserDetailsServiceImpl userDetailsService;
    final JwtUtils jwtUtils;


    @PostMapping("/register")
    public ResponseInfo<SysUser> registerUser(HttpServletRequest httpServletRequest, @RequestBody SysUser sysUser) {
        SysUser user = sysUserService.addUser(httpServletRequest, sysUser);
        return ResponseInfo.success(user);
    }

    @PostMapping("/login")
    public ResponseInfo<SecurityUser> loginUser(@RequestBody AuthenticateRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getName(), request.getPassword()));
        SecurityUser userDetails = userDetailsService.loadUserByUsername(request.getName());
        userDetails.setToken(jwtUtils.generateToken(userDetails));
        return ResponseInfo.success(userDetails);
    }
}
