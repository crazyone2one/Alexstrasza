package cn.master.backend.service.impl;

import cn.master.backend.constants.UserGroupConstants;
import cn.master.backend.entity.SysProject;
import cn.master.backend.entity.SysUserGroup;
import cn.master.backend.mapper.SysProjectMapper;
import cn.master.backend.mapper.SysUserGroupMapper;
import cn.master.backend.mapper.SysUserMapper;
import cn.master.backend.request.AddProjectRequest;
import cn.master.backend.request.ProjectRequest;
import cn.master.backend.security.JwtUtils;
import cn.master.backend.service.SysProjectService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-04
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysProjectServiceImpl extends ServiceImpl<SysProjectMapper, SysProject> implements SysProjectService {
    final SysUserMapper sysUserMapper;
    final SysUserGroupMapper sysUserGroupMapper;

    @Override
    public IPage<SysProject> getProjectList(ProjectRequest request, Page<SysProject> producePage) {
        LambdaQueryWrapper<SysProject> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(request.getName()), SysProject::getName, request.getName());
        wrapper.eq(StringUtils.isNotBlank(request.getWorkspaceId()), SysProject::getWorkspaceId, request.getWorkspaceId());
        wrapper.eq(StringUtils.isNoneBlank(request.getProjectId()), SysProject::getId, request.getProjectId());
        wrapper.orderByDesc(SysProject::getUpdateTime);
        IPage<SysProject> iPage = baseMapper.selectPageVo(producePage, wrapper);
        for (SysProject record : iPage.getRecords()) {
            record.setMemberSize(sysUserMapper.getProjectMemberList(record.getId()).size());
        }
        return iPage;
    }

    @Override
    public SysProject addProject(HttpServletRequest httpServletRequest, AddProjectRequest project) {
        if (StringUtils.isBlank(project.getName())) {
            throw new IllegalArgumentException("project_name_is_null");
        }
        LambdaQueryWrapper<SysProject> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysProject::getWorkspaceId, project.getWorkspaceId());
        wrapper.eq(SysProject::getName, project.getName());
        if (baseMapper.exists(wrapper)) {
            throw new IllegalArgumentException("project_name_already_exists");
        }
        String systemId = genSystemId();
        project.setSystemId(systemId);
        baseMapper.insert(project);

        SysUserGroup userGroup = SysUserGroup.builder().userId(project.getCreateUser()).groupId(UserGroupConstants.PROJECT_ADMIN)
                .sourceId(project.getId()).build();
        sysUserGroupMapper.insert(userGroup);
        //  创建新项目检查当前用户 last_project_id
        sysUserMapper.updateLastProjectIdIfNull(project.getId(), project.getCreateUser());
        return project;
    }

    @Override
    public SysProject updateProject(AddProjectRequest project) {
        checkProjectExist(project);
        baseMapper.updateById(project);
        return project;
    }

    @Override
    public String deleteProject(String projectId) {
        // User Group
        deleteProjectUserGroup(projectId);
        baseMapper.deleteById(projectId);
        return null;
    }

    @Override
    public List<SysProject> getByCaseTemplateId(String templateId) {
        LambdaQueryWrapper<SysProject> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysProject::getCaseTemplateId, templateId);
        return baseMapper.selectList(wrapper);
    }

    private void deleteProjectUserGroup(String projectId) {
        LambdaQueryWrapper<SysUserGroup> wrapper = new LambdaQueryWrapper<SysUserGroup>().eq(SysUserGroup::getSourceId, projectId);
        sysUserGroupMapper.delete(wrapper);
    }

    private void checkProjectExist(AddProjectRequest project) {
        LambdaQueryWrapper<SysProject> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysProject::getId, project.getId());
        wrapper.eq(SysProject::getName, project.getName());
        // TODO: 2023/1/4 workspace条件
        if (baseMapper.exists(wrapper)) {
            throw new IllegalArgumentException("project_name_already_exists");
        }
    }

    private String genSystemId() {
        String maxSystemIdInDb = baseMapper.getMaxSystemId();
        String systemId = "10001";
        if (StringUtils.isNotEmpty(maxSystemIdInDb)) {
            systemId = String.valueOf(Long.parseLong(maxSystemIdInDb) + 1);
        }
        return systemId;
    }

}
