<script setup lang="ts">

import { Http,} from '@/http/http';
import router from '@/router';
import { type Ref, ref } from 'vue';

var cartItems : Ref<any> = ref([])
var cartItemsKeys : Ref<any> = ref([])
var boughtTickets : Ref<any> = ref([])
var boughtTicketKeys : Ref<any> = ref([])



const model_amount : Ref<number> = ref(1)
const model_totalPrice : Ref<number> = ref(0)
const model_select : Ref<unknown> = ref("")
const model_tickettype : Ref<unknown> = ref("")


var temp = await Http.get("/events")
var response_events: Ref<any> = ref(temp)
console.log(response_events)


function addToCart() {


    //Event's tickets data is stored here temporary and is accessable with the ticketType id
    let temp_ticketdata = {}
    for (let i = 0; i < model_select.value.ticketTypes.length; i++) {
        const item = model_select.value.ticketTypes[i]
        temp_ticketdata[item.id] = item
    }

    for (let i = 0; i < model_amount.value; i++) {
        cartItems.value.push({
            "ticket": model_select.value,
            "type": temp_ticketdata[model_tickettype.value]
        })
    }

}

function deleteFromCart(ticket) {
    alert("Deleting")
}

</script>

<template>
    <div>
        <select v-model="model_select">
            <option v-for="event in response_events" :value="event" >
                {{event.date}} |
                {{event.city}} |
                {{event.place}} |
                {{event.name}} |
                Lippuja: {{event.ticketCount}}
            </option>
        </select>


        <div v-if="model_select != ''">
            <select v-model="model_tickettype">
                <option v-for="ticket in model_select.ticketTypes" :value="ticket.id" >
                    {{ticket.name}} {{ticket.price}} €
                </option>
            </select>


            <input type="number" value=1 v-model="model_amount">
            <button @click="addToCart()" :disabled="model_tickettype == ''">Add to cart</button>
        </div>
        
        <h3>Shopping Cart</h3>
        <table class="table table-bordered border-primary">
            <thead class="thead-dark">
                <tr>
                    <th>Date</th>
                    <th>City</th>
                    <th>Place</th>
                    <th>Name</th>
                    <th>TicketType</th>
                    <th>Price</th>
                    <th></th>


                </tr>
            </thead>

            <tbody>
                <tr v-for="item in cartItems">
                    <td>{{item.ticket.date}}</td>
                    <td>{{item.ticket.city}}</td>
                    <td>{{item.ticket.place}}</td>
                    <td>{{item.ticket.name}}</td>
                    <!-- item.type has all data about the sold ticket type in it -->
                    <td>{{item.type.name}}</td>
                    <td>{{item.type.price}} €</td>
                    <td><button @click="deleteFromCart(item)">delete</button></td>
                </tr>

                <tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td>{{model_totalPrice}} €</td>
                    <td></td>
                </tr>
            </tbody>
        </table>
    </div>

</template>


<style scoped>
    select,input,option {
        margin-top:20px;
        padding: 15px;
        font-size: 16px;
        width: 35vw;
        font-family: Consas, monospace;
        margin-right: 15px;
    }
    
    input {
        width: 100px;
    }

</style>
