<script setup lang="ts">
import { Http } from '@/http/http';
import { ref } from 'vue';

const ticketCode = ref('');
const ticketData = ref(null);
const ticketValidity = ref(null);
const eventData = ref(null);  // Reactive reference for event data
const isLoading = ref(false);
const error = ref('');

const fetchTicket = async () => {
    error.value = '';
    ticketData.value = null;  // Reset ticket data before new fetch
    eventData.value = null;  // Reset ticket data before new fetch
    ticketValidity.value = null;  // Reset validity when searching for new ticket
    if (!ticketCode.value) {
        error.value = "Please enter a ticket code.";
        return;
    }
    isLoading.value = true;
    try {
        // Adjust endpoint to fetch ticket details by UUID
        const response = await Http.get(`/tickets/byUuid?uuid=${ticketCode.value}`);
        console.log("Received data:", response); // Log to see what the API returns
        const data = response[0];
        if (data) {
            ticketData.value = data;  // Assuming the API directly returns ticket data
            console.log("Received data:", data); // Log to see what the API returns
            await fetchEventDetails(data.event);
        } else {
            error.value = "No ticket found with the provided code.";
        }
    } catch (err) {
        error.value = "Failed to fetch ticket data.";
        console.error(err);
    } finally {
        isLoading.value = false;
    }
};

const fetchEventDetails = async (eventId) => {
    try {
        const response = await Http.get(`/events/${eventId}`);
        const eventInfo = response[0];
        console.log("Received event data:", eventInfo); // Log to see what the API returns¨
        if (eventInfo) {
            eventData.value = eventInfo;  // Store the event data in the reactive property
        } else {
            error.value = "Failed to fetch event details.";
        }
    } catch (err) {
        console.error("Failed to fetch event details:", err);
        error.value = "Failed to fetch event details.";
    }
};

const markTicketAsUsed = async () => {
    if (!ticketCode.value) {
        error.value = "Please enter a ticket code to mark as used.";
        return;
    }
    isLoading.value = true;
    try {
        await Http.put(`/tickets/markAsUsed?uuid=${ticketCode.value}`);
        ticketData.value.used = true;  // Update the local state to reflect the change
        ticketValidity.value = true;  // Assume ticket is now valid and marked as used
        alert("Ticket has been marked as used successfully.");
    } catch (err) {
        error.value = "Failed to mark the ticket as used.";
        console.error(err);
    } finally {
        isLoading.value = false;
    }
};

function formatDate(dateString) {
    if (!dateString) return ''; // Handle null or undefined dates

    const date = new Date(dateString);
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0'); // Month is 0-indexed
    const year = date.getFullYear();
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');

    return `${day}.${month}.${year} klo ${hours}:${minutes}`;
}


</script>



<template>
    <div>
        <h3>Lipun tarkastus</h3>
        <br>
        <input v-model="ticketCode" type="text" placeholder="Lisää tarkastuskoodi" />
        <button @click="fetchTicket" :disabled="isLoading">
            {{ isLoading ? 'Tarkastetaan...' : 'Hae lipun tiedot' }}
        </button>

        <div v-if="error" class="error">{{ error }}</div>

        <div v-if="ticketData && eventData">
            <h3>Lipun tiedot:</h3>
            <table class="ticket-details">
                <tbody>
                    <tr>
                        <th>Tapahtuman nimi</th>
                        <td>{{ eventData.name }}</td>
                    </tr>
                    <tr>
                        <th>Ajankohta</th>
                        <td>{{ formatDate(eventData.date) }}</td> <!-- Formatted date -->
                    </tr>
                    <tr>
                        <th>Paikka</th>
                        <td>{{ eventData.place }}</td>
                    </tr>
                    <tr>
                        <th>Kaupunki</th>
                        <td>{{ eventData.city }}</td>
                    </tr>
                    <tr>
                        <th>Lipputyyppi</th>
                        <td>{{ ticketData.ticketType.name }}</td>
                    </tr>
                    <tr>
                        <th>Hinta</th>
                        <td>{{ ticketData.ticketType.price }} €</td>
                    </tr>
                    <tr>
                        <th>Tila</th>
                        <td>{{ ticketData.used ? 'Käytetty' : 'Käyttämätön' }}</td>
                    </tr>
                </tbody>
            </table>
            <!-- Button for checking ticket validity -->
            <button @click="markTicketAsUsed" :disabled="isLoading || ticketData.used">
                Merkitse käytetyksi
            </button>
        </div>
    </div>
</template>


<style scoped>
input {
    width: 250px;
}

.error {
    color: red;
}

button {
    margin: 3px;
}

button[disabled] {
    opacity: 0.5;
}

table {
    width: 500px;
    margin-top: 20px;
}
</style>

