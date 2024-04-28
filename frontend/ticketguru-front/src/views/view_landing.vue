<script setup lang="ts">

import { Http,} from '@/http/http';
import router from '@/router';

function goToEventManagement() {
    router.push({ name: 'event_management' });
}

</script>

<template>
    <div>
        <h3>Ticketguru</h3>
        <a href="http://localhost:3000/logout">Kirjaudu ulos</a>
        <br><br><button @click="goToEventManagement">Hallitse tapahtumia</button>
    </div>

</template>


<style scoped>



</style>
