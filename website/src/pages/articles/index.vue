<script setup lang="ts">
import { onMounted, ref, shallowRef, watch } from "vue";
import { useRouter } from "vue-router";
import { useArticleStore } from "@/store/useArticleStore";
import http from "@/request";
import { ElMessage, ElMessageBox } from "element-plus";
import { Delta } from "quill";

const router = useRouter();
const keyword = ref("");
const loading = ref(false);
const articleStore = useArticleStore();

async function loadArticles() {
  loading.value = true;
  try {
    await articleStore.queryArticles({
      keyword: keyword.value || undefined,
      pageNum: articleStore.pageNum,
      pageSize: articleStore.pageSize,
    });
  } finally {
    loading.value = false;
  }
}

watch(keyword, () => {
  articleStore.pageNum = 1;
  loadArticles();
});

onMounted(() => {
  getCategories();
  loadArticles();
});

function goEdit(id: number) {
  router.push({ name: "ArticleEdit", params: { id } });
}
const categories = shallowRef<string[]>([]);

async function getCategories() {
  const b = await http<D.Result<Array<{ name: string }>>>("/category/all");
  if (b.data) {
    categories.value = b.data.map((i) => i.name);
  }
}

async function goCreate(categoryName: string) {
  const res = await http.post<D.Result<number>>("/posts/create", {
    data: {
      title: "新建文章",
      content: JSON.stringify(new Delta()),
      postType: "article",
      categoryName: categoryName || "前端",
    },
  });
  if (res?.data) {
    router.push({ name: "ArticleEdit", params: { id: res.data } });
  }
}

function statusLabel(status: D.ArticleStatus) {
  return status === "published" ? "已发布" : "草稿";
}

function typeLabel(type: D.PostType) {
  return type === "images" ? "图文" : "文章";
}

function displayTitle(article: D.Article): string {
  if (article.status === "draft") {
    return article.draftTitle || article.title || "";
  }
  if (article.draftTitle != null && article.draftTitle !== article.title) {
    return `${article.title} ← ${article.draftTitle}`;
  }
  return article.title || "";
}

function hasUnpublishedDraft(article: D.Article): boolean {
  if (article.status === "draft") return true;
  if (!article.draftContent && !article.draftTitle) return false;
  const titleChanged =
    article.draftTitle != null &&
    article.draftTitle?.trim() !== article.title?.trim();
  const contentChanged =
    !!article.draftContent &&
    article.draftContent?.trim() !== article.content?.trim();
  return titleChanged || contentChanged;
}

function onPageChange(p: number) {
  articleStore.pageNum = p;
  loadArticles();
}

function onSizeChange(s: number) {
  articleStore.pageSize = s;
  articleStore.pageNum = 1;
  loadArticles();
}

async function onDel(d: string) {
  try {
    await ElMessageBox.confirm("确定要删除这篇文章吗？", "删除确认", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    });
  } catch {
    return;
  }

  const b = await http<D.Result<boolean>>(`/posts/detail/${d}`, {
    method: "DELETE",
  });
  if (b.data) {
    ElMessage("删除成功！");
    loadArticles();
  }
}

const newCategory = ref("");
</script>

<template>
  <div :class="s.page">
    <div :class="s.header">
      <div>
        <h2 :class="s.heading">文章管理</h2>
        <p :class="s.desc">查看并编辑所有文章，支持搜索与快速创建</p>
      </div>
      <div :class="s.headerActions">
        <el-input
          v-model="keyword"
          placeholder="搜索标题或内容"
          clearable
          :class="s.searchInput"
        >
          <template #prefix> </template>
        </el-input>
        <el-dropdown>
          <el-button type="primary" link> 新建文章 </el-button>
          <template #dropdown style="padding: 10px">
            <div style="padding: 10px">
              <el-text>分类</el-text>
              <el-dropdown-menu>
                <el-dropdown-item
                  @click="() => goCreate(value)"
                  v-for="value in categories"
                  >{{ value }}</el-dropdown-item
                >
              </el-dropdown-menu>
              <el-input
                size="small"
                v-model.trim="newCategory"
                placeholder="输入新分类"
              />
              <el-button
                link
                type="primary"
                @click="goCreate(newCategory)"
                size="small"
                >新建</el-button
              >
            </div>
          </template>
        </el-dropdown>
      </div>
    </div>

    <div :class="s.tableCard">
      <el-table
        :data="articleStore.articles"
        v-loading="loading"
        size="default"
        style="width: 100%"
      >
        <el-table-column prop="title" label="标题" min-width="240">
          <template #default="{ row }">
            <el-link
              type="primary"
              :underline="false"
              @click="goEdit(row.id)"
              :class="s.linkTitle"
            >
              {{ displayTitle(row) }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column
          prop="categoryName"
          label="分类"
          width="100"
          align="center"
        >
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
        <el-table-column label="草稿" width="130" align="center">
          <template #default="{ row }">
            <el-tag
              v-if="row.status === 'draft'"
              type="warning"
              effect="light"
              size="small"
            >
              未发布
            </el-tag>
            <el-tag
              v-else-if="hasUnpublishedDraft(row)"
              type="danger"
              effect="light"
              size="small"
            >
              有未发布修改
            </el-tag>
            <span v-else :class="s.noDraft">—</span>
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
            <el-button type="primary" link size="small" @click="goEdit(row.id)"
              >编辑</el-button
            >
            <el-button type="danger" link size="small" @click="onDel(row.id)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <div :class="s.paginationWrapper">
        <el-pagination
          v-model:current-page="articleStore.pageNum"
          v-model:page-size="articleStore.pageSize"
          :total="articleStore.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          background
          @current-change="onPageChange"
          @size-change="onSizeChange"
        />
      </div>
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
  // background: #ffffff;
  // border: 1px solid #e2e8f0;
  // overflow: hidden;
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

.noDraft {
  color: #cbd5e1;
}

.paginationWrapper {
  display: flex;
  justify-content: flex-end;
  padding: 16px 20px;
  border-top: 1px solid #e2e8f0;
}
</style>
