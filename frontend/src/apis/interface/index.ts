// * 请求响应参数(不包含data)
export interface IResult {
  code: string
  message: string
  success: boolean
}
// * 请求响应参数(包含data)
export interface IResultData<T = unknown> extends IResult {
  data: T
}
// * 分页请求参数
export interface IReqPage {
  name: string
  page: number
  limit: number
}
// *分页响应结果
export interface IPageResponse<T = unknown> {
  total: number
  records: T[]
}
