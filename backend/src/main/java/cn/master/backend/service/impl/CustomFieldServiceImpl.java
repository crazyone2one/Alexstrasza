package cn.master.backend.service.impl;

import cn.master.backend.entity.CustomField;
import cn.master.backend.exception.CustomException;
import cn.master.backend.mapper.CustomFieldMapper;
import cn.master.backend.request.QueryCustomFieldRequest;
import cn.master.backend.service.CustomFieldService;
import cn.master.backend.service.CustomFieldTemplateService;
import cn.master.backend.util.ServiceUtils;
import cn.master.backend.util.SessionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 自定义字段表 服务实现类
 * </p>
 *
 * @author 11's papa
 * @since 2023-01-09
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CustomFieldServiceImpl extends ServiceImpl<CustomFieldMapper, CustomField> implements CustomFieldService {
    final CustomFieldTemplateService customFieldTemplateService;

    @Override
    public String addCustomField(CustomField customField) {
        checkExist(customField);
        customField.setGlobal(false);
        customField.setThirdPart(false);
        customField.setCreateUser(SessionUtils.getUserId());
        baseMapper.insert(customField);
        return customField.getId();
    }

    @Override
    public IPage<CustomField> getPageList(QueryCustomFieldRequest request, Page<CustomField> producePage) {
        request.setOrders(ServiceUtils.getDefaultOrder(request.getOrders()));
        return baseMapper.selectPageVO(producePage, request);
    }

    @Override
    public String deleteCustomField(String id) {
        baseMapper.deleteById(id);
        customFieldTemplateService.deleteByFieldId(id);
        return "delete successfully:" + id;
    }

    @Override
    public List<CustomField> getDefaultField(QueryCustomFieldRequest request) {
        LambdaQueryWrapper<CustomField> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomField::getSystem, true).eq(CustomField::getScene, request.getScene());
        wrapper.eq(CustomField::getProjectId,request.getProjectId());
        List<CustomField> customFields = baseMapper.selectList(wrapper);
        Set<String> workspaceSystemFieldNames = customFields.stream()
                .map(CustomField::getName)
                .collect(Collectors.toSet());
        List<CustomField> globalFields = getGlobalField(request.getScene());
        // 工作空间的系统字段加上全局的字段
        globalFields.forEach(item -> {
            if (!workspaceSystemFieldNames.contains(item.getName())) {
                customFields.add(item);
            }
        });
        return customFields;
    }

    private List<CustomField> getGlobalField(String scene) {
        LambdaQueryWrapper<CustomField> wrapper = new LambdaQueryWrapper<CustomField>()
                .eq(CustomField::getGlobal, true)
                .eq(CustomField::getScene, scene);
        return baseMapper.selectList(wrapper);
    }
    private void checkExist(CustomField customField) {
        if (Objects.nonNull(customField.getName())) {
            LambdaQueryWrapper<CustomField> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(CustomField::getName, customField.getName());
            wrapper.eq(CustomField::getProjectId, customField.getProjectId());
            wrapper.eq(CustomField::getScene, customField.getScene());
            if (StringUtils.isNotBlank(customField.getId())) {
                wrapper.ne(CustomField::getId, customField.getId());
            }
            if (baseMapper.exists(wrapper)) {
                throw new CustomException("工作空间下已存在该字段：" + customField.getName());
            }
        }
    }
}
