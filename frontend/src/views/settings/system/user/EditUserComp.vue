<script setup lang="ts">
import { ref } from 'vue'
import ModalComponent from '/@/components/ModalComponent.vue'
import { useI18n } from 'vue-i18n'
import { NForm, NFormItem, NInput, FormInst, NSelect, SelectOption, NButton } from 'naive-ui'
import { GROUP_TYPE } from '/@/utils/constants'
import { IGroup, IUserInfo, specialCreateUser, specialModifyUser } from '/@/apis/modules/user'
import { getAllUserGroupByType, getUserAllGroups, ISystemGroup } from '/@/apis/modules/user-group'
import { getGroupResource, IWorkspace } from '/@/apis/modules/workspace'
import { IProject } from '/@/apis/modules/project'
import CryptoJS from 'crypto-js'

const { t } = useI18n()

const createUserForm = ref<FormInst | null>(null)
const type = ref<string>('Add')
const dialogTitle = ref<string>('创建用户')
const modalDialog = ref<InstanceType<typeof ModalComponent> | null>(null)
const model = ref<IUserInfo>({
  name: '',
  username: '',
  userId: '',
  email: '',
  phone: '',
  password: '',
  groups: [
    {
      type: '',
      showSearchGetMore: false,
    },
  ],
  authorities: [],
})
const rules = {
  name: [
    { required: true, trigger: ['blur'], message: t('commons.input_name') },
    { min: 2, max: 50, message: t('commons.input_limit', [2, 50]), trigger: 'blur' },
  ],
  description: { min: 2, max: 90, message: t('commons.input_limit', [2, 90]), trigger: 'blur' },
  scopeId: [{ required: true, message: t('group.select_belong_organization'), trigger: 'blur' }],
  type: [{ required: true, message: t('group.select_type'), trigger: 'blur' }],
}
const emits = defineEmits(['refresh'])
const userGroup = ref<ISystemGroup[]>([])
const limitOptionCount = ref<number>(400)
// * 保存
const handleSubmit = (): void => {
  createUserForm.value?.validate((errors) => {
    if (!errors) {
      console.log(model.value)
      if (type.value === 'Add') {
        // 密码加密
        model.value.password = CryptoJS.MD5(model.value.password as string).toString()
        specialCreateUser(model.value).then(() => {
          window.$message?.success(t('commons.save_success'))
          emits('refresh')
          modalDialog.value?.closeModal()
        })
      } else {
        specialModifyUser(model.value).then(() => {
          window.$message?.success(t('commons.save_success'))
          emits('refresh')
          modalDialog.value?.closeModal()
        })
      }
    }
  })
}
// * 重置表单
const resetModel = (): void => {
  createUserForm.value?.restoreValidation()
  model.value.id = ''
  model.value.name = ''
  model.value.username = ''
  model.value.userId = ''
  model.value.email = ''
  model.value.phone = ''
  model.value.groups = [
    {
      type: '',
      showSearchGetMore: false,
    },
  ]
  model.value.password = ''
}
// * 打开编辑框
const openEditModal = (params?: IUserInfo, typeParam?: string, title?: string) => {
  modalDialog.value?.toggleModal()
  type.value = typeParam || type.value
  dialogTitle.value = title || dialogTitle.value
  resetModel()
  if (type.value === 'Edit') {
    getUserAllGroups(params?.id as string).then((res) => {
      const data = res.data
      model.value.groups = data
      for (const group of model.value.groups) {
        handleWorkspaceOption(group, group.workspaces)
        handleProjectOption(group, group.projects)
      }
    })
    model.value = Object.assign({}, params)
  }
  getAllUserGroup()
}
const getLabel = (index: number): string => {
  const a = index + 1
  return t('commons.group') + a
}
const getAllUserGroup = (): void => {
  getAllUserGroupByType({ type: GROUP_TYPE.SYSTEM }).then((resp) => {
    userGroup.value = resp.data
  })
}
const activeGroup = (group: IGroup) => {
  const temp = userGroup.value.filter((ug) => {
    if (!group.selects) {
      return true
    }
    let sign = true
    for (const groupSelect of group.selects) {
      if (ug.id + '+' + ug.type === groupSelect) {
        sign = false
        break
      }
    }
    return sign
  })
  const options: SelectOption[] = []
  temp.forEach((item) => {
    const option: SelectOption = {}
    option.label = item.name
    option.value = item.id + '+' + item.type
    options.push(option)
  })
  return options
}
const currentGroupWSIds = ref<string[]>([])
const currentWSGroupIndex = ref(-1)
const btnAddGroup = ref(false)

const groupType = (idType: string) => {
  if (!idType) {
    return
  }
  return idType.split('+')[1]
}
//* 选择用户组
const getResource = (idType: string, index: number) => {
  if (!idType) {
    return
  }
  const id = idType.split('+')[0]
  const type = idType.split('+')[1]
  if (index > 0 && model.value.groups[index].ids && model.value.groups[index].ids.lenght > 0) {
    return
  }
  let isHaveWorkspace = false
  if (type === GROUP_TYPE.PROJECT) {
    for (let index = 0; index < model.value.groups.length; index++) {
      const element = model.value.groups[index]
      const _type = element.type.split('+')[1]
      if (_type === GROUP_TYPE.WORKSPACE) {
        isHaveWorkspace = true
        break
      }
    }
  } else if (type === GROUP_TYPE.WORKSPACE) {
    isHaveWorkspace = true
  }
  getGroupResource(id, type).then((res) => {
    const data = res.data
    if (data) {
      _setResource(data, index, type)
      if (id === 'super_group') {
        return
      }
      if (isHaveWorkspace === false) {
        addWorkspaceGroup(id, index)
      }
    }
  })
}
const addWorkspaceGroup = (id: string, index: number) => {
  let isHaveWorkSpace
  model.value.groups.forEach((item) => {
    if (item.type === 'ws_member+WORKSPACE') {
      isHaveWorkSpace = true
    }
  })
  if (isHaveWorkSpace) {
    return
  }

  getGroupResource(id, GROUP_TYPE.WORKSPACE).then((res) => {
    const data = res.data
    if (data) {
      const roleInfo: { ids: string[]; selects: string[]; type: string } = {
        ids: [],
        selects: [],
        type: 'ws_member+WORKSPACE',
      }
      const ids = model.value.groups.map((r) => r.type)
      ids.forEach((id: string) => {
        roleInfo.selects.push(id)
      })
      if (currentGroupWSIds.value.length > 0) {
        roleInfo.ids = []
        currentGroupWSIds.value.forEach((item) => {
          roleInfo.ids.push(item)
        })
      } else {
        roleInfo.ids = []
      }
      const groups = model.value.groups
      groups.push(roleInfo)
      currentWSGroupIndex.value = index + 1
      _setResource(data, index + 1, GROUP_TYPE.WORKSPACE)
    }
  })
}
const _setResource = (data: any, index: number, type: string) => {
  switch (type) {
    case GROUP_TYPE.WORKSPACE:
      model.value.groups[index].workspace = data.workspaces
      handleWorkspaceOption(model.value.groups[index], data.workspaces)
      break
    case GROUP_TYPE.PROJECT:
      model.value.groups[index].project = data.projects
      handleProjectOption(model.value.groups[index], data.projects)
      break
    default:
      break
  }
}
const handleWorkspaceOption = (group: any, workspaces: IWorkspace[]) => {
  if (!workspaces) {
    return
  }
  group.showSearchGetMore = workspaces.length > limitOptionCount.value
  const options = workspaces.slice(0, limitOptionCount.value)
  const _options: SelectOption[] = []
  options.forEach((item) => {
    const option: SelectOption = {}
    option.label = item.name
    option.value = item.id
    _options.push(option)
  })

  group.workspaceOptions = _options
  if (!group.ids || group.ids.length === 0) {
    return
  }
  for (const id of group.ids) {
    const index = options.findIndex((o) => o.id === id)
    if (index <= -1) {
      const obj = workspaces.find((d) => d.id === id)
      if (obj) {
        group.workspaceOptions.unshift(obj)
      }
    }
  }
}
const handleProjectOption = (group: any, projects: IProject[]) => {
  if (!projects) {
    return
  }
  group.showSearchGetMore = projects.length > limitOptionCount.value
  const options = projects.slice(0, limitOptionCount.value)
  const _options: SelectOption[] = []
  options.forEach((item) => {
    const option: SelectOption = {}
    option.label = item.name
    option.value = item.id
    _options.push(option)
  })
  group.projectOptions = _options
  if (!group.ids || group.ids.length === 0) {
    return
  }
  for (const id of group.ids) {
    const index = options.findIndex((o) => o.id === id)
    if (index <= -1) {
      const obj = projects.find((d) => d.id === id)
      if (obj) {
        group.projectOptions.unshift(obj)
      }
    }
  }
}
// * 删除用户组
const removeGroup = (item: IGroup) => {
  const index = model.value.groups.indexOf(item)
  const isRemove = checkRemove(item, index)
  if (!isRemove) {
    return
  }
  if (item.type) {
    const _type = item.type.split('+')[1]
    if (_type === GROUP_TYPE.WORKSPACE) {
      currentWSGroupIndex.value = -1
    } else {
      if (currentWSGroupIndex.value > index) {
        currentWSGroupIndex.value = currentWSGroupIndex.value - 1
      }
    }
  }
  if (index !== -1) {
    model.value.groups.splice(index, 1)
  }
  if (model.value.groups.length < userGroup.value.length) {
    btnAddGroup.value = false
  }
}

const checkRemove = (item: IGroup, index: number) => {
  if (!item.type) {
    return true
  }
  const type = item.type.split('+')[1]
  if (type === GROUP_TYPE.WORKSPACE) {
    let isHaveWorkspace = 0
    let isHaveProject = 0
    for (let i = 0; i < model.value.groups.length; i++) {
      if (index === i) {
        continue
      }
      const group = model.value.groups[i]
      if (!group.type) {
        continue
      }
      const _type = group.type.split('+')[1]
      if (_type === GROUP_TYPE.WORKSPACE) {
        isHaveWorkspace += 1
      }
      if (_type === GROUP_TYPE.PROJECT) {
        isHaveProject += 1
      }
    }
    if (isHaveWorkspace === 0 && isHaveProject > 0) {
      window.$message?.warning(t('commons.not_eligible_for_deletion'))
      return false
    } else {
      currentGroupWSIds.value = []
    }
  }
  return true
}
const updateWorkSpace = (index: number, type: string) => {
  const _type = type.split('+')[1]
  if (_type === GROUP_TYPE.WORKSPACE) {
    currentGroupWSIds.value.forEach((item) => {
      model.value.groups[index].ids.push(item)
    })
  } else {
    model.value.groups[index].ids = []
  }
}
const setWorkSpaceIds = (ids: string[], projects: IProject[]) => {
  projects.forEach((project) => {
    ids.forEach((item) => {
      if (item === project.id) {
        currentGroupWSIds.value.push(project.workspaceId as string)
        if (
          model.value.groups[currentWSGroupIndex.value] &&
          model.value.groups[currentWSGroupIndex.value].ids.indexOf(project.workspaceId) === -1
        ) {
          model.value.groups[currentWSGroupIndex.value].ids.push(project.workspaceId)
        }
      }
    })
  })
}
// const addGroup = (validForm: any) => {
//   validForm.value?.validate((errors) => {
//     if (errors) {
//       const roleInfo: { selects: string[] } = { selects: [] }
//       const ids = model.value.groups.map((r) => r.type)
//       ids.forEach((id) => {
//         roleInfo.selects.push(id)
//       })
//       const groups = model.value.groups
//       groups.push(roleInfo)
//       if (model.value.groups.length > userGroup.value.length - 1) {
//         btnAddGroup.value = true
//       }
//     } else {
//       return false
//     }
//   })
// }
defineExpose({ openEditModal })
</script>

<template>
  <modal-component ref="modalDialog" :title="dialogTitle" sub-class="width:40%" @confirm="handleSubmit">
    <template #content>
      <n-form
        ref="createUserForm"
        :model="model"
        :rules="rules"
        label-placement="left"
        label-width="auto"
        require-mark-placement="right-hanging"
      >
        <n-form-item :label="$t('commons.username')" path="name">
          <n-input v-model:value="model.name" :placeholder="$t('user.input_name')" />
        </n-form-item>
        <n-form-item :label="$t('commons.email')" path="email">
          <n-input v-model:value="model.email" :placeholder="$t('user.input_email')" />
        </n-form-item>
        <n-form-item :label="$t('commons.phone')" path="phone">
          <n-input v-model:value="model.phone" :placeholder="$t('user.input_phone')" />
        </n-form-item>
        <n-form-item v-if="type === 'Add'" :label="$t('commons.password')" path="password">
          <n-input
            v-model:value="model.password"
            :placeholder="$t('user.input_password')"
            type="password"
            show-password-on="mousedown"
          />
        </n-form-item>
        <div v-for="(group, index) in model.groups" :key="index">
          <n-form-item :label="getLabel(index)" :path="'groups.' + index + '.type'">
            <n-select
              v-model:value="group.type"
              :options="activeGroup(group)"
              :placeholder="$t('user.select_group')"
              :disabled="model.groups[index].type !== null && model.groups[index].type !== ''"
              @update:value="getResource(group.type, index)"
            />
            <span class="ml-5">
              <n-button v-if="model.groups.length > 1" type="error" @click="removeGroup(group)">
                {{ $t('commons.delete') }}
              </n-button>
            </span>
          </n-form-item>
          <div v-if="groupType(group.type) === GROUP_TYPE.WORKSPACE">
            <n-form-item :label="t('commons.workspace')" :path="'groups.' + index + '.ids'">
              <n-select
                v-model:value="group.ids"
                multiple
                :options="group.workspaceOptions"
                :placeholder="$t('workspace.select')"
                @update:value="updateWorkSpace(group.index, group.type)"
              />
            </n-form-item>
          </div>
          <div v-if="groupType(group.type) === GROUP_TYPE.PROJECT">
            <n-form-item :label="t('commons.project')" :path="'groups.' + index + '.ids'">
              <n-select
                v-model:value="group.ids"
                multiple
                :options="group.projectOptions"
                :placeholder="$t('user.select_project')"
                @update:value="setWorkSpaceIds(group.ids, group.projects)"
              />
            </n-form-item>
          </div>
        </div>
        <!-- <n-form-item>
          <n-button :disabled="btnAddGroup" @click="addGroup(createUserForm)">{{ $t('group.add') }}</n-button>
        </n-form-item> -->
      </n-form>
    </template>
  </modal-component>
</template>

<style scoped></style>
