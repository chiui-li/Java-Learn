// Placeholder upload service — 替换 getFileUrl 对接真实上传接口
//
// 真实对接示例 (使用 axios):
//   export async function getFileUrl(file: File): Promise<string> {
//     const form = new FormData()
//     form.append("file", file)
//     const { data } = await axios.post("/api/upload", form)
//     return data.url
//   }

export async function getFileUrl(file: File): Promise<string> {
  // TODO: 对接真实上传接口
  // 当前实现仅在浏览器端生成临时 URL，刷新后失效
  return URL.createObjectURL(file)
}
