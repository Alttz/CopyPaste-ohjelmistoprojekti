<script setup lang="ts">

import { Http,} from '@/http/http';
import router from '@/router';
import { type Ref, ref, computed } from 'vue';

var cartItems : Ref<any> = ref([])
var boughtTickets : Ref<any> = ref([])



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
            "type": temp_ticketdata[model_tickettype.value],
            "index": cartItems.value.length
        })

        model_totalPrice.value += parseInt(temp_ticketdata[model_tickettype.value].price)
    }

}

function deleteFromCart(ticket) {
    model_totalPrice.value -= parseInt(ticket.type.price)
    cartItems.value.splice(ticket.index, 1)
}

function clearCartItems() {
    cartItems.value = []
    model_totalPrice.value = 0
}

function doPurchase() {
    localStorage.removeItem("purchaseData")
    localStorage.setItem("purchaseData",JSON.stringify(cartItems.value))
    router.push({name: 'doPurchase'});
}

const sortedEvents = computed(() => {
    return response_events.value.sort((b, a) => {
        const dateB = new Date(b.date);
        const dateA = new Date(a.date);
        return dateA - dateB;
    }).map(event => {
        const formattedDate = new Date(event.date).toLocaleString('fi-FI', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
        return { ...event, formattedDate };
    });
});

</script>

<template>
    <div>
        <h3>Lipun myynti</h3>
        <h4>Valitse tapahtuma</h4>
        <select v-model="model_select">
            <option value="" disabled selected>Valitse tapahtuma</option>
            <option v-for="event in sortedEvents" :value="event">
                {{ event.formattedDate.substr(0, 10) }} klo {{ event.formattedDate.substr(11) }} |
                {{ event.name }} |
                {{ event.city }} |
                {{ event.place }} |
                Lippuja: {{ event.ticketCount }}
            </option>
        </select>

        <div v-if="model_select != ''">
            <h4>Valitse ostettavat liput</h4>
            <select v-model="model_tickettype">
                <option value="" disabled selected>Valitse lippu</option>
                <option v-for="ticket in model_select.ticketTypes" :value="ticket.id" >
                    {{ticket.name}} {{ticket.price}} €
                </option>
            </select>


            <input type="number" min="1" value=1 v-model="model_amount" style="min-width:none;">
            <button @click="addToCart()" :disabled="model_tickettype == ''">Lisää ostoskoriin</button>
        </div>
        
        <h3>Ostoskori</h3>
        <button @click="clearCartItems()">Tyhjennä ostoskori</button>
        <table class="table table-bordered border-primary">
            <thead class="thead-dark">
                <tr>
                    <th>Ajankohta</th>
                    <th>Kaupunki</th>
                    <th>Paikka</th>
                    <th>Nimi</th>
                    <th>Lippu</th>
                    <th>Hinta</th>
                    <th></th>
                </tr>
            </thead>

            <tbody>
                <tr v-for="item in cartItems">
                    <td>{{ item.ticket.formattedDate.substr(0, 10) }} klo {{ item.ticket.formattedDate.substr(11) }}</td>
                    <td>{{item.ticket.city}}</td>
                    <td>{{item.ticket.place}}</td>
                    <td>{{item.ticket.name}}</td>
                    <!-- item.type has all data about the sold ticket type in it -->
                    <td>{{item.type.name}}</td>
                    <td>{{item.type.price}} €</td>
                    <td><button @click="deleteFromCart(item)">Poista</button></td>
                </tr>

                <tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td>{{model_totalPrice}} €</td>
                    <td><button @click="doPurchase()" style="color:green; font-weight:bold;">Osta</button></td>
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
        //min-width: 35vw;
        font-family: Consas, monospace;
        margin-right: 15px;
    }
    
    h4 {
        margin: 0px;
        margin-top:35px;
    }

</style>
