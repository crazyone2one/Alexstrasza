import { IPageResponse, IReqPage, IResultData } from '../interface'
import { IUserInfo } from './user'
import service from '/@/apis/index'

export interface IWorkspace {
  id?: string
  organizationId?: string
  name: string
  description: string
  memberSize?: number
}
interface IReqParams extends IReqPage {
  id?: string
}
/**
 * 查询接口
 * @param params
 * @returns
 */
export const getWorkspaceList = async (params: IReqParams): Promise<IResultData<IPageResponse<IWorkspace>>> => {
  const page = params.page
  const limit = params.limit
  return await service.post(`/workspace/list/all/${page}/${limit}`, params)
}
/**
 * 添加workspace接口
 * @param param 参数
 * @returns
 */
export const addWorkspaceSpecial = (param: IWorkspace): Promise<IResultData<IWorkspace>> => {
  return service.post('/workspace/special/add', param)
}

export const updateWorkspaceSpecial = (param: IWorkspace): Promise<IResultData<IWorkspace>> => {
  return service.post('/workspace/special/update', param)
}

export const getUserWorkspaceList = (): Promise<IResultData<IWorkspace[]>> => {
  return service.get('/workspace/list/userworkspace')
}

export const getWorkspaces = (): Promise<IResultData<IWorkspace[]>> => {
  return service.get('/workspace/list')
}
/**
 * 切换用户workspace
 * @param workspaceId workspaceId
 * @returns 更新后的用户数据
 */
export const switchWorkspace = (workspaceId: string): Promise<IResultData<IUserInfo>> => {
  return service.get(`/user/switch/source/ws/${workspaceId}`)
}
