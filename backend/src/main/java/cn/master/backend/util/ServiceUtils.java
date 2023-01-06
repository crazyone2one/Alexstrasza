package cn.master.backend.util;

import cn.master.backend.entity.SysUser;
import cn.master.backend.mapper.SysUserMapper;
import org.springframework.util.CollectionUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author create by 11's papa on 2023/1/5-13:29
 */
public class ServiceUtils {
    public static final int ORDER_STEP = 5000;

    public static Map<String, SysUser> getUserMap(List<String> userIds) {
        SysUserMapper sysUserMapper = SpringContextUtils.getBean(SysUserMapper.class);
        if (CollectionUtils.isEmpty(userIds)) {
            return new LinkedHashMap<>();
        }
        assert sysUserMapper != null;
        return sysUserMapper.queryNameByIds(userIds);
    }

    public static Map<String, String> getUserNameMap(List<String> userIds) {
        Map<String, SysUser> userMap = getUserMap(userIds);
        LinkedHashMap<String, String> nameMap = new LinkedHashMap<>();
        userMap.forEach((k, v) -> {
            nameMap.put(k, v.getName());
        });
        return nameMap;
    }
}
