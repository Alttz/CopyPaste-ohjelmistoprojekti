<script setup lang="ts">
import { ref } from 'vue';
import router from '@/router';
import { Http } from '@/http/http'; // Ensure Http is correctly imported

const defaultEventData  = ({
    name: '',
    date: '',
    city: '',
    place: '',
    ticketCount: 0
});

const eventData = ref({...defaultEventData});

async function submitEvent() {
    console.log('Submitting:', eventData.value);
    try {
        const response = await Http.post('/events', eventData.value);
        console.log('Event submitted successfully:', response);
        alert('Event added successfully!'); 
        Object.assign(eventData.value, {...defaultEventData});
    } catch (error) {
        console.error('Failed to submit event:', error);
        alert('Failed to add event.'); 
    }}

function goBack() {
    router.push({ name: 'event_management' });
}
</script>

<template>
    <div>
        <br>
        <button class="btn btn-secondary" @click="goBack">Takaisin</button>
        <h1>Uusi tapahtuma</h1>
        <form @submit.prevent="submitEvent">
            <div class="form-group">
                <label for="eventDate">Aika: </label>
                <input type="text" id="eventDate" v-model="eventData.date" required>
            </div>
            <div class="form-group">
                <label for="eventName">Paikka: </label>
                <input type="text" id="eventName" v-model="eventData.place" required>
            </div>
            <div class="form-group">
                <label for="eventCity">Kuvaus: </label>
                <input type="text" id="eventCity" v-model="eventData.name" required>
            </div>
            <div class="form-group">
                <label for="eventCity">Kaupunki: </label>
                <input type="text" id="eventCity" v-model="eventData.city" required>
            </div>
            <div class="form-group">
                <label for="ticketCount">Lippu kpl: </label>
                <input type="number" id="ticketCount" v-model.number="eventData.ticketCount" required min="1">
            </div>
            <br>
            <button type="submit" class="btn btn-primary">Tallenna</button>
        </form>
    </div>
</template>

<style scoped>
</style>
	