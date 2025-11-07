<template>
   <div class="app-container">
      <h4 class="form-header h4">基本信息</h4>
      <el-form :model="form" label-width="80px">
         <el-row>
            <el-col :span="8" :offset="2">
               <el-form-item label="用户昵称" prop="nickName">
                  <el-input v-model="form.nickName" disabled />
               </el-form-item>
            </el-col>
            <el-col :span="8" :offset="2">
               <el-form-item label="登录账号" prop="userName">
                  <el-input v-model="form.userName" disabled />
               </el-form-item>
            </el-col>
         </el-row>
      </el-form>

      <h4 class="form-header h4">角色信息</h4>
      <el-table v-loading="loading" :row-key="getRowKey" @row-click="clickRow" ref="roleRef" @selection-change="handleSelectionChange" :data="roles.slice((pageNum - 1) * pageSize, pageNum * pageSize)">
         <el-table-column label="序号" width="55" type="index" align="center">
            <template #default="scope">
               <span>{{ (pageNum - 1) * pageSize + scope.$index + 1 }}</span>
            </template>
         </el-table-column>
         <el-table-column type="selection" :reserve-selection="true" :selectable="checkSelectable" width="55"></el-table-column>
         <el-table-column label="角色编号" align="center" prop="roleId" />
         <el-table-column label="角色名称" align="center" prop="roleName" />
         <el-table-column label="权限字符" align="center" prop="roleKey" />
         <el-table-column label="创建时间" align="center" prop="createTime" width="180">
            <template #default="scope">
               <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
         </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="pageNum" v-model:limit="pageSize" />

      <el-form label-width="100px">
         <div style="text-align: center;margin-left:-120px;margin-top:30px;">
            <el-button type="primary" @click="submitForm()">提交</el-button>
            <el-button @click="close()">返回</el-button>
         </div>
      </el-form>
   </div>
</template>

<script setup name="AuthRole">
import { getUser, updateAuthRole, getAuthRoleList } from "@/api/system/user"

const route = useRoute()
const { proxy } = getCurrentInstance()

// 响应式数据
const loading = ref(true)
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const roleIds = ref([])
const roles = ref([])
const form = ref({
  nickName: '',
  userName: '',
  userId: ''
})

// 方法定义
const clickRow = (row) => {
  if (checkSelectable(row)) {
    proxy.$refs.roleRef.toggleRowSelection(row)
  }
}

const handleSelectionChange = (selection) => {
  roleIds.value = selection.map(item => item.roleId)
}

const getRowKey = (row) => row.roleId

const checkSelectable = (row) => row.status === "1"

const close = () => {
  proxy.$tab.closeOpenPage({ path: "/system/user" })
}

const submitForm = async () => {
  try {
    await updateAuthRole({
      userId: form.value.userId,
      roleIds: roleIds.value.join(",")
    })
    proxy.$modal.msgSuccess("授权成功")
    close()
  } catch (error) {
    console.error('授权失败:', error)
  }
}

// 数据初始化
const initData = async () => {
  const userId = route.params?.userId
  if (!userId) return

  loading.value = true

  try {
    // 并行获取用户信息和角色列表
    const [userResponse, roleResponse] = await Promise.all([
      getUser(userId),
      getAuthRoleList(userId)
    ])

    // 设置用户信息
    form.value = userResponse.data

    // 设置角色列表
    roles.value = roleResponse.data
    total.value = roles.value.length

    // 设置已选中的角色
    await nextTick()
    const userRoleIds = form.value.roles?.map(role => role.roleId) || []
    roles.value.forEach(row => {
      if (userRoleIds.includes(row.roleId)) {
        proxy.$refs.roleRef.toggleRowSelection(row)
      }
    })
  } catch (error) {
    console.error('数据初始化失败:', error)
    proxy.$modal.msgError('数据加载失败')
  } finally {
    loading.value = false
  }
}

// 页面加载时初始化数据
onMounted(() => {
  initData()
})
</script>
