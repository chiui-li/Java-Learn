/**
 * 七牛云 OSS 文件上传
 *
 * 流程：
 *   1. 请求后端获取 Qiniu 上传 token（GET /user/upload/token）
 *   2. 将文件直接上传至七牛
 *   3. 返回 CDN 可访问 URL
 *
 * 后端需提供：
 *   GET /user/upload/token → { token: string, domain: string }
 *   其中 domain 是绑定的 CDN 域名（如 https://cdn.example.com）
 *
 * 七牛上传区域（uploadUrl）：
 *   华东: https://upload.qiniup.com
 *   华北: https://upload-z1.qiniup.com
 *   华南: https://upload-z2.qiniup.com
 *   北美: https://upload-na0.qiniup.com
 *   东南亚: https://upload-as0.qiniup.com
 */

import http from "@/request";
import imageCompression from "browser-image-compression";
const uploadUrl = "https://upload.qiniup.com"; // 按需修改

const domain = "http://tggdle3bb.hd-bkt.clouddn.com"; // 后端返回的 CDN 域名

// /**
//  * 获取七牛 OSS 访问 URL
//  *
//  * @param file - 待上传的文件
//  * @returns CDN 可访问的完整 URL
//  */
// export async function getFileUrl(file: File): Promise<string> {
//   const token = await getUploadToken();

//   const form = new FormData();
//   form.append("token", token);
//   form.append("file", file);

//   const resp = await fetch(uploadUrl, {
//     method: "POST",
//     body: form,
//   });

//   if (!resp.ok) {
//     throw new Error(`七牛上传失败 (${resp.status}): ${await resp.text()}`);
//   }

//   const result: { hash: string; key: string } = await resp.json();
//   return `${domain}/${result.key}`;
// }

async function compressImageIfNeeded(file: File): Promise<File> {
  if (!file.type.startsWith("image/")) {
    return file;
  }

  try {
    return await imageCompression(file, {
      maxSizeMB: 0.5,
      maxWidthOrHeight: 1920,
      useWebWorker: true,
      fileType: file.type,
    });
  } catch (error) {
    console.warn("图片压缩失败，使用原文件上传", error);
    return file;
  }
}

export async function getFileUrl(file: File): Promise<string> {
  const compressedFile = await compressImageIfNeeded(file);
  const formData = new FormData();
  formData.append("file", compressedFile);
  formData.append("ext", "." + compressedFile.name.split(".").at(-1));
  const b = await http.post<D.Result<string>>("/user/upload", {
    data: formData,
  });

  return `${import.meta.env.VITE_API_BASE_URL}/getFile/${b.data!}`;
}
