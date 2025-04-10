<template>
  <div id="pictureDetailPage">
    <a-row :gutter="[16, 16]">
      <!--      图片预览-->
      <a-col :sm="24" :md="16" :xl="18">
        <a-card
          title="图片预览"
          body-style="display: flex; justify-content: center; align-items: center"
        >
          <a-image
            :src="picture.url"
            :alt="picture.name"
            style="max-height: 435px; object-fit: contain"
          />
        </a-card>
      </a-col>
      <!--      图片信息-->
      <a-col :sm="24" :md="8" :xl="6">
        <a-card title="图片信息">
          <a-descriptions :column="1">
            <a-descriptions-item label="作者">
              <a-space>
                <a-avatar size="large" :src="picture.user?.userAvatar" />
                <div style="margin-left: 10px">{{ picture.user?.userName }}</div>
              </a-space>
            </a-descriptions-item>
            <a-descriptions-item label="名称">
              {{ picture.name ?? '未命名' }}
            </a-descriptions-item>
            <a-descriptions-item label="简介">
              {{ picture.introduction ?? '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="分类">
              {{ picture.category ?? '默认' }}
            </a-descriptions-item>
            <a-descriptions-item label="标签">
              <a-tag v-for="tag in picture.tags" :key="tag">
                {{ tag }}
              </a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="格式">
              {{ picture.picFormat ?? '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="宽度">
              {{ picture.picWidth ?? '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="高度">
              {{ picture.picHeight ?? '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="宽高比">
              {{ picture.picScale ?? '-' }}
            </a-descriptions-item>
            <a-descriptions-item label="大小">
              {{ formatSize(picture.picSize) }}
            </a-descriptions-item>
          </a-descriptions>
          <!--          图片操作-->
          <a-space wrap>
            <a-button :icon="h(DownloadOutlined)" type="primary" @click="doDownload">
              下载
            </a-button>
            <a-button v-if="canEdit" :icon="h(EditOutlined)" type="default" @click="doEdit">
              编辑
            </a-button>
            <a-button v-if="canEdit" :icon="h(DeleteOutlined)" danger @click="doDelete">
              删除
            </a-button>
          </a-space>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, h, computed } from 'vue'
import { deletePictureUsingPost, getPictureVoByIdUsingGet } from '@/api/pictureController.ts'
import { message } from 'ant-design-vue'
import { downloadImage, formatSize } from '@/utils'
import { EditOutlined, DeleteOutlined, DownloadOutlined } from '@ant-design/icons-vue'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'
import ACCESS_ENUM from '@/access/accessEnum.ts'
import { useRouter } from 'vue-router'

interface Props {
  id: number
}

const props = defineProps<Props>()
const picture = ref<API.PictureVO>({})
const router = useRouter()

const loginUserStore = useLoginUserStore()
//权限校验
const canEdit = computed(() => {
  const loginUser = loginUserStore.loginUser
  if (!loginUser.id) {
    return false
  }
  const user = picture.value.user || {}
  return loginUser.id === user.id || loginUser.userRole === ACCESS_ENUM.ADMIN
})

//获取图片详情
const fetchPictureDetail = async () => {
  const res = await getPictureVoByIdUsingGet({
    id: props.id,
  })
  if (res.data.code === 0 && res.data.data) {
    picture.value = res.data.data
  } else {
    message.error('获取图片详情失败 ' + res.data.message)
  }
}

const doDelete = async () => {
  const id = picture.value.id
  if (!id) {
    return
  }
  const res = await deletePictureUsingPost({
    id,
  })
  if (res.data.code === 0) {
    message.success('删除成功')
    // 跳转到图片列表页
    setTimeout(() => {
      router.push({
        path: '/',
        replace: true,
      })
    }, 1000)
  } else {
    message.error('删除失败 ' + res.data.message)
  }
}

const doEdit = () => {
  router.push(`/add_picture?id=${picture.value.id}`)
}

// 处理下载
const doDownload = () => {
  downloadImage(picture.value.url)
}

onMounted(() => {
  fetchPictureDetail()
})
</script>

<style scoped>
#pictureDetailPage {
  margin-bottom: 16px;
}

:deep(.ant-descriptions-item-label) {
  align-items: center !important;
}
</style>
