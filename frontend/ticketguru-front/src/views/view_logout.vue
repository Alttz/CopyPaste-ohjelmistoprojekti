<script setup lang="ts">
//import CompEditableTable from '@/components/CompEditableTable.vue';
//import CompBox from '@/components/CompBox.vue';

import { Http,} from '@/http/http';
import router from '@/router';
import { type Ref, ref } from 'vue';

localStorage.clear();
  Http.setToken("user has logged out");

function goToLogin() {
    router.push({ name: 'login' });
}

</script>

<template>

    <div>
        <p>You have been logged out</p>
        <button @click="goToLogin">Kirjaudu sisään</button>
    </div>

</template>



<style scoped>



</style>
