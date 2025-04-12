<template>
  <div id="globalSider">
    <a-layout-sider v-if="loginUserStore.loginUser.id" breakpoint="lg" width="200">
      <a-menu
        mode="inline"
        :default-selected-keys="current"
        :items="menuItems"
        @click="doMenuClick"
      />
    </a-layout-sider>
  </div>
</template>
<script lang="ts" setup>
import { h, ref } from 'vue'
import { PictureOutlined, UserOutlined } from '@ant-design/icons-vue'
import { useRouter } from 'vue-router'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'

//获取登录用户信息
const loginUserStore = useLoginUserStore()

// 菜单列表
const menuItems = [
  {
    key: '/',
    icon: () => h(PictureOutlined),
    label: '公共空间',
  },
  {
    key: '/my_space',
    icon: () => h(UserOutlined),
    label: '我的空间',
  },
]

const router = useRouter()
//当前路由:高亮菜单项
const current = ref<string[]>([])
router.afterEach((to, _from, _next) => {
  current.value = [to.path]
})

//路由跳转事件
const doMenuClick = ({ key }: { key: string }) => {
  router.push({
    path: key,
  })
}
</script>

<style scoped>
#globalSider .ant-layout-sider {
  background: none;
}
</style>
