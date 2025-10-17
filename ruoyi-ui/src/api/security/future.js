import request from '@/utils/request'

// 查询实时期货分析数据
export function findList() {
  return request({
    url: '/money/findList',
    method: 'post'
  })
}

// 查询实时期货分析数据
export function findListByPoints() {
  return request({
    url: '/money/findListByPoints',
    method: 'post'
  })
}
