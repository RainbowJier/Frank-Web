<template>
   <div class="app-container">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
         <el-form-item label="字典名称" prop="dictType">
            <el-select v-model="queryParams.dictType" style="width: 200px">
               <el-option
                  v-for="item in typeOptions"
                  :key="item.dictId"
                  :label="item.dictName"
                  :value="item.dictType"
               />
            </el-select>
         </el-form-item>
         <el-form-item label="字典标签" prop="dictLabel">
            <el-input
               v-model="queryParams.dictLabel"
               placeholder="请输入字典标签"
               clearable
               style="width: 200px"
               @keyup.enter="handleQuery"
            />
         </el-form-item>
         <el-form-item label="状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="数据状态" clearable style="width: 200px">
               <el-option
                  v-for="dict in sys_normal_disable"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
               />
            </el-select>
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
               @click="handleAdd"
               v-hasPermi="['system:dict:add']"
            >新增</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="success"
               plain
               icon="Edit"
               :disabled="single"
               @click="handleUpdate"
               v-hasPermi="['system:dict:edit']"
            >修改</el-button>
         </el-col>
         <el-col :span="1.5">
            <el-button
               type="danger"
               plain
               icon="Delete"
               :disabled="multiple"
               @click="handleDelete"
               v-hasPermi="['system:dict:remove']"
            >删除</el-button>
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

      <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
         <el-table-column type="selection" width="55" align="center" />
         <el-table-column label="字典编码" align="center" prop="dictCode" />
         <el-table-column label="字典标签" align="center" prop="dictLabel">
            <template #default="scope">
               <span v-if="(scope.row.listClass == '' || scope.row.listClass == 'default') && (scope.row.cssClass == '' || scope.row.cssClass == null)">{{ scope.row.dictLabel }}</span>
               <el-tag v-else :type="scope.row.listClass == 'primary' ? '' : scope.row.listClass" :class="scope.row.cssClass">{{ scope.row.dictLabel }}</el-tag>
            </template>
         </el-table-column>
         <el-table-column label="字典键值" align="center" prop="dictValue" />
         <el-table-column label="字典排序" align="center" prop="dictSort" />
         <el-table-column label="状态" align="center" prop="status">
            <template #default="scope">
               <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
            </template>
         </el-table-column>
         <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
         <el-table-column label="创建时间" align="center" prop="createTime" width="180">
            <template #default="scope">
               <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
         </el-table-column>
         <el-table-column label="操作" align="center" width="160" class-name="small-padding fixed-width">
            <template #default="scope">
               <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:dict:edit']">修改</el-button>
               <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['system:dict:remove']">删除</el-button>
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

      <!-- 添加或修改参数配置对话框 -->
      <el-dialog :title="title" v-model="open" width="500px" append-to-body>
         <el-form ref="dataRef" :model="form" :rules="rules" label-width="80px">
            <el-form-item label="字典类型">
               <el-input v-model="form.dictType" :disabled="true" />
            </el-form-item>
            <el-form-item label="数据标签" prop="dictLabel">
               <el-input v-model="form.dictLabel" placeholder="请输入数据标签" />
            </el-form-item>
            <el-form-item label="数据键值" prop="dictValue">
               <el-input v-model="form.dictValue" placeholder="请输入数据键值" />
            </el-form-item>
            <el-form-item label="样式属性" prop="cssClass">
               <el-input v-model="form.cssClass" placeholder="请输入样式属性" />
            </el-form-item>
            <el-form-item label="显示排序" prop="dictSort">
               <el-input-number v-model="form.dictSort" controls-position="right" :min="0" />
            </el-form-item>
            <el-form-item label="回显样式" prop="listClass">
               <el-select v-model="form.listClass">
                  <el-option
                     v-for="item in listClassOptions"
                     :key="item.value"
                     :label="item.label + '(' + item.value + ')'"
                     :value="item.value"
                  ></el-option>
               </el-select>
            </el-form-item>
            <el-form-item label="状态" prop="status">
               <el-radio-group v-model="form.status">
                  <el-radio
                     v-for="dict in sys_normal_disable"
                     :key="dict.value"
                     :value="dict.value"
                  >{{ dict.label }}</el-radio>
               </el-radio-group>
            </el-form-item>
            <el-form-item label="备注" prop="remark">
               <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
            </el-form-item>
         </el-form>
         <template #footer>
            <div class="dialog-footer">
               <el-button type="primary" @click="submitForm">确 定</el-button>
               <el-button @click="cancel">取 消</el-button>
            </div>
         </template>
      </el-dialog>
   </div>
</template>

<script setup name="Data">
import { ref, reactive, toRefs, getCurrentInstance, computed, nextTick, onMounted, onUnmounted } from 'vue'
import { useRoute } from 'vue-router'
import useDictStore from '@/store/modules/dict'
import { optionselect as getDictOptionselect, getType } from "@/api/system/dict/type"
import { listData, getData, delData, addData, updateData } from "@/api/system/dict/data"

// 当前实例和路由
const { proxy } = getCurrentInstance()
const { sys_normal_disable } = proxy.useDict("sys_normal_disable")
const route = useRoute()

// 响应式数据
const dataList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const defaultDictType = ref("")
const typeOptions = ref([])

// 常量定义
const LIST_CLASS_OPTIONS = Object.freeze([
  { value: "default", label: "默认" },
  { value: "primary", label: "主要" },
  { value: "success", label: "成功" },
  { value: "info", label: "信息" },
  { value: "warning", label: "警告" },
  { value: "danger", label: "危险" }
])

// 计算属性
const listClassOptions = computed(() => LIST_CLASS_OPTIONS)

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    dictType: undefined,
    dictLabel: undefined,
    status: undefined
  },
  rules: {
    dictLabel: [{ required: true, message: "数据标签不能为空", trigger: "blur" }],
    dictValue: [{ required: true, message: "数据键值不能为空", trigger: "blur" }],
    dictSort: [{ required: true, message: "数据顺序不能为空", trigger: "blur" }]
  }
})

const { queryParams, form, rules } = toRefs(data)

/**
 * 查询字典类型详细
 * @param {string|number} dictId 字典ID
 */
async function getTypes(dictId) {
  try {
    const response = await getType(dictId)
    queryParams.value.dictType = response.data.dictType
    defaultDictType.value = response.data.dictType
    await getList()
  } catch (error) {
    console.error('获取字典类型详情失败:', error)
    proxy.$modal.msgError('获取字典类型详情失败')
  }
}

/** 查询字典类型列表 */
async function getTypeList() {
  try {
    const response = await getDictOptionselect()
    typeOptions.value = response.data || []
  } catch (error) {
    console.error('获取字典类型列表失败:', error)
    typeOptions.value = []
  }
}

/** 查询字典数据列表 */
async function getList() {
  loading.value = true
  try {
    const response = await listData(queryParams.value)
    dataList.value = response.data.rows || []
    total.value = response.data.total || 0
  } catch (error) {
    console.error('获取字典数据列表失败:', error)
    proxy.$modal.msgError('获取数据列表失败')
    dataList.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

/** 取消按钮 */
function cancel() {
  open.value = false
  reset()
}

/** 表单重置 */
function reset() {
  form.value = {
    dictCode: undefined,
    dictLabel: undefined,
    dictValue: undefined,
    cssClass: undefined,
    listClass: "default",
    dictSort: 0,
    status: "1",
    remark: undefined
  }
  nextTick(() => {
    proxy.resetForm("dataRef")
  })
}

/** 搜索按钮操作 */
async function handleQuery() {
  queryParams.value.pageNum = 1
  await getList()
}

/** 返回按钮操作 */
function handleClose() {
  const obj = { path: "/system/dict" }
  proxy.$tab.closeOpenPage(obj)
}

/** 重置按钮操作 */
async function resetQuery() {
  proxy.resetForm("queryRef")
  queryParams.value.dictType = defaultDictType.value
  await handleQuery()
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加字典数据"
  form.value.dictType = queryParams.value.dictType
}

/**
 * 表格多选框选中数据
 * @param {Array} selection 选中的数据项
 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.dictCode)
  single.value = selection.length !== 1
  multiple.value = selection.length === 0
}

/**
 * 修改按钮操作
 * @param {Object} row 行数据
 */
async function handleUpdate(row) {
  reset()
  try {
    const dictCode = row?.dictCode || ids.value
    if (!dictCode) {
      proxy.$modal.msgError('请选择要修改的数据')
      return
    }

    const response = await getData(dictCode)
    form.value = response.data
    // 将数值型的status转换为字符型，以便与sys_normal_disable字典中的字符型值匹配
    if (form.value.status !== undefined) {
      form.value.status = String(form.value.status)
    }
    open.value = true
    title.value = "修改字典数据"
  } catch (error) {
    console.error('获取字典数据详情失败:', error)
    proxy.$modal.msgError('获取数据详情失败')
  }
}

/** 提交表单 */
async function submitForm() {
  try {
    const valid = await proxy.$refs["dataRef"].validate()
    if (!valid) return

    const dictStore = useDictStore()

    if (form.value.dictCode) {
      await updateData(form.value)
      proxy.$modal.msgSuccess("修改成功")
    } else {
      await addData(form.value)
      proxy.$modal.msgSuccess("新增成功")
    }

    open.value = false
    dictStore.removeDict(queryParams.value.dictType)
    await getList()

  } catch (error) {
    console.error('表单提交失败:', error)
    proxy.$modal.msgError('操作失败，请重试')
  }
}

/**
 * 删除按钮操作
 * @param {Object} row 行数据
 */
async function handleDelete(row) {
  try {
    const dictCodes = row?.dictCode || ids.value
    if (!dictCodes) {
      proxy.$modal.msgError('请选择要删除的数据')
      return
    }

    // 将单条数据也转换为数组格式
    const dictCodesArray = Array.isArray(dictCodes) ? dictCodes : [dictCodes]

    await proxy.$modal.confirm(`是否确认删除字典编码为"${dictCodes}"的数据项？`)
    await delData(dictCodesArray)

    proxy.$modal.msgSuccess("删除成功")
    useDictStore().removeDict(queryParams.value.dictType)
    await getList()

  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      proxy.$modal.msgError('删除失败，请重试')
    }
  }
}


// 初始化数据
onMounted(async () => {
  const dictId = route.params?.dictId
  if (dictId) {
    await Promise.all([
      getTypes(dictId),
      getTypeList()
    ])
  } else {
    await getTypeList()
    proxy.$modal.msgWarning('未找到字典类型参数')
  }
})

// 组件销毁时清理数据
onUnmounted(() => {
  dataList.value = []
  typeOptions.value = []
  ids.value = []
})
</script>
