<template>
  <q-page class="flex flex-center">
    <auth-component />
  </q-page>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import AuthComponent from 'src/components/AuthComponent.vue';

export default defineComponent({
  name: 'AuthPage',
  components: {
    AuthComponent,
  },
});
</script>
