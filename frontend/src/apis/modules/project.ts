import { IPageResponse, IReqPage, IResultData } from '../interface'
import { IUserInfo } from './user'
import service from '/@/apis/index'

export interface IProject {
  id?: string
  name: string
  description: string
  memberSize?: number
  createUserName?: string
  createTime?: Date
  updateTime?: Date
  platform?: string
  thirdPartTemplate?: string
  tapdId?: string
  caseTemplateId?: string
  issueTemplateId?: string
  workspaceId?: string
  createUser?: string
}
interface IReqParams extends IReqPage {
  id?: string
}

/**
 * 查询接口
 * @param params
 * @returns
 */
export const getProjectPages = async (params: IReqParams): Promise<IResultData<IPageResponse<IProject>>> => {
  const page = params.page
  const limit = params.limit
  return await service.post(`/project/list/${page}/${limit}`, params)
}

export const getProjectMemberPages = (workspaceId: string, param: unknown): Promise<IResultData<IUserInfo[]>> => {
  return service.post(`/user/ws/project/member/list/${workspaceId}`, param)
}
/**
 * save project
 * @param param
 * @returns
 */
export const saveProject = (param: IProject): Promise<IResultData<IProject>> => {
  return service.post(`/project/add`, param)
}
/**
 * modify project
 * @param param
 * @returns
 */
export const modifyProject = (param: IProject): Promise<IResultData<IProject>> => {
  return service.post(`/project/update`, param)
}
