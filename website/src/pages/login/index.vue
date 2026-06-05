<script setup lang="ts">
import { useRouter } from "vue-router";
import { post } from "@/request";
import "./index.less";
import { reactive, ref } from "vue";

const form = reactive<D.User>({
  username: "",
  password: "",
  email: "",
});

const router = useRouter();
const options = reactive([
  {
    label: "登录",
    value: "0",
  },
  {
    label: "注册",
    value: "1",
  },
]);
const type = ref("0");
const onLogin = async () => {
  const res = await post<D.User>("/user/login", {
    data: form,
    method: "post",
  });
  router.push("/users");
};

const onRegister = async () => {};
</script>

<template>
  <div :class="s.login">
    <el-form :class="s.form" :model="form" label-width="auto">
      <el-form-item :class="s.logo" label="">
        <h1>后台管理系统</h1>
      </el-form-item>
      <el-form-item label="">
        <el-segmented
          style="margin: auto"
          v-model="type"
          :options="options"
          size="default"
        />
      </el-form-item>
      <el-form-item label="">
        <el-input placeholder="用户名" v-model="form.username" />
      </el-form-item>
      <el-form-item v-if="type === '1'" label="">
        <el-input placeholder="邮箱" v-model="form.email" />
      </el-form-item>
      <el-form-item label="">
        <el-input placeholder="密码" type="password" v-model="form.password" />
      </el-form-item>
      <el-form-item label="">
        <div :class="s.act">
          <el-button v-if="type === '0'" type="primary" link @click="onLogin"
            >登录</el-button
          >
          <el-button v-if="type === '1'" type="primary" link @click="onRegister"
            >注册</el-button
          >
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
