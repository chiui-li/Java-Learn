import http from "@/request";
import { defineStore } from "pinia";
import { ref } from "vue";

export const useArticleStore = defineStore("article-store", () => {
  const articles = ref<D.Article[]>([]);

  const nextId = ref(1);
  const pageNum = ref(1);
  const pageSize = ref(10);
  const total = ref(0);


  async function queryArticles(opts?: { keyword?: string; pageNum?: number; pageSize?: number }) {
    const params = { keyword: opts?.keyword, pageNum: opts?.pageNum ?? pageNum.value, pageSize: opts?.pageSize ?? pageSize.value };
    const res = await http<D.ArticleListRes>("/posts/list", { params });
    articles.value = res.data?.list ?? [];
    total.value = res.data?.total ?? 0;
    pageNum.value = params.pageNum;
    nextId.value = (res.data?.total ?? 0) + 1;
  }

  return {
    articles,
    queryArticles,
    pageNum,
    pageSize,
    total,
  };
});
