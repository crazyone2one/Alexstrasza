package cn.master.backend.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author 11's papa
 * @since 2022-12-31
 */
@Getter
@Setter
@TableName("sys_workspace")
public class SysWorkspace implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Workspace ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;


    /**
     * Workspace name
     */
    @TableField("name")
    private String name;

    /**
     * Workspace description
     */
    @TableField("description")
    private String description;

    /**
     * Create timestamp
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * Update timestamp
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField("create_user")
    private String createUser;
}
