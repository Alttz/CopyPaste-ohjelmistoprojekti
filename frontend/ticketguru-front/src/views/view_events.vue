<script setup lang="ts">
import CompEditableTable from '@/components/CompEditableTable.vue';
//import CompSelect from '@/components/CompSelect.vue'
//import CompBox from '@/components/CompBox.vue';

import { Http,} from '@/http/http';
import router from '@/router';
import { type Ref, ref } from 'vue';
import CompBuyTickets from '@/components/CompBuyTickets.vue'


var cartItems : Ref<any> = ref([])
var cartItemsKeys : Ref<any> = ref([])

function handleAddToCart(ticketData) {
    cartItems.value.push(ticketData)
    cartItemsKeys.value = Object.keys(cartItems.value[0])
    console.log(ticketData)
}

function buyTickets() {
    //Do http.ts call here to send post buy request
    alert("Buying tickets....")
    
}

</script>

<template>
    <div>
        <h3>Events</h3>
        <Suspense>
            <CompEditableTable to-fetch="/events" :passed-components="{'ticketTypes': Select}" @addToCart="handleAddToCart" />
        </Suspense>

        <h3>Shopping Cart</h3>
        adadad{{cartItems.value}}
        <table class="table table-bordered border-primary">
            <thead class="thead-dark">
                <tr>
                    <th v-for="header in cartItemsKeys">{{header}}</th>
                </tr>
            </thead>

            <tbody>
                <tr v-for="ticket in cartItems">
                    <td v-for="data in ticket">{{data}}</td>
                    <td><button>Delete</button></td>
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
	