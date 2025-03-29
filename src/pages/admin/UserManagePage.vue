<template>
  <div id="userManagePage">
    <!--    搜索框-->
    <a-form layout="inline" :model="searchParams" @finish="doSearch" style="margin-bottom: 20px">
      <a-form-item label="账号">
        <a-input v-model:value="searchParams.userAccount" placeholder="输入账号" allow-clear />
      </a-form-item>
      <a-form-item label="用户名">
        <a-input v-model:value="searchParams.userName" placeholder="输入用户名" allow-clear />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit">搜索</a-button>
      </a-form-item>
    </a-form>

    <!--    表格-->
    <a-table
      :columns="columns"
      :data-source="dataList"
      :pagination="pagination"
      @change="doTableChange"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'userAvatar'">
          <a-image :src="record.userAvatar" :width="80" :height="80" style="border-radius: 50%" />
        </template>
        <template v-else-if="column.dataIndex === 'userRole'">
          <div v-if="record.userRole === 'admin'">
            <a-tag color="green">管理员</a-tag>
          </div>
          <div v-else-if="record.userRole === 'user'">
            <a-tag color="blue">普通用户</a-tag>
          </div>
        </template>
        <template v-if="column.dataIndex === 'createTime'">
          {{ dayjs(record.createTime).format('YYYY-MM-DD HH:mm:ss') }}
        </template>
        <template v-if="column.dataIndex === 'updateTime'">
          {{ dayjs(record.updateTime).format('YYYY-MM-DD HH:mm:ss') }}
        </template>
        <template v-else-if="column.key === 'action'">
          <a-popconfirm
            title="确定删除吗？"
            ok-text="确定"
            cancel-text="取消"
            @confirm="() => doDelete(record.id)"
          >
            <a-button danger> 删除</a-button>
          </a-popconfirm>
          <a-button style="margin-left: 10px" @click="showEditModal(record)">编辑</a-button>
        </template>
      </template>
    </a-table>

    <!-- 编辑用户信息的模态框 -->
    <a-modal
      v-model:open="editModalOpen"
      title="编辑用户信息"
      centered
      :confirm-loading="confirmLoading"
      @ok="handleEditOk"
    >
      <a-form
        :model="editForm"
        layout="horizontal"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 18 }"
        style="margin-top: 40px"
      >
        <a-form-item label="用户名">
          <a-input v-model:value="editForm.userName" />
        </a-form-item>
        <a-form-item label="简介">
          <a-textarea v-model:value="editForm.userProfile" />
        </a-form-item>
        <a-form-item label="头像链接">
          <a-input v-model:value="editForm.userAvatar" />
        </a-form-item>
        <a-form-item label="用户角色">
          <a-select v-model:value="editForm.userRole">
            <a-select-option value="admin">管理员</a-select-option>
            <a-select-option value="user">普通用户</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>
<script lang="ts" setup>
import { computed, onMounted, reactive, ref } from 'vue'
import {
  deleteUserUsingPost,
  listUserVoByPageUsingPost,
  updateUserUsingPost,
} from '@/api/userController.ts'
import { message } from 'ant-design-vue'
import dayjs from 'dayjs'
import { isEqual, cloneDeep } from 'lodash-es'

const columns = [
  {
    title: 'id',
    dataIndex: 'id',
    sorter: true,
  },
  {
    title: '账号',
    dataIndex: 'userAccount',
  },
  {
    title: '用户名',
    dataIndex: 'userName',
  },
  {
    title: '头像',
    dataIndex: 'userAvatar',
  },
  {
    title: '简介',
    dataIndex: 'userProfile',
  },
  {
    title: '用户角色',
    dataIndex: 'userRole',
    sorter: true,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    sorter: true,
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
    sorter: true,
  },
  {
    title: '操作',
    key: 'action',
  },
]

//定义数据
const dataList = ref<API.UserVO[]>([])
const total = ref<number>(0)
//搜索条件
const searchParams = reactive<API.UserQueryRequest>({
  current: 1,
  pageSize: 5,
  sortField: 'createTime',
  sortOrder: 'ascend',
})

//获取数据
const fetchData = async () => {
  const res = await listUserVoByPageUsingPost({
    ...searchParams,
  })
  if (res.data.code === 0 && res.data.data) {
    dataList.value = res.data.data.records ?? []
    total.value = res.data.data.total ?? 0
  } else {
    message.error('获取数据失败' + res.data.message)
  }
}

onMounted(() => {
  fetchData()
})

//分页参数
const pagination = computed(() => {
  return {
    current: searchParams.current,
    pageSize: searchParams.pageSize,
    total: total.value,
    showSizeChanger: true,
    showTotal: (total: number) => `总共 ${total} 条`,
  }
})
//表格变化事件
const doTableChange = (pagination: any, filters: any, sorter: any) => {
  searchParams.current = pagination.current
  searchParams.pageSize = pagination.pageSize
  searchParams.sortField = sorter.field
  searchParams.sortOrder = sorter.order === 'ascend' ? 'ascend' : 'descend'
  //重新获取数据
  fetchData()
}
//搜索事件
const doSearch = () => {
  //重置页码
  searchParams.current = 1
  fetchData()
}

//删除用户
const doDelete = async (id: number) => {
  if (!id) {
    return
  }
  const res = await deleteUserUsingPost({
    id,
  })
  if (res.data.code === 0) {
    message.success('删除成功')
    //刷新数据
    fetchData()
  } else {
    message.error('删除失败')
  }
}

// 编辑用户信息
const editModalOpen = ref(false)
const confirmLoading = ref(false)
// 编辑表单
const editForm = reactive<API.UserUpdateRequest>({})
const originalForm = reactive<API.UserVO>({})
const showEditModal = (record: API.UserUpdateRequest) => {
  Object.assign(editForm, record)
  Object.assign(originalForm, cloneDeep(record))
  editModalOpen.value = true
}

const handleEditOk = async () => {
  if (isEqual(editForm, originalForm)) {
    message.info('没有任何改动')
    editModalOpen.value = false
    return
  }
  confirmLoading.value = true
  const res = await updateUserUsingPost(editForm)
  if (res.data.code === 0) {
    message.success('更新成功')
    await fetchData()
    editModalOpen.value = false
  } else {
    message.error('更新失败' + res.data.message)
  }
  confirmLoading.value = false
}
</script>
