import { IPageResponse, IReqPage, IResultData } from '../interface'
import service from '/@/apis/index'

export interface ISystemGroup {
  id?: string
  name: string
  description: string
  system: boolean
  type: string
  scopeId?: string
  scopeName?: string
  global: boolean
  memberSize?: number
  creator?: string
}
export interface IUserGroup {
  type: string
}
interface ISearch extends IReqPage {}
export const createUserGroup = (param: ISystemGroup): Promise<IResultData<ISystemGroup>> => {
  return service.post('/user/group/add', param)
}

export const modifyUserGroup = (param: ISystemGroup): Promise<IResultData<ISystemGroup>> => {
  return service.post('/user/group/edit', param)
}
export const getAllUserGroupByType = (param: any): Promise<IResultData<ISystemGroup[]>> => {
  return service.post('/user/group/get', param)
}
export const getUserGroupListByPage = (params: ISearch) => {
  const page = params.page
  const limit = params.limit
  return service.post(`/user/group/get/${page}/${limit}`, params)
}
export const getUserAllGroups = (userId: string) => {
  return service.get(`/user/group/all/${userId}`)
}

export const getUserGroupList = (params: any): Promise<IResultData<ISystemGroup[]>> => {
  return service.post('/user/group/list', params)
}
