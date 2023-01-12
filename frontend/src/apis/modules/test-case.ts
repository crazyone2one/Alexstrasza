import { IPageResponse, IReqPage, IResultData } from '../interface'
import { ITreeNode } from './test-case-node'
import service from '/@/apis/index'

export const getTestCaseNodesByCaseFilter = (projectId: string, param: any): Promise<IResultData<ITreeNode[]>> => {
  return service.post('/case/node/list/' + projectId, param)
}
