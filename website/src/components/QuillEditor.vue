<script setup lang="ts">
import Quill, { Delta } from "quill";
import { onMounted, ref } from "vue";
import "quill/dist/quill.snow.css";
const toolbarOptions = [
  ["bold", "italic", "underline", "strike"], // toggled buttons
  ["blockquote", "code-block"],
  ["link", "image", "video", "formula"],

  [{ header: 1 }, { header: 2 }], // custom button values
  [{ list: "ordered" }, { list: "bullet" }, { list: "check" }],
  [{ script: "sub" }, { script: "super" }], // superscript/subscript
  [{ indent: "-1" }, { indent: "+1" }], // outdent/indent
  [{ direction: "rtl" }], // text direction

  [{ size: ["small", false, "large", "huge"] }], // custom dropdown
  [{ header: [1, 2, 3, 4, 5, 6, false] }],

  [{ color: [] }, { background: [] }], // dropdown with defaults from theme
  [{ font: [] }],
  [{ align: [] }],

  ["clean"], // remove formatting button
];

const editorContainer = ref<HTMLDivElement | null>(null);
let quill: Quill | null = null;

const emit = defineEmits<{
  (e: "update:modelValue", value: Delta): void;
}>();

const props = defineProps<{
  modelValue: Delta;
}>();

onMounted(() => {
  if (editorContainer.value) {
    quill = new Quill(editorContainer.value, {
      theme: "snow",
      modules: {
        toolbar: toolbarOptions,
      },
      // toolbar: "#toolbar",
    });
    try {
      quill.setContents(
        typeof props.modelValue === "string"
          ? JSON.parse(props.modelValue)
          : props.modelValue,
      );
    } catch {
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
    <!-- <div ref="toolbar" id="toolbar" class="s['toolbar']"></div> -->
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
