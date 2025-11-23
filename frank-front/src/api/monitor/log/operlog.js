import request from '@/utils/request'

// 查询操作日志列表
export function list(query) {
  return request({
    url: '/sys-log-operlog/page',
    method: 'post',
    data: query
  })
}

// 删除操作日志
export function delOperlog(operIds) {
  return request({
    url: '/sys-log-operlog/',
    method: 'post',
    data: operIds
  })
}

// 清空操作日志
export function cleanOperlog() {
  return request({
    url: '/sys-log-operlog/clean',
    method: 'get'
  })
}
