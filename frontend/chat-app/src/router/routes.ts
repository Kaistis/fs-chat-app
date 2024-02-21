import { RouteRecordRaw } from 'vue-router';

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      // Define a route for the chat page
      { path: '', component: () => import('pages/ChatPage.vue') },
      // You can add more routes here for other pages of your application
    ],
  },

  // Always leave this as the last one,
  // but you can also remove it if you don't want to navigate to a 404 page
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue'),
  },
];

export default routes;
