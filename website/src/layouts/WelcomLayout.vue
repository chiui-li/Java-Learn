<script setup lang="ts">
</script>

<template>
  <div :class="s.welcomLayout">
    <router-view />
  </div>
</template>

<style lang="css" module="s">
.welcomLayout {
  width: 100%;
  height: 100%;
  display: flex;
}
</style>
