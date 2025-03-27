import { generateService } from '@umijs/openapi'

export default generateService({
  requestLibPath: "import request from '@/request'",
  schemaPath: 'http://localhost:8000/api/v2/api-docs',
  serversPath: './src',
})
