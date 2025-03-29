<template>
  <div id="UserLoginPage">
    <h2 class="title">用户登录</h2>
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
        <div class="tips">
          没有账号？
          <RouterLink to="/user/register"> 立即注册</RouterLink>
        </div>

        <a-form-item>
          <a-button type="primary" html-type="submit" style="width: 100%">登录</a-button>
        </a-form-item>
      </a-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { userLoginUsingPost } from '@/api/userController.ts'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'
import { message } from 'ant-design-vue'
import router from '@/router'

const formState = reactive<API.UserLoginRequest>({
  userAccount: '',
  userPassword: '',
})
const loginUserStore = useLoginUserStore()
/**
 * 登录表单提交事件
 * @param values 表单数据
 */
const handleSubmit = async (values: API.UserLoginRequest) => {
  try {
    const res = await userLoginUsingPost(values)
    if (res.data.code === 0 && res.data.data) {
      await loginUserStore.fetchLoginUser()
      message.success('登录成功')
      router.push({
        path: '/',
        replace: true,
      })
    } else {
      message.error('登录失败 ' + res.data.message)
    }
  } catch (e) {
    message.error('登录失败' + e)
  }
}
</script>

<style scoped>
#UserLoginPage {
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
