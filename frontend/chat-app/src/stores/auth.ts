import { defineStore } from 'pinia';
import { useStorage } from '@vueuse/core';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    // Use useStorage for reactive localStorage access
    username: useStorage<string | null>('username', null),
  }),
  getters: {
    getUsername: (state) => state.username,
    isUserLoggedIn: (state) => !!state.username,
  },
  actions: {
    setUsername(username: string) {
      this.username = username;
    },
    logout() {
      this.username = null;
    },
  },
});
