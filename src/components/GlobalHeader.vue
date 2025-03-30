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
      <!-- 国际化 -->
      <a-col flex="200px">
        <a-radio-group v-model:value="locale" @change="localeChangeFunc(locale)">
          <a-radio-button key="en" :value="'en'">English</a-radio-button>
          <a-radio-button key="cn" :value="'zh'">中文</a-radio-button>
        </a-radio-group>
      </a-col>

      <!--      用户信息展示-->
      <a-col flex="120px">
        <div class="user-login-status">
          <div v-if="loginUserStore.loginUser.id">
            <a-dropdown>
              <a-space>
                <a-avatar :src="loginUserStore.loginUser.userAvatar" size="large"></a-avatar>
                {{ loginUserStore.loginUser.userName ?? 'null' }}
              </a-space>
              <template #overlay>
                <a-menu>
                  <a-menu-item @click="router.push('/user/info')">
                    <UserOutlined />
                    个人信息
                  </a-menu-item>
                  <a-menu-item @click="doAbout">
                    <InfoCircleOutlined />
                    关于作者
                  </a-menu-item>
                  <a-menu-item @click="doLogout">
                    <LogoutOutlined />
                    退出登录
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
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
import { computed, h, ref } from 'vue'
import {
  HomeOutlined,
  LogoutOutlined,
  InfoCircleOutlined,
  UserOutlined,
} from '@ant-design/icons-vue'
import { type MenuProps, message } from 'ant-design-vue'
import { useRouter } from 'vue-router'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'
import { userLogoutUsingPost } from '@/api/userController.ts'
import ACCESS_ENUM from '@/access/accessEnum.ts'

//获取登录用户信息
const loginUserStore = useLoginUserStore()

// 菜单列表
const originItems = [
  {
    key: '/',
    icon: () => h(HomeOutlined),
    label: '主页',
    title: '主页',
  },
  {
    key: '/admin/userManage',
    label: '用户管理',
    title: '用户管理',
  },
  /*  {
      key: 'others',
      label: h('a', { href: 'https://gitee.com/PCDL233', target: '_blank' }, '关于作者'),
      title: '关于作者',
    },*/
  {
    key: '/about',
    label: '关于',
    title: '关于',
  },
]

// 过滤菜单项
const filterMenus = (menus = [] as MenuProps['items']) => {
  return menus?.filter((menu) => {
    if (menu.key.startsWith('/admin')) {
      const loginUser = loginUserStore.loginUser
      if (!loginUser || loginUser.userRole !== 'admin') {
        return false
      }
    }
    return true
  })
}

// 展示在菜单的路由数组
const items = computed<MenuProps['items']>(() => filterMenus(originItems))

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

//退出登录
const doLogout = async () => {
  const res = await userLogoutUsingPost()
  if (res.data.code === 0) {
    loginUserStore.setLoginUser({
      userName: ACCESS_ENUM.NOT_LOGIN,
    })
    message.success('退出登录成功')
    router.push('/user/login')
  } else {
    message.success('退出登录失败' + res.data.message)
  }
}
//关于
const doAbout = () => {
  window.open('https://gitee.com/PCDL233', '_blank')
}
//国际化
const { localeChangeFunc, localeFather } = defineProps(['localeChangeFunc', 'localeFather'])
const locale = ref(localeFather)
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
