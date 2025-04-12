<template>
  <div id="mySpacePage">
    <a-spin tip="Loading..."></a-spin>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'
import { listSpaceVoByPageUsingPost } from '@/api/spaceController.ts'
import { message } from 'ant-design-vue'
import { onMounted } from 'vue'

const router = useRouter()
const loginUserStore = useLoginUserStore()

const checkUserSpace = async () => {
  const loginUser = loginUserStore.loginUser
  //未登录
  if (!loginUser) {
    await router.replace('/user/login')
    return
  }
  //已登录
  const res = await listSpaceVoByPageUsingPost({
    userId: loginUser.id,
    current: 1,
    pageSize: 1,
  })
  if (res.data.code === 0 && res.data.data) {
    const records = res.data.data.records ?? []
    //如果有空间，跳转到第一个空间
    if (records.length > 0) {
      const space = records[0]
      await router.replace(`/space/${space.id}`)
    } else {
      //如果没有空间，跳转到创建空间
      await router.replace('/add_space')
      message.warning('请先创建空间')
    }
  } else {
    message.error('加载我的空间失败: ' + (res.data.message ?? '未知错误'))
  }
}

onMounted(() => {
  checkUserSpace()
})
</script>

<style scoped>
#mySpacePage {
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
