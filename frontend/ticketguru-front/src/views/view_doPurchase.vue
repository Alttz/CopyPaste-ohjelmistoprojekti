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
    console.log(localStorage.getItem("purchaseData"))

    let temp_purchaseRequestRows = []
    for (const [key, value] of Object.entries(purchaseData.value)) {
        temp_purchaseRequestRows.push({
            "eventId": key,
            "ticketTypeNames": [value.type.name]
        })
    }

    let template_request = {
        "userId": 1,
        "purchaseRequestRows": temp_purchaseRequestRows
    }
    
    displayOnPage.value = JSON.stringify(template_request)
    console.log(template_request)

    //const response = await Http.post("/purchases",template_request)
    


</script>

<template>
    <div>
        <h3>doPurchase</h3>
        <p>{{displayOnPage}}</p>
    </div>

</template>


<style scoped>



</style>
