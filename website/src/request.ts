import axios, { type AxiosResponse } from "axios";
import router from "./router";

const ins = axios.create({
  baseURL: "http://localhost:8080",
  withCredentials: true,
});

ins.interceptors.request.use((req) => {
  return req;
});

ins.interceptors.response.use(
  (res: AxiosResponse<any>) => {
    if (res.status === 401) {
      router.replace("/welcome/user");
      return res.data;
    }

    return res.data;
  },
  (error) => {
    console.error("请求错误:", error);
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
