<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { Http } from '@/http/http';

const route = useRoute();
const router = useRouter();

const eventData = ref({
    id: null,
    name: '',
    date: null,
    city: '',
    place: '',
    ticketCount: 0
});

onMounted(async () => {
    const eventId = route.params.id;
    if (eventId) {
        try {
            const response = await Http.get(`/events/${eventId}`);
            console.log("Received data:", response); // Log to see what the API returns
            const data = response[0]; // Extract the first element from the array
            eventData.value = { ...eventData.value, ...data };
        } catch (error) {
            console.error('Failed to fetch event:', error);
            alert('Failed to load event data.');
        }
    }
});



async function updateEvent() {
    try {
        const response = await Http.update(`/events/${eventData.value.id}`, eventData.value);
        console.log('Event updated successfully:', response);
        alert('Event updated successfully!');
        router.push({ name: 'event_management' });
    } catch (error) {
        console.error('Failed to update event:', error);
        alert('Failed to update event.');
    }
}

function goBack() {
    router.push({ name: 'event_management' });
}
</script>

<template>
    <div>
        <br>
        <button class="btn btn-secondary" @click="goBack">Takaisin</button>
        <h1>Muokkaa tapahtumaa</h1>
        <div v-if="eventData.id">
            <form @submit.prevent="updateEvent">
                <div class="form-group">
                    <label for="eventDate">Aika: </label>
                    <input type="date" id="eventDate" v-model="eventData.date" required>
                </div>
                <div class="form-group">
                    <label for="eventPlace">Paikka: </label>
                    <input type="text" id="eventPlace" v-model="eventData.place" required>
                </div>
                <div class="form-group">
                    <label for="eventName">Kuvaus: </label>
                    <input type="text" id="eventName" v-model="eventData.name" required>
                </div>
                <div class="form-group">
                    <label for="eventCity">Kaupunki: </label>
                    <input type="text" id="eventCity" v-model="eventData.city" required>
                </div>
                <div class="form-group">
                    <label for="ticketCount">Lippuja kpl: </label>
                    <input type="number" id="ticketCount" v-model.number="eventData.ticketCount" required min="1">
                </div>
                <br>
                <button type="submit" class="btn btn-primary">Päivitä</button>
            </form>
        </div>
        <div v-else>
            Loading event data...
        </div>
    </div>
</template>
