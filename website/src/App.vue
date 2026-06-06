<script setup lang="ts">
import { RouterView } from "vue-router";
</script>
<template>
  <main :class="s.pages">
    <RouterView v-slot="{ Component }">
      <transition name="fade">
        <component :is="Component" />
      </transition>
    </RouterView>
  </main>
</template>

<style lang="less" module="s">
.pages {
  display: flex;
  width: 100%;
  min-height: 100vh;
}
</style>
