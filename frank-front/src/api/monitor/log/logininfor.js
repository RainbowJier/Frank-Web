import request from '@/utils/request'

// 查询登录日志列表
export function list(query) {
  return request({
    url: '/sys-log-login/list',
    method: 'post',
    data: query
  })
}

// 删除登录日志
export function delLogininfor(infoIds) {
  return request({
    url: '/sys-log-login/delete',
    method: 'post',
    data: infoIds
  })
}

// 清空登录日志
export function cleanLogininfor() {
  return request({
    url: '/sys-log-login/clean',
    method: 'get'
  })
}
