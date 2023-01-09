import { IReqPage, IResultData, IPageResponse } from '../interface'
import service from '/@/apis/index'

export interface ICustomField {
  id?: string
  name: string
  scene: string
  type: string
  remark: string
  options: string
  system: boolean
  global: boolean
  project_id: string
  third_part: boolean
  createTime: Date
  updateTime: Date
}
interface ISearch extends IReqPage {
  projectId: string
}

export const getCustomFieldPages = (params: ISearch): Promise<IResultData<IPageResponse<ICustomField>>> => {
  const page = params.page
  const limit = params.limit
  return service.post(`/custom/field/list/${page}/${limit}`, params)
}
