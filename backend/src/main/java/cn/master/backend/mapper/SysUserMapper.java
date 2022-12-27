package cn.master.backend.mapper;

import cn.master.backend.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2022-12-27
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    @Select("select * from sys_user where name=#{name}")
    SysUser findByName(String name);
}
