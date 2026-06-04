import { defineStore } from "pinia"
import { ref } from "vue"

export interface User {
    username: string
    email: string

}

// 注意解构会导致失去响应
export const useUser = defineStore('search-filters', () => {
  const user = ref<User>({
    username: "",
    email: ""
  })
  return {
    user,
    setUser(u: User){
        user.value = u
    }
  }
})
