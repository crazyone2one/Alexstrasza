export interface ITreeNode {
  id: string
  key: string
  projectId: string
  name: string
  parentId: string
  level: number
  createTime: string
  updateTime: string
  pos: number
  label: string
  children: ITreeNode[]
  caseNum: number
}
