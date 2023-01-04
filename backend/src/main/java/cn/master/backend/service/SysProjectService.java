package cn.master.backend.service;

import cn.master.backend.entity.SysProject;
import cn.master.backend.request.AddProjectRequest;
import cn.master.backend.request.ProjectRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-04
 */
public interface SysProjectService extends IService<SysProject> {

    /**
     * desc: 分页查询
     *
     * @param request     查询条件
     * @param producePage 分页条件
     * @return com.baomidou.mybatisplus.core.metadata.IPage<cn.master.backend.entity.SysProject>
     */
    IPage<SysProject> getProjectList(ProjectRequest request, Page<SysProject> producePage);

    /**
     * desc: 添加
     *
     * @param httpServletRequest httpServletRequest
     * @param project            参数
     * @return cn.master.backend.entity.SysProject
     */
    SysProject addProject(HttpServletRequest httpServletRequest, AddProjectRequest project);

    SysProject updateProject(AddProjectRequest project);
}
