<script setup lang="ts">
//import CompEditableTable from '@/components/CompEditableTable.vue';
//import CompBox from '@/components/CompBox.vue';

import { Http,} from '@/http/http';
import router from '@/router';
import { type Ref, ref } from 'vue';


let events : Ref<any> = ref(Http.get("/events"))
//var items : Ref<any> = ref(temp)

</script>

<template>

    <div>
        <p>Events</p>
        <div>{{events}}</div>
    </div>

</template>


<style scoped>



</style>
	