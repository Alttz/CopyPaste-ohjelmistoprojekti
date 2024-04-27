<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { Http } from '@/http/http';

const route = useRoute();
const router = useRouter();

const eventData = ref({
    id: null,
    name: '',
    date: '',
    city: '',
    place: '',
    ticketCount: 0
});

onMounted(async () => {
    const eventId = route.params.id;
    if (eventId) {
        try {
            const data = await Http.get(`/events/${eventId}`);
            console.log("Received data:", data); // Log to see what the API returns
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
        <button class="btn btn-secondary" @click="goBack">Back</button>
        <h1>Edit Event</h1>
        <div v-if="eventData.id">
            <form @submit.prevent="updateEvent">
                <div class="form-group">
                    <label for="eventDate">Date</label>
                    <input type="text" id="eventDate" v-model="eventData.date" required>
                </div>
                <div class="form-group">
                    <label for="eventPlace">Place</label>
                    <input type="text" id="eventPlace" v-model="eventData.place" required>
                </div>
                <div class="form-group">
                    <label for="eventName">Description</label>
                    <input type="text" id="eventName" v-model="eventData.name" required>
                </div>
                <div class="form-group">
                    <label for="eventCity">City</label>
                    <input type="text" id="eventCity" v-model="eventData.city" required>
                </div>
                <div class="form-group">
                    <label for="ticketCount">Ticket count</label>
                    <input type="number" id="ticketCount" v-model.number="eventData.ticketCount" required min="1">
                </div>
                <button type="submit" class="btn btn-primary">Update</button>
            </form>
        </div>
        <div v-else>
            Loading event data...
        </div>
    </div>
</template>
