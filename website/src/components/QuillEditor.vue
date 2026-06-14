<script setup lang="ts">
import Quill, { Delta } from "quill";
import { onMounted, ref } from "vue";
import "quill/dist/quill.snow.css";
import { getFileUrl } from "@/utils/upload";
import ImageResize from "@/components/quill/ImageResize";
import ImageBlot from "./quill/ImageBlot";
import VideoBlot from "./quill/VideoBlot";
import VideoResize from "./quill/VideoResize";
import { ElLoading, ElMessage } from "element-plus";
const Font = Quill.import("formats/font");

// Quill.register("modules/imageUploader", ImageUploader);

Quill.register("formats/image", ImageBlot);
Quill.register("formats/video", VideoBlot);
Quill.register("modules/ImageResize", ImageResize);
Quill.register("modules/VideoResize", VideoResize);

Font.whitelist = [
  "inter",
  "system",
  "jetbrains-mono",
  "fira-code",
  "source-code-pro",
  "cascadia-code",
  "ibm-plex-sans",
  "roboto",
];

const Size = Quill.import("attributors/style/size");

Size.whitelist = ["12px", "14px", "16px", "18px", "20px", "24px", "32px"];

Quill.register(Size, true);

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

      const typeLabel = embedType === "image" ? "图片" : "视频";
      const loading = ElLoading.service({
        target: editorContainer.value!,
        text: `正在上传${typeLabel}…`,
      });

      try {
        // 核心上传步骤，对接真实 OSS 后替换 getFileUrl 实现
        const url = await getFileUrl(file);
        const range = quill!.getSelection(true);
        const embedValue =
          embedType === "image" || embedType === "video" ? { url } : url;
        quill!.insertEmbed(range.index, embedType, embedValue, "user");
      } catch (e) {
        console.error(`[QuillEditor] ${embedType} upload failed:`, e);
        ElMessage.error(`${typeLabel}上传失败，请重试`);
      } finally {
        loading.close();
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
        // imageUploader: {
        //   upload: (file: File) => {
        //     return getFileUrl(file);
        //   },
        // },
        syntax: true,
        toolbar: {
          container: ".toolbar",
          handlers: {
            image: createUploadHandler("image"),
            video: createUploadHandler("video"),
          },
        },
        ImageResize: true,
        VideoResize: true,
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
  <div class="quill-editor editor-quill-instance">
    <div className="toolbar">
      <span className="ql-formats">
        <select className="ql-header" defaultValue="3">
          <option value="1">h1</option>
          <option value="2">h2</option>
          <option value="3">h3</option>
          <option value="4">h4</option>
          <option value="5">h5</option>
          <option value="6">h6</option>
        </select>
        <select className="ql-font" defaultValue="Roboto">
          <option value="inter">Inter</option>
          <option value="system">System UI</option>
          <option value="jetbrains-mono">JetBrains Mono</option>
          <option value="fira-code">Fira Code</option>
          <option value="source-code-pro">Source Code Pro</option>
          <option value="cascadia-code">Cascadia Code</option>
          <option value="ibm-plex-sans">IBM Plex Sans</option>
          <option value="roboto">Roboto</option>
        </select>
        <select className="ql-size" defaultValue="16px">
          <option value="12px">12</option>
          <option value="14px">14</option>
          <option value="16px">16</option>
          <option value="18px">18</option>
          <option value="20px">20</option>
          <option value="24px">24</option>
          <option value="32px">32</option>
        </select>
      </span>
      <span className="ql-formats">
        <button className="ql-bold"></button>
        <button className="ql-italic"></button>
        <button className="ql-underline"></button>
      </span>
      <!-- <span className="ql-formats" defaultValue="16px">
        <button value="12px">12</button>
        <button value="14px">14</button>
        <button value="16px">16</button>
        <button value="18px">18</button>
        <button value="20px">20</button>
        <button value="24px">24</button>
        <button value="32px">32</button>
      </span> -->
      <span className="ql-formats">
        <button className="ql-list" value="ordered"></button>
        <button className="ql-list" value="bullet"></button>
        <select className="ql-align" defaultValue="false">
          <option label="left"></option>
          <option label="center" value="center"></option>
          <option label="right" value="right"></option>
          <option label="justify" value="justify"></option>
        </select>
      </span>
      <span className="ql-formats">
        <button className="ql-link"></button>
        <button className="ql-image"></button>
        <button className="ql-video"></button>
      </span>
      <span className="ql-formats">
        <button className="ql-formula"></button>
        <button className="ql-code-block"></button>
      </span>
      <span className="ql-formats">
        <button className="ql-clean"></button>
      </span>
    </div>
    <div ref="editorContainer" class="quill-editor-content"></div>
  </div>
</template>

<style lang="less">
.quill-editor {
  width: 100%;
  .editor-container {
    min-height: 300px;
  }
}
</style>
