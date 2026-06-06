import Login from "@/pages/login/index.vue";
import Index from "@/pages/dashborad/index.vue";
// import Users from "./pages/users/index.vue";
import WelcomLayout from "./layouts/WelcomLayout.vue";
import BackendLayout from "./layouts/BackendLayout.vue";

export default [
  {
    path: "/welcome",
    component: WelcomLayout,
    children: [{ path: "user", component: Login }],
  },
  {
    path: "/backend",
    component: BackendLayout,
    children: [
      {
        path: "dashborad",
        component: Index,
        meta: {
          title: "仪表盘",
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
