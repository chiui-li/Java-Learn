<script setup lang="ts">
import Quill, { Delta } from "quill";
import { onMounted, ref } from "vue";
import "quill/dist/quill.snow.css";
import { getFileUrl } from "@/utils/upload";
// import ImageBlot from "@/components/quill/ImageBlot";
import ImageResize from "@/components/quill/ImageResize";
import http from "@/request";
import ImageUploader from "quill-image-uploader";
import "quill-image-uploader/dist/quill.imageUploader.min.css";

Quill.register("modules/imageUploader", ImageUploader);

// 放行 blob: URL（getFileUrl 对接 OSS 后返回 http/https，届时自动走默认校验）
const VideoFormat = Quill.import("formats/video") as any;
const videoSanitize = VideoFormat.sanitize;
VideoFormat.sanitize = (url: string) => {
  if (url.startsWith("blob:")) return url;
  return videoSanitize(url);
};

// Quill.register("formats/image", ImageBlot);
Quill.register("modules/ImageResize", ImageResize);

const toolbarOptions = [
  ["bold", "italic", "underline", "strike"],
  ["blockquote", "code-block"],
  ["link", "image", "video", "formula"],

  [{ header: 1 }, { header: 2 }],
  [{ list: "ordered" }, { list: "bullet" }, { list: "check" }],
  [{ script: "sub" }, { script: "super" }],
  [{ indent: "-1" }, { indent: "+1" }],
  [{ direction: "rtl" }],

  [{ size: ["small", false, "large", "huge"] }],
  [{ header: [1, 2, 3, 4, 5, 6, false] }],

  [{ color: [] }, { background: [] }],
  [{ font: [] }],
  [{ align: [] }],

  ["clean"],
];

const editorContainer = ref<HTMLDivElement | null>(null);
let quill: Quill | null = null;

const emit = defineEmits<{
  (e: "update:modelValue", value: Delta): void;
}>();

const props = defineProps<{
  modelValue: Delta;
}>();

function createUploadHandler(embedType: "image" | "video") {
  return () => {
    if (!quill) return;
    const input = document.createElement("input");
    input.type = "file";
    input.accept = embedType === "image" ? "image/*" : "video/*";
    input.addEventListener("change", async () => {
      const file = input.files?.[0];
      if (!file) return;
      try {
        // 核心上传步骤，对接真实 OSS 后替换 getFileUrl 实现
        const url = await getFileUrl(file);
        const range = quill!.getSelection(true);
        const embedValue = embedType === "image" ? { url } : url;
        quill!.insertEmbed(range.index, embedType, embedValue, "user");
        // quill!.setSelection(range.index + 1);
      } catch (e) {
        console.error(`[QuillEditor] ${embedType} upload failed:`, e);
      }
    });
    input.click();
  };
}

defineExpose({
  getHtml: () => {
    return quill?.getSemanticHTML() || "";
  },
});

onMounted(() => {
  if (editorContainer.value) {
    quill = new Quill(editorContainer.value, {
      theme: "snow",
      modules: {
        imageUploader: {
          upload: (file: File) => {
            return getFileUrl(file);
          },
        },
        toolbar: {
          container: toolbarOptions,
          handlers: {
            image: createUploadHandler("image"),
            video: createUploadHandler("video"),
          },
        },
        ImageResize: true,
      },
    });

    try {
      quill.setContents(
        typeof props.modelValue === "string"
          ? JSON.parse(props.modelValue)
          : props.modelValue,
      );
    } catch {
      console.error(
        "[QuillEditor] Invalid initial content, loading empty editor",
      );
      quill.setContents(new Delta());
    }
    quill.on(Quill.events.TEXT_CHANGE, () => {
      emit("update:modelValue", quill?.getContents() || new Delta());
    });
  }
});
</script>

<template>
  <div :class="s['quill-editor']">
    <div ref="editorContainer" class="s['editor-container']"></div>
  </div>
</template>

<style lang="less" module="s">
.quill-editor {
  width: 100%;
  .editor-container {
    min-height: 300px;
  }
  :global {
    * {
      line-height: normal;
    }
  }
}
</style>
