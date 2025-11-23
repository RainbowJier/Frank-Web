<template>
   <div class="app-container">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
         <el-form-item label="登录地址" prop="ipaddr">
            <el-input
               v-model="queryParams.ipaddr"
               placeholder="请输入登录地址"
               clearable
               style="width: 240px;"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="用户名称" prop="userName">
            <el-input
               v-model="queryParams.userName"
               placeholder="请输入用户名称"
               clearable
               style="width: 240px;"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="状态" prop="status">
            <el-select
               v-model="queryParams.status"
               placeholder="登录状态"
               clearable
               style="width: 240px"
            >
               <el-option
                  v-for="dict in sys_common_status"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
               />
            </el-select>
         </el-form-item>
         <el-form-item label="登录时间" style="width: 308px">
            <el-date-picker
               v-model="dateRange"
               value-format="YYYY-MM-DD HH:mm:ss"
               type="daterange"
               range-separator="-"
               start-placeholder="开始日期"
               end-placeholder="结束日期"
               :default-time="[new Date(2000, 1, 1, 0, 0, 0), new Date(2000, 1, 1, 23, 59, 59)]"
            ></el-date-picker>
         </el-form-item>
         <el-form-item>
            <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
         </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb8">
         <el-col :span="1.5">
            <el-button
               type="danger"
               plain
               icon="Delete"
               :disabled="multiple"
               @click="handleDelete"
               v-hasPermi="['monitor:logininfor:remove']"
            >删除</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="warning"
               plain
               icon="Delete"
               @click="handleClean"
               v-hasPermi="['monitor:logininfor:remove']"
            >清空</el-button>
         </el-col>
             <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table
         ref="logininforRef"
         v-loading="loading"
         :data="logininforList"
         @selection-change="handleSelectionChange"
         :default-sort="defaultSort"
         @sort-change="handleSortChange"
      >
         <el-table-column type="selection" width="55" align="center" />
         <el-table-column
            label="用户名称"
            align="center"
            prop="userName"
            :show-overflow-tooltip="true"
            sortable="custom"
            :sort-orders="['descending', 'ascending']"
            min-width="120"
         />
         <el-table-column label="IP地址" align="center" prop="ipaddr" :show-overflow-tooltip="true" width="130" />
         <el-table-column label="登录地点" align="center" prop="loginLocation" :show-overflow-tooltip="true" min-width="140" />
         <el-table-column label="操作系统" align="center" prop="os" :show-overflow-tooltip="true" width="120" />
         <el-table-column label="浏览器" align="center" prop="browser" :show-overflow-tooltip="true" width="100" />
         <el-table-column label="登录状态" align="center" prop="status" width="100">
            <template #default="scope">
               <dict-tag :options="sys_common_status" :value="scope.row.status" />
            </template>
         </el-table-column>
         <el-table-column label="描述" align="center" prop="msg" :show-overflow-tooltip="true" min-width="150" />
         <el-table-column
            label="访问时间"
            align="center"
            prop="loginTime"
            sortable="custom"
            :sort-orders="['descending', 'ascending']"
            width="180"
         >
            <template #default="scope">
               <span>{{ parseTime(scope.row.loginTime) }}</span>
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
   </div>
</template>

<style lang="scss" scoped>
.app-container {
  :deep(.el-form--inline .el-form-item) {
    margin-right: 12px;
  }
}
</style>

<script setup name="Logininfor">
import { list, delLogininfor, cleanLogininfor } from "@/api/monitor/log/logininfor"

// 响应式数据状态
const logininforList = ref([])
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const multiple = ref(true)
const total = ref(0)
const dateRange = ref([])
const defaultSort = ref({ prop: "loginTime", order: "descending" })

// 查询参数对象
const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  ipaddr: undefined,
  userName: undefined,
  status: undefined,
  orderByColumn: undefined,
  isAsc: undefined
})

// 组件依赖
const { proxy } = getCurrentInstance()
const { sys_common_status } = proxy.useDict("sys_common_status")

// 监听排序变化，自动刷新列表
watch(defaultSort, (newSort) => {
  if (newSort.prop && newSort.order) {
    queryParams.value.orderByColumn = newSort.prop
    queryParams.value.isAsc = newSort.order === 'ascending' ? 'asc' : 'desc'
    getList()
  }
}, { deep: true })

/** 查询登录日志列表 */
async function getList() {
  try {
    loading.value = true
    const params = proxy.addDateRange(queryParams.value, dateRange.value)
    const response = await list(params)

    logininforList.value = response.data?.rows || []
    total.value = response.data?.total || 0
  } catch (error) {
    console.error('获取登录日志列表失败:', error)
    proxy.$modal.msgError('获取数据失败')
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  dateRange.value = []
  proxy.resetForm("queryRef")
  queryParams.value.pageNum = 1
  nextTick(() => {
    proxy.$refs["logininforRef"]?.sort(defaultSort.value.prop, defaultSort.value.order)
  })
}

/** 表格多选框选中数据事件 */
function handleSelectionChange(selection) {
  if (!selection?.length) {
    ids.value = []
    multiple.value = true
    return
  }

  // 过滤掉无效的 id（null、undefined、空字符串）
  ids.value = selection
    .map(item => item.id)
    .filter(id => id != null && id !== '')

  multiple.value = false
}

/** 表格排序触发事件 */
function handleSortChange(column) {
  if (column.prop) {
    queryParams.value.orderByColumn = column.prop
    queryParams.value.isAsc = column.order === 'ascending' ? 'asc' : 'desc'
    getList()
  }
}

/** 删除按钮操作 */
async function handleDelete(row) {
  try {
    // 获取要删除的ID，支持单个删除和批量删除
    const targetIds = row?.id || ids.value

    // 验证是否有选中的数据
    if (!targetIds || (Array.isArray(targetIds) && targetIds.length === 0)) {
      proxy.$modal.msgWarning("请选择要删除的数据")
      return
    }

    // 构建确认提示信息
    const idText = Array.isArray(targetIds) ? targetIds.join(', ') : targetIds
    const confirmText = `是否确认删除访问编号为"${idText}"的数据项？`

    await proxy.$modal.confirm(confirmText)
    await delLogininfor(targetIds)

    proxy.$modal.msgSuccess("删除成功")
    await getList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除登录日志失败:', error)
      proxy.$modal.msgError('删除失败')
    }
  }
}



/** 清空按钮操作 */
async function handleClean() {
  try {
    await proxy.$modal.confirm("是否确认清空所有登录日志数据项?")
    await cleanLogininfor()
    proxy.$modal.msgSuccess("清空成功")
    await getList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('清空失败:', error)
      proxy.$modal.msgError('清空失败')
    }
  }
}


// 组件初始化：加载数据
onMounted(() => {
  getList()
})
</script>
