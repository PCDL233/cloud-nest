<template>
  <div id="UserRegisterPage">
    <h2 class="title">用户注册</h2>
    <div class="desc">智绘云巢</div>
    <div class="loginForm">
      <a-form :model="formState" autocomplete="off" @finish="handleSubmit">
        <a-form-item name="userAccount" :rules="[{ required: true, message: '请输入账号' }]">
          <a-input v-model:value="formState.userAccount" placeholder="请输入账号" />
        </a-form-item>

        <a-form-item
          name="userPassword"
          :rules="[
            { required: true, message: '请输入密码' },
            {
              min: 8,
              message: '密码长度不能小于8位',
            },
          ]"
        >
          <a-input-password v-model:value="formState.userPassword" placeholder="请输入密码" />
        </a-form-item>
        <a-form-item
          name="checkPassword"
          :rules="[
            { required: true, message: '请输入密码' },
            {
              min: 8,
              message: '确认密码长度不能小于8位',
            },
          ]"
        >
          <a-input-password v-model:value="formState.checkPassword" placeholder="请确认密码" />
        </a-form-item>
        <div class="tips">
          已有账号？
          <RouterLink to="/user/login"> 立即登录</RouterLink>
        </div>

        <a-form-item>
          <a-button type="primary" html-type="submit" style="width: 100%">注册</a-button>
        </a-form-item>
      </a-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { userRegisterUsingPost } from '@/api/userController.ts'
import { message } from 'ant-design-vue'
import router from '@/router'

const formState = reactive<API.UserRegisterRequest>({
  userAccount: '',
  userPassword: '',
  checkPassword: '',
})
/**
 * 注册表单提交事件
 * @param values 表单数据
 */
const handleSubmit = async (values: API.UserRegisterRequest) => {
  try {
    //校验密码是否一致
    if (values.userPassword !== values.checkPassword) {
      message.error('两次输入的密码不一致')
      return
    }
    const res = await userRegisterUsingPost(values)
    if (res.data.code === 0 && res.data.data) {
      message.success('注册成功')
      router.push({
        path: '/user/login',
        replace: true,
      })
    } else {
      message.error('注册失败 ' + res.data.message)
    }
  } catch (e) {
    message.error('注册失败' + e)
  }
}
</script>

<style scoped>
#UserRegisterPage {
  max-width: 360px;
  margin: 0 auto;
}

.title {
  text-align: center;
  margin-bottom: 20px;
}

.desc {
  color: #666666;
  text-align: center;
  margin-bottom: 16px;
}

.tips {
  text-align: right;
  font-size: 13px;
  color: #bbb;
  margin-bottom: 16px;
}
</style>
