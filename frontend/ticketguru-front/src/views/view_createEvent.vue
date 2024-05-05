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

const eventData = ref({ ...defaultEventData });

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
        Object.assign(eventData.value, { ...defaultEventData });  // Reset form after submission
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
                <label for="eventDate" class="inline-label">Aika:</label>
                <div class="datetime-picker-wrapper">
                    <VueDatePicker id="eventDate" v-model="eventData.date" :utc="false" locale="fi" cancelText="peruuta"
                        selectText="valmis" required :format="format"></VueDatePicker>
                </div>

            </div>
            <div class="form-group">
                <label for="eventName" class="inline-label">Paikka:</label>
                <input type="text" id="eventName" class="input" v-model="eventData.place" required>
            </div>
            <div class="form-group">
                <label for="eventCity" class="inline-label">Kuvaus:</label>
                <input type="text" id="eventCity" class="input" v-model="eventData.name" required>
            </div>
            <div class="form-group">
                <label for="eventCity" class="inline-label">Kaupunki:</label>
                <input type="text" id="eventCity" class="input" v-model="eventData.city" required>
            </div>
            <div class="form-group">
                <label for="ticketCount" class="inline-label">Lippuja kpl:</label>
                <input type="number" id="ticketCount" class="input" v-model.number="eventData.ticketCount" required
                    min="1">
            </div>
            <br>
            <button type="submit" class="btn btn-primary">Tallenna</button>
        </form>
    </div>
</template>

<style scoped>
/* Custom styles for inputs */
.input {
    width: calc(250px);
    /* Adjust width as needed */
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
    width: calc(275px);
    /* Adjust width as needed */
    display: inline-block;
}

.datetime-picker {
    width: 100%;
}

.datetime-picker input {
    height: 38px;
    /* Adjust height to match other inputs */
}

.inline-label {
    display: inline-block;
    width: 100px;
    /* Adjust width as needed */
}

.form-group {
    margin-bottom: 10px;
    /* Adjust margin between form groups */
}
</style>
