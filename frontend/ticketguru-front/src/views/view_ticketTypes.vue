<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { Http } from '@/http/http';

const route = useRoute();
const router = useRouter();
const event = ref(null);
const editingId = ref(null);
const editingTicketType = ref({ name: '', price: '' });
const newTicketType = ref({
    name: '',
    price: ''
});

onMounted(async () => {
    const eventId = route.params.id;
    try {
        const eventData = await Http.get(`/events/${eventId}`);
        event.value = eventData;
    } catch (error) {
        console.error('Failed to fetch event details:', error);
    }
});

function startEdit(ticketType) {
    editingId.value = ticketType.id;
    editingTicketType.value = { ...ticketType };
}

async function saveEdit() {
    if (editingTicketType.value.name.trim() === '' || editingTicketType.value.price === null) {
        alert('Please fill in all fields.');
        return;
    }

    // Create an updated array of ticket types
    const updatedTicketTypes = event.value.ticketTypes.map(ticketType => {
        if (ticketType.id === editingId.value) {
            return {
                name: editingTicketType.value.name,
                price: parseFloat(editingTicketType.value.price)
            };
        } else {
            return {
                name: ticketType.name,
                price: parseFloat(ticketType.price)
            };
        }
    });

    const url = `/events/${event.value.id}/tickettypes`;

    try {
        const response = await Http.update(url, updatedTicketTypes);
        event.value.ticketTypes = response; // Assuming the API returns the updated list of ticket types
        editingId.value = null; // Reset the editing ID to end the edit state
        console.log('Update successful:', response);
    } catch (error) {
        console.error('Failed to update ticket types:', error);
        handleApiError(error); // Call a function to handle the error
    }
}

function handleApiError(error) {
    if (error.response && error.response.data && error.response.data.errorDescription === "Cannot update ticket types as tickets have already been created.") {
        alert("Cannot update ticket types as tickets have already been created.");
    } else {
        alert("Failed to update ticket types. Please try again.");
    }
}

function cancelEdit() {
    editingId.value = null;
}

async function addTicketType() {
    if (editingTicketType.value.name.trim() === '' || editingTicketType.value.price === null) {
        alert('Please fill in all fields.');
        return;
    }

    const newTicketTypes = [{
        name: editingTicketType.value.name,
        price: parseFloat(editingTicketType.value.price)
    }];

    const url = `/events/${event.value.id}/tickettypes`;

    try {
        const response = await Http.post(url, newTicketTypes);
        event.value.ticketTypes.push(...response); // Assuming the API returns the new ticket types
        editingTicketType.value = { name: '', price: '' }; // Reset the form
        console.log('New ticket type added successfully:', response);
    } catch (error) {
        console.error('Error adding new ticket type:', error);
        alert('Failed to add new ticket type.');
    }
}

function backToEventList() {
    router.push({ name: 'event_management' });
}
</script>

<template>
    <div>
        <button @click="backToEventList">Takaisin</button>
        <h1>Lipputyypit | {{ event?.name }}</h1>
        <table class="table" v-if="event && event.ticketTypes && event.ticketTypes.length > 0">
            <thead>
                <tr>
                    <th>Kuvaus</th>
                    <th>Hinta</th>
                    <th>Toiminnot</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="ticketType in event.ticketTypes" :key="ticketType.id">
                    <td>
                        <input v-if="editingId === ticketType.id" v-model="editingTicketType.name">
                        <span v-else>{{ ticketType.name }}</span>
                    </td>
                    <td>
                        <input type="number" v-if="editingId === ticketType.id"
                            v-model.number="editingTicketType.price">
                        <span v-else>{{ ticketType.price }} €</span>
                    </td>
                    <td>
                        <button v-if="editingId === ticketType.id" class="btn btn-success"
                            @click="saveEdit">Tallenna</button>
                        <button v-if="editingId === ticketType.id" class="btn btn-secondary"
                            @click="cancelEdit">Peruuta</button>
                        <button v-else class="btn btn-primary" @click="startEdit(ticketType)">Muokkaa</button>
                    </td>
                </tr>
            </tbody>
        </table>
        <div v-else>
            <p>Lipputyyppejä ei löytynyt.</p>
        </div>

        <div>
            <div>
                <label for="newName">Kuvaus</label>
                <input id="newName" v-model="editingTicketType.name" required>
            </div>
            <div>
                <label for="newPrice">Hinta</label>
                <input type="number" id="newPrice" v-model.number="editingTicketType.price" required>
            </div>
            <button @click="addTicketType" class="btn btn-success">Tallenna</button>
        </div>

    </div>
</template>
