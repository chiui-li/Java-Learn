<script setup lang="ts">
import TiptapEditor from "@/components/TiptapEditor.vue";
import { computed, ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { useArticleStore } from "@/store/useArticleStore";
import http from "@/request";

const route = useRoute();
const router = useRouter();
const articleStore = useArticleStore();

const isNew = computed(
  () =>
    route.name === "ArticleCreate" ||
    route.params.id === "new" ||
    route.path.endsWith("/new"),
);
const articleForm = ref<D.Article>({
  id: 0,
  userId: 1000,
  title: "",
  content: "",
  createdAt: "",
  updatedAt: "",
  viewCount: 0,
  postType: "article",
  draftContent: "",
  draftTitle: null,
  draftUpdatedAt: "",
  status: "draft",
});

async function loadArticle() {
  const id = route.params.id;
  const res = await http<D.ArticleRes>("/posts/detail/" + id);

  articleForm.value = { ...res.data };
}

watch(
  () => route.path,
  () => {
    loadArticle();
  },
  { immediate: true },
);

function goBack() {
  router.push("/backend/articles");
}

function saveAsDraft() {
  const id = route.params.id;
  http.post(`/posts/detail/update/${id}/draft`, {
    data: articleForm.value,
  });

  // const saved = articleStore.saveArticle({
  //   ...articleForm.value,
  //   status: "draft",
  //   draftUpdatedAt: new Date().toISOString().slice(0, 19).replace("T", " "),
  // });
  // if (isNew.value) {
  //   router.replace(`/backend/articles/${saved.id}/edit`);
  // }
  ElMessage.success("已保存为草稿");
}

function publishArticle() {
  const saved = articleStore.saveArticle({
    ...articleForm.value,
    status: "published",
    updatedAt: new Date().toISOString().slice(0, 19).replace("T", " "),
    draftUpdatedAt:
      articleForm.value.draftUpdatedAt ||
      new Date().toISOString().slice(0, 19).replace("T", " "),
  });
  if (isNew.value) {
    router.replace(`/backend/articles/${saved.id}/edit`);
  }
  ElMessage.success("已发布文章");
}
</script>

<template>
  <div :class="s.page">
    <div :class="s.header">
      <div>
        <!-- <p :class="s.tag">文章编辑</p> -->
        <h2 :class="s.heading">{{ "编辑文章" }}</h2>
        <p :class="s.desc">
          {{
            isNew
              ? "填写文章内容并保存草稿或直接发布"
              : "调整文章信息并保存更新"
          }}
        </p>
      </div>
      <div :class="s.headerActions">
        <el-button @click="goBack">
          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
            style="margin-right: 4px"
          >
            <polyline points="15 18 9 12 15 6" />
          </svg>
          返回
        </el-button>
        <el-button type="warning" @click="saveAsDraft">
          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
            style="margin-right: 4px"
          >
            <path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7" />
            <path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z" />
          </svg>
          保存草稿
        </el-button>
        <el-button type="primary" @click="publishArticle">
          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
            style="margin-right: 4px"
          >
            <polyline points="20 6 9 17 4 12" />
          </svg>
          {{ "发布" }}
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

        <el-form-item label="正文">
          <TiptapEditor v-model="articleForm.draftContent" />
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
  background: #ffffff;
  border-radius: 16px;
  padding: 28px;
  box-shadow: 0 2px 8px rgba(15, 23, 42, 0.04);
  border: 1px solid #e2e8f0;
}

.formRow {
  display: flex;
  gap: 20px;
}

.formRow :global(.el-form-item) {
  flex: 1;
}
</style>
