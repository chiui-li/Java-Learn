import http from "@/request";
import { defineStore } from "pinia";
import { ref } from "vue";

export const useUserInfo = defineStore("user-info", () => {
  const user = ref<D.User>({
    username: "",
    email: "",
    avatar: "",
    bio: "",
    github: "",
    website: "",
    createdAt: "",
    updatedAt: "",
  });

  async function who() {
    const res = await http<D.Result<D.User>>("/user/who");
    if (res.data) {
      user.value = res.data;
    }
  }

  async function updateProfile(data: Partial<D.User>) {
    const res = await http.post<D.Result<D.User>>("/user/update", { data });
    await who();
    return res;
  }

  return {
    user,
    who,
    updateProfile,
  };
});
