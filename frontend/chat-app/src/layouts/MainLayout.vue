<template>
  <q-layout view="hHh lpR fFf">
    <q-header elevated class="bg-primary text-white">
      <q-toolbar>
        <q-toolbar-title> Chat Application </q-toolbar-title>
        <q-btn flat @click="toggleLeftDrawer" round dense icon="menu" />
      </q-toolbar>
    </q-header>

    <q-drawer show-if-above v-model="leftDrawerOpen" bordered>
      <q-list>
        <q-item-label header>Navigation</q-item-label>
        <q-item clickable v-ripple to="/">
          <q-item-section avatar>
            <q-icon name="chat" />
          </q-item-section>
          <q-item-section> Chat Room </q-item-section>
        </q-item>
        <!-- Add more navigation items here -->
      </q-list>
    </q-drawer>

    <q-page-container>
      <router-view />
    </q-page-container>

    <q-footer elevated class="bg-grey-8 text-white">
      <q-toolbar>
        <q-toolbar-title> &copy; 2024 Chat App </q-toolbar-title>
      </q-toolbar>
    </q-footer>
  </q-layout>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';

export default defineComponent({
  name: 'MainLayout',
  setup() {
    const leftDrawerOpen = ref(false);

    function toggleLeftDrawer() {
      leftDrawerOpen.value = !leftDrawerOpen.value;
    }

    return { leftDrawerOpen, toggleLeftDrawer };
  },
});
</script>

<style scoped>
.q-header {
  z-index: 1;
}
</style>
