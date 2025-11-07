<template>
  <div class="login">
    <div class="login-container">
      <div class="login-card">
        <div class="login-header">
          <div class="logo">
            <div class="logo-icon">
              <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M12 2L2 7L12 12L22 7L12 2Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                <path d="M2 17L12 22L22 17" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                <path d="M2 12L12 17L22 12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </div>
          </div>
          <h1 class="title">{{ title }}</h1>
          <p class="subtitle">Welcome back! Please sign in to your account</p>
        </div>

        <el-form ref="loginRef" :model="loginForm" :rules="loginRules" class="login-form">
          <el-form-item prop="username">
            <el-input
              v-model="loginForm.username"
              type="text"
              size="large"
              auto-complete="off"
              placeholder="Username"
              class="custom-input"
            >
              <template #prefix>
                <svg-icon icon-class="user" class="input-icon" />
              </template>
            </el-input>
          </el-form-item>

          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              size="large"
              auto-complete="off"
              placeholder="Password"
              class="custom-input"
              @keyup.enter="handleLogin"
              show-password
            >
              <template #prefix>
                <svg-icon icon-class="password" class="input-icon" />
              </template>
            </el-input>
          </el-form-item>

          <el-form-item prop="code" v-if="captchaEnabled" class="captcha-item">
            <div class="captcha-container">
              <el-input
                v-model="loginForm.code"
                size="large"
                auto-complete="off"
                placeholder="Verification Code"
                class="custom-input captcha-input"
                @keyup.enter="handleLogin"
              >
                <template #prefix>
                  <svg-icon icon-class="validCode" class="input-icon" />
                </template>
              </el-input>
              <div class="captcha-image" @click="getCode">
                <img v-if="codeUrl" :src="codeUrl" alt="Verification Code" />
                <div v-else class="captcha-placeholder">
                  <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M4 4H20V20H4V4Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    <path d="M9 9H15V15H9V9Z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                  </svg>
                  Click to refresh
                </div>
              </div>
            </div>
          </el-form-item>

          <el-form-item class="button-item">
            <el-button
              :loading="loading"
              size="large"
              type="primary"
              class="login-button"
              @click.prevent="handleLogin"
            >
              <span v-if="!loading">Sign In</span>
              <span v-else>Signing in...</span>
            </el-button>

            <div class="register-link" v-if="register">
              <span>Don't have an account? </span>
              <router-link :to="'/register'">Sign up</router-link>
            </div>
          </el-form-item>
        </el-form>
      </div>
    </div>

  
    <div class="copyright">
      <span>© 2025 {{ title }}. All rights reserved.</span>
    </div>
  </div>
</template>

<script setup>
import { getCodeImg } from "@/api/login"
import Cookies from "js-cookie"
import { encrypt, decrypt } from "@/utils/jsencrypt"
import useUserStore from '@/store/modules/user'

const title = import.meta.env.VITE_APP_TITLE
const userStore = useUserStore()
const route = useRoute()
const router = useRouter()
const { proxy } = getCurrentInstance()

const loginForm = ref({
  username: "admin",
  password: "frank",
  rememberMe: false,
  code: "",
  uuid: ""
})

const loginRules = {
  username: [{ required: true, trigger: "blur", message: "请输入您的账号" }],
  password: [{ required: true, trigger: "blur", message: "请输入您的密码" }],
  code: [{ required: true, trigger: "change", message: "请输入验证码" }]
}

const codeUrl = ref("")
const loading = ref(false)
// 验证码开关
const captchaEnabled = ref(false)
// 注册开关
const register = ref(false)
const redirect = ref(undefined)

watch(route, (newRoute) => {
  redirect.value = newRoute.query && newRoute.query.redirect
}, { immediate: true })

function handleLogin() {
  proxy.$refs.loginRef.validate(valid => {
    if (valid) {
      loading.value = true
      // 勾选了需要记住密码设置在 cookie 中设置记住用户名和密码
      if (loginForm.value.rememberMe) {
        Cookies.set("username", loginForm.value.username, { expires: 30 })
        Cookies.set("password", encrypt(loginForm.value.password), { expires: 30 })
        Cookies.set("rememberMe", loginForm.value.rememberMe, { expires: 30 })
      } else {
        // 否则移除
        Cookies.remove("username")
        Cookies.remove("password")
        Cookies.remove("rememberMe")
      }
      // 调用action的登录方法
      userStore.login(loginForm.value).then(() => {
        const query = route.query
        const otherQueryParams = Object.keys(query).reduce((acc, cur) => {
          if (cur !== "redirect") {
            acc[cur] = query[cur]
          }
          return acc
        }, {})
        router.push({ path: redirect.value || "/", query: otherQueryParams })
      }).catch(() => {
        loading.value = false
        // 重新获取验证码
        if (captchaEnabled.value) {
          getCode()
        }
      })
    }
  })
}

/**
 * 获取验证码
 */
// function getCode() {
//   getCodeImg().then(res => {
//     captchaEnabled.value = res.captchaEnabled === undefined ? true : res.captchaEnabled
//     if (captchaEnabled.value) {
//       codeUrl.value = "data:image/gif;base64," + res.img
//       loginForm.value.uuid = res.uuid
//     }
//   })
// }

// function getCookie() {
//   const username = Cookies.get("username")
//   const password = Cookies.get("password")
//   const rememberMe = Cookies.get("rememberMe")
//   loginForm.value = {
//     username: username === undefined ? loginForm.value.username : username,
//     password: password === undefined ? loginForm.value.password : decrypt(password),
//     rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
//   }
// }


// getCode()
// getCookie()
</script>

<style lang='scss' scoped>
.login {
  min-height: 100vh;
  background: linear-gradient(135deg, #3b82f6, #1e40af);
  padding: 20px;
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-card {
  background: white;
  border-radius: 12px;
  padding: 40px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
}

.login-header {
  text-align: center;
  margin-bottom: 35px;
}

.logo {
  margin-bottom: 20px;
}

.logo-icon {
  width: 50px;
  height: 50px;
  margin: 0 auto 15px;
  background: #3b82f6;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;

  svg {
    width: 24px;
    height: 24px;
  }
}

.title {
  margin: 0 0 8px 0;
  font-size: 24px;
  font-weight: 600;
  color: #3b82f6;
}

.subtitle {
  margin: 0;
  font-size: 14px;
  color: #6b7280;
}

.login-form {
  .el-form-item {
    margin-bottom: 24px;
  }

  .custom-input {
    :deep(.el-input__wrapper) {
      border: 1px solid #d1d5db;
      border-radius: 8px;
      padding: 12px 16px;

      &.is-focus {
        border-color: #3b82f6;
      }
    }

    :deep(.el-input__inner) {
      font-size: 14px;
      &::placeholder {
        color: #9ca3af;
      }
    }
  }

  .input-icon {
    color: #9ca3af;
    margin-right: 8px;
  }

  .captcha-item {
    .captcha-container {
      display: flex;
      gap: 12px;
      align-items: center;
    }

    .captcha-input {
      flex: 1;
    }

    .captcha-image {
      width: 100px;
      height: 40px;
      border: 1px solid #d1d5db;
      border-radius: 6px;
      cursor: pointer;
      overflow: hidden;

      &:hover {
        border-color: #3b82f6;
      }

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }

      .captcha-placeholder {
        display: flex;
        align-items: center;
        justify-content: center;
        color: #9ca3af;
        font-size: 12px;
        height: 100%;
      }
    }
  }

  .button-item {
    margin-top: 32px;
    margin-bottom: 0;
  }

  .login-button {
    width: 100%;
    height: 44px;
    border-radius: 6px;
    background: #3b82f6;
    border: none;
    font-size: 14px;
    font-weight: 500;

    &:hover {
      background: #2563eb;
    }

    &.is-loading {
      background: #2563eb;
    }
  }

  .register-link {
    text-align: center;
    margin-top: 16px;
    font-size: 13px;
    color: #6b7280;

    a {
      color: #3b82f6;
      text-decoration: none;
      font-weight: 500;

      &:hover {
        color: #2563eb;
      }
    }
  }
}

.copyright {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  text-align: center;
  color: rgba(255, 255, 255, 0.8);
  font-size: 12px;
}

@media (max-width: 480px) {
  .login-card {
    padding: 24px;
  }

  .captcha-container {
    flex-direction: column;
    gap: 12px;
  }

  .captcha-image {
    width: 100%;
  }
}
</style>
