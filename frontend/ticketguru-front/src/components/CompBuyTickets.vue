<script setup lang="ts">
	import { type Ref, ref } from 'vue';
    import { Http } from '@/http/http';
    //import router from '@/router';
    
    //import CompBox from '@/components/CompBox.vue'
    //import CompSelect from '@/components/CompSelect.vue'


	const props = defineProps<{
        toFetch:string
        allowClick?: boolean
        clickRedirectTarget?: string
        allowEdit?: boolean
        showDeleted?: boolean
        shownColumns?: any
        allowSearch?: boolean
        passedComponents?: any
    }>()

    /*
	var temp = (await Http.get(props.toFetch))
    var items : Ref<any> = ref(temp)

    var keys : Ref<any> = ref(Object.keys(temp))

    var ticketTypes : Ref<any> = ref(temp["ticketTypes"])
    */

</script>

<template>
    <div class="custombox">
        <table class="table table-bordered border-primary">
            <thead class="thead-dark">
                <tr>
                    <th>id</th>
                    <th>date</th>
                    <th>place</th>
                    <th>name</th>
                    <th>ticketType</th>
                    <th>Price</th>                    
                </tr>
            </thead>
    
            <tbody>
                <tr>
                    <td>0</td>
                    <td>wlalalal</td>
                    <td>wlalalal</td>
                    <td>wlalalal</td>
                    <td>wlalalal</td>
                    <td>24.00 €</td>
                </tr>
            </tbody>
        </table>
        
        <hr>
        <h4>Total: 0.00€</h4>
        <button>Buy</button>
        
        <!--
        <div className="tickets">
            <select>
                <option v-for="type in ticketTypes">{{type["name"]}} | {{type["price"]}} €</option>
            </select>
        </div>
        -->
    </div>
</template>

<style scoped>

</style>

