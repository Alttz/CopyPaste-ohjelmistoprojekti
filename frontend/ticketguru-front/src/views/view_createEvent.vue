<script setup lang="ts">
import { ref } from 'vue';
import router from '@/router';
import { Http } from '@/http/http';
import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css';

const defaultEventData = {
    name: '',
    date: new Date().toISOString().slice(0, 16),  // Set default date as current date and time
    city: '',
    place: '',
    ticketCount: 1  // Set a default value other than 0 if needed
};

const eventData = ref({...defaultEventData});

const format = (date) => {
  const day = date.getDate();
  const month = date.getMonth() + 1;
  const year = date.getFullYear();

  const hours = date.getHours().toString().padStart(2, '0'); // Ensures two digits
  const minutes = date.getMinutes().toString().padStart(2, '0'); // Ensures two digits

  return `${day}.${month}.${year} klo ${hours}:${minutes}`;
}

async function submitEvent() {
    console.log('Submitting:', eventData.value);

    // Create a temporary Date object from the eventData.date
    const tempDate = new Date(eventData.value.date);
    tempDate.setHours(tempDate.getHours() + 3);  // Add 3 hours to the time

    // Prepare the data to send, using the adjusted date
    const dataToSend = {
        ...eventData.value,
        date: tempDate.toISOString()  // Use the adjusted date in ISO string format
    };

    try {
        const response = await Http.post('/events', dataToSend);
        console.log('Event submitted successfully:', response);
        alert('Event added successfully!');
        Object.assign(eventData.value, {...defaultEventData});  // Reset form after submission
        router.push({ name: 'event_management' });
    } catch (error) {
        console.error('Failed to submit event:', error);
        alert('Failed to add event.');
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
        <h1>Uusi tapahtuma</h1>
        <form @submit.prevent="submitEvent">
            <div class="form-group">
                <label for="eventDate">Aika:</label>
                <VueDatePicker id="eventDate" v-model="eventData.date" :utc="false" locale="fi" cancelText="peruuta" selectText="valmis" required :format="format"></VueDatePicker>
            </div>
            <div class="form-group">
                <label for="eventName">Paikka:</label>
                <input type="text" id="eventName" v-model="eventData.place" required>
            </div>
            <div class="form-group">
                <label for="eventCity">Kuvaus:</label>
                <input type="text" id="eventCity" v-model="eventData.name" required>
            </div>
            <div class="form-group">
                <label for="eventCity">Kaupunki:</label>
                <input type="text" id="eventCity" v-model="eventData.city" required>
            </div>
            <div class="form-group">
                <label for="ticketCount">Lippuja kpl:</label>
                <input type="number" id="ticketCount" v-model.number="eventData.ticketCount" required min="1">
            </div>
            <br>
            <button type="submit" class="btn btn-primary">Tallenna</button>
        </form>
    </div>
</template>


<style scoped>
</style>
	