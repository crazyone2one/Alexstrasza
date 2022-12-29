import { IReqPage, IResultData } from '../interface'
import service from '/@/apis/index'

interface IReqLogin {
  name: string
  password: string
}
export interface IUserInfo {
  userId?: string
  email: string
  username: string
  phone: string
  token: string
  last_workspace_id: string
  last_organization_id: string
  authorities: []
}
export const loginApi = async (params: IReqLogin): Promise<IResultData<IUserInfo>> => {
  return await service.post('/user/login', params)
}

export const registerApi = async (params: unknown): Promise<IResultData<unknown>> => {
  return await service.post('/user/register', params)
}
