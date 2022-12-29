package cn.master.backend.service;

import cn.master.backend.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2022-12-27
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * desc: 添加用户信息
     *
     * @param httpServletRequest HttpServletRequest
     * @param sysUser            参数
     * @return cn.master.backend.entity.SysUser
     */
    SysUser addUser(HttpServletRequest httpServletRequest, SysUser sysUser);
}
