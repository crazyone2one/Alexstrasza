package cn.master.backend.controller;

import cn.master.backend.config.ResponseInfo;
import cn.master.backend.entity.SysProject;
import cn.master.backend.entity.SysUser;
import cn.master.backend.entity.UserDTO;
import cn.master.backend.entity.UserGroupPermissionDTO;
import cn.master.backend.request.*;
import cn.master.backend.security.JwtUtils;
import cn.master.backend.security.SecurityUser;
import cn.master.backend.service.BaseCheckPermissionService;
import cn.master.backend.service.SysUserService;
import cn.master.backend.service.impl.UserDetailsServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    final BaseCheckPermissionService baseCheckPermissionService;


    @PostMapping("/register")
    public ResponseInfo<UserDTO> registerUser(HttpServletRequest httpServletRequest, @RequestBody UserRequest sysUser) {
        UserDTO user = sysUserService.addUser(httpServletRequest, sysUser);
        return ResponseInfo.success(user);
    }

    @PostMapping("/special/add")
    public ResponseInfo<UserDTO> insertUser(HttpServletRequest httpServletRequest, @RequestBody UserRequest sysUser) {
        UserDTO user = sysUserService.addUser(httpServletRequest, sysUser);
        return ResponseInfo.success(user);
    }

    @PostMapping("/special/update")
    public ResponseInfo<String> updateUser(@RequestBody UserRequest sysUser) {
        return ResponseInfo.success(sysUserService.updateUserRole(sysUser));
    }

    @PostMapping("/login")
    public ResponseInfo<SecurityUser> loginUser(@RequestBody AuthenticateRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getName(), request.getPassword()));
        SecurityUser userDetails = userDetailsService.loadUserByUsername(request.getName());
        userDetails.setToken(jwtUtils.generateToken(userDetails));
        return ResponseInfo.success(userDetails);
    }

    @PostMapping("/special/ws/member/list/all")
    public ResponseInfo<List<SysUser>> getMemberListByAdmin(@RequestBody QueryMemberRequest request) {
        return ResponseInfo.success(sysUserService.getMemberList(request));
    }

    @GetMapping("/switch/source/ws/{sourceId}")
    public ResponseInfo<SysUser> switchWorkspace(HttpServletRequest httpServletRequest, @PathVariable String sourceId) {
        SysUser userDTO = sysUserService.switchUserResource("workspace", sourceId, httpServletRequest);
        return ResponseInfo.success(userDTO);
    }

    @PostMapping("/ws/project/member/list/{workspaceId}")
    public ResponseInfo<List<SysUser>> getMemberListByProject(@RequestBody QueryMemberRequest request, @PathVariable String workspaceId) {
        baseCheckPermissionService.checkProjectBelongToWorkspace(request.getProjectId(), workspaceId);
        return ResponseInfo.success(sysUserService.getProjectMemberList(request));
    }

    @PostMapping("/update/current")
    public ResponseInfo<UserDTO> updateCurrentUser(@RequestBody SysUser user) {
        UserDTO userDTO = sysUserService.updateCurrentUser(user);
        return ResponseInfo.success(userDTO);
    }

    @GetMapping("/current/{userId}")
    public ResponseInfo<UserDTO> getCurrentUser(@PathVariable String userId) {
        return ResponseInfo.success(sysUserService.getCurrentUser(userId));
    }

    @PostMapping("/ws/member/list/{page}/{limit}")
    public ResponseInfo<Map<String, Object>> getProjectList(@RequestBody QueryMemberRequest request, @PathVariable long page, @PathVariable long limit) {
        Page<SysUser> producePage = new Page<>(page, limit);
        IPage<SysUser> pageInfo = sysUserService.getMemberList(request, producePage);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", pageInfo.getTotal());
        result.put("records", pageInfo.getRecords());
        return ResponseInfo.success(result);
    }

    @PostMapping("special/list/{page}/{limit}")
    public ResponseInfo<Map<String, Object>> getUserList(@RequestBody UserRequest request, @PathVariable long page, @PathVariable long limit) {
        Page<SysUser> producePage = new Page<>(page, limit);
        IPage<SysUser> pageInfo = sysUserService.getUserListWithRequest(request, producePage);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", pageInfo.getTotal());
        result.put("records", pageInfo.getRecords());
        return ResponseInfo.success(result);
    }

    @GetMapping("/special/user/group/{userId}")
    public ResponseInfo<UserGroupPermissionDTO> getUserGroup(@PathVariable String userId) {
        return ResponseInfo.success(sysUserService.getUserGroup(userId));
    }

    @PostMapping("/special/update_status")
    public ResponseInfo<String> updateUserStatus(@RequestBody SysUser user) {
        sysUserService.updateUser(user);
        return ResponseInfo.success();
    }

    @PostMapping("/special/password")
    public ResponseInfo<Integer> updateUserPassword(@RequestBody EditPassWordRequest request) {
        return ResponseInfo.success(sysUserService.updateUserPassword(request));
    }

    @GetMapping("/project/member/option/{projectId}")
    public ResponseInfo<List<SysUser>> getProjectMemberOption(@PathVariable String projectId) {
        return ResponseInfo.success(sysUserService.getProjectMemberOption(projectId));
    }

    @GetMapping("/list")
    public ResponseInfo<List<SysUser>> getUserList() {
        return ResponseInfo.success(sysUserService.getUserList());
    }

    /**
     * 添加工作空间成员
     */
    @PostMapping("/ws/member/add")
    public ResponseInfo<String> addMember(@RequestBody AddMemberRequest request) {
        return ResponseInfo.success(sysUserService.addWorkspaceMember(request));
    }
}
