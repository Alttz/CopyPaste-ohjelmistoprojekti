<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'

</script>

<template>
  <nav>
    <RouterLink to="/landing">Home</RouterLink>
    <RouterLink to="/events">Events</RouterLink>
    <RouterLink to="/event_management">Tapahtuman hallinta</RouterLink>
    <RouterLink to="/logout">Logout</RouterLink>
  </nav>

  <RouterView />
</template>

<style scoped>
  nav a {
    padding-left:25px;
  }
</style>
