import request from '@/utils/request'

export function findSinaList() {
  return request({
    url: '/money/findSinaList',
    method: 'post'
  })
}

export function findSinaFiveList() {
  return request({
    url: '/money/findSinaFiveList',
    method: 'post'
  })
}
