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


var boughtTickets : Ref<any> = ref([])
var boughtTicketKeys : Ref<any> = ref([])

function handleAddToCart(ticketData) {
    cartItems.value.push(ticketData)
    cartItemsKeys.value = Object.keys(cartItems.value[0])
    console.log(ticketData)
}

function buyTickets() {
    //Do http.ts call here to send post buy request



    let temp_purchaseRequestRows = []

    let ticketTypesForEvents = {}
    for (let i = 0; i < cartItems.value.length; i++) {
        let ticket = cartItems.value[i]

        //Let's find all different ticketTypes that are sold to each event and add them into a dictionary in a list with key that is the event's id
        if(!(ticket["id"] in ticketTypesForEvents)) {
            ticketTypesForEvents[ticket["id"]] = []
        }
        ticketTypesForEvents[ticket["id"]].push(ticket["ticketType"])
    }


    for (const [key, value] of Object.entries(ticketTypesForEvents)) {
        temp_purchaseRequestRows.push({
            "eventId": key,
            "ticketTypeNames":value
        })
    }


    let template_request = {
        "userId": 1,
        "purchaseRequestRows": temp_purchaseRequestRows
    }

    const response = Http.post("/purchases",template_request)


    cartItems.value = []
    cartItemsKeys.value = []


    
    console.log(response)
    
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




        <table class="table table-bordered border-primary">
            <thead class="thead-dark">
                <tr>
                    <th v-for="header in boughtTicketKeys">{{header}}</th>
                </tr>
            </thead>

            <tbody>
                <tr v-for="ticket in boughtTickets">
                    <td v-for="data in ticket">{{data}}</td>
                    <td><button>Delete</button></td>
                </tr>
            </tbody>
        </table>
    </div>

</template>


<style scoped>



</style>
	