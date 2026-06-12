import Login from "@/pages/login/index.vue";
import Index from "@/pages/dashborad/index.vue";
import WelcomLayout from "./layouts/WelcomLayout.vue";
import BackendLayout from "./layouts/BackendLayout.vue";

export default [
  { path: "/back", redirect: { path: "/back/backend/dashborad" } },
  {
    path: "/back/welcome",
    component: WelcomLayout,
    children: [{ path: "user", component: Login }],
  },
  {
    path: "/back/backend",
    component: BackendLayout,
    name: "backend",
    children: [
      {
        path: "dashborad",
        component: Index,
        meta: {
          title: "仪表盘",
        },
      },
      {
        path: "profile",
        component: () => import("./pages/users/profile.vue"),
        meta: {
          title: "个人设置",
        },
      },
      {
        path: "articles",
        name: "ArticleList",
        component: () => import("./pages/articles/index.vue"),
        meta: {
          title: "文章列表",
        },
      },
      {
        path: "articles/new",
        name: "ArticleCreate",
        component: () => import("./pages/articles/edit.vue"),
      },
      {
        path: "articles/:id/edit",
        name: "ArticleEdit",
        component: () => import("./pages/articles/edit.vue"),
      },
    ],
  },
];
