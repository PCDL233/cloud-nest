<template>
  <div id="UserInfoCard">
    <a-card class="custom-card" :title="editMode ? '编辑信息' : '个人信息'" :bordered="false">
      <template #extra>
        <a-button class="edit-btn" type="link" @click="editMode = !editMode">
          {{ editMode ? '取消' : '编辑' }}
        </a-button>
      </template>
      <a-form
        :model="userInfo"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 18 }"
        layout="horizontal"
        :disabled="!editMode"
        class="custom-form"
      >
        <a-form-item label="用户名">
          <a-input v-model:value="userInfo.userName" />
        </a-form-item>
        <a-form-item label="账号">
          <a-input :value="userInfo.userAccount" disabled />
        </a-form-item>
        <a-form-item label="头像">
          <div class="avatar-container">
            <a-avatar :src="userInfo.userAvatar" size="large" />
            <a-input v-model:value="userInfo.userAvatar" class="avatar-input" />
          </div>
        </a-form-item>
        <a-form-item label="简介">
          <a-textarea
            v-model:value="userInfo.userProfile"
            :auto-size="{ minRows: 2, maxRows: 4 }"
            placeholder="请输入简介"
          />
        </a-form-item>
        <a-form-item label="用户角色">
          <a-select v-model:value="userInfo.userRole">
            <a-select-option value="admin">管理员</a-select-option>
            <a-select-option value="user">普通用户</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="创建时间">
          <a-input :value="dayjs(userInfo.createTime).format('YYYY-MM-DD HH:mm:ss')" disabled />
        </a-form-item>
        <a-form-item label="更新时间">
          <a-input :value="dayjs(userInfo.updateTime).format('YYYY-MM-DD HH:mm:ss')" disabled />
        </a-form-item>
        <a-form-item v-if="editMode" class="action-buttons">
          <a-button type="primary" @click="handleSubmit" style="margin-right: 10px">
            提交
          </a-button>
          <a-button @click="cancelEdit">取消</a-button>
        </a-form-item>
      </a-form>
    </a-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive } from 'vue'
import { message } from 'ant-design-vue'
import { updateUserUsingPost } from '@/api/userController.ts'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'
import dayjs from 'dayjs'

const editMode = ref(false)
//获取登录用户信息
const loginUserStore = useLoginUserStore()
const userInfo = loginUserStore.loginUser

const originalUserInfo = reactive<API.LoginUserVO>({ ...userInfo })
const handleSubmit = async () => {
  try {
    const res = await updateUserUsingPost(userInfo)
    if (res.data.code === 0) {
      message.success('修改成功')
      Object.assign(originalUserInfo, userInfo)
      editMode.value = false
    } else {
      message.error('修改失败: ' + res.data.message)
    }
  } catch (error) {
    message.error('更新失败')
  }
}

const cancelEdit = () => {
  Object.assign(userInfo, originalUserInfo)
  editMode.value = false
}
</script>

<style scoped>
#UserInfoCard {
  max-width: 720px;
  margin: 20px auto;
  padding: 24px;
  border-radius: 8px;
}

.edit-btn {
  padding: 0;
  margin: 0 12px;
  font-size: 16px;
}

.custom-form {
  padding: 24px;
  background: #fff;
  border-radius: 8px;
}

.avatar-container {
  display: flex;
  align-items: center;
  gap: 16px;
}

.avatar-input {
  width: 100%;
}

.action-buttons {
  text-align: right;
}

.ant-form-item-label > label {
  color: #1890ff;
}
</style>
