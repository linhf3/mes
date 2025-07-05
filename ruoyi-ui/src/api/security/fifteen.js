import request from '@/utils/request'

// 查询新浪15分钟日志列表
export function listFifteen(query) {
  return request({
    url: '/security/fifteen/list',
    method: 'get',
    params: query
  })
}

// 查询新浪15分钟日志详细
export function getFifteen(id) {
  return request({
    url: '/security/fifteen/' + id,
    method: 'get'
  })
}

// 新增新浪15分钟日志
export function addFifteen(data) {
  return request({
    url: '/security/fifteen',
    method: 'post',
    data: data
  })
}

// 修改新浪15分钟日志
export function updateFifteen(data) {
  return request({
    url: '/security/fifteen',
    method: 'put',
    data: data
  })
}

// 删除新浪15分钟日志
export function delFifteen(id) {
  return request({
    url: '/security/fifteen/' + id,
    method: 'delete'
  })
}
