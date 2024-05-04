<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'

</script>

<template>
  <nav>
    <RouterLink to="/landing">Home</RouterLink>
    <RouterLink to="/buy">Buy Tickets</RouterLink>
    <RouterLink to="/event_management">Event Management</RouterLink>
    <RouterLink to="/ticketcheck">Lipun tarkastus</RouterLink>
    <RouterLink to="/logout">Logout</RouterLink>
  </nav>

  <Suspense>
    <RouterView />
  </Suspense>
</template>

<style scoped>
  nav a {
    padding-left:25px;
  }
</style>
