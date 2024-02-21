<template>
  <div>
    <ChatComponent />
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import ChatComponent from '../components/ChatComponent.vue';

export default defineComponent({
  name: 'ChatPage',
  components: {
    ChatComponent,
  },
});
</script>
