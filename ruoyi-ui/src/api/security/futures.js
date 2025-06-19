import request from '@/utils/request'

// 查询证劵交易列表
export function listFutures(query) {
  return request({
    url: '/security/futures/list',
    method: 'get',
    params: query
  })
}

// 查询证劵交易详细
export function getFutures(id) {
  return request({
    url: '/security/futures/' + id,
    method: 'get'
  })
}

// 新增证劵交易
export function addFutures(data) {
  return request({
    url: '/security/futures',
    method: 'post',
    data: data
  })
}

// 修改证劵交易
export function updateFutures(data) {
  return request({
    url: '/security/futures',
    method: 'put',
    data: data
  })
}

// 删除证劵交易
export function delFutures(id) {
  return request({
    url: '/security/futures/' + id,
    method: 'delete'
  })
}
