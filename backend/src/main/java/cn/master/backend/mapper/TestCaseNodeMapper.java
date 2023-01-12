package cn.master.backend.mapper;

import cn.master.backend.entity.TestCaseNode;
import cn.master.backend.entity.TestCaseNodeDTO;
import cn.master.backend.request.QueryTestCaseRequest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-11
 */
public interface TestCaseNodeMapper extends BaseMapper<TestCaseNode> {
    List<TestCaseNodeDTO> getCountNodes(@Param("request") QueryTestCaseRequest request);

    @Select("select id, id as `key`, project_id, `name`, `name` as label, parent_id, `level`, create_time, update_time, pos, create_user from test_case_node where test_case_node.project_id = #{projectId} order by pos asc")
    List<TestCaseNodeDTO> getNodeTreeByProjectId(@Param("projectId") String projectId);

}
