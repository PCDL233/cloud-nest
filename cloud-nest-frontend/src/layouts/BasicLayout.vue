<template>
  <div id="basicLayout">
    <a-config-provider :locale="locale === 'en' ? enUS : zhCN">
      <a-layout style="min-height: 100vh">
        <!--顶部-->
        <a-layout-header class="header">
          <GlobalHeader :localeFather="locale" :localeChangeFunc="updateLocale" />
        </a-layout-header>
        <a-layout>
          <GlobalSider class="sider" />
          <!--主体内容-->
          <a-layout-content class="content">
            <router-view />
          </a-layout-content>
        </a-layout>
        <!--底部-->
        <a-layout-footer class="footer">
          <a href="https://gitee.com/pcdl233" target="_blank" rel="noopener noreferrer">
            by PCDL233
          </a>
        </a-layout-footer>
      </a-layout>
    </a-config-provider>
  </div>
</template>

<script setup lang="ts">
import GlobalHeader from '@/components/GlobalHeader.vue'
import enUS from 'ant-design-vue/es/locale/en_US'
import zhCN from 'ant-design-vue/es/locale/zh_CN'
import { ref } from 'vue'
import dayjs from 'dayjs'
import 'dayjs/locale/zh-cn'
import GlobalSider from '@/components/GlobalSider.vue'

const savedLocale = localStorage.getItem('locale') || 'zh'
dayjs.locale(savedLocale)
const locale = ref(savedLocale)

//国际化
const updateLocale = (newLocale: string) => {
  locale.value = newLocale
  dayjs.locale(newLocale)
  localStorage.setItem('locale', newLocale)
}
</script>

<style scoped>
#basicLayout .header {
  background: white;
  color: unset;
  margin-bottom: 2px;
  padding-inline: 20px;
}

#basicLayout .sider {
  background: #ffffff;
  border-right: 0.5px solid #eee;
  padding-top: 20px;
}

#basicLayout :deep(.ant-menu-root) {
  border-bottom: none !important;
  border-inline-end: none !important;
}

#basicLayout .content {
  padding: 28px;
  background: linear-gradient(to right, #efefef, #fff);
  margin-bottom: 30px;
}

#basicLayout .footer {
  background: #efefef;
  padding: 16px;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  text-align: center;
}
</style>
