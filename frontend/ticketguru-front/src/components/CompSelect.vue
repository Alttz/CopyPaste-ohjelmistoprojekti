<script setup lang="ts">
    import { type Ref, ref } from 'vue';
    
    
    const props = defineProps<{
        todisplay:any
        allowClick?: boolean
        allowEdit?: boolean
    }>()
    
    let data : Ref<any> = ref(props.todisplay)

</script>

<template>
    <div>
        <select >
            <option v-for="d in data" value="{{d}}">{{d["name"]}} | {{d["price"]}}$</option>
        </select>
       
    </div>
</template>

<style scoped>

</style>

