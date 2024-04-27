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

async function buyTickets() {
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

    const response = await Http.post("/purchases",template_request)

    boughtTickets.value = response["successfulPurchases"]
    boughtTicketKeys.value = Object.keys(response["successfulPurchases"][0]['tickets'][0])
    cartItems.value = []
    cartItemsKeys.value = []
    
    console.log(response)
}

function goToEventManagement() {
    router.push({ name: 'event_management' });
}


</script>

<template>
    <div>
        <a href="http://localhost:3000/logout">Kirjaudu ulos</a>
        <br><br><button @click="goToEventManagement">Hallitse tapahtumia</button>
        <h3>Myy lippuja</h3>
        <Suspense>
            <CompEditableTable to-fetch="/events" :passed-components="{'ticketTypes': Select}" @addToCart="handleAddToCart" />
        </Suspense>

        <h3>Ostoskori</h3>

        <table class="table table-bordered border-primary">
            <thead class="thead-dark">
                <tr>
                    <th v-for="header in cartItemsKeys">{{header}}</th>
                </tr>
            </thead>

            <tbody>
                <tr v-for="ticket in cartItems">
                    <td v-for="data in ticket">{{data}}</td>
                    <td>€</td>
                </tr>
            </tbody>
        </table>
        <button @click=buyTickets>Osta</button>


        <hr>
        <h4>Ostetut liput</h4>
        <table class="table table-bordered border-primary">
            <thead class="thead-dark">
                <tr>
                    <th v-for="header in boughtTicketKeys">{{header}}</th>
                </tr>
            </thead>

            <tbody>
                <template v-for="ticket in boughtTickets">
                    <template v-for="data in ticket['tickets']">
                        <tr>
                            <td>{{data.id}}</td>
                            <td>{{data.ticketType['name']}} | {{data.ticketType['price']}} €</td>
                            <td>{{data.uuid}}</td>
                            <td>{{data.used}}</td>
                            <td>{{data.event}}</td>
                        </tr>
                    </template>
                </template>
            </tbody>
        </table>
    </div>

</template>


<style scoped>



</style>
	