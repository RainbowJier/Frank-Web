import axios from 'axios'
import { ElNotification, ElMessageBox, ElMessage, ElLoading } from 'element-plus'
import { getToken } from '@/utils/auth'
import errorCode from '@/utils/errorCode'
import { tansParams, blobValidate } from '@/utils/ruoyi'
import cache from '@/plugins/cache'
import { saveAs } from 'file-saver'
import useUserStore from '@/store/modules/user'

/**
 * HTTP 请求工具类
 * 基于 axios 封装的统一请求处理，包含认证、错误处理、防重复提交等功能
 */

// 全局下载加载实例
let downloadLoadingInstance = null

// 重新登录控制标志

// 请求配置常量
const REQUEST_CONFIG = {
  TIMEOUT: 10000,
  CONTENT_TYPE: 'application/json;charset=utf-8',
  REPEAT_SUBMIT_INTERVAL: 1000, // 防重复提交间隔时间(ms)
  MAX_REQUEST_SIZE: 5 * 1024 * 1024, // 最大请求数据大小 5M
  SESSION_CACHE_KEY: 'sessionObj'
}

// 创建axios实例
const service = axios.create({
  baseURL: import.meta.env.VITE_APP_BASE_URL + import.meta.env.VITE_APP_BASE_API,
  timeout: REQUEST_CONFIG.TIMEOUT,
  headers: {
    'Content-Type': REQUEST_CONFIG.CONTENT_TYPE
  }
})

/**
 * 检查并防止重复提交
 * @param {Object} config - 请求配置
 * @returns {Promise<Object>} 返回配置或拒绝Promise
 */
function checkRepeatSubmit(config) {
  const requestObj = {
    url: config.url,
    data: typeof config.data === 'object' ? JSON.stringify(config.data) : config.data,
    time: Date.now()
  }

  // 检查请求数据大小
  const requestSize = JSON.stringify(requestObj).length
  if (requestSize >= REQUEST_CONFIG.MAX_REQUEST_SIZE) {
    console.warn(`[${config.url}]: 请求数据大小超出允许的5M限制，无法进行防重复提交验证。`)
    return Promise.resolve(config)
  }

  try {
    const sessionObj = cache.session.getJSON(REQUEST_CONFIG.SESSION_CACHE_KEY)

    if (!sessionObj) {
      cache.session.setJSON(REQUEST_CONFIG.SESSION_CACHE_KEY, requestObj)
      return Promise.resolve(config)
    }

    const { url: s_url, data: s_data, time: s_time } = sessionObj
    const timeDiff = requestObj.time - s_time

    // 检查是否为重复提交
    if (s_data === requestObj.data &&
        timeDiff < REQUEST_CONFIG.REPEAT_SUBMIT_INTERVAL &&
        s_url === requestObj.url) {
      const message = '数据正在处理，请勿重复提交'
      console.warn(`[${s_url}]: ${message}`)
      return Promise.reject(new Error(message))
    }

    cache.session.setJSON(REQUEST_CONFIG.SESSION_CACHE_KEY, requestObj)
    return Promise.resolve(config)
  } catch (error) {
    console.error('防重复提交检查出错:', error)
    return Promise.resolve(config)
  }
}

/**
 * 处理GET请求参数
 * @param {Object} config - 请求配置
 * @returns {Object} 处理后的配置
 */
function handleGetRequest(config) {
  if (config.method === 'get' && config.params) {
    let url = config.url + '?' + tansParams(config.params)
    url = url.slice(0, -1)
    config.params = {}
    config.url = url
  }
  return config
}

/**
 * 添加认证头
 * @param {Object} config - 请求配置
 * @returns {Object} 处理后的配置
 */
function addAuthHeader(config) {
  const isToken = (config.headers || {}).isToken === false
  const token = getToken()

  if (token && !isToken) {
    config.headers['Authorization'] = `Bearer ${token}`
  }

  return config
}

// 请求拦截器
service.interceptors.request.use(config => {
  try {
    // 添加认证头
    config = addAuthHeader(config)

    // 处理GET请求参数
    config = handleGetRequest(config)

    // 检查防重复提交
    const isRepeatSubmit = (config.headers || {}).repeatSubmit === false
    if (!isRepeatSubmit && ['post', 'put'].includes(config.method)) {
      return checkRepeatSubmit(config)
    }

    return config
  } catch (error) {
    console.error('请求拦截器错误:', error)
    return Promise.reject(error)
  }
}, error => {
  console.error('请求拦截器错误:', error)
  return Promise.reject(error)
})

/**
 * 处理401未授权错误
 */
function handleUnauthorizedError() {
  ElMessageBox.confirm(
      '登录状态已过期，您可以继续留在该页面，或者重新登录',
      '系统提示',
      {
        confirmButtonText: '重新登录',
        cancelButtonText: '取消',
        type: 'warning'
      }
    ).then(() => {
      const userStore = useUserStore()
      userStore.logOut().then(() => {
        location.href = '/index'
      })
    }).catch(() => {
      // 取消重新登录操作
    })
  return Promise.reject('无效的会话，或者会话已过期，请重新登录。')
}

/**
 * 处理服务器错误
 * @param {string} msg - 错误消息
 * @param {number} code - 错误码
 */
function handleServerError(msg, code) {
  switch (code) {
    case 500:
      ElMessage({
        message: msg,
        type: 'error',
        duration: 5000
      })
      return Promise.reject(new Error(msg))

    case 601:
      ElMessage({
        message: msg,
        type: 'warning',
        duration: 3000
      })
      return Promise.reject(new Error(msg))

    default:
      ElNotification.error({
        title: msg,
        duration: 3000
      })
      return Promise.reject('error')
  }
}

/**
 * 格式化网络错误消息
 * @param {string} message - 原始错误消息
 * @returns {string} 格式化后的错误消息
 */
function formatNetworkErrorMessage(message) {
  if (!message) return '未知网络错误'

  if (message === "Network Error") {
    return "后端接口连接异常"
  }

  if (message.includes("timeout")) {
    return "系统接口请求超时"
  }

  if (message.includes("Request failed with status code")) {
    const statusCode = message.slice(-3)
    return `系统接口${statusCode}异常`
  }

  return message
}

// 响应拦截器
service.interceptors.response.use(
  res => {
    try {
      // 处理二进制数据响应
      if (res.request.responseType === 'blob' || res.request.responseType === 'arraybuffer') {
        return res.data
      }

      // 获取响应状态码和消息
      const code = res.data?.code || 200
      const msg = errorCode[code] || res.data?.msg || errorCode['default']

      console.log(code)
      // 处理不同的响应状态码
      switch (code) {
        case 200:
          return Promise.resolve(res.data)

        case 401:
          return handleUnauthorizedError()

        default:
          return handleServerError(msg, code)
      }
    } catch (error) {
      console.error('响应处理错误:', error)
      return Promise.reject(error)
    }
  },
  error => {
    console.error('响应拦截器错误:', error)

    const message = formatNetworkErrorMessage(error.message)
    ElMessage({
      message,
      type: 'error',
      duration: 5000
    })

    return Promise.reject(error)
  }
)

/**
 * 通用文件下载方法
 * @param {string} url - 下载接口地址
 * @param {Object} params - 下载参数
 * @param {string} filename - 文件名
 * @param {Object} config - 额外配置
 * @returns {Promise} 下载Promise
 */
export function download(url, params, filename, config = {}) {
  // 确保没有重复的加载实例
  if (downloadLoadingInstance) {
    downloadLoadingInstance.close()
  }

  downloadLoadingInstance = ElLoading.service({
    text: "正在下载数据，请稍候",
    background: "rgba(0, 0, 0, 0.7)"
  })

  const downloadConfig = {
    transformRequest: [(params) => tansParams(params)],
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    responseType: 'blob',
    ...config
  }

  return service.post(url, params, downloadConfig)
    .then(async (data) => {
      try {
        const isBlob = blobValidate(data)

        if (isBlob) {
          const blob = new Blob([data])
          saveAs(blob, filename)
        } else {
          const resText = await data.text()
          const rspObj = JSON.parse(resText)
          const errMsg = errorCode[rspObj.code] || rspObj.msg || errorCode['default']
          ElMessage.error(errMsg)
        }
      } catch (error) {
        console.error('下载响应处理错误:', error)
        ElMessage.error('下载文件解析失败')
      } finally {
        closeDownloadLoading()
      }
    })
    .catch((error) => {
      console.error('下载请求失败:', error)
      ElMessage.error('下载文件出现错误，请联系管理员！')
      closeDownloadLoading()
    })
}

/**
 * 关闭下载加载提示
 */
function closeDownloadLoading() {
  if (downloadLoadingInstance) {
    downloadLoadingInstance.close()
    downloadLoadingInstance = null
  }
}

/**
 * 取消正在进行的请求
 * @param {string} url - 要取消的请求URL
 */
export function cancelRequest(url) {
  // 可以在这里实现请求取消逻辑
  console.warn(`取消请求: ${url}`)
}

/**
 * 清除会话缓存（用于登出时清理）
 */
export function clearSessionCache() {
  try {
    cache.session.remove(REQUEST_CONFIG.SESSION_CACHE_KEY)
  } catch (error) {
    console.error('清除会话缓存失败:', error)
  }
}

/**
 * 获取当前请求配置
 * @returns {Object} 返回axios实例配置
 */
export function getRequestConfig() {
  return service.defaults
}

/**
 * 设置请求超时时间
 * @param {number} timeout - 超时时间（毫秒）
 */
export function setTimeout(timeout) {
  service.defaults.timeout = timeout
}

export default service
