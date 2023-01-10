<script setup lang="ts">
import { computed, ref, watchEffect, onMounted } from 'vue'
import { ICustomField, IOption } from '/@/apis/modules/custom-field'
import { NSelect, SelectOption } from 'naive-ui'
import { useI18n } from 'vue-i18n'
import { getProjectMemberOption } from '/@/apis/modules/user'
import { useAppStore } from '/@/store/modules/app'

interface IProps {
  templateData: ICustomField
  prop: string
  isEdit: boolean
  disabled?: boolean
}

const props = withDefaults(defineProps<IProps>(), {
  disabled: false,
})
const { t } = useI18n()
const appStore = useAppStore()
const emit = defineEmits(['update:template'])
const model = computed({
  get: () => {
    const data = props.templateData
    if (data.options) {
      const _options = data.options as IOption[]
      data.options = _options
    }
    return data
  },
  set: (val) => {
    emit('update:template', val)
  },
})
const memberOptions = ref<SelectOption[]>([])
const selectOptions = ref<SelectOption[]>([])

const handleUpdateValue = (value: string) => {
  model.value.defaultValue = value
}
watchEffect(() => {
  if (model.value.options) {
    const _options = model.value.options as IOption[]
    _options.forEach((item) => {
      const _option = { label: '', value: '' }
      _option.label = model.value.system ? t(item.text) : item.text
      _option.value = item.value
      selectOptions.value.push(_option)
    })
  }
})
onMounted(() => {
  if (['select', 'multipleSelect', 'checkbox', 'radio'].indexOf(model.value.type) > -1 && model.value.options) {
    const values = model.value[props.prop]
    if (['multipleSelect', 'checkbox'].indexOf(model.value.type) > -1) {
      if (values && values instanceof Array) {
        for (let i = values.length - 1; i >= 0; i--) {
          const _options = model.value.options as IOption[]
          if (!_options.find((item) => item.value === values[i])) {
            // 删除已删除的选项
            values.splice(i, 1)
          }
        }
      } else {
        model.value[props.prop] = []
      }
    } else {
      const _options = model.value.options as IOption[]
      if (!_options.find((item) => item.value === values)) {
        // 没有选项则清空
        model.value[props.prop] = ''
      }
    }
  }
  if (['member', 'multipleMember'].indexOf(model.value.type) < 0) {
    return
  }
  getProjectMemberOption(appStore.getProjectId()).then((res) => {
    const members = res.data
    members.forEach((member) => {
      const _option = { label: '', value: '' }
      _option.label = `${member.name}(${member.email ? member.email : ''})`
      _option.value = member.id as string
      memberOptions.value.push(_option)
    })

    if (model.value.name === '责任人' && model.value.system && props.isEdit) {
      memberOptions.value.unshift({ value: 'CURRENT_USER', label: '创建人' })
    }
  })
})
</script>
<template>
  <div>
    <n-select
      v-if="model.type === 'select' || model.type === 'multipleSelect'"
      v-model:value="model[prop]"
      :options="selectOptions"
      :disabled="props.disabled"
      :multiple="model.type === 'multipleSelect'"
      filterable
      clearable
      :placeholder="$t('commons.default')"
      @update:value="handleUpdateValue"
    />
    <n-select
      v-else-if="model.type === 'member' || model.type === 'multipleMember'"
      v-model:value="model[prop]"
      :options="memberOptions"
      :disabled="props.disabled"
      :multiple="model.type === 'multipleMember'"
      filterable
      clearable
      :placeholder="$t('commons.default')"
      @update:value="handleUpdateValue"
    />
  </div>
</template>

<style scoped></style>
