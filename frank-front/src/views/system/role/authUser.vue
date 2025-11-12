
<template>
   <div class="app-container">
      <el-form :model="queryParams" ref="queryRef" v-show="showSearch" :inline="true">
         <el-form-item label="用户名称" prop="userName">
            <el-input
               v-model="queryParams.userName"
               placeholder="请输入用户名称"
               clearable
               style="width: 240px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="手机号码" prop="phonenumber">
            <el-input
               v-model="queryParams.phonenumber"
               placeholder="请输入手机号码"
               clearable
               style="width: 240px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item>
            <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
         </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb8">
         <el-col :span="1.5">
            <el-button
               type="primary"
               plain
               icon="Plus"
               @click="openSelectUser"
               v-hasPermi="['system:role:add']"
            >添加用户</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="danger"
               plain
               icon="CircleClose"
               :disabled="multiple"
               @click="cancelAuthUser()"
               v-hasPermi="['system:role:remove']"
            >批量取消授权</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button 
               type="warning" 
               plain 
               icon="Close"
               @click="handleClose"
            >关闭</el-button>
         </el-col>
         <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
         <el-table-column type="selection" width="55" align="center" />
         <el-table-column label="用户名称" prop="userName" :show-overflow-tooltip="true" />
         <el-table-column label="用户昵称" prop="nickName" :show-overflow-tooltip="true" />
         <el-table-column label="邮箱" prop="email" :show-overflow-tooltip="true" />
         <el-table-column label="手机" prop="phonenumber" :show-overflow-tooltip="true" />
         <el-table-column label="状态" align="center" prop="status">
            <template #default="scope">
               <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
            </template>
         </el-table-column>
         <el-table-column label="创建时间" align="center" prop="createTime" width="180">
            <template #default="scope">
               <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template #default="scope">
               <el-button link type="primary" icon="CircleClose" @click="cancelAuthUser(scope.row)" v-hasPermi="['system:role:remove']">取消授权</el-button>
            </template>
         </el-table-column>
      </el-table>

      <pagination
         v-show="total > 0"
         :total="total"
         v-model:page="queryParams.pageNum"
         v-model:limit="queryParams.pageSize"
         @pagination="getList"
      />
      <select-user ref="selectRef" :roleId="queryParams.roleId" @ok="handleQuery" />
   </div>
</template>

<script setup name="AuthUser">
import selectUser from "./selectUser"
import { allocatedUserList, authUserCancelAll } from "@/api/system/role"

// 响应式数据定义
const route = useRoute()
const { proxy } = getCurrentInstance()
const { sys_normal_disable } = proxy.useDict("sys_normal_disable")

// 表格数据相关
const userList = ref([])
const loading = ref(false)
const total = ref(0)
const selectedUsers = ref([])

// 搜索相关
const showSearch = ref(true)
const queryRef = ref()
const selectRef = ref()

// 计算属性
const multiple = computed(() => !selectedUsers.value.length)
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  roleId: route.params.roleId,
  userName: "",
  phonenumber: "",
})

// 获取已分配用户列表
async function getList() {
  try {
    loading.value = true
    const response = await allocatedUserList(queryParams)
    userList.value = response.data?.rows || []
    total.value = response.data?.total || 0
  } catch (error) {
    console.error("获取用户列表失败:", error)
    proxy.$modal.msgError("获取用户列表失败")
    userList.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

// 返回角色列表
function handleClose() {
  const obj = { path: "/system/role" }
  proxy.$tab.closeOpenPage(obj)
}

// 查询操作
function handleQuery() {
  queryParams.pageNum = 1
  getList()
}

// 重置查询条件
function resetQuery() {
  proxy.resetForm("queryRef")
  queryParams.userName = ""
  queryParams.phonenumber = ""
  handleQuery()
}

// 表格选择变化
function handleSelectionChange(selection) {
  selectedUsers.value = selection
}

// 打开用户选择弹窗
function openSelectUser() {
  proxy.$refs["selectRef"].show()
}

// 取消用户授权
async function cancelAuthUser(row) {
  const roleId = queryParams.roleId
  const cancelUserIds = row ? [row.userId] : selectedUsers.value.map(user => user.userId)

  if (!cancelUserIds?.length) {
    proxy.$modal.msgError("请选择要取消授权的用户")
    return
  }

  const confirmMessage = row
    ? `确认要取消用户"${row.userName}"的角色授权吗？`
    : `确认要取消选中的 ${cancelUserIds.length} 个用户的角色授权吗？`

  try {
    await proxy.$modal.confirm(confirmMessage)
    await authUserCancelAll({ roleId, userIds: cancelUserIds })
    proxy.$modal.msgSuccess("取消授权成功")
    await getList()
  } catch (error) {
    if (error !== "cancel") {
      console.error("取消授权失败:", error)
      proxy.$modal.msgError("取消授权失败")
    }
  }
}

// 页面加载时获取数据
onMounted(() => {
  if (!queryParams.roleId) {
    proxy.$modal.msgError("角色ID不能为空")
    handleClose()
    return
  }
  getList()
})
</script>
