<template>
  <div id="addSpace">
    <h2 style="margin-bottom: 16px">
      {{ route.query.id ? '修改空间' : '创建空间' }}
    </h2>
    <!--    空间信息表单-->
    <a-form layout="vertical" :model="spaceForm" @finish="handleSubmit">
      <a-form-item label="空间名称" name="spaceName">
        <a-input v-model:value="spaceForm.spaceName" placeholder="请输入空间名称" />
      </a-form-item>
      <a-form-item label="空间级别" name="spaceLevel">
        <a-select
          style="min-width: 180px"
          v-model:value="spaceForm.spaceLevel"
          placeholder="请选择空间级别"
          :options="SPACE_LEVEL_OPTIONS"
          allowClear
        />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 100%" :loading="loading"
          >提交
        </a-button>
      </a-form-item>
    </a-form>
    <!--    空间级别介绍-->
    <a-card title="空间级别介绍">
      <a-typography-paragraph>
        * 目前仅支持开通普通空间，如需开通其他空间级别，请联系管理员
        <a href="https://gitee.com/pcdl233" target="_blank" />
      </a-typography-paragraph>
      <a-typography-paragraph v-for="spaceLevel in spaceLevelList">
        {{ spaceLevel.text }}: 大小 {{ spaceLevel.maxSize }}，条数 {{ spaceLevel.maxCount }}
      </a-typography-paragraph>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  addSpaceUsingPost,
  getSpaceVoByIdUsingGet,
  listSpaceLevelUsingGet,
  updateSpaceUsingPost,
} from '@/api/spaceController.ts'
import { message } from 'ant-design-vue'
import { SPACE_LEVEL_OPTIONS } from '@/constants/space.ts'

const route = useRoute()
const router = useRouter()

const space = ref<API.SpaceVO>()
// 空间信息表单
const spaceForm = reactive<API.SpaceAddRequest | API.SpaceUpdateRequest>({})
const spaceLevelList = ref<API.SpaceLevel[]>([])
const loading = ref<boolean>(false)

/**
 * 获取空间级别列表
 */
const fetchSpaceLevelList = async () => {
  const res = await listSpaceLevelUsingGet()
  if (res.data.code === 0 && res.data.data) {
    spaceLevelList.value = res.data.data
  } else {
    message.error('获取空间级别失败，' + res.data.message)
  }
}

/**
 * 提交表单
 * @param values
 */
const handleSubmit = async () => {
  const spaceID = space.value?.id
  loading.value = true
  let res
  //修改操作
  if (spaceID) {
    res = await updateSpaceUsingPost({
      id: spaceID,
      ...spaceForm,
    })
  } else {
    //创建操作
    res = await addSpaceUsingPost({
      ...spaceForm,
    })
  }

  if (res.data.code === 0 && res.data.data) {
    message.success('操作成功')
    // 跳转到空间详情页
    router.push({
      path: `/space/${res.data.data}`,
    })
  } else {
    message.error('操作失败，' + res.data.message)
  }
  loading.value = false
}

//获取旧数据
const getOldSpace = async () => {
  const id = route.query?.id
  if (id) {
    const res = await getSpaceVoByIdUsingGet({
      id,
    })
    if (res.data.code === 0 && res.data.data) {
      const data = res.data.data
      space.value = data
      //填充表单数据
      spaceForm.spaceName = data.spaceName
      spaceForm.spaceLevel = data.spaceLevel
    }
  }
}

onMounted(() => {
  fetchSpaceLevelList()
  getOldSpace()
})
</script>

<style scoped>
#addSpace {
  max-width: 720px;
  margin: 0 auto;
}
</style>
