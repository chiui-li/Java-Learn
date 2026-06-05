import Login from "@/pages/login/index.vue";
// import Users from "./pages/users/index.vue";
import WelcomLayout from "./layouts/WelcomLayout.vue";

export default [
  {
    path: "/welcome",
    component: WelcomLayout,
    children: [{ path: "user", component: Login }],
  },
];
