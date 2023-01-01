package cn.master.backend.service;

import cn.master.backend.entity.SysWorkspace;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 11's papa
 * @since 2022-12-31
 */
public interface SysWorkspaceService extends IService<SysWorkspace> {

    /**
     * desc: 查询列表数据
     *
     * @param workspace   查询条件
     * @param producePage 分页条件
     * @return com.baomidou.mybatisplus.core.metadata.IPage<cn.master.backend.entity.SysWorkspace>
     */
    IPage<SysWorkspace> getAllWorkspaceList(SysWorkspace workspace, Page<SysWorkspace> producePage);

    /**
     * desc: 添加workspace数据
     *
     * @param workspace          参数
     * @param httpServletRequest HttpServletRequest
     * @return cn.master.backend.entity.SysWorkspace
     */
    SysWorkspace addWorkspaceByAdmin(SysWorkspace workspace, HttpServletRequest httpServletRequest);

    SysWorkspace updateWorkspaceByAdmin(SysWorkspace workspace);

    String deleteWorkspaceById(String workspaceId);
}
