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
        <p>Temporary landing page</p>
    </div>

</template>


<style scoped>



</style>
