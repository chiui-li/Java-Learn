import { defineStore } from "pinia";
import { ref } from "vue";

const now = () => new Date().toISOString().slice(0, 19).replace("T", " ");

export const useArticleStore = defineStore("article-store", () => {
  const articles = ref<D.Article[]>([
    {
      id: 1,
      userId: 1001,
      title: "Vue3 与 Element Plus 快速搭建后台管理",
      content:
        "本文讲解如何使用 Vue 3 + Element Plus 构建一个现代后台管理页面，包括路由、菜单以及表单编辑。",
      createdAt: "2026-06-01 09:30:00",
      updatedAt: "2026-06-02 11:20:00",
      viewCount: 248,
      postType: "article",
      draftContent: "",
      draftTitle: null,
      draftUpdatedAt: "2026-06-02 11:20:00",
      status: "published",
    },
    {
      id: 2,
      userId: 1002,
      title: "图文展示页面设计思路",
      content:
        "本文展示如何在后台页面中管理 images 类型文章，并提供列表与详情编辑能力。",
      createdAt: "2026-06-03 10:15:00",
      updatedAt: "2026-06-03 10:15:00",
      viewCount: 92,
      postType: "images",
      draftContent: "图文内容草稿示例",
      draftTitle: "图文内容草稿标题",
      draftUpdatedAt: "2026-06-03 10:15:00",
      status: "draft",
    },
    {
      id: 3,
      userId: 1003,
      title: "文章列表与详情页交互设计",
      content: "该文章介绍文章列表页与文章编辑页之间的数据流和页面联动设计。",
      createdAt: "2026-06-04 14:20:00",
      updatedAt: "2026-06-04 14:20:00",
      viewCount: 56,
      postType: "article",
      draftContent: "",
      draftTitle: null,
      draftUpdatedAt: "2026-06-04 14:20:00",
      status: "published",
    },
  ]);

  const nextId = ref(4);

  function findArticle(id: number) {
    return articles.value.find((article) => article.id === id);
  }

  function saveArticle(payload: Partial<Article> & { id?: number }) {
    if (payload.id != null) {
      const article = findArticle(payload.id);
      if (article) {
        Object.assign(article, payload, {
          updatedAt: now(),
        });
        return article;
      }
    }

    const created: Article = {
      id: nextId.value++,
      userId: payload.userId ?? 1000,
      title: payload.title ?? "未命名文章",
      content: payload.content ?? "",
      createdAt: now(),
      updatedAt: now(),
      viewCount: payload.viewCount ?? 0,
      postType: payload.postType ?? "article",
      draftContent: payload.draftContent ?? "",
      draftTitle: payload.draftTitle ?? null,
      draftUpdatedAt: payload.draftUpdatedAt ?? now(),
      status: payload.status ?? "draft",
    };

    articles.value.unshift(created);
    return created;
  }

  function deleteArticle(id: number) {
    articles.value = articles.value.filter((article) => article.id !== id);
  }

  return {
    articles,
    findArticle,
    saveArticle,
    deleteArticle,
  };
});
