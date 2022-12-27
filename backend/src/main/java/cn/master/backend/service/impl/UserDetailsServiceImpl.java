package cn.master.backend.service.impl;

import cn.master.backend.entity.SysUser;
import cn.master.backend.mapper.SysUserMapper;
import cn.master.backend.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author create by 11's papa on 2022/12/27-14:40
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    final SysUserMapper sysUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserMapper.findByName(username);
        if (Objects.isNull(sysUser)) {
            throw new UsernameNotFoundException("用户名错误/不存在");
        }
        return new SecurityUser(sysUser.getName(), sysUser.getPassword(), List.of(new SimpleGrantedAuthority("USER")));
    }
}
