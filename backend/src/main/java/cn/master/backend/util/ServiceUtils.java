package cn.master.backend.util;

import cn.master.backend.entity.SysUser;
import cn.master.backend.mapper.SysUserMapper;
import cn.master.backend.request.OrderRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.BiFunction;

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

    public static Long getNextOrder(String groupId, BiFunction<String, Long, Long> getLastOrderFunc) {
        Long lastOrder = getLastOrderFunc.apply(groupId, null);
        return (lastOrder == null ? 0 : lastOrder) + ServiceUtils.ORDER_STEP;
    }

    public static List<OrderRequest> getDefaultOrder(List<OrderRequest> orders) {
        return getDefaultOrder(null, orders);
    }

    public static List<OrderRequest> getDefaultOrder(String prefix, List<OrderRequest> orders) {
        return getDefaultOrderByField(prefix, orders, "update_time");
    }

    private static List<OrderRequest> getDefaultOrderByField(String prefix, List<OrderRequest> orders, String field) {
        if (Objects.isNull(orders)) {
            OrderRequest orderRequest = new OrderRequest();
            orderRequest.setName(field);
            orderRequest.setType("desc");
            if (StringUtils.isNotBlank(prefix)) {
                orderRequest.setPrefix(prefix);
            }
            orders = new ArrayList<>();
            orders.add(orderRequest);
            return orders;
        }
        return orders;
    }
}
