<template>
  <div id="spaceDetailPage">
    <!--    空间信息-->
    <a-flex justify="space-between" style="padding-right: 50px">
      <h2>{{ space.spaceName }}（私有空间）</h2>
      <a-space size="middle">
        <a-button type="primary" :href="`/add_picture?spaceId=${id}`"> + 创建图片</a-button>
        <a-tooltip
          :title="`占用空间 ${formatSize(space.totalSize)} / ${formatSize(space.maxSize)}`"
        >
          <a-tooltip
            :title="`占用空间: ${formatSize(space.totalSize)} / ${formatSize(space.maxSize)}`"
          >
            <a-progress
              type="circle"
              :percent="(((space.totalSize || 0) * 100) / (space.maxSize || 1)).toFixed(1)"
              :size="42"
            />
          </a-tooltip>
        </a-tooltip>
      </a-space>
    </a-flex>
    <div style="margin-bottom: 20px" />
    <!-- 图片列表 -->
    <PictureList :dataList="dataList" :loading="loading" />
    <!-- 分页 -->
    <a-pagination
      style="text-align: right"
      v-model:current="searchParams.current"
      v-model:pageSize="searchParams.pageSize"
      :total="total"
      :show-total="() => `图片总数 ${total} / ${space.maxCount}`"
      @change="onPageChange"
    />
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { message } from 'ant-design-vue'
import { getSpaceVoByIdUsingGet } from '@/api/spaceController.ts'
import { listPictureVoByPageUsingPost } from '@/api/pictureController.ts'
import { formatSize } from '@/utils'
import PictureList from '@/components/PictureList.vue'

interface Props {
  id: number
}

const props = defineProps<Props>()
const space = ref<API.SpaceVO>({})

//<获取空间详情>
const fetchSpaceDetail = async () => {
  const res = await getSpaceVoByIdUsingGet({
    id: props.id,
  })
  if (res.data.code === 0 && res.data.data) {
    space.value = res.data.data
  } else {
    message.error('获取空间详情失败 ' + res.data.message)
  }
}

//<获取图片列表>
const dataList = ref<API.PictureVO[]>([])
const total = ref<number>(0)
const loading = ref<boolean>(true)
//搜索条件
const searchParams = reactive<API.PictureQueryRequest>({
  current: 1,
  pageSize: 12,
  sortField: 'createTime',
  sortOrder: 'descend',
})

//获取数据
const fetchData = async () => {
  loading.value = true
  //转换搜索参数(将分类和标签转换为字符串)
  const params = {
    spaceId: props.id,
    ...searchParams,
  }

  const res = await listPictureVoByPageUsingPost(params)
  if (res.data.code === 0 && res.data.data) {
    dataList.value = res.data.data.records ?? []
    total.value = res.data.data.total ?? 0
  } else {
    message.error('获取数据失败' + res.data.message)
  }
  loading.value = false
}

onMounted(() => {
  fetchSpaceDetail()
  fetchData()
})

//分页参数
const onPageChange = (page: number, pageSize: number) => {
  searchParams.current = page
  searchParams.pageSize = pageSize
  fetchData()
}
</script>

<style scoped>
#spaceDetailPage {
  margin-bottom: 16px;
}

:deep(.ant-descriptions-item-label) {
  align-items: center !important;
}
</style>
