import { createApp } from "vue";
import "./style.css";
import App from "./App.vue";
import { createPinia } from "pinia";
const app = createApp(App);
const pinia = createPinia();
import "normalize.css";
import router from "./router.ts";

app.use(pinia);
app.use(router);
app.mount("#app");
