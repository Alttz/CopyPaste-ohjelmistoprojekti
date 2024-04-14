<script setup lang="ts">
import CompEditableTable from '@/components/CompEditableTable.vue';
//import CompBox from '@/components/CompBox.vue';

import axios from "axios"
import { Http, } from '@/http/http';
import router from '@/router';
import { type Ref, ref } from 'vue';

const ticketCode = ref('');
const ticketValidity = ref(null);
const isLoading = ref(false);
const error = ref('');

const fetchTicketValidity = async () => {
    error.value = '';  // Clear previous errors on new request
    if (!ticketCode.value) {
        error.value = "Please enter a ticket code.";
        return;
    }
    isLoading.value = true;
    try {
        const response = await Http.get(`tarkastukset/${ticketCode.value}`);
        ticketValidity.value = response.response;  // Directly using the 'response' key from the API
    } catch (err) {
        error.value = "Failed to fetch ticket data.";
        console.error(err);
    } finally {
        isLoading.value = false;
    }
};

</script>

<template>
    <div>
        <input v-model="ticketCode" type="text" placeholder="Enter Ticket Code" />
        <button @click="fetchTicketValidity" :disabled="isLoading">
            {{ isLoading ? 'Checking...' : 'Check Ticket' }}
        </button>

        <div v-if="error" class="error">{{ error }}</div>
        <div v-if="ticketValidity !== null">
            <h3>Ticket Status:</h3>
            <p v-if="ticketValidity">{{ ticketCode }} is valid and now marked as used.</p>
            <p v-else>{{ ticketCode }} is either already used or does not exist.</p>
        </div>
    </div>
</template>



<style scoped>
.error {
    color: red;
}

button[disabled] {
    opacity: 0.5;
}
</style>
