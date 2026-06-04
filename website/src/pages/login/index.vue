<script setup lang="ts">
import { useRouter } from "vue-router";
import request from "../../request";
import "./index.less"
import { reactive } from "vue";

const form = reactive({
  username: '',
  password: "",
})

const router = useRouter()

const onLogin = async () => {
  const res = await request("/user/login", {
    data: form,
    method: 'post'
  })
  console.log("res", res)
  router.push("/users")
}

</script>

<template>
  <div :class="s.login">
    <el-form :class="s.form" :model="form" label-width="auto" >
      <el-form-item :class="s.logo" label="">
        <h1>后台管理系统</h1>
      </el-form-item>
      <el-form-item label="">
        <el-input placeholder="用户名" v-model="form.username" />
      </el-form-item>
      <el-form-item label="">
        <el-input placeholder="密码" type="password" v-model="form.password" />
      </el-form-item>
    <el-form-item label="">
      <div :class="s.act">
        <el-button type="primary" @click="onLogin">登录</el-button>
        <el-button type="primary" link >注册</el-button>
      </div>
    </el-form-item>

    </el-form>
  </div>

</template>
<style lang="less" module="s">
.login {
  width: 100%;
  place-self: center;
  place-items: center;
  margin-bottom: 100px;
}

.form {
  display: flex;
  width: 300px;
  flex-direction: column;
  height: 300px;
  place-self: center;
}

.logo {
  margin: 20px auto;
}

.act {
  margin: 5px auto;
}
</style>

