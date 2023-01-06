package cn.master.backend.mapper;

import cn.master.backend.entity.RelatedSource;
import cn.master.backend.entity.SysGroup;
import cn.master.backend.entity.SysUserGroup;
import cn.master.backend.entity.UserGroupDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2022-12-31
 */
@Mapper
public interface SysUserGroupMapper extends BaseMapper<SysUserGroup> {
    List<RelatedSource> getRelatedSource(@Param("userId") String userId);
    List<SysGroup> getWorkspaceMemberGroups(@Param("workspaceId") String workspaceId, @Param("userId") String userId);
    List<UserGroupDTO> getUserGroup(@Param("userId") String userId, @Param("projectId") String projectId);
}
