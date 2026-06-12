<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import { useUserInfo } from "@/store/useUser";
import { getFileUrl } from "@/utils/upload";
import { ElMessage, ElMessageBox } from "element-plus";

const userInfo = useUserInfo();

const loading = ref(false);
const saving = ref(false);
const avatarUploading = ref(false);

const form = reactive({
  username: "",
  email: "",
  bio: "",
  github: "",
  website: "",
  avatar: "",
  createdAt: "",
  updatedAt: "",
});

function syncFromStore() {
  const u = userInfo.user;
  form.username = u.username || "";
  form.email = u.email || "";
  form.bio = u.bio || "";
  form.github = u.github || "";
  form.website = u.website || "";
  form.avatar = u.avatar || "";
  form.createdAt = u.createdAt || "";
  form.updatedAt = u.updatedAt || "";
}

onMounted(async () => {
  loading.value = true;
  try {
    await userInfo.who();
    syncFromStore();
  } finally {
    loading.value = false;
  }
});

async function handleAvatarUpload() {
  const input = document.createElement("input");
  input.type = "file";
  input.accept = "image/*";
  input.addEventListener("change", async () => {
    const file = input.files?.[0];
    if (!file) return;
    avatarUploading.value = true;
    try {
      const url = await getFileUrl(file);
      form.avatar = url as string;
      ElMessage.success("头像已上传");
    } catch (e) {
      console.error("Avatar upload failed:", e);
      ElMessage.error("头像上传失败");
    } finally {
      avatarUploading.value = false;
    }
  });
  input.click();
}

async function handleSave() {
  saving.value = true;
  try {
    const res = await userInfo.updateProfile({
      username: form.username,
      email: form.email,
      bio: form.bio,
      github: form.github,
      website: form.website,
      avatar: form.avatar,
    });
    if (res.data) {
      syncFromStore();
      ElMessage.success("保存成功");
    }
  } catch (e) {
    console.error("Profile update failed:", e);
  } finally {
    saving.value = false;
  }
}

async function handleReset() {
  try {
    await ElMessageBox.confirm("确定要重置所有修改吗？", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "info",
    });
  } catch {
    return;
  }
  syncFromStore();
}
</script>

<template>
  <div :class="s.page">
    <div :class="s.header">
      <div>
        <h2 :class="s.heading">个人设置</h2>
        <p :class="s.desc">管理您的个人信息与公开资料</p>
      </div>
    </div>

    <div :class="s.card">
      <div v-loading="loading">
        <!-- Avatar -->
        <div :class="s.avatarSection">
          <div :class="s.avatarLabel">头像</div>
          <div :class="s.avatarContent">
            <div :class="s.avatarWrap">
              <img
                v-if="form.avatar"
                :src="form.avatar"
                :class="s.avatarImg"
                alt="avatar"
              />
              <div v-else :class="s.avatarPlaceholder">
                <svg
                  width="40"
                  height="40"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="#94a3b8"
                  stroke-width="1.5"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                >
                  <path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2" />
                  <circle cx="12" cy="7" r="4" />
                </svg>
              </div>
              <div :class="s.avatarOverlay" @click="handleAvatarUpload">
                <svg
                  width="20"
                  height="20"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="#fff"
                  stroke-width="2"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                >
                  <path
                    d="M23 19a2 2 0 01-2 2H3a2 2 0 01-2-2V8a2 2 0 012-2h4l2-3h6l2 3h4a2 2 0 012 2z"
                  />
                  <circle cx="12" cy="13" r="4" />
                </svg>
                <span>{{ avatarUploading ? "上传中…" : "更换" }}</span>
              </div>
            </div>
          </div>
        </div>

        <el-divider :class="s.divider" />

        <!-- Base info -->
        <el-form
          label-position="top"
          :class="s.form"
          @submit.prevent="handleSave"
        >
          <div :class="s.formRow">
            <el-form-item label="用户名" :class="s.formItem">
              <el-input
                v-model="form.username"
                placeholder="请输入用户名"
                maxlength="50"
                disabled
              />
            </el-form-item>
            <el-form-item label="邮箱" :class="s.formItem">
              <el-input
                v-model="form.email"
                placeholder="请输入邮箱"
                maxlength="100"
              />
            </el-form-item>
          </div>

          <el-form-item label="个人简介">
            <el-input
              v-model="form.bio"
              type="textarea"
              :rows="3"
              placeholder="介绍一下你自己"
              maxlength="200"
              show-word-limit
            />
          </el-form-item>

          <div :class="s.formRow">
            <el-form-item label="GitHub" :class="s.formItem">
              <el-input
                v-model="form.github"
                placeholder="https://github.com/username"
              >
                <template #prefix>
                  <svg
                    width="16"
                    height="16"
                    viewBox="0 0 24 24"
                    fill="#94a3b8"
                  >
                    <path
                      d="M12 0C5.37 0 0 5.37 0 12c0 5.31 3.435 9.795 8.205 11.385.6.105.825-.255.825-.57 0-.285-.015-1.23-.015-2.235-3.015.555-3.795-.735-4.035-1.41-.135-.345-.72-1.41-1.23-1.695-.42-.225-1.02-.78-.015-.795.945-.015 1.62.87 1.845 1.23 1.08 1.815 2.805 1.305 3.495.99.105-.78.42-1.305.765-1.605-2.67-.3-5.46-1.335-5.46-5.925 0-1.305.465-2.385 1.23-3.225-.12-.3-.54-1.53.12-3.18 0 0 1.005-.315 3.3 1.23.96-.27 1.98-.405 3-.405s2.04.135 3 .405c2.295-1.56 3.3-1.23 3.3-1.23.66 1.65.24 2.88.12 3.18.765.84 1.23 1.905 1.23 3.225 0 4.605-2.805 5.625-5.475 5.925.435.375.81 1.095.81 2.22 0 1.605-.015 2.895-.015 3.3 0 .315.225.69.825.57A12.02 12.02 0 0024 12c0-6.63-5.37-12-12-12z"
                    />
                  </svg>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item label="个人网站" :class="s.formItem">
              <el-input
                v-model="form.website"
                placeholder="https://example.com"
              />
            </el-form-item>
          </div>
        </el-form>
      </div>
    </div>

    <!-- Metadata -->
    <div v-if="form.createdAt || form.updatedAt" :class="s.card">
      <div :class="s.metaTitle">账号信息</div>
      <div :class="s.metaGrid">
        <div :class="s.metaItem">
          <span :class="s.metaLabel">创建时间</span>
          <span :class="s.metaValue">{{ form.createdAt || "—" }}</span>
        </div>
        <div :class="s.metaItem">
          <span :class="s.metaLabel">更新时间</span>
          <span :class="s.metaValue">{{ form.updatedAt || "—" }}</span>
        </div>
      </div>
    </div>

    <!-- Actions -->
    <div :class="s.actions">
      <el-button @click="handleReset" :disabled="saving">重置</el-button>
      <el-button type="primary" :loading="saving" @click="handleSave">
        保存修改
      </el-button>
    </div>
  </div>
</template>

<style lang="less" module="s">
.page {
  max-width: 720px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 24px;
}

.heading {
  margin: 0;
  font-size: 26px;
  font-weight: 700;
  color: #0f172a;
}

.desc {
  margin: 6px 0 0;
  color: #64748b;
  font-size: 14px;
}

.card {
  background: #fff;
  border-radius: 16px;
  border: 1px solid #e2e8f0;
  padding: 28px 32px;
  margin-bottom: 20px;
}

/* Avatar */
.avatarSection {
  display: flex;
  align-items: flex-start;
  gap: 24px;
}

.avatarLabel {
  font-size: 14px;
  font-weight: 600;
  color: #0f172a;
  padding-top: 4px;
  flex-shrink: 0;
  width: 60px;
}

.avatarContent {
  flex: 1;
}

.avatarWrap {
  position: relative;
  width: 96px;
  height: 96px;
  border-radius: 50%;
  overflow: hidden;
  cursor: pointer;
}

.avatarImg {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatarPlaceholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f1f5f9;
  border: 2px dashed #cbd5e1;
  border-radius: 50%;
}

.avatarOverlay {
  position: absolute;
  inset: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 4px;
  background: rgba(15, 23, 42, 0.55);
  color: #fff;
  font-size: 12px;
  opacity: 0;
  transition: opacity 0.2s ease;
}

.avatarWrap:hover .avatarOverlay {
  opacity: 1;
}

.divider {
  margin: 24px 0;
}

/* Form */
.form {
  width: 100%;
}

.formRow {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.formItem {
  width: 100%;
}

/* Metadata */
.metaTitle {
  font-size: 14px;
  font-weight: 600;
  color: #0f172a;
  margin-bottom: 16px;
}

.metaGrid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.metaItem {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.metaLabel {
  font-size: 12px;
  color: #94a3b8;
}

.metaValue {
  font-size: 14px;
  color: #334155;
}

/* Actions */
.actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>
