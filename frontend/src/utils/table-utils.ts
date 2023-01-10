import { ICustomField } from '../apis/modules/custom-field'

export const getCustomFieldsKeys = (customFields: ICustomField[]): Set<string> => {
  let keys = new Set()
  customFields.forEach((item) => {
    if (item.key) {
      keys.add(item.key)
    }
  })
  return keys as Set<string>
}

export const generateTableHeaderKey = (keys: Set<string>, customFields?: ICustomField[]) => {
  let customFieldKeys = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'
  for (let i = 0; i < customFieldKeys.length; i++) {
    let key = customFieldKeys[i]
    if (keys.has(key)) {
      continue
    }
    keys.add(key)
    return key
  }
  return ''
}
