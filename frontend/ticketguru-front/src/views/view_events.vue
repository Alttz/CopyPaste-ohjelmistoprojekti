<script setup lang="ts">
import CompEditableTable from '@/components/CompEditableTable.vue';
//import CompSelect from '@/components/CompSelect.vue'
//import CompBox from '@/components/CompBox.vue';

import { Http,} from '@/http/http';
import router from '@/router';
import { type Ref, ref } from 'vue';
import CompBuyTickets from '@/components/CompBuyTickets.vue'






</script>

<template>
    <div>
        <h3>Events</h3>
        <Suspense>
            <CompEditableTable to-fetch="/events" :passed-components="{'ticketTypes': Select}" />
        </Suspense>

        <h3>Shopping Cart</h3>
        <Suspense>
            <CompBuyTickets to-fetch="/events/4/"></CompBuyTickets>
        </Suspense>
    </div>

</template>


<style scoped>



</style>
	