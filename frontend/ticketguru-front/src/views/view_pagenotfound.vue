<script setup lang="ts">

import { Http,} from '@/http/http';
import router from '@/router';

function goToEventManagement() {
    router.push({ name: 'event_management' });
}

</script>

<template>
    <div>
        <h2>404 Page not found</h2>
    </div>

</template>


<style scoped>

    h2 {
        text-align:center;
        margin-top:40vh;
    }
    

</style>
