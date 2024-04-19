<script setup lang="ts">
import CompEditableTable from '@/components/CompEditableTable.vue';
//import CompBox from '@/components/CompBox.vue';

import { Http,} from '@/http/http';
import router from '@/router';
import { type Ref, ref } from 'vue';






</script>

<template>
    <div>
        <Suspense>
            <CompEditableTable to-fetch="/events"/>
        </Suspense>
    </div>

</template>


<style scoped>



</style>
	