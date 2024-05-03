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


    let model_viewTicketOnPage : Ref<unknown> = ref("")

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

    /*
    function doPrint(ticketData) {
        localStorage.removeItem("printTicketData")
        localStorage.setItem("printTicketData",JSON.stringify(ticketData))
        router.push({name: 'printTicket'});
    }
    */

    function enlargeTicket(ticketData) {
        model_viewTicketOnPage.value = ticketData
        console.log(model_viewTicketOnPage.value)
    }

    function doPrint() {
        // Get HTML to print from element
        const prtHtml = document.querySelector("#printwrapper").innerHTML

        // Get all stylesheets HTML
        let stylesHtml = '';
        for (const node of [...document.querySelectorAll('link[rel="stylesheet"], style')]) {
            stylesHtml += node.outerHTML;
        }

        // Open the print window
        const WinPrint = window.open('', '', 'left=0,top=0,width=800,height=900,toolbar=0,scrollbars=0,status=0');

        WinPrint.document.write(`<!DOCTYPE html>
        <html>
            <head>
                ${stylesHtml}
            </head>
            <body>
                ${prtHtml}
            </body>
        </html>`);

        WinPrint.document.close();
        WinPrint.focus();
        WinPrint.print();
        WinPrint.close();
    }


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
                <tr v-for="ticket in item.tickets" @click="enlargeTicket(ticket)">
                    <td>{{item.purchaseDate}}</td>
                    <td>{{ticket.ticketType.event}}</td>
                    <td>{{item.appUser.username}}</td>
                    <td>{{ticket.ticketType.name}}</td>
                    <td>{{ticket.ticketType.price}} €</td>
                    <td>{{ticket.uuid}}</td>
                </tr>
            </template>
        </tbody>



        <template v-if="model_viewTicketOnPage != ''">
            <div id="printwrapper">
                <div className="ticketviewer">
                    <h3>Ticketguru</h3>
                    <p>Tapahtumalippu</p>
                    <img src="../assets/qr350.png">
                    <div className="info">
                        <table>
                            <tbody>
                                <tr><td>{{model_viewTicketOnPage.uuid}}</td></tr>
                                <tr><td>{{model_viewTicketOnPage.ticketType.event}}</td></tr>
                                <tr><td>{{model_viewTicketOnPage.ticketType.name}}</td></tr>
                                <tr><td>{{model_viewTicketOnPage.ticketType.price}}.00€</td></tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <button @click="doPrint()">Tulosta</button>
        </template>


    </div>

</template>


<style scoped>
    .ticketviewer {
        background-color: #ffffff;
        border: 2px solid black;
        borde-radius: 10px;
        margin-top: 25px;
        margin-bottom: 25px;
        //max-width: 25vw;
        float:left;
        padding: 10px;

    }

    .info td {
        border: 2px solid black;
        border-radius: 5px;

    }

    .info tr {
        margin-botton: 5px;
    }

    img {
        border-radius: 10px;
        padding: 5px;
        width: 80%;
        margin-left: auto;
        margin-right:auto;
    }

    h4,h3 {
        margin: 0px;
    }

</style>
