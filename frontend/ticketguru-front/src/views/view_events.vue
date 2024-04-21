<script setup lang="ts">
import CompEditableTable from '@/components/CompEditableTable.vue';
//import CompSelect from '@/components/CompSelect.vue'
//import CompBox from '@/components/CompBox.vue';

import { Http,} from '@/http/http';
import router from '@/router';
import { type Ref, ref } from 'vue';
import CompBuyTickets from '@/components/CompBuyTickets.vue'


var cartItems : Ref<any> = ref({})

function handleAddToCart(ticketData) {
    cartItems.value = ticketData
    console.log(cartItems.value)
}

function buyTickets() {
    //Do http.ts call here to send post buy request
    
}

</script>

<template>
    <div>
        <h3>Events</h3>
        <Suspense>
            <CompEditableTable to-fetch="/events" :passed-components="{'ticketTypes': Select}" @addToCart="handleAddToCart" />
        </Suspense>

        <h3>Shopping Cart</h3>

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
        <button @click=buyTickets>Buy</button>
    </div>

</template>


<style scoped>



</style>
	