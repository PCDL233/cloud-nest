<template>
  <div id="addPicture">
    <h2 style="margin-bottom: 16px">
      {{ route.query.id ? '修改图片' : '创建图片' }}
    </h2>
    <a-typography-paragraph v-if="spaceId">
      <a-tag color="blue" style="margin-right: 8px">
        <a :href="`/space/${spaceId}`"> 空间ID：{{ spaceId }} </a>
      </a-tag>
    </a-typography-paragraph>
    <!--    选择上传方式-->
    <a-tabs v-model:activeKey="uploadType">
      <a-tab-pane key="file" tab="文件上传">
        <!--    图片文件上传组件-->
        <PictureUpload
          :picture="picture"
          :spaceId="spaceId"
          :on-success="onSuccess"
          class="tab-content"
        />
      </a-tab-pane>
      <a-tab-pane key="url" tab="URL上传" force-render>
        <!--    URL图片上传组件-->
        <UrlPictureUpload
          :picture="picture"
          :spaceId="spaceId"
          :on-success="onSuccess"
          class="tab-content"
        />
      </a-tab-pane>
    </a-tabs>
    <!--    图片信息表单-->
    <a-form v-if="picture" layout="vertical" :model="pictureForm" @finish="handleSubmit">
      <a-form-item label="名称" name="name">
        <a-input v-model:value="pictureForm.name" placeholder="请输入名称" />
      </a-form-item>
      <a-form-item label="简介" name="introduction">
        <a-textarea
          v-model:value="pictureForm.introduction"
          placeholder="请输入简介"
          :auto-size="{ minRows: 2, maxRows: 5 }"
          allowClear
        />
      </a-form-item>
      <a-form-item label="分类" name="category">
        <a-auto-complete
          v-model:value="pictureForm.category"
          placeholder="请输入分类"
          :options="categoryOptions"
          allowClear
        />
      </a-form-item>
      <a-form-item label="标签" name="tags">
        <a-select
          v-model:value="pictureForm.tags"
          mode="tags"
          placeholder="请输入标签"
          :options="tagOptions"
          allowClear
        />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 100%">创建</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import PictureUpload from '@/components/PictureUpload.vue'
import { computed, onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  editPictureUsingPost,
  getPictureVoByIdUsingGet,
  listPictureTagCategoryUsingGet,
} from '@/api/pictureController.ts'
import { message } from 'ant-design-vue'
import UrlPictureUpload from '@/components/UrlPictureUpload.vue'

const router = useRouter()
const route = useRoute()
const uploadType = ref<'file' | 'url'>('file')
const picture = ref<API.PictureVO>()
//空间id
const spaceId = computed(() => {
  return route.query?.spaceId
})

/**
 * @description: 图片上传成功回调
 * @param newPicture
 */
const onSuccess = (newPicture: API.PictureVO) => {
  picture.value = newPicture
  pictureForm.name = newPicture.name
}

// 图片信息表单
const pictureForm = reactive<API.PictureEditRequest>({})

/**
 * 提交表单
 * @param values
 */
const handleSubmit = async (values: any) => {
  const pictureId = picture.value?.id
  if (!pictureId) {
    return
  }
  const res = await editPictureUsingPost({
    id: pictureId,
    spaceId: spaceId.value,
    ...values,
  })
  if (res.data.code === 0 && res.data.data) {
    message.success('创建成功')
    // 跳转到图片详情页
    router.push({
      path: `/picture/${pictureId}`,
    })
  } else {
    message.error('创建失败，' + res.data.message)
  }
}

const categoryOptions = ref<String[]>([])
const tagOptions = ref<String[]>([])
/**
 * 获取分类和标签列表
 */
const getTagCategoryOptions = async () => {
  const res = await listPictureTagCategoryUsingGet()
  if (res.data.code === 0 && res.data.data) {
    tagOptions.value = (res.data.data.tagList ?? []).map((data: String) => {
      return {
        value: data,
        label: data,
      }
    })
    categoryOptions.value = (res.data.data.categoryList ?? []).map((data: String) => {
      return {
        value: data,
        label: data,
      }
    })
  } else {
    message.error('获取失败，' + res.data.message)
  }
}

//获取旧数据
const getOldPicture = async () => {
  const id = route.query.id
  if (id) {
    const res = await getPictureVoByIdUsingGet({
      id,
    })
    if (res.data.code === 0 && res.data.data) {
      const data = res.data.data
      picture.value = data
      pictureForm.name = data.name
      pictureForm.introduction = data.introduction
      pictureForm.category = data.category
      pictureForm.tags = data.tags
    }
  }
}

onMounted(() => {
  getTagCategoryOptions()
  getOldPicture()
})
</script>

<style scoped>
#addPicture {
  max-width: 720px;
  margin: 0 auto;
}

.tab-content {
  margin-top: 20px;
}
</style>
