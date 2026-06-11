<script setup lang="ts">
import { watch, ref } from "vue";
import { useEditor, EditorContent } from "@tiptap/vue-3";
import StarterKit from "@tiptap/starter-kit";
import Underline from "@tiptap/extension-underline";
import Link from "@tiptap/extension-link";
import Placeholder from "@tiptap/extension-placeholder";
import { Node } from "@tiptap/core";
import Image from "@tiptap/extension-image";
import CodeBlockLowlight from "@tiptap/extension-code-block-lowlight";
import { common, createLowlight } from "lowlight";
import { getFileUrl } from "@/utils/upload";

const lowlight = createLowlight(common);

const VideoNode = Node.create({
  name: "video",
  group: "block",
  atom: true,
  addAttributes() {
    return {
      src: { default: null, parseHTML: (el) => el.getAttribute("src") },
    };
  },
  parseHTML() {
    return [{ tag: "video" }];
  },
  renderHTML({ HTMLAttributes }) {
    return ["video", { controls: true, ...HTMLAttributes }];
  },
  addCommands() {
    return {
      setVideo:
        (options) =>
        ({ commands }) => {
          return commands.insertContent({ type: this.name, attrs: options });
        },
    };
  },
});

const props = defineProps<{ modelValue?: string }>();
const emit = defineEmits<{ "update:modelValue": [value: string] }>();

const linkDialog = ref(false);
const linkUrl = ref("");
const imageDialog = ref(false);
const imageUrl = ref("");
const videoDialog = ref(false);
const videoUrl = ref("");

const editor = useEditor({
  content: props.modelValue,
  extensions: [
    StarterKit.configure({
      heading: { levels: [1, 2, 3] },
      codeBlock: {
        tabSize: 2,
      },
    }),
    Underline,
    Link.configure({
      openOnClick: false,
      HTMLAttributes: { rel: "noopener noreferrer" },
    }),
    Placeholder.configure({
      placeholder: "请输入文章正文...",
    }),
    CodeBlockLowlight.configure({ lowlight }),
    Image.configure({
      inline: true,
      resize: {
        enabled: true,
        directions: ["top", "bottom", "left", "right"], // can be any direction or diagonal combination
        minWidth: 50,
        minHeight: 50,
        alwaysPreserveAspectRatio: true,
      },
    }),
    VideoNode,
  ],
  onUpdate({ editor }) {
    emit("update:modelValue", editor.getHTML());
  },
});

watch(
  () => props.modelValue,
  (val) => {
    if (editor.value && val !== editor.value.getHTML()) {
      editor.value.commands.setContent(val || "", {});
    }
  },
);

function exec(fn: () => boolean | undefined) {
  fn();
  editor.value?.chain().focus().run();
}

function setHeading(level: number | null) {
  if (!editor.value) return;
  if (level === null) {
    editor.value.chain().focus().setParagraph().run();
  } else {
    editor.value.chain().focus().toggleHeading({ level }).run();
  }
}

function openLink() {
  if (!editor.value) return;
  const prev = editor.value.getAttributes("link").href;
  linkUrl.value = prev || "";
  linkDialog.value = true;
}

function applyLink() {
  if (!editor.value) return;
  const url = linkUrl.value.trim();
  if (url) {
    editor.value.chain().focus().setLink({ href: url }).run();
  } else {
    editor.value.chain().focus().unsetLink().run();
  }
  linkDialog.value = false;
}

function removeLink() {
  if (!editor.value) return;
  editor.value.chain().focus().unsetLink().run();
  linkDialog.value = false;
}

function addImage() {
  if (!editor.value) return;
  const url = imageUrl.value.trim();
  if (url) {
    editor.value.chain().focus().setImage({ src: url }).run();
  }
  imageDialog.value = false;
  imageUrl.value = "";
}

function uploadImage(e) {
  const file = e.target.files?.[0];
  if (!file) return;
  getFileUrl(file).then((url) => {
    if (url && editor.value) {
      editor.value.chain().focus().setImage({ src: url }).run();
    }
    imageDialog.value = false;
  });
}

function addVideo() {
  if (!editor.value) return;
  const url = videoUrl.value.trim();
  if (url) {
    editor.value.chain().focus().setVideo({ src: url }).run();
  }
  videoDialog.value = false;
  videoUrl.value = "";
}

function uploadVideo(e) {
  const file = e.target.files?.[0];
  if (!file) return;
  getFileUrl(file).then((url) => {
    if (url && editor.value) {
      editor.value.chain().focus().setVideo({ src: url }).run();
    }
    videoDialog.value = false;
  });
}
</script>

<template>
  <div class="editorWrap" v-if="editor">
    <div class="toolbar">
      <div class="toolbarGroup">
        <button
          type="button"
          :class="['toolBtn', { active: editor.isActive('bold') }]"
          title="加粗"
          @click="exec(() => editor?.chain().toggleBold().run())"
        >
          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2.5"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <path d="M6 4h8a4 4 0 014 4 4 4 0 01-4 4H6z" />
            <path d="M6 12h9a4 4 0 014 4 4 4 0 01-4 4H6z" />
          </svg>
        </button>
        <button
          type="button"
          :class="['toolBtn', { active: editor.isActive('italic') }]"
          title="斜体"
          @click="exec(() => editor?.chain().toggleItalic().run())"
        >
          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2.5"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <line x1="19" y1="4" x2="10" y2="4" />
            <line x1="14" y1="20" x2="5" y2="20" />
            <line x1="15" y1="4" x2="9" y2="20" />
          </svg>
        </button>
        <button
          type="button"
          :class="['toolBtn', { active: editor.isActive('underline') }]"
          title="下划线"
          @click="exec(() => editor?.chain().toggleUnderline().run())"
        >
          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2.5"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <path d="M6 3v7a6 6 0 006 6 6 6 0 006-6V3" />
            <line x1="4" y1="21" x2="20" y2="21" />
          </svg>
        </button>
        <button
          type="button"
          :class="['toolBtn', { active: editor.isActive('strike') }]"
          title="删除线"
          @click="exec(() => editor?.chain().toggleStrike().run())"
        >
          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2.5"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <path
              d="M17.12 18.12c-.95.64-2.23 1.12-3.62 1.12-3.31 0-5.5-1.57-5.5-4.5 0-1.12.25-2.02.81-2.74"
            />
            <path
              d="M6.88 5.88C7.8 5.08 9.14 4.5 10.5 4.5c3.31 0 5.5 1.57 5.5 4.5 0 .54-.09 1.04-.25 1.5"
            />
            <line x1="3" y1="12" x2="21" y2="12" />
          </svg>
        </button>
      </div>

      <div class="divider"></div>

      <div class="toolbarGroup">
        <button
          type="button"
          :class="[
            'toolBtn',
            { active: editor.isActive('heading', { level: 1 }) },
          ]"
          title="标题 1"
          @click="setHeading(1)"
        >
          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2.5"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <path d="M3 12h10" />
            <path d="M3 6v12" />
            <path d="M13 6v12" />
            <path d="M18 10l3 4" />
            <path d="M21 10l-3 4" />
          </svg>
        </button>
        <button
          type="button"
          :class="[
            'toolBtn',
            { active: editor.isActive('heading', { level: 2 }) },
          ]"
          title="标题 2"
          @click="setHeading(2)"
        >
          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2.5"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <path d="M3 12h10" />
            <path d="M3 6v12" />
            <path d="M13 6v12" />
            <path d="M17 12l3-4" />
            <path d="M20 12l-3 4" />
          </svg>
        </button>
        <button
          type="button"
          :class="[
            'toolBtn',
            {
              active:
                editor.isActive('paragraph') && !editor.isActive('heading'),
            },
          ]"
          title="正文"
          @click="setHeading(null)"
        >
          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2.5"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <line x1="3" y1="6" x2="21" y2="6" />
            <line x1="3" y1="10" x2="15" y2="10" />
            <line x1="3" y1="14" x2="18" y2="14" />
            <line x1="3" y1="18" x2="12" y2="18" />
          </svg>
        </button>
      </div>

      <div class="divider"></div>

      <div class="toolbarGroup">
        <button
          type="button"
          :class="['toolBtn', { active: editor.isActive('bulletList') }]"
          title="无序列表"
          @click="exec(() => editor?.chain().toggleBulletList().run())"
        >
          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2.5"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <line x1="9" y1="6" x2="21" y2="6" />
            <line x1="9" y1="12" x2="21" y2="12" />
            <line x1="9" y1="18" x2="21" y2="18" />
            <circle cx="4" cy="6" r="1" />
            <circle cx="4" cy="12" r="1" />
            <circle cx="4" cy="18" r="1" />
          </svg>
        </button>
        <button
          type="button"
          :class="['toolBtn', { active: editor.isActive('orderedList') }]"
          title="有序列表"
          @click="exec(() => editor?.chain().toggleOrderedList().run())"
        >
          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2.5"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <line x1="10" y1="6" x2="21" y2="6" />
            <line x1="10" y1="12" x2="21" y2="12" />
            <line x1="10" y1="18" x2="21" y2="18" />
            <path d="M4 6h1v4" />
            <path d="M4 10h2" />
            <path d="M6 18H4c0-1 2-2 2-3s-1-1.5-2-1" />
          </svg>
        </button>
        <button
          type="button"
          :class="['toolBtn', { active: editor.isActive('blockquote') }]"
          title="引用"
          @click="exec(() => editor?.chain().toggleBlockquote().run())"
        >
          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2.5"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <path
              d="M3 21c3 0 7-1 7-8V5c0-1.25-.756-2.017-2-2H4c-1.25 0-2 .75-2 1.972V11c0 1.25.75 2 2 2 1 0 1 0 1 1v1c0 1-1 2-2 2s-1 .008-1 1.031V20c0 1 0 1 1 1z"
            />
            <path
              d="M15 21c3 0 7-1 7-8V5c0-1.25-.757-2.017-2-2h-4c-1.25 0-2 .75-2 1.972V11c0 1.25.75 2 2 2h.75c0 2.25.25 4-2.75 4v3c0 1 0 1 1 1z"
            />
          </svg>
        </button>
        <button
          type="button"
          :class="['toolBtn', { active: editor.isActive('code') }]"
          title="行内代码"
          @click="exec(() => editor?.chain().toggleCode().run())"
        >
          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2.5"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <polyline points="10 15 6 12 10 9" />
            <polyline points="14 9 18 12 14 15" />
          </svg>
        </button>
        <button
          type="button"
          :class="['toolBtn', { active: editor.isActive('codeBlock') }]"
          title="代码块"
          @click="exec(() => editor?.chain().toggleCodeBlock().run())"
        >
          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2.5"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <rect x="3" y="4" width="18" height="16" rx="2" />
            <line x1="8" y1="10" x2="16" y2="10" />
            <line x1="8" y1="14" x2="14" y2="14" />
          </svg>
        </button>
      </div>

      <div class="divider"></div>

      <div class="toolbarGroup">
        <button
          type="button"
          :class="['toolBtn', { active: editor.isActive('link') }]"
          title="链接"
          @click="openLink"
        >
          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2.5"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <path
              d="M10 13a5 5 0 007.54.54l3-3a5 5 0 00-7.07-7.07l-1.72 1.71"
            />
            <path
              d="M14 11a5 5 0 00-7.54-.54l-3 3a5 5 0 007.07 7.07l1.71-1.71"
            />
          </svg>
        </button>
      </div>

      <div class="toolbarGroup">
        <button
          type="button"
          :class="['toolBtn']"
          title="图片"
          @click="imageDialog = true"
        >
          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2.5"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <rect x="3" y="3" width="18" height="18" rx="2" />
            <circle cx="8.5" cy="8.5" r="1.5" />
            <polyline points="21 15 16 10 5 21" />
          </svg>
        </button>
        <button
          type="button"
          :class="['toolBtn']"
          title="视频"
          @click="videoDialog = true"
        >
          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2.5"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <polygon points="5 3 19 12 5 21 5 3" />
          </svg>
        </button>
      </div>

      <div class="divider"></div>

      <div class="toolbarGroup">
        <button
          type="button"
          class="toolBtn"
          title="撤销"
          @click="exec(() => editor.chain().undo().run())"
        >
          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2.5"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <polyline points="1 4 1 10 7 10" />
            <path d="M3.51 15a9 9 0 102.13-9.36L1 10" />
          </svg>
        </button>
        <button
          type="button"
          class="toolBtn"
          title="重做"
          @click="exec(() => editor.chain().redo().run())"
        >
          <svg
            width="16"
            height="16"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2.5"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <polyline points="23 4 23 10 17 10" />
            <path d="M20.49 15a9 9 0 11-2.12-9.36L23 10" />
          </svg>
        </button>
      </div>
    </div>

    <EditorContent :editor="editor" class="editorContent" />

    <!-- Link dialog -->
    <teleport to="body">
      <div
        :class="['overlay', { show: linkDialog }]"
        @click.self="linkDialog = false"
      >
        <div class="linkModal">
          <div class="linkModalHeader">
            <span>插入链接</span>
            <button type="button" class="linkClose" @click="linkDialog = false">
              <svg
                width="16"
                height="16"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2.5"
                stroke-linecap="round"
                stroke-linejoin="round"
              >
                <line x1="18" y1="6" x2="6" y2="18" />
                <line x1="6" y1="6" x2="18" y2="18" />
              </svg>
            </button>
          </div>
          <input
            v-model="linkUrl"
            placeholder="请输入链接地址"
            class="linkInput"
            @keyup.enter="applyLink"
          />
          <div class="linkActions">
            <button type="button" class="linkRemoveBtn" @click="removeLink">
              移除链接
            </button>
            <button type="button" class="linkApplyBtn" @click="applyLink">
              确定
            </button>
          </div>
        </div>
      </div>
    </teleport>

    <!-- Image dialog -->
    <teleport to="body">
      <div
        :class="['overlay', { show: imageDialog }]"
        @click.self="imageDialog = false"
      >
        <div class="linkModal">
          <div class="linkModalHeader">
            <span>插入图片</span>
            <button
              type="button"
              class="linkClose"
              @click="imageDialog = false"
            >
              <svg
                width="16"
                height="16"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2.5"
                stroke-linecap="round"
                stroke-linejoin="round"
              >
                <line x1="18" y1="6" x2="6" y2="18" />
                <line x1="6" y1="6" x2="18" y2="18" />
              </svg>
            </button>
          </div>
          <p class="uploadHint">粘贴图片地址或选择文件上传</p>
          <input
            v-model="imageUrl"
            placeholder="请输入图片地址"
            class="linkInput"
            @keyup.enter="addImage"
          />
          <label class="uploadBtn">
            <svg
              width="14"
              height="14"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
              style="margin-right: 6px"
            >
              <path d="M21 15v4a2 2 0 01-2 2H5a2 2 0 01-2-2v-4" />
              <polyline points="17 8 12 3 7 8" />
              <line x1="12" y1="3" x2="12" y2="15" />
            </svg>
            选择图片文件
            <input type="file" accept="image/*" hidden @change="uploadImage" />
          </label>
          <div class="linkActions">
            <button
              type="button"
              class="linkRemoveBtn"
              @click="imageDialog = false"
            >
              取消
            </button>
            <button type="button" class="linkApplyBtn" @click="addImage">
              插入
            </button>
          </div>
        </div>
      </div>
    </teleport>

    <!-- Video dialog -->
    <teleport to="body">
      <div
        :class="['overlay', { show: videoDialog }]"
        @click.self="videoDialog = false"
      >
        <div class="linkModal">
          <div class="linkModalHeader">
            <span>插入视频</span>
            <button
              type="button"
              class="linkClose"
              @click="videoDialog = false"
            >
              <svg
                width="16"
                height="16"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2.5"
                stroke-linecap="round"
                stroke-linejoin="round"
              >
                <line x1="18" y1="6" x2="6" y2="18" />
                <line x1="6" y1="6" x2="18" y2="18" />
              </svg>
            </button>
          </div>
          <p class="uploadHint">粘贴视频链接或选择文件上传</p>
          <input
            v-model="videoUrl"
            placeholder="请输入视频地址"
            class="linkInput"
            @keyup.enter="addVideo"
          />
          <label class="uploadBtn">
            <svg
              width="14"
              height="14"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
              style="margin-right: 6px"
            >
              <path d="M21 15v4a2 2 0 01-2 2H5a2 2 0 01-2-2v-4" />
              <polyline points="17 8 12 3 7 8" />
              <line x1="12" y1="3" x2="12" y2="15" />
            </svg>
            选择视频文件
            <input type="file" accept="video/*" hidden @change="uploadVideo" />
          </label>
          <div class="linkActions">
            <button
              type="button"
              class="linkRemoveBtn"
              @click="videoDialog = false"
            >
              取消
            </button>
            <button type="button" class="linkApplyBtn" @click="addVideo">
              插入
            </button>
          </div>
        </div>
      </div>
    </teleport>
  </div>
</template>

<style scoped>
.editorWrap {
  width: 100%;
  border: 1px solid #e2e8f0;
  border-radius: 10px;
  overflow: hidden;
  background: #fff;
}

.toolbar {
  display: flex;
  align-items: center;
  gap: 2px;
  padding: 6px 8px;
  border-bottom: 1px solid #e2e8f0;
  background: #f8fafc;
  flex-wrap: wrap;
}

.toolbarGroup {
  display: flex;
  align-items: center;
  gap: 1px;
}

.toolBtn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  border: none;
  background: transparent;
  border-radius: 6px;
  cursor: pointer;
  color: #475569;
  transition: all 0.15s ease;
}

.toolBtn:hover {
  background: #e2e8f0;
  color: #0f172a;
}

.toolBtn.active {
  background: #eef2ff;
  color: #6366f1;
}

.divider {
  width: 1px;
  height: 20px;
  background: #e2e8f0;
  margin: 0 4px;
  flex-shrink: 0;
}

.editorContent {
  padding: 16px 20px;
  min-height: 360px;
}

.editorContent :deep(.ProseMirror) {
  outline: none;
  min-height: 320px;
  font-size: 15px;
  line-height: 1.8;
  color: #0f172a;
}

.editorContent :deep(.ProseMirror p) {
  margin: 0 0 8px;
}

.editorContent :deep(.ProseMirror p.is-editor-empty:first-child::before) {
  content: attr(data-placeholder);
  float: left;
  color: #94a3b8;
  pointer-events: none;
  height: 0;
}

.editorContent :deep(.ProseMirror h1) {
  font-size: 22px;
  font-weight: 700;
  margin: 20px 0 10px;
  color: #0f172a;
  line-height: 1.4;
}

.editorContent :deep(.ProseMirror h2) {
  font-size: 18px;
  font-weight: 700;
  margin: 16px 0 8px;
  color: #0f172a;
  line-height: 1.4;
}

.editorContent :deep(.ProseMirror h3) {
  font-size: 16px;
  font-weight: 600;
  margin: 14px 0 6px;
  color: #0f172a;
  line-height: 1.4;
}

.editorContent :deep(.ProseMirror ul),
.editorContent :deep(.ProseMirror ol) {
  padding-left: 24px;
  margin: 8px 0;
}

.editorContent :deep(.ProseMirror li) {
  margin: 4px 0;
}

.editorContent :deep(.ProseMirror blockquote) {
  border-left: 3px solid #6366f1;
  margin: 12px 0;
  padding: 8px 16px;
  color: #475569;
  background: #f8fafc;
  border-radius: 0 6px 6px 0;
}

.editorContent :deep(.ProseMirror a) {
  color: #6366f1;
  text-decoration: underline;
  cursor: pointer;
}

.editorContent :deep(.ProseMirror code) {
  background: #f1f5f9;
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 13px;
  color: #e11d48;
}

.editorContent :deep(.ProseMirror pre) {
  background: #0f172a;
  color: #e2e8f0;
  padding: 16px;
  border-radius: 8px;
  overflow-x: auto;
  margin: 12px 0;
}

.editorContent :deep(.ProseMirror pre code) {
  background: none;
  color: inherit;
  padding: 0;
  font-size: 14px;
}

.editorContent :deep(.ProseMirror hr) {
  border: none;
  border-top: 1px solid #e2e8f0;
  margin: 16px 0;
}

/* Link modal */
.overlay {
  position: fixed;
  inset: 0;
  background: rgba(15, 23, 42, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
  opacity: 0;
  pointer-events: none;
  transition: opacity 0.2s ease;
}

.overlay.show {
  opacity: 1;
  pointer-events: auto;
}

.linkModal {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  width: 380px;
  box-shadow: 0 20px 60px rgba(15, 23, 42, 0.12);
}

.linkModalHeader {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 15px;
  font-weight: 600;
  color: #0f172a;
  margin-bottom: 14px;
}

.linkClose {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
  border: none;
  background: transparent;
  border-radius: 6px;
  cursor: pointer;
  color: #94a3b8;
}

.linkClose:hover {
  background: #f1f5f9;
  color: #475569;
}

.linkInput {
  width: 100%;
  padding: 10px 14px;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  font-size: 14px;
  outline: none;
  box-sizing: border-box;
  transition: border-color 0.2s ease;
}

.linkInput:focus {
  border-color: #6366f1;
}

.linkActions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 14px;
}

.linkRemoveBtn {
  padding: 8px 16px;
  border: 1px solid #e2e8f0;
  background: transparent;
  border-radius: 8px;
  font-size: 13px;
  color: #64748b;
  cursor: pointer;
}

.linkRemoveBtn:hover {
  border-color: #cbd5e1;
  color: #475569;
}

.linkApplyBtn {
  padding: 8px 20px;
  border: none;
  background: #6366f1;
  color: #fff;
  border-radius: 8px;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
}

.linkApplyBtn:hover {
  background: #4f46e5;
}

/* Syntax highlighting */
.editorContent :deep(.ProseMirror pre) {
  background: #0f172a !important;
  color: #e2e8f0;
  padding: 16px !important;
  border-radius: 8px;
  overflow-x: auto;
  margin: 12px 0;
}

.editorContent :deep(.ProseMirror pre code) {
  background: none !important;
  color: inherit;
  padding: 0;
  font-size: 14px;
  line-height: 1.6;
}

.editorContent :deep(.hljs-keyword) {
  color: #c084fc;
}

.editorContent :deep(.hljs-string) {
  color: #34d399;
}

.editorContent :deep(.hljs-number) {
  color: #fbbf24;
}

.editorContent :deep(.hljs-comment) {
  color: #64748b;
  font-style: italic;
}

.editorContent :deep(.hljs-built_in) {
  color: #60a5fa;
}

.editorContent :deep(.hljs-title) {
  color: #f472b6;
}

.editorContent :deep(.hljs-params) {
  color: #e2e8f0;
}

.editorContent :deep(.hljs-literal) {
  color: #c084fc;
}

.editorContent :deep(.hljs-attr) {
  color: #fbbf24;
}

.editorContent :deep(.hljs-selector-tag) {
  color: #c084fc;
}

.editorContent :deep(.hljs-selector-class) {
  color: #f472b6;
}

/* Video element in editor */
.editorContent :deep(video) {
  max-width: 100%;
  border-radius: 8px;
  margin: 12px 0;
}

/* Upload button */
.uploadBtn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  padding: 10px 14px;
  margin-top: 10px;
  border: 1px dashed #cbd5e1;
  border-radius: 8px;
  font-size: 13px;
  color: #64748b;
  cursor: pointer;
  transition: all 0.15s ease;
  box-sizing: border-box;
}

.uploadBtn:hover {
  border-color: #6366f1;
  color: #6366f1;
  background: #f8fafc;
}

.uploadHint {
  font-size: 13px;
  color: #94a3b8;
  margin: -8px 0 14px;
}
</style>
