<script setup lang="ts">
import { useRouter } from "vue-router";
import { post } from "@/request";
import { reactive, ref } from "vue";
import { ElNotification, type FormInstance } from "element-plus";
import { hashPassword } from "@/utils/crypto";

const form = reactive<D.User>({
  username: "",
  password: "",
  email: "",
});


const router = useRouter();
const options = reactive([
  { label: "登录", value: "0" },
  { label: "注册", value: "1" },
]);

const type = ref("0");
const loading = ref(false);

const rules = {
  username: [
    { required: true, message: "请输入用户名" },
    { min: 2, max: 100, message: "用户名长度 2 - 100" },
  ],
  email: [
    { required: true, message: "请输入邮箱", trigger: "blur" },
  ],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
  ],
};

const formRef = ref<FormInstance>();

const onLogin = async () => {
  await formRef.value?.validate();
  loading.value = true;
  try {
    const payload = { ...form, password: await hashPassword(form.password) };
    const res = await post<D.UserRes>("/user/login", {
      data: payload,
      method: "post",
    });
    if (res?.data?.email) {
      ElNotification({
        title: "登录成功",
        message: "欢迎回来",
        type: "success",
      });
      router.push("/backend/dashborad");
    }
  } finally {
    loading.value = false;
  }
};

const onRegister = async () => {
  await formRef.value?.validate();
  loading.value = true;
  try {
    const payload = { ...form, password: await hashPassword(form.password) };
    const res = await post<D.UserRes>("/user/register", {
      data: payload,
      method: "post",
    });
    if (res.data) {
      ElNotification({
        title: "注册成功",
        message: "快去登录吧",
        type: "success",
      });
      type.value = "0";
    }
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div :class="s.loginWrap">
    <div :class="s.bgOrb1" />
    <div :class="s.bgOrb2" />
    <div :class="s.bgGrid" />

    <div :class="s.card">
      <div :class="s.cardHeader">
        <div :class="s.logoWrap">
          <svg width="40" height="40" viewBox="0 0 40 40" fill="none">
            <rect width="40" height="40" rx="10" fill="url(#lgrad)" />
            <path d="M13 28V12l7 10 7-10v16" stroke="#fff" stroke-width="2.5" stroke-linecap="round"
              stroke-linejoin="round" fill="none" />
            <defs>
              <linearGradient id="lgrad" x1="0" y1="0" x2="40" y2="40">
                <stop stop-color="#6366f1" />
                <stop offset="1" stop-color="#8b5cf6" />
              </linearGradient>
            </defs>
          </svg>
        </div>
        <!-- <h1 :class="s.title">Java Learn</h1> -->
        <p :class="s.desc">文章管理后台</p>
      </div>

      <el-segmented v-model="type" :options="options" size="large" :class="s.segmented" />

      <el-form ref="formRef" :rules="rules" :model="form" label-width="0" :class="s.form">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名" :prefix-icon="false" size="large">
            <template #prefix>
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#94a3b8" stroke-width="1.8"
                stroke-linecap="round" stroke-linejoin="round">
                <path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2" />
                <circle cx="12" cy="7" r="4" />
              </svg>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item v-if="type === '1'" prop="email">
          <el-input v-model="form.email" placeholder="邮箱" size="large">
            <template #prefix>
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#94a3b8" stroke-width="1.8"
                stroke-linecap="round" stroke-linejoin="round">
                <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z" />
                <polyline points="22,6 12,13 2,6" />
              </svg>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input v-model="form.password" placeholder="密码" type="password" size="large">
            <template #prefix>
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="#94a3b8" stroke-width="1.8"
                stroke-linecap="round" stroke-linejoin="round">
                <rect x="3" y="11" width="18" height="11" rx="2" ry="2" />
                <path d="M7 11V7a5 5 0 0110 0v4" />
              </svg>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item>
          <el-button v-if="type === '0'" type="primary" size="large" :loading="loading" :class="s.submitBtn"
            @click="onLogin">
            登录
          </el-button>
          <el-button v-if="type === '1'" type="primary" size="large" :loading="loading" :class="s.submitBtn"
            @click="onRegister">
            注册
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style lang="less" module="s">
.loginWrap {
  width: 100%;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #0f172a 0%, #1e293b 50%, #0f172a 100%);
  position: relative;
  overflow: hidden;
}

.bgOrb1 {
  position: absolute;
  top: -200px;
  left: -200px;
  width: 600px;
  height: 600px;
  background: radial-gradient(circle, rgba(99, 102, 241, 0.15) 0%, transparent 70%);
  border-radius: 50%;
  pointer-events: none;
}

.bgOrb2 {
  position: absolute;
  bottom: -300px;
  right: -200px;
  width: 700px;
  height: 700px;
  background: radial-gradient(circle, rgba(139, 92, 246, 0.1) 0%, transparent 70%);
  border-radius: 50%;
  pointer-events: none;
}

.bgGrid {
  position: absolute;
  inset: 0;
  background-image:
    linear-gradient(rgba(148, 163, 184, 0.04) 1px, transparent 1px),
    linear-gradient(90deg, rgba(148, 163, 184, 0.04) 1px, transparent 1px);
  background-size: 60px 60px;
  pointer-events: none;
}

.card {
  position: relative;
  width: 400px;
  padding: 40px 36px 32px;
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(24px);
  -webkit-backdrop-filter: blur(24px);
  border-radius: 24px;
  border: 1px solid rgba(255, 255, 255, 0.08);
  box-shadow: 0 24px 80px rgba(0, 0, 0, 0.3);
  animation: cardEnter 0.6s ease-out;
}

@keyframes cardEnter {
  from {
    opacity: 0;
    transform: translateY(24px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.cardHeader {
  text-align: center;
  margin-bottom: 28px;
}

.logoWrap {
  display: inline-flex;
  margin-bottom: 16px;
}

.title {
  margin: 0;
  font-size: 24px;
  font-weight: 700;
  color: #f1f5f9;
  letter-spacing: -0.02em;
}

.desc {
  margin: 6px 0 0;
  font-size: 14px;
  color: #64748b;
}

.segmented {
  display: flex;
  justify-content: center;
  margin-bottom: 28px;

  :global(.el-segmented) {
    background: rgba(255, 255, 255, 0.06);
    border: 1px solid rgba(255, 255, 255, 0.08);

    .el-segmented__item {
      color: #94a3b8;
      font-weight: 500;
      min-width: 100px;

      &.is-selected {
        background: linear-gradient(135deg, #6366f1, #4f46e5);
        color: #fff;
        box-shadow: 0 4px 12px rgba(99, 102, 241, 0.3);
      }
    }
  }
}

.form {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.form :global(.el-input__wrapper) {
  background: rgba(255, 255, 255, 0.06) !important;
  box-shadow: 0 0 0 1px rgba(148, 163, 184, 0.15) inset !important;
}

.form :global(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 2px #6366f1 inset !important;
}

.form :global(.el-input__inner) {
  color: #f1f5f9 !important;
}

.form :global(.el-input__inner::placeholder) {
  color: #475569 !important;
}

.submitBtn {
  width: 100%;
  margin-top: 4px;
  height: 46px !important;
  font-size: 15px;
  font-weight: 600;
  border-radius: 12px !important;
  background: linear-gradient(135deg, #6366f1, #4f46e5) !important;
  border: none !important;
  box-shadow: 0 6px 20px rgba(99, 102, 241, 0.3) !important;
  transition: all 0.2s ease !important;
}

.submitBtn:hover {
  transform: translateY(-1px);
  box-shadow: 0 8px 28px rgba(99, 102, 241, 0.4) !important;
}

.submitBtn:active {
  transform: translateY(0);
}
</style>
