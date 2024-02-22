<template>
  <div class="q-pa-md">
    <q-card class="my-card">
      <q-card-section>
        <div class="text-h6">Login</div>
      </q-card-section>

      <q-card-section>
        <q-input
          v-model="username"
          label="Username"
          outlined
          dense
          @keyup.enter="login"
        >
        </q-input>
        <!-- Placeholder for future password field -->
        <!-- <q-input v-model="password" label="Password" type="password" outlined dense @keyup.enter="login" /> -->
      </q-card-section>

      <q-card-section>
        <q-btn label="Enter Chat" color="primary" @click="login" />
      </q-card-section>
    </q-card>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';
import { useAuthStore } from 'src/stores/auth';
import { useRouter } from 'vue-router';

export default defineComponent({
  name: 'AuthComponent',
  setup() {
    const authStore = useAuthStore();
    const username = ref('');
    // const password = ref(''); // Placeholder for future password implementation
    const router = useRouter();

    const login = () => {
      if (username.value.trim()) {
        authStore.setUsername(username.value); // Set the username in the store
        router.push({ path: '/chat' }); // Navigate to the ChatPage
      } else {
        // Handle the case where username is empty
      }
    };

    return {
      username,
      // password, // TODO
      login,
    };
  },
});
</script>

<style scoped>
.my-card {
  width: 300px;
  max-width: 90vw;
}
</style>
