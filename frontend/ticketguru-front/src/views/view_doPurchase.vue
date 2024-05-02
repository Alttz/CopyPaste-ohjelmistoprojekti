<script setup lang="ts">
    import { Http,} from '@/http/http';
    import router from '@/router';
    import { type Ref, ref } from 'vue';
    

    /*
    {
        "userId": 1,
        "purchaseRequestRows": [
            {
                "eventId": 5,
                "ticketTypeNames": ["Aikuinen", "Aikuinen", "VIP", "VIP", "Lapsi"]
            },
            {
                "eventId": 4,
                "ticketTypeNames": ["Aikuinen", "Aikuinen", "Opiskelija"]
            }
        ]
    }
    */





    let purchaseData : Ref<any> = ref(JSON.parse(localStorage.getItem("purchaseData")))
    let displayOnPage : Ref<string> = ref("")
    //console.log(localStorage.getItem("purchaseData"))


    //Group all ticket's properly to a dict with event's id as their key
    let ticketTypesForEvents = {}
    for (let i = 0; i < purchaseData.value.length; i++) {
        let ticket = purchaseData.value[i].type

        //Let's find all different ticketTypes that are sold to each event and add them into a dictionary in a list with key that is the event's id
        if(!(ticket["event"] in ticketTypesForEvents)) {
            ticketTypesForEvents[ticket["event"]] = []
        }
        ticketTypesForEvents[ticket["event"]].push(ticket)
    }


    //Constructing the proper request to be passed to API
    let temp_purchaseRequestRows = []
    for (const [key, value] of Object.entries(ticketTypesForEvents)) {

        //This processing could be done in the grouping section maybe better.
        var temp_processed_types = []
        for (let i = 0; i < value.length; i++) {
            temp_processed_types.push(value[i].name)
        }

        temp_purchaseRequestRows.push({
            "eventId": key,
            "ticketTypeNames": temp_processed_types
        })
    }

    let template_request = {
        "userId": 1,
        "purchaseRequestRows": temp_purchaseRequestRows
    }
    
    displayOnPage.value = JSON.stringify(template_request)
    console.log(template_request)


    const response = await Http.post("/purchases",template_request)
    var response_items : Ref<any> = ref(response)
    console.log(response)


</script>

<template>
    <div>
        <h3>Thankyou for your Purchase!</h3>

        <tbody>
            <th>Date</th>
            <th>Event</th>
            <th>Username</th>
            <th>TicketType</th>
            <th>Price</th>
            <th>Verification code</th>

        </tbody>
        <tbody>
            <template v-for="item in response_items.successfulPurchases">
                <tr v-for="ticket in item.tickets">
                    <td>{{item.purchaseDate}}</td>
                    <td>{{ticket.ticketType.event}}</td>
                    <td>{{item.appUser.username}}</td>
                    <td>{{ticket.ticketType.name}}</td>
                    <td>{{ticket.ticketType.price}} €</td>
                    <td>{{ticket.uuid}}</td>
                    <td><button >Print</button></td>
                </tr>
            </template>
        </tbody>


    </div>

</template>


<style scoped>



</style>
