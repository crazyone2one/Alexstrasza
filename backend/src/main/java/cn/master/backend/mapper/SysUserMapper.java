package cn.master.backend.mapper;

import cn.master.backend.entity.SysUser;
import cn.master.backend.request.QueryMemberRequest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2022-12-27
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * desc: search for user by username
     *
     * @param name name of the user
     * @return cn.master.backend.entity.SysUser
     */
    @Select("select * from sys_user where name=#{name}")
    SysUser findByName(String name);

    /**
     * desc: 是否是超级用户
     *
     * @param userId userid
     * @return boolean
     */
    @Select("select count(*) from sys_user_group where user_id = #{userId} and group_id = 'super_group'")
    boolean isSuperUser(String userId);

    /**
     * desc: search name by user id
     *
     * @param userId id of the user
     * @return java.lang.String
     */
    @Select("select name from sys_user where id = #{userId}")
    String getNameById(String userId);

    @Select("SELECT DISTINCT * from (SELECT u.*  FROM sys_user_group ug JOIN `sys_user` u  ON ug.user_id = u.id WHERE ug.source_id = #{projectId} ORDER BY u.update_time DESC) temp")
    List<SysUser> getProjectMemberList(String projectId);

    @Update("UPDATE sys_user SET last_project_id = #{projectId} WHERE id = #{userId}")
    void updateLastProjectIdIfNull(@Param("projectId") String projectId, @Param("userId") String userId);

    IPage<SysUser> selectPageVo(IPage<?> page, @Param("member") QueryMemberRequest request);

    @MapKey("id")
    Map<String, SysUser> queryNameByIds(List<String> userIds);
}
