/// <reference types="vite/client" />
interface ImportMetaEnv {
  readonly VITE_APP_TITLE: string
  readonly VITE_APP_BASE_API: string
  readonly VITE_APP_PROXY_URL: string
  readonly VITE_APP_PORT: number
  // 更多环境变量...
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}
