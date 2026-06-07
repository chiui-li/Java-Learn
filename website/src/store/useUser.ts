import http from "@/request"
import { defineStore } from "pinia"
import { onMounted, ref } from "vue"

export interface User {
  username: string
  email: string
}

// 注意解构会导致失去响应
export const useUserInfo = defineStore('search-filters', () => {
  const user = ref<User>({
    username: "",
    email: ""
  })

  async function who() {
    const res = await http<D.Result<User>>("/user/who")
    user.value = res.data!
    console.log("res", user.value);
  }


  return {
    user,
    setUser() {
      user.value = {
        email: "1",
        username: "2",
      }
    },
    who,
  }
})
