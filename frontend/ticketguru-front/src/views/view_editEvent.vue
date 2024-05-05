<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { Http } from '@/http/http';

import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css'

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

const format = (date) => {
  const day = date.getDate();
  const month = date.getMonth() + 1;
  const year = date.getFullYear();

  const hours = date.getHours().toString().padStart(2, '0'); // Ensures two digits
  const minutes = date.getMinutes().toString().padStart(2, '0'); // Ensures two digits

  return `${day}.${month}.${year} klo ${hours}:${minutes}`;
}

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
    let adjustedDate = eventData.value.date; // Copy the original date

    // Check if the date is set and create a new date object for adjustments
    if (adjustedDate) {
        let tempDate = new Date(adjustedDate);
        tempDate.setHours(tempDate.getHours() + 2); // Adjust hours here
        adjustedDate = tempDate.toISOString(); // Convert the adjusted date back to ISO string
    }

    // Create a copy of eventData to send to the server
    let dataToSend = {
        ...eventData.value,
        date: adjustedDate // Use the adjusted date
    };

    try {
        const response = await Http.update(`/events/${dataToSend.id}`, dataToSend);
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
                    <label for="eventDate" class="inline-label">Aika: </label>
                    <div class="datetime-picker-wrapper">
                        <VueDatePicker class="datetime-picker input" id="eventDate" v-model="eventData.date" :utc="false" locale="fi" cancelText="peruuta" selectText="valmis" required :format="format"></VueDatePicker>
                    </div>
                </div>
                <div class="form-group">
                    <label for="eventPlace" class="inline-label">Paikka: </label>
                    <input type="text" id="eventPlace" class="input" v-model="eventData.place" required>
                </div>
                <div class="form-group">
                    <label for="eventName" class="inline-label">Kuvaus: </label>
                    <input type="text" id="eventName" class="input" v-model="eventData.name" required>
                </div>
                <div class="form-group">
                    <label for="eventCity" class="inline-label">Kaupunki: </label>
                    <input type="text" id="eventCity" class="input" v-model="eventData.city" required>
                </div>
                <div class="form-group">
                    <label for="ticketCount" class="inline-label">Lippuja kpl: </label>
                    <input type="number" id="ticketCount" class="input" v-model.number="eventData.ticketCount" required min="1">
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

<style>
/* Custom styles for inputs */
.input {
  width: calc(250px); /* Adjust width as needed */
  padding: 0.375rem 0.75rem;
  font-size: 1rem;
  line-height: 1.5;
  color: #495057;
  background-color: #fff;
  background-clip: padding-box;
  border: 1px solid #ced4da;
  border-radius: 0.25rem;
  transition: border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out;
}

.datetime-picker-wrapper {
  width: calc(275px); /* Adjust width as needed */
  display: inline-block;
}

.datetime-picker {
  width: 100%;
}

.datetime-picker input {
  height: 38px; /* Adjust height to match other inputs */
}

.inline-label {
  display: inline-block;
  width: 100px; /* Adjust width as needed */
}

.form-group {
  margin-bottom: 10px; /* Adjust margin between form groups */
}
</style>




