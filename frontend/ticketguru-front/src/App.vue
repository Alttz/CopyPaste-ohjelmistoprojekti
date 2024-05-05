<script setup lang="ts">
import { RouterLink, RouterView, useRouter } from 'vue-router'
import { Http } from '@/http/http' // Import Http module to check authentication status

</script>

<template>
  <nav>
    <RouterLink v-if="Http.isAuthenticated()" to="/event_management">Event Management</RouterLink>
    <RouterLink v-if="Http.isAuthenticated()" to="/buy">Buy Tickets</RouterLink>
    <RouterLink v-if="Http.isAuthenticated()" to="/ticketcheck">Lipun tarkastus</RouterLink>
    <RouterLink v-if="Http.isAuthenticated()" to="/logout">Logout</RouterLink>
  </nav>

  <Suspense>
    <RouterView />
  </Suspense>
</template>

<style scoped>
  nav a {
    padding-left:25px;
    color: blue; /* Style it like a hyperlink */
    text-decoration: underline; /* Underline to mimic standard link appearance */
    cursor: pointer; /* Cursor to pointer to indicate it's clickable */
  }
</style>
