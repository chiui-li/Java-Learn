<script setup lang="ts">
import { computed } from "vue";
import { useRouter } from "vue-router";
import { useArticleStore } from "@/store/useArticleStore";

const router = useRouter();
const articleStore = useArticleStore();

const totalArticles = computed(() => articleStore.articles.length);
const publishedCount = computed(
  () => articleStore.articles.filter((a) => a.status === "published").length,
);
const draftCount = computed(
  () => articleStore.articles.filter((a) => a.status === "draft").length,
);
const totalViews = computed(() =>
  articleStore.articles.reduce((s, a) => s + (a.viewCount || 0), 0),
);
const recentArticles = computed(() =>
  [...articleStore.articles]
    .sort(
      (a, b) =>
        new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime(),
    )
    .slice(0, 5),
);

function goArticles() {
  router.push("/backend/articles");
}

function goCreate() {
  router.push("/backend/articles/new");
}
</script>

<template>
  <div :class="s.page">
    <div :class="s.header">
      <div>
        <h2 :class="s.heading">概览</h2>
        <p :class="s.desc">查看您的内容数据与最新动态</p>
      </div>
      <div :class="s.headerActions">
        <el-button @click="goArticles">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"
            stroke-linecap="round" stroke-linejoin="round" style="margin-right: 4px">
            <path
              d="M19 20H5a2 2 0 01-2-2V6a2 2 0 012-2h10a2 2 0 012 2v1m2 13a2 2 0 01-2-2V7m2 13a2 2 0 002-2V9a2 2 0 00-2-2h-2m-4-3H9M7 16h6M7 8h6v4H7V8z" />
          </svg>
          全部文章
        </el-button>
        <el-button type="primary" @click="goCreate">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"
            stroke-linecap="round" stroke-linejoin="round" style="margin-right: 4px">
            <line x1="12" y1="5" x2="12" y2="19" />
            <line x1="5" y1="12" x2="19" y2="12" />
          </svg>
          新建文章
        </el-button>
      </div>
    </div>

    <!-- Stats -->
    <div :class="s.statsGrid">
      <div :class="[s.statCard, { [s.cardBlue]: true }]">
        <div :class="s.statIcon">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"
            stroke-linecap="round" stroke-linejoin="round">
            <path
              d="M19 20H5a2 2 0 01-2-2V6a2 2 0 012-2h10a2 2 0 012 2v1m2 13a2 2 0 01-2-2V7m2 13a2 2 0 002-2V9a2 2 0 00-2-2h-2m-4-3H9M7 16h6M7 8h6v4H7V8z" />
          </svg>
        </div>
        <div :class="s.statBody">
          <div :class="s.statValue">{{ totalArticles }}</div>
          <div :class="s.statLabel">全部文章</div>
        </div>
        <div :class="s.statTrend">+{{ publishedCount + draftCount }}</div>
      </div>
      <div :class="[s.statCard, { [s.cardGreen]: true }]">
        <div :class="s.statIcon">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"
            stroke-linecap="round" stroke-linejoin="round">
            <path d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
        </div>
        <div :class="s.statBody">
          <div :class="s.statValue">{{ publishedCount }}</div>
          <div :class="s.statLabel">已发布</div>
        </div>
      </div>
      <div :class="[s.statCard, { [s.cardAmber]: true }]">
        <div :class="s.statIcon">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"
            stroke-linecap="round" stroke-linejoin="round">
            <path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7" />
            <path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z" />
          </svg>
        </div>
        <div :class="s.statBody">
          <div :class="s.statValue">{{ draftCount }}</div>
          <div :class="s.statLabel">草稿</div>
        </div>
      </div>
      <div :class="[s.statCard, { [s.cardPurple]: true }]">
        <div :class="s.statIcon">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"
            stroke-linecap="round" stroke-linejoin="round">
            <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z" />
            <circle cx="12" cy="12" r="3" />
          </svg>
        </div>
        <div :class="s.statBody">
          <div :class="s.statValue">{{ totalViews }}</div>
          <div :class="s.statLabel">总浏览</div>
        </div>
      </div>
    </div>

    <!-- Recent articles -->
    <div :class="s.section">
      <div :class="s.sectionHeader">
        <h3 :class="s.sectionTitle">近期文章</h3>
        <el-button link type="primary" @click="goArticles">查看全部</el-button>
      </div>
      <div :class="s.recentCard">
        <div v-if="recentArticles.length === 0" :class="s.empty">
          <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="#cbd5e1" stroke-width="1.5"
            stroke-linecap="round" stroke-linejoin="round">
            <path d="M14 2H6a2 2 0 00-2 2v16a2 2 0 002 2h12a2 2 0 002-2V8z" />
            <polyline points="14 2 14 8 20 8" />
            <line x1="16" y1="13" x2="8" y2="13" />
            <line x1="16" y1="17" x2="8" y2="17" />
          </svg>
          <p>暂无文章，快去创建第一篇吧</p>
        </div>
        <div v-for="article in recentArticles" :key="article.id" :class="s.articleRow"
          @click="router.push(`/backend/articles/${article.id}/edit`)">
          <div :class="s.articleInfo">
            <div :class="s.articleTitle">{{ article.title }}</div>
            <div :class="s.articleMeta">
              <span :class="s.articleDate">{{ article.createdAt }}</span>
              <span :class="[
                s.statusDot,
                {
                  [s.published]: article.status === 'published',
                  [s.draft]: article.status === 'draft',
                },
              ]" />
              <span :class="article.status === 'published' ? s.publishedText : s.draftText
                ">
                {{ article.status === "published" ? "已发布" : "草稿" }}
              </span>
              <span v-if="article.viewCount !== undefined">
                · {{ article.viewCount }} 次浏览</span>
            </div>
          </div>
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#94a3b8" stroke-width="2"
            stroke-linecap="round" stroke-linejoin="round">
            <polyline points="9 18 15 12 9 6" />
          </svg>
        </div>
      </div>
    </div>

    <!-- Quick stats -->
    <div :class="s.section">
      <h3 :class="s.sectionTitle">快速操作</h3>
      <div :class="s.quickGrid">
        <div :class="s.quickCard" @click="goCreate">
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="#6366f1" stroke-width="2"
            stroke-linecap="round" stroke-linejoin="round">
            <line x1="12" y1="5" x2="12" y2="19" />
            <line x1="5" y1="12" x2="19" y2="12" />
          </svg>
          <span>新建文章</span>
        </div>
        <div :class="s.quickCard" @click="goArticles">
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="#6366f1" stroke-width="2"
            stroke-linecap="round" stroke-linejoin="round">
            <path
              d="M19 20H5a2 2 0 01-2-2V6a2 2 0 012-2h10a2 2 0 012 2v1m2 13a2 2 0 01-2-2V7m2 13a2 2 0 002-2V9a2 2 0 00-2-2h-2m-4-3H9M7 16h6M7 8h6v4H7V8z" />
          </svg>
          <span>管理文章</span>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="less" module="s">
.page {
  width: 100%;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 18px;
  margin-bottom: 28px;
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
}

/* Stats */
.statsGrid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 28px;
}

.statCard {
  display: flex;
  align-items: flex-start;
  gap: 14px;
  padding: 20px;
  background: #fff;
  border-radius: 16px;
  border: 1px solid #e2e8f0;
  box-shadow: 0 2px 8px rgba(15, 23, 42, 0.04);
  position: relative;
  transition: all 0.2s ease;
}

.statCard:hover {
  box-shadow: 0 8px 24px rgba(15, 23, 42, 0.08);
  transform: translateY(-2px);
}

.statIcon {
  width: 44px;
  height: 44px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
  flex-shrink: 0;
}

.cardBlue .statIcon {
  background: #eef2ff;
  color: #6366f1;
}

.cardGreen .statIcon {
  background: #f0fdf4;
  color: #22c55e;
}

.cardAmber .statIcon {
  background: #fffbeb;
  color: #f59e0b;
}

.cardPurple .statIcon {
  background: #f5f3ff;
  color: #8b5cf6;
}

.statBody {
  flex: 1;
  min-width: 0;
}

.statValue {
  font-size: 28px;
  font-weight: 700;
  color: #0f172a;
  line-height: 1.1;
}

.statLabel {
  font-size: 13px;
  color: #64748b;
  margin-top: 4px;
}

.statTrend {
  position: absolute;
  top: 16px;
  right: 16px;
  font-size: 12px;
  font-weight: 600;
  color: #22c55e;
  background: #f0fdf4;
  padding: 2px 8px;
  border-radius: 999px;
}

/* Section */
.section {
  margin-bottom: 28px;
}

.sectionHeader {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;
}

.sectionTitle {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #0f172a;
}

.recentCard {
  background: #fff;
  border-radius: 16px;
  border: 1px solid #e2e8f0;
  overflow: hidden;
}

.empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 48px 24px;
  gap: 12px;
  color: #94a3b8;
  font-size: 14px;
}

.articleRow {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 20px;
  cursor: pointer;
  transition: background 0.15s ease;
  border-bottom: 1px solid #f1f5f9;
}

.articleRow:last-child {
  border-bottom: none;
}

.articleRow:hover {
  background: #f8fafc;
}

.articleInfo {
  flex: 1;
  min-width: 0;
}

.articleTitle {
  font-size: 14px;
  font-weight: 600;
  color: #0f172a;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.articleMeta {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: #94a3b8;
  margin-top: 4px;
}

.articleDate {
  flex-shrink: 0;
}

.statusDot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
}

.published {
  background: #22c55e;
}

.draft {
  background: #f59e0b;
}

.publishedText {
  color: #22c55e;
  font-weight: 500;
}

.draftText {
  color: #f59e0b;
  font-weight: 500;
}

/* Quick actions */
.quickGrid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 14px;
}

.quickCard {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 18px 20px;
  background: #fff;
  border-radius: 14px;
  border: 1px solid #e2e8f0;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  color: #334155;
  transition: all 0.2s ease;
}

.quickCard:hover {
  border-color: #6366f1;
  box-shadow: 0 4px 16px rgba(99, 102, 241, 0.12);
  transform: translateY(-1px);
}
</style>
