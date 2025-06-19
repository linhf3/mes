import request from '@/utils/request'

// 查询波动日志列表
export function listLog(query) {
  return request({
    url: '/security/log/list',
    method: 'get',
    params: query
  })
}

// 查询波动日志详细
export function getLog(id) {
  return request({
    url: '/security/log/' + id,
    method: 'get'
  })
}

// 新增波动日志
export function addLog(data) {
  return request({
    url: '/security/log',
    method: 'post',
    data: data
  })
}

// 修改波动日志
export function updateLog(data) {
  return request({
    url: '/security/log',
    method: 'put',
    data: data
  })
}

// 删除波动日志
export function delLog(id) {
  return request({
    url: '/security/log/' + id,
    method: 'delete'
  })
}
