<template>
  <div id="globalHeader">
    <a-row :wrap="false">
      <a-col flex="200px">
        <router-link to="/">
          <div class="title-bar">
            <img class="logo" src="../assets/logo.svg" alt="logo" />
            <div class="title">智绘云巢</div>
          </div>
        </router-link>
      </a-col>
      <a-col flex="auto">
        <a-menu
          v-model:selectedKeys="current"
          mode="horizontal"
          :items="items"
          @click="doMenuClick"
        />
      </a-col>
      <a-col flex="200px">
        <a-radio-group v-model:value="locale" @change="handleLocaleChange">
          <a-radio-button key="en" :value="'en'">English</a-radio-button>
          <a-radio-button key="cn" :value="'zh'">中文</a-radio-button>
        </a-radio-group>
      </a-col>
      <a-col flex="120px">
        <div class="user-login-status">
          <div v-if="loginUserStore.loginUser.id">
            {{ loginUserStore.loginUser.username ?? 'null' }}
          </div>
          <div v-else>
            <a-button type="primary" href="/user/login">登录</a-button>
          </div>
        </div>
      </a-col>
    </a-row>
  </div>
</template>
<script lang="ts" setup>
import { h, ref } from 'vue'
import { HomeOutlined } from '@ant-design/icons-vue'
import type { MenuProps } from 'ant-design-vue'
import { useRouter } from 'vue-router'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'

//登录用户信息
const loginUserStore = useLoginUserStore()
loginUserStore.fetchLoginUser()

const items = ref<MenuProps['items']>([
  {
    key: '/',
    icon: () => h(HomeOutlined),
    label: '主页',
    title: '主页',
  },
  {
    key: '/about',
    label: '关于',
    title: '关于',
  },
  {
    key: 'others',
    label: h('a', { href: 'https://gitee.com/pcdl233', target: '_blank' }, '其它'),
    title: '其它',
  },
])

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

//国际化自定义事件(子传父)
const emit = defineEmits(['update-locale'])
const locale = ref('zh')
const handleLocaleChange = (e: Event) => {
  const newLocale = (e.target as HTMLInputElement).value
  locale.value = newLocale
  emit('update-locale', newLocale)
}
</script>

<style scoped>
#globalHeader .title-bar {
  display: flex;
  align-items: center;
}

.title {
  color: black;
  font-size: 16px;
  margin-left: 16px;
}

.logo {
  height: 30px;
}

.user-login-status {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
</style>
