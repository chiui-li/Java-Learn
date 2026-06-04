import axios from "axios";

const request = axios.create({
  baseURL: "http://localhost:8080",
  withCredentials: true,
});

request.interceptors.request.use((req) => {
  return req;
});

request.interceptors.response.use((res) => {
  return res;
});

export default request;
