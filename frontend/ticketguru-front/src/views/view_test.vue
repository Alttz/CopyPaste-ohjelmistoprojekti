<script setup lang="ts">
import { Http } from '@/http/http';
import { ref } from 'vue';

const ticketCode = ref('');
const ticketData = ref(null);
const ticketValidity = ref(null);  // Ensure this is reset appropriately
const isLoading = ref(false);
const error = ref('');

const fetchTicket = async () => {
    error.value = '';
    ticketValidity.value = null;  // Reset validity when searching for new ticket
    if (!ticketCode.value) {
        error.value = "Please enter a ticket code.";
        return;
    }
    isLoading.value = true;
    try {
        const response = await Http.get('myyntitapahtumat');
        const tickets = response.flatMap(event => event.liputDto);
        const foundTicket = tickets.find(ticket => ticket.tarkistuskoodi === ticketCode.value);
        if (foundTicket) {
            ticketData.value = foundTicket;
        } else {
            ticketData.value = null;
            error.value = "No ticket found with the provided code.";
        }
    } catch (err) {
        error.value = "Failed to fetch ticket data.";
        console.error(err);
    } finally {
        isLoading.value = false;
    }
};

const fetchTicketValidity = async () => {
    if (!ticketCode.value) {
        error.value = "Please enter a ticket code for validation.";
        return;
    }
    isLoading.value = true;
    try {
        const response = await Http.get(`tarkastukset/${ticketCode.value}`);
        ticketValidity.value = response.response;  // Use the response directly
    } catch (err) {
        error.value = "Failed to validate ticket.";
        console.error(err);
    } finally {
        isLoading.value = false;
    }
};
</script>


<template>
    <div>
        <input v-model="ticketCode" type="text" placeholder="Lisää tarkastuskoodi" />
        <button @click="fetchTicket" :disabled="isLoading">
            {{ isLoading ? 'Tarkastetaan...' : 'Hae lipun tiedot' }}
        </button>

        <div v-if="error" class="error">{{ error }}</div>

        <div v-if="ticketData">
            <h3>Lipun tiedot:</h3>
            <p>Tapahtuman nimi: {{ ticketData.tapahtumanNimi }}</p>
            <p>Ajankohta: {{ ticketData.tapahtumaAika }}</p>
            <p>Paikka: {{ ticketData.tapahtumaPaikka }}</p>
            <p>Lipputyyppi: {{ ticketData.lipputyyppi }}</p>
            <p>Hinta: {{ ticketData.hinta }} €</p>
            
            <!-- Button for checking ticket validity -->
            <button @click="fetchTicketValidity" :disabled="isLoading">
                {{ isLoading ? 'Checking...' : 'Check Ticket' }}
            </button>
        </div>

        <div v-if="ticketValidity !== null">
            <h3>Tarkasta lippu:</h3>
            <p v-if="ticketValidity">{{ ticketCode.value }} Lippu on validi ja merkattu nyt käytetyksi.</p>
            <p v-else>{{ ticketCode.value }} Lippu on jo käytetty tai sitä ei ole olemassa.</p>
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
