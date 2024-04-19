<script setup lang="ts">
import CompEditableTable from '@/components/CompEditableTable.vue';
//import CompSelect from '@/components/CompSelect.vue'
//import CompBox from '@/components/CompBox.vue';

import { Http,} from '@/http/http';
import router from '@/router';
import { type Ref, ref } from 'vue';

import { useRouter, useRoute } from 'vue-router';

const route = useRoute()
const id = route.params.id



</script>

<template>
    <div>
        <p>Vieweing single event: {{id}}</p>
    </div>

</template>


<style scoped>



</style>
