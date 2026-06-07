<script setup lang="ts">
import http from "@/request";
import { useUserInfo } from "@/store/useUser";
import { computed, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";

const router = useRouter();
const route = useRoute();

const menuRoutes = computed(() =>
  router
    .getRoutes()
    .filter(
      (item) =>
        item.path.startsWith("/backend/") &&
        item.meta?.title &&
        !item.path.includes(":"),
    )
    .map((item) => ({ path: item.path, title: String(item.meta?.title) })),
);

const userInfo = useUserInfo()

onMounted(() => {
  userInfo.who()
})


async function logout() {
  await http("/user/logout")
  router.push("/welcome/user")
}

const activeIndex = computed(() => route.path);

</script>

<template>
  <div :class="s.layout">
    <aside :class="s.sidebar">
      <div :class="s.sidebarInner">
        <div :class="s.brand">
          <div :class="s.brandLogo">
            <svg width="32" height="32" viewBox="0 0 32 32" fill="none">
              <rect width="32" height="32" rx="8" fill="url(#brand-grad)" />
              <path d="M10 22V10l6 8 6-8v12" stroke="#fff" stroke-width="2.5" stroke-linecap="round"
                stroke-linejoin="round" fill="none" />
              <defs>
                <linearGradient id="brand-grad" x1="0" y1="0" x2="32" y2="32">
                  <stop stop-color="#6366f1" />
                  <stop offset="1" stop-color="#8b5cf6" />
                </linearGradient>
              </defs>
            </svg>
          </div>
          <div :class="s.brandText">
            <div :class="s.brandTitle"></div>
            <div :class="s.brandSubtitle">管理后台</div>
          </div>
        </div>

        <div :class="s.menuSection">
          <div :class="s.menuLabel">导航菜单</div>
          <el-menu :default-active="activeIndex" :router="true" background-color="transparent" text-color="#94a3b8"
            active-text-color="#ffffff">
            <el-menu-item v-for="item in menuRoutes" :key="item.path" :index="item.path">
              <!-- <svg class="menu-icon" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round">
                <path :d="menuIcons[item.title] || 'M12 6v6m0 0v6m0-6h6m-6 0H6'" />
              </svg> -->
              <span>{{ item.title }}</span>
            </el-menu-item>
          </el-menu>
        </div>

        <div :class="s.userSection">
          <div :class="s.userAvatar">
            <svg width="36" height="36" viewBox="0 0 36 36" fill="none">
              <circle cx="18" cy="18" r="18" fill="url(#avatar-grad)" />
              <path d="M18 16a4 4 0 100-8 4 4 0 000 8zm-8 10c0-4.418 3.582-8 8-8s8 3.582 8 8" stroke="#fff"
                stroke-width="1.8" fill="none" />
              <defs>
                <linearGradient id="avatar-grad" x1="0" y1="0" x2="36" y2="36">
                  <stop stop-color="#6366f1" />
                  <stop offset="1" stop-color="#4f46e5" />
                </linearGradient>
              </defs>
            </svg>
          </div>
          <div :class="s.userInfo">
            <div :class="s.userName">用户</div>
            <div :class="s.userRole">{{ userInfo?.user?.email }}</div>
          </div>
          <div :class="s.logout" @click="logout">注销</div>
          <div :class="s.userStatus" />
        </div>
      </div>
    </aside>
    <div :class="s.content">
      <router-view />
    </div>
  </div>
</template>

<style lang="less" module="s">
.layout {
  display: flex;
  width: 100%;
  height: 100vh;
  overflow: hidden;
}

.logout {
  cursor: pointer;
  color: #fff;
  font-size: small;
}

.sidebar {
  width: 240px;
  height: 100vh;
  overflow: hidden;
  background: #0f172a;
  position: relative;
  flex-shrink: 0;
  overflow: hidden;
}

.sidebarInner {
  display: flex;
  flex-direction: column;
  height: 100%;
  padding: 20px 0;
  position: relative;
  z-index: 1;
}

.brand {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 0 20px 20px;
  border-bottom: 1px solid rgba(148, 163, 184, 0.1);
  margin-bottom: 16px;
}

.brandLogo {
  flex-shrink: 0;
}

.brandText {
  flex: 1;
  min-width: 0;
}

.brandTitle {
  font-size: 17px;
  font-weight: 700;
  color: #f1f5f9;
  letter-spacing: -0.01em;
}

.brandSubtitle {
  font-size: 12px;
  color: #64748b;
  margin-top: 2px;
}

.menuSection {
  flex: 1;
  overflow-y: auto;
}

.menuLabel {
  font-size: 11px;
  font-weight: 600;
  color: #475569;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  padding: 0 20px;
  margin-bottom: 12px;
}

.userSection {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 14px 16px;
  margin: auto 12px 12px;
  background: rgba(255, 255, 255, 0.04);
  border-radius: 12px;
}

.userAvatar {
  flex-shrink: 0;
}

.userInfo {
  flex: 1;
  min-width: 0;
}

.userName {
  font-size: 13px;
  font-weight: 600;
  color: #e2e8f0;
}

.userRole {
  font-size: 11px;
  color: #64748b;
  margin-top: 1px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.userStatus {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #22c55e;
  box-shadow: 0 0 0 2px rgba(34, 197, 94, 0.2);
  flex-shrink: 0;
}

.content {
  flex: 1;
  padding: 28px 32px;
  height: 100vh;

  background: #f1f5f9;
  overflow-y: auto;
}
</style>
