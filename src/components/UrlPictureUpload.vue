<template>
  <div id="url-picture-upload">
    <a-input-group compact>
      <a-input
        v-model:value="fileUrl"
        style="width: calc(100% - 100px)"
        placeholder="请输入图片URL地址"
      />
      <a-button type="primary" style="width: 100px" @click="handleUpload" :loading="loading"
        >提交
      </a-button>
    </a-input-group>
    <div class="img-wrapper">
      <img v-if="picture?.url" :src="picture?.url" alt="avatar" />
    </div>
  </div>
</template>
<script lang="ts" setup>
import { ref } from 'vue'
import { message } from 'ant-design-vue'
import { uploadPictureByUrlUsingPost } from '@/api/pictureController.ts'

interface Props {
  picture?: API.PictureVO
  onSuccess?: (newPicture: API.PictureVO) => void
}

const props = defineProps<Props>()

const loading = ref<boolean>(false)
const fileUrl = ref<string>()

/**
 * @description: URL上传图片
 */
const handleUpload = async () => {
  loading.value = true
  try {
    const params: API.PictureUploadRequest = { fileUrl: fileUrl.value }
    //如果有图片id，则传递图片id
    if (props.picture) {
      params.id = props.picture.id
    }
    const res = await uploadPictureByUrlUsingPost(params)
    if (res.data.code === 0 && res.data.data) {
      message.success('图片上传成功')
      //将上传成功的图片信息传递给父组件
      props.onSuccess?.(res.data.data)
    } else {
      message.error('图片上传失败 ' + res.data.message)
    }
  } catch (error) {
    console.error('图片上传失败：' + error)
    message.error('图片上传失败:' + error)
  } finally {
    loading.value = false
  }
}
</script>
<style scoped>
#url-picture-upload {
  margin-bottom: 16px;
}

#url-picture-upload img {
  max-width: 200px;
  max-height: 300px;
}

#url-picture-upload .img-wrapper {
  text-align: center;
  margin-top: 16px;
}
</style>
