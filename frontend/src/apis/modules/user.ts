import { IReqPage, IResultData, IPageResponse } from '../interface'
import { ISystemGroup } from './user-group'
import { IWorkspace } from './workspace'
import service from '/@/apis/index'

interface IReqLogin {
  name: string
  password: string
}
interface IWsMember extends IReqPage {
  workspaceId: string
}
interface IUserSearch extends IReqPage {}
export interface IUserInfo {
  id?: string
  userId: string
  email: string
  name: string
  username: string
  phone: string
  token?: string
  password?: string
  status?: string
  groups: any[]
  lastWorkspaceId?: string
  lastProjectId?: string
  authorities?: ISystemGroup[]
}
export interface IGroup {
  ids?: string[]
  selects?: string[]
  showSearchGetMore: boolean
  type: string
  workspace?: IWorkspace[]
  workspaceOptions?: { label: string; value: string }[]
}
// * login api methods
export const loginApi = async (params: IReqLogin): Promise<IResultData<IUserInfo>> => {
  return await service.post('/user/login', params)
}
// * register api
export const registerApi = async (params: unknown): Promise<IResultData<unknown>> => {
  return await service.post('/user/register', params)
}
// * update current user
export const updateCurrentUser = async (user: unknown) => {
  return await service.post('/user/update/current', user)
}
// * get current user
export const getCurrentUser = async (userId: string): Promise<IResultData<IUserInfo>> => {
  return await service.get(`/user/current/${userId}`)
}

export const getWorkspaceMemberPages = async (params: IWsMember): Promise<IResultData<IPageResponse<IUserInfo>>> => {
  const page = params.page
  const limit = params.limit
  return await service.post(`/user/ws/member/list/${page}/${limit}`, params)
}

export const getWorkspaceMemberGroup = (workspaceId: string, userId: string) => {
  return service.get(`/user/group/list/ws/${workspaceId}/${userId}`)
}

/**
 *  列表数据查询*/
export const specialListUsers = async (params: IUserSearch): Promise<IResultData<IPageResponse<IUserInfo>>> => {
  const page = params.page
  const limit = params.limit
  return await service.post(`/user/special/list/${page}/${limit}`, params)
}

export const specialGetUserGroup = (userId: string) => {
  return service.get(`/user/special/user/group/${userId}`)
}
// * add user
export const specialCreateUser = (param: IUserInfo) => {
  return service.post('/user/special/add', param)
}
//* update user
export const specialModifyUser = (param: IUserInfo) => {
  return service.post('/user/special/update', param)
}
export const specialModifyUserDisable = (param: IUserInfo) => {
  return service.post('/user/special/update_status', param)
}
