<script setup lang="ts">
import { onMounted } from 'vue';
import request from '../../request';


onMounted(() => {
  request("/user/all");
})

</script>

<template>
  <div>
    hello my users
  </div>
</template>
