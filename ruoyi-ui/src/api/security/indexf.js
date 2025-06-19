import request from '@/utils/request'

// 查询实时期货分析数据
export function findList() {
  return request({
    url: '/money/findList',
    method: 'post'
  })
}

export function findSinaList() {
  return request({
    url: '/money/findSinaList',
    method: 'post'
  })
}

export function updateFluctuationLog() {
  return request({
    url: '/money/updateFluctuationLog',
    method: 'post'
  })
}
