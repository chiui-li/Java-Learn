<script setup lang="ts">
import { onUnmounted, ref, shallowRef, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import http from "@/request";
import QuillEditor from "@/components/QuillEditor.vue";
import { isEqual } from "lodash-es";

const route = useRoute();
const router = useRouter();

const articleForm = ref<D.Article>({
  id: 0,
  createdAt: "",
  updatedAt: "",
  draftTitle: null,
  status: "draft",
});

const draftContent = shallowRef<any>(undefined);

const saving = ref(false);
const saved = ref(true);
let draftTimer: ReturnType<typeof setTimeout> | null = null;

function clearDraftTimer() {
  if (draftTimer) {
    clearTimeout(draftTimer);
    draftTimer = null;
  }
}
const id = route.params.id;

onUnmounted(() => {
  clearDraftTimer();
});

async function flushDraft() {
  clearDraftTimer();
  saving.value = true;
  try {
    await http.post(`/posts/detail/update/${id}/draft`, {
      data: {
        // ...articleForm.value,
        // categoryName: articleForm.value.categoryName,
        id,
        draftTitle: articleForm.value.draftTitle,
        draftContent:
          typeof draftContent.value === "string"
            ? draftContent.value
            : JSON.stringify(draftContent.value),
      },
    });
    saved.value = true;
  } catch {
    // silent
  } finally {
    saving.value = false;
  }
}

function autoSaveDraft() {
  saved.value = false;
  clearDraftTimer();
  draftTimer = setTimeout(flushDraft, 5000);
}

watch(
  () => draftContent.value,
  (n, o) => {
    if (typeof o === "string" || !o) {
    } else {
      autoSaveDraft();
    }
  },
  {
    deep: false,
    flush: "post",
  },
);

watch(
  () => articleForm.value.draftTitle,
  (n, o) => {
    if (!o) {
    } else {
      autoSaveDraft();
    }
  },
);

const editor = ref<InstanceType<typeof QuillEditor>>();

onUnmounted(() => {
  flushDraft();
});

async function loadArticle() {
  const id = route.params.id;
  const res = await http<D.ArticleRes>("/posts/detail/" + id);
  articleForm.value = { ...res.data, draftContent: undefined };
  draftContent.value =
    typeof res?.data?.draftContent === "string"
      ? JSON.parse(res.data.draftContent)
      : [];
  saved.value = true;
}

watch(
  () => route.path,
  () => {
    loadArticle();
  },
  { immediate: true },
);

function goBack() {
  clearDraftTimer();
  router.push("/back/backend/articles");
}

async function saveAsDraft() {
  await flushDraft();
  ElMessage.success("已保存为草稿");
}

async function publishArticle() {
  const html = editor.value?.getHtml();
  await http.post(`/posts/detail/publish/${id}`, {
    data: {
      id,
      draftTitle: articleForm.value.draftTitle,
      draftContent:
        typeof draftContent.value === "string"
          ? draftContent.value
          : JSON.stringify(draftContent.value),
      contentHtml: html,
    },
  });
  ElMessage.success("已发布");
  // await loadArticle();
}

async function unpublishArticle() {
  const id = route.params.id;
  await http.post(`/posts/detail/unpublish/${id}`, {
    // data: articleForm.value,
  });
  ElMessage.success("已取消发布");
  // await loadArticle();
}

async function getCategories() {
  await http("/category/all");
}

getCategories();
</script>

<template>
  <div :class="s.page">
    <div :class="s.header">
      <div>
        <!-- <p :class="s.tag">文章编辑</p> -->
        <h2 :class="s.heading">{{ "编辑文章" }}</h2>
      </div>
      <div :class="s.headerActions">
        <span :class="[s.saveIndicator, saving ? s.saving : s.saved]">
          {{ saving ? "保存中…" : saved ? "草稿已保存" : "未保存的更改" }}
        </span>
        <el-button @click="goBack"> 返回 </el-button>
        <el-button type="warning" @click="saveAsDraft"> 保存草稿 </el-button>
        <el-button type="primary" @click="publishArticle">
          {{ "发布" }}
        </el-button>
        <el-button
          type="primary"
          v-if="articleForm.status === 'published'"
          @click="unpublishArticle"
        >
          {{ "取消发布" }}
        </el-button>
      </div>
    </div>

    <div :class="s.formCard">
      <el-form label-width="80px" :model="articleForm" label-position="top">
        <el-form-item label="标题">
          <el-input
            v-model="articleForm.draftTitle"
            placeholder="请输入文章标题"
            size="large"
          />
        </el-form-item>
        <el-form-item disabled label="分类">
          <el-input
            disabled
            v-model="articleForm.categoryName"
            placeholder="请输入文章标题"
            size="large"
          />
        </el-form-item>
        <el-form-item label="正文">
          <QuillEditor
            ref="editor"
            v-if="draftContent !== undefined"
            v-model="draftContent"
          />
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style lang="less" module="s">
.page {
  max-width: 100%;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 24px;
  margin-bottom: 24px;
}

.tag {
  display: inline-flex;
  align-items: center;
  padding: 5px 12px;
  margin-bottom: 10px;
  border-radius: 999px;
  background: #eef2ff;
  color: #4f46e5;
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 0.02em;
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

.headerActions {
  display: flex;
  gap: 10px;
  flex-shrink: 0;
  flex-wrap: wrap;
}

.formCard {
  // background: #ffffff;
  // border-radius: 16px;
  // padding: 28px;
  // box-shadow: 0 2px 8px rgba(15, 23, 42, 0.04);
  // border: 1px solid #e2e8f0;
}

.formRow {
  display: flex;
  gap: 20px;
}

.formRow :global(.el-form-item) {
  flex: 1;
}

.saveIndicator {
  display: inline-flex;
  align-items: center;
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 6px;
  white-space: nowrap;
  transition: all 0.2s ease;
}

.saving {
  color: #6366f1;
  background: #eef2ff;
}

.saved {
  color: #059669;
  background: #ecfdf5;
}

.saveIndicator:empty + .saveIndicator {
  display: none;
}
</style>
