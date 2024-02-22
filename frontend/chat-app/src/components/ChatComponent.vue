<template>
  <q-page class="container flex flex-center">
    <div class="q-pa-md full-width">
      <h4 class="text-h4 q-my-md">
        <q-badge v-if="connection_ready" color="green" label="Connected" />
        <q-badge v-if="connection_error" color="red" label="Error" />
      </h4>

      <q-scroll-area
        ref="scrollArea"
        class="chat-messages q-pa-md"
        style="height: 60vh"
      >
        <div class="q-mb-md q-message-name">
          <q-chat-message
            v-for="(msg, idx) in messages"
            :key="idx"
            :text="[msg.message]"
            :name="msg.username === 'me' ? '' : msg.username"
            :stamp="msg.timestamp"
            :sent="msg.username === 'me'"
          />
        </div>
      </q-scroll-area>

      <div class="send-zone q-mt-md">
        <q-input
          ref="messageInput"
          v-model="new_message"
          outlined
          placeholder="Type a message"
          @keyup.enter="send_message"
          class="full-width"
        />
      </div>
    </div>
  </q-page>
</template>

<script lang="ts">
import { defineComponent, ref, nextTick, onMounted, onUnmounted } from 'vue';
import { useAuthStore } from 'src/stores/auth';

interface Message {
  username: string;
  message: string;
  timestamp: string;
}

export default defineComponent({
  name: 'ChatComponent',
  setup() {
    const messageInput = ref(null); // TODO: add focus
    const scrollArea = ref(null);
    const authStore = useAuthStore();
    const connection_ready = ref(false);
    const connection_error = ref(false);
    const new_message = ref('');
    const messages = ref<Message[]>([]);
    let websocket: WebSocket | null = null;

    // Function to scroll to the bottom
    const animateScroll = () => {
      nextTick(() => {
        const scrollTarget = scrollArea.value?.getScrollTarget();
        const duration = 300; // ms - use 0 to instant scroll
        scrollArea.value?.setScrollPosition(
          'vertical',
          scrollTarget.scrollHeight,
          duration
        );
      });
    };

    const setFocus = () => {
      nextTick(() => {
        messageInput.value?.focus();
      });
    };

    const send_message = () => {
      if (new_message.value.trim() && websocket) {
        const messageToSend = {
          username: authStore.getUsername,
          message: new_message.value,
        };
        websocket.send(JSON.stringify(messageToSend));
        new_message.value = '';
      }
    };

    onMounted(() => {
      websocket = new WebSocket('ws://localhost:8080/chat');

      websocket.onopen = () => {
        connection_ready.value = true;
      };

      websocket.onmessage = (event) => {
        const data = JSON.parse(event.data);
        if (Array.isArray(data)) {
          messages.value = data.map((msg) => ({
            username:
              msg.username === authStore.getUsername ? 'me' : msg.username,
            message: msg.message,
            timestamp: new Date(msg.timestamp).toLocaleTimeString(),
          }));
        } else {
          const received: Message = {
            username:
              data.username === authStore.getUsername ? 'me' : data.username,
            message: data.message,
            timestamp: new Date(data.timestamp).toLocaleTimeString(),
          };
          messages.value.push(received);
        }

        animateScroll(); // Also scroll to bottom when the component is mounted
        setFocus(); // Set focus on message input
      };

      websocket.onerror = () => {
        connection_error.value = true;
      };
    });

    onUnmounted(() => {
      if (websocket) websocket.close();
    });

    return {
      connection_ready,
      connection_error,
      new_message,
      messages,
      send_message,
      scrollArea,
      animateScroll,
      messageInput,
      setFocus,
    };
  },
});
</script>

<style lang="scss" scoped>
.container {
  .chat-messages {
    background: url('../assets/background.jpg') no-repeat center center;
    background-size: cover;
  }

  .send-zone {
    .q-input {
      background: #1f1b1b00;
    }
  }
}
</style>
