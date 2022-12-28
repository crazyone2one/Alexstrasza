import { IReqPage, IResultData } from '../interface'
import service from '/@/apis/index'

interface IReqLogin {
  name: string
  password: string
}
export interface IUserInfo {
  userId: string
  email: string
  nickname: string
  password: string
  status: boolean
  token: string
  username: string
  mobile: string
  authorities: unknown
}
export const loginApi = async (params: IReqLogin): Promise<IResultData<IUserInfo>> => {
  return await service.post('/user/login', params)
}

export const registerApi = async (params: unknown): Promise<IResultData<IUserInfo>> => {
  return await service.post('/user/register', params)
}
