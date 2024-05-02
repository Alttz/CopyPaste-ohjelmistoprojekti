<script setup lang="ts">
import CompEditableTable from '@/components/CompEditableTable.vue';
//import CompSelect from '@/components/CompSelect.vue'
//import CompBox from '@/components/CompBox.vue';
import CompBuyTickets from '@/components/CompBuyTickets.vue';
import { Http,} from '@/http/http';
import router from '@/router';
import { type Ref, ref } from 'vue';

import { useRouter, useRoute } from 'vue-router';

const route = useRoute()
const id = route.query.id
const routeString = "/events/"+id


</script>

<template>
    <div>
        <p>Vieweing single event: {{id}}</p>
        <Suspense>
            <CompBuyTickets :to-fetch="routeString"></CompBuyTickets>
        </Suspense>
    </div>

</template>


<style scoped>



</style>
