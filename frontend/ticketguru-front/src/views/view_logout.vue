<script setup lang="ts">
//import CompEditableTable from '@/components/CompEditableTable.vue';
//import CompBox from '@/components/CompBox.vue';

import { Http,} from '@/http/http';
import router from '@/router';
import { type Ref, ref } from 'vue';

Http.setToken("user has logged out")
localStorage.clear()


</script>

<template>

    <div>
        <p>You have been logged out</p>
        <a href="http://localhost:3000/login">Kirjaudu sisään</a>
    </div>

</template>


<style scoped>



</style>
