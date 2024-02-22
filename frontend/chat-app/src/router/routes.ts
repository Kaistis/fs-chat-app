import { RouteRecordRaw } from 'vue-router';

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      // Define a route for the authentication page as the default route
      { path: '', component: () => import('pages/AuthPage.vue') },
      // Define a route for the chat page
      {
        path: '/chat',
        component: () => import('pages/ChatPage.vue'),
        props: {
          default: true,
        },
      },
    ],
  },
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue'),
  },
];

export default routes;
