<script setup lang="ts">

import { Http,} from '@/http/http';
import router from '@/router';
import { type Ref, ref } from 'vue';

var cartItems : Ref<any> = ref([])
var cartItemsKeys : Ref<any> = ref([])
var boughtTickets : Ref<any> = ref([])
var boughtTicketKeys : Ref<any> = ref([])



const model_amount : Ref<number> = ref()
const model_select : Ref<unknown> = ref()


var temp = await Http.get("/events")
var response_events: Ref<any> = ref(temp)
console.log(response_events)

</script>

<template>
    <div>
    
        <form>
            <select v-model="model_select">
                <option v-for="event in response_events" :value="event" >
                    {{event.date}} |
                    {{event.city}} |
                    {{event.place}} |
                    {{event.name}} |
                    Lippuja: {{event.ticketCount}}
                </option>
            </select>

            

            
            
            <div v-if="model_select != undefined">
                <select>
                    <option v-for="ticket in model_select.ticketTypes" :value="ticket.id" >
                        {{ticket.name}} {{ticket.price}} €
                    </option>
                </select>
            
            
                <input type="number" value=1 v-model="model_amount">        
                <button>Add to cart</button>
                            {{model_amount}}
            
            </div>
        </form>
        
        <h3>Shopping Cart</h3>
        <table class="table table-bordered border-primary">
            <thead class="thead-dark">
                <tr>
                    <th>adadad</th>
                </tr>
            </thead>

            <tbody>

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
