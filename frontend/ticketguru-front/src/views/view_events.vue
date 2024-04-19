<script setup lang="ts">
//import CompEditableTable from '@/components/CompEditableTable.vue';
//import CompBox from '@/components/CompBox.vue';

import { Http,} from '@/http/http';
import router from '@/router';
import { type Ref, ref } from 'vue';


let events : Ref<any> = ref(Http.get("/events"))
let headers : Ref<any> = ref(Object.keys(events))
//var items : Ref<any> = ref(temp)


</script>

<template>

    <div>
        <p>Events</p>
        <table>
            <thead>
                <tr>
                    <th v-for="header in headers" :key="header">{{ header }}</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="(row, index) in events" :key="index">
                    <td v-for="(value, key) in row" :key="key">{{ value }}</td>
                </tr>
            </tbody>
        </table>
    </div>

</template>


<style scoped>



</style>
	