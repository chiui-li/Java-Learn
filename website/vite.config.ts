import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import AutoImport from "unplugin-auto-import/vite";
import Components from "unplugin-vue-components/vite";
import { ElementPlusResolver } from "unplugin-vue-components/resolvers";
import path from "path";
import viteCompression from "vite-plugin-compression";

// https://vite.dev/config/
export default defineConfig({
  // base: import.meta?.env?.base || "/",
  plugins: [
    vue(),
    AutoImport({
      resolvers: [ElementPlusResolver()],
    }),
    Components({
      resolvers: [ElementPlusResolver()],
    }),
    viteCompression({
      verbose: true, // 是否在控制台输出压缩结果
      threshold: 1024, // 只对大于 10KB 的文件进行压缩
      algorithm: "gzip", // 压缩算法，可选 'gzip' 或 'brotliCompress' (br)
      ext: ".gz", // 生成的压缩文件后缀
      // deleteOriginFile: trufae, // 压缩后是否删除源文件，建议设为 false
    }),
  ],
  resolve: {
    alias: {
      // 关键2：将 '@' 指向 'src' 目录
      "@": path.resolve(__dirname, "src"),
    },
  },
});
