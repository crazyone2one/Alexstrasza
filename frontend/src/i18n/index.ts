import { createI18n } from 'vue-i18n'

// User defined lang
import enLocale from './lang/en'
import zhLocale from './lang/zh-cn'
const messages = {
  en: {
    ...enLocale,
  },
  'zh-cn': {
    ...zhLocale,
  },
}

// Create i18n instance with options
export const i18n = createI18n({
  legacy: false, // you must set `false`, to use Composition API
  silentTranslationWarn: true,
  missingWarn: false,
  silentFallbackWarn: true,
  fallbackWarn: false,
  locale: 'zh-cn', // set locale
  fallbackLocale: 'en', // set fallback locale
  messages, // set locale messages
})
