import { IReqPage, IResultData, IPageResponse } from '../interface'
import service from '/@/apis/index'

interface IReqLogin {
  name: string
  password: string
}
interface IWsMember extends IReqPage {
  workspaceId: string
}
export interface IUserInfo {
  id?: string
  userId: string
  email: string
  name: string
  username: string
  phone: string
  token?: string
  password?: string
  groups: any[]
  lastWorkspaceId?: string
  lastProjectId?: string
  authorities?: []
}
// * login api methods
export const loginApi = async (params: IReqLogin): Promise<IResultData<IUserInfo>> => {
  return await service.post('/user/login', params)
}
// * register api
export const registerApi = async (params: unknown): Promise<IResultData<unknown>> => {
  return await service.post('/user/register', params)
}
// * update user
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
