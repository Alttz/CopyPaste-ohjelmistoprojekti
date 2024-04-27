<script setup lang="ts">
import CompEventManagementTable from '@/components/CompEventManagementTable.vue';
import router from '@/router';
//import CompSelect from '@/components/CompSelect.vue'
//import CompBox from '@/components/CompBox.vue';

function goToEvents() {
    router.push({ name: 'events' });
}

</script>

<template>
    <div>
        <a href="http://localhost:3000/logout">Kirjaudu ulos</a>
        <br><br><button @click="goToEvents">Myy lippuja</button>
        <h3>Tapahtumat</h3>
        <Suspense>
            <CompEventManagementTable to-fetch="/events" :passed-components="{'ticketTypes': Select}"/>
        </Suspense>
    </div>

</template>

<style scoped>
</style>
	