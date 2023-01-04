package cn.master.backend.mapper;

import cn.master.backend.entity.SysProject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.type.JdbcType;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-04
 */
@Mapper
public interface SysProjectMapper extends BaseMapper<SysProject> {
    /**
     * desc: 分页查询数据
     *
     * @param page    分页条件
     * @param wrapper 查询条件
     * @return com.baomidou.mybatisplus.core.metadata.IPage<cn.master.backend.entity.SysProject>
     */
    @Select("select * from sys_project ${ew.customSqlSegment}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "workspace_id", property = "workspaceId"),
            @Result(column = "workspace_id", property = "workspaceName", javaType = String.class,
                    one = @One(select = "cn.master.backend.mapper.SysWorkspaceMapper.getNameById")),
            @Result(column = "create_user", property = "createUser"),
            @Result(column = "create_user", property = "createUserName", javaType = String.class,
                    one = @One(select = "cn.master.backend.mapper.SysUserMapper.getNameById", fetchType = FetchType.EAGER)),
    })
    IPage<SysProject> selectPageVo(IPage<?> page, @Param(Constants.WRAPPER) LambdaQueryWrapper<SysProject> wrapper);

    @Select(" SELECT MAX(system_id) from sys_project")
    String getMaxSystemId();
}
