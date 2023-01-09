package cn.master.backend.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author create by 11's papa on 2023/1/8-12:50
 */
@Setter
@Getter
public class BaseQueryRequest {
    private String projectId;
    private String name;
    private String workspaceId;
    private List<String> ids;
    private List<String> moduleIds;
    private List<String> nodeIds;
    /**
     * 排序条件
     */
    private List<OrderRequest> orders;
}
