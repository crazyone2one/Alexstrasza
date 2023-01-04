import { useReactivated } from 'naive-ui/es/_utils'
import { IReqPage, IResultData } from '../interface'
import service from '/@/apis/index'

interface IReqLogin {
  name: string
  password: string
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
export const loginApi = async (params: IReqLogin): Promise<IResultData<IUserInfo>> => {
  return await service.post('/user/login', params)
}

export const registerApi = async (params: unknown): Promise<IResultData<unknown>> => {
  return await service.post('/user/register', params)
}

export const updateCurrentUser = (user: unknown) => {
  return service.post('/user/update/current', user)
}

export const getCurrentUser = (userId: string): Promise<IResultData<IUserInfo>> => {
  return service.get(`/user/current/${userId}`)
}
