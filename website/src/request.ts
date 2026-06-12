import axios, { type AxiosResponse } from "axios";
import router from "./router";
import { ElMessage } from "element-plus";

const ins = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  withCredentials: import.meta.env.DEV,
});

ins.interceptors.request.use((req) => {
  return req;
});

ins.interceptors.response.use(
  (res: AxiosResponse<any>) => {
    console.log("res ---->", res);
    if (res.status === 401) {
      router.replace("/back/welcome/user");
      return res.data;
    }
    if (res?.data?.errMsg) {
      ElMessage({ message: res?.data?.errMsg });
      throw new Error(res?.data?.errMsg);
    }
    return res.data;
  },
  (error) => {
    if (error.response.status === 401) {
      ElMessage({ message: "请登录" });
      router.replace("/back/welcome/user");
      return;
    }
    if (error.response) {
      ElMessage({ message: error.response });
      // router.replace("/welcome/user");
    }
    return Promise.reject(error);
  },
);

function http<T>(...args: Parameters<typeof ins>) {
  return ins(...args) as Promise<T>;
}

const post = <T>(...args: Parameters<typeof ins>) => {
  const [url = "", config = {}] = args;
  return http<T>(url, {
    ...config,
    method: "POST",
  });
};

http.post = post;

export { post };

export default http;
