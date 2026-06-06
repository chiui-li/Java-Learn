<script setup lang="ts">
import { computed, ref } from "vue";
import { useRouter } from "vue-router";
import { useArticleStore } from "@/store/useArticleStore";
import http from "@/request";

const router = useRouter();
const keyword = ref("");
const articleStore = useArticleStore();

const filteredArticles = computed(() => {
  const key = keyword.value.trim().toLowerCase();
  if (!key) {
    return articleStore.articles;
  }
  return articleStore.articles.filter(
    (article) =>
      article.title.toLowerCase().includes(key) ||
      article.content.toLowerCase().includes(key),
  );
});

function goEdit(id: number) {
  router.push({ name: "ArticleEdit", params: { id } });
}

async function goCreate() {
  // router.push({ name: "ArticleCreate" });
  const res = await http.post<D.Result<number>>("/posts/create", {
    data: {
      title: "新建文章",
      content: "来写点什么吧",
      postType: "article",
    },
  });
  console.log("res", res);
  if (res.data) {
    router.push({ name: "ArticleEdit", params: { id: res.data } });
  }
}

function statusLabel(status: D.ArticleStatus) {
  return status === "published" ? "已发布" : "草稿";
}

function typeLabel(type: D.PostType) {
  return type === "images" ? "图文" : "文章";
}
</script>

<template>
  <div :class="s.page">
    <div :class="s.header">
      <div>
        <!-- <p :class="s.tag">文章</p> -->
        <h2 :class="s.heading">文章管理</h2>
        <p :class="s.desc">查看并编辑所有文章，支持搜索与快速创建</p>
      </div>
      <div :class="s.headerActions">
        <el-input
          v-model="keyword"
          placeholder="搜索标题或内容"
          clearable
          :class="s.searchInput"
          size="large"
        >
          <template #prefix>
            <svg
              width="18"
              height="18"
              viewBox="0 0 24 24"
              fill="none"
              stroke="#94a3b8"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
            >
              <circle cx="11" cy="11" r="8" />
              <line x1="21" y1="21" x2="16.65" y2="16.65" />
            </svg>
          </template>
        </el-input>
        <el-button type="primary" size="large" @click="goCreate">
          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2.5"
            stroke-linecap="round"
            stroke-linejoin="round"
            style="margin-right: 4px"
          >
            <line x1="12" y1="5" x2="12" y2="19" />
            <line x1="5" y1="12" x2="19" y2="12" />
          </svg>
          新建文章
        </el-button>
      </div>
    </div>

    <div :class="s.tableCard">
      <el-table :data="filteredArticles" size="default" style="width: 100%">
        <el-table-column prop="title" label="标题" min-width="240">
          <template #default="{ row }">
            <el-link
              type="primary"
              :underline="false"
              @click="goEdit(row.id)"
              :class="s.linkTitle"
            >
              {{ row.title }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column label="类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag
              :type="row.postType === 'images' ? 'success' : 'primary'"
              effect="light"
              size="small"
            >
              {{ typeLabel(row.postType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag
              :type="row.status === 'published' ? 'success' : 'warning'"
              effect="light"
              size="small"
            >
              <template #default>
                <span
                  :class="
                    row.status === 'published'
                      ? s.statusDotGreen
                      : s.statusDotAmber
                  "
                />
                {{ statusLabel(row.status) }}
              </template>
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="170" />
        <el-table-column prop="updatedAt" label="更新时间" width="170" />
        <el-table-column
          prop="viewCount"
          label="浏览"
          width="80"
          align="center"
        />
        <el-table-column label="操作" width="120" align="center" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="goEdit(row.id)"
              >编辑</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<style module="s" lang="less">
.page {
  max-width: 1200px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 18px;
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
  align-items: center;
  gap: 12px;
  flex-shrink: 0;
}

.searchInput {
  width: 280px;
}

.tableCard {
  background: #ffffff;
  border: 1px solid #e2e8f0;
  overflow: hidden;
}

.linkTitle {
  font-weight: 500;
  color: #6366f1;
  font-size: 14px;
}

.statusDotGreen {
  display: inline-block;
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #22c55e;
  margin-right: 5px;
  vertical-align: middle;
}

.statusDotAmber {
  display: inline-block;
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #f59e0b;
  margin-right: 5px;
  vertical-align: middle;
}
</style>
