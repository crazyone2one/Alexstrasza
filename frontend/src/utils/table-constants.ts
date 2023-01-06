type IUserGroupScope = {
  [key: string]: string
}
export const USER_GROUP_SCOPE: IUserGroupScope = {
  SYSTEM: 'group.system',
  WORKSPACE: 'group.workspace',
  PROJECT: 'group.project',
  PERSONAL: 'group.personal',
}
