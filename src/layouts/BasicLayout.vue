<template>
  <div id="basicLayout">
    <a-config-provider :locale="locale === 'en' ? enUS : zhCN">
      <a-layout style="min-height: 100vh">
        <a-layout-header class="header">
          <GlobalHeader :local="locale" @update:locale="updateLocale" />
        </a-layout-header>
        <a-layout-content class="content">
          <a-space wrap>
            <a-select show-search style="width: 200px">
              <a-select-option value="jack">jack</a-select-option>
              <a-select-option value="lucy">lucy</a-select-option>
            </a-select>
            <a-date-picker />
            <a-time-picker />
            <a-range-picker style="width: 200px" />
          </a-space>
          <!--主体内容-->
          <router-view />
        </a-layout-content>
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

dayjs.locale('en')
const locale = ref('en')

//国际化
const updateLocale = (newLocale: string) => {
  locale.value = newLocale
  dayjs.locale(newLocale)
}
</script>

<style scoped>
#basicLayout .header {
  background: white;
  color: unset;
  margin-bottom: 15px;
  padding-inline: 20px;
}

#basicLayout .content {
  padding: 20px;
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
