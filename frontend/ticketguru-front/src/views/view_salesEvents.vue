<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { Http } from '@/http/http';

const route = useRoute();
const router = useRouter();
const eventId = ref(Number(route.params.id));
const purchases = ref([]);
const filteredEvents = ref([]);
const showModal = ref(false);  // State to control modal visibility
const selectedPurchase = ref(null);  // State to hold the selected purchase details

onMounted(async () => {
    try {
        const response = await Http.get('/purchases');
        console.log("Received data:", response);
        purchases.value = response;
        filterEvents();

    } catch (error) {
        console.error('Failed to load purchases:', error);
    }});

    function filterEvents() {
    filteredEvents.value = purchases.value
        .filter(purchase => purchase.tickets.some(ticket => ticket.event === eventId.value))
        .map(purchase => {
            const totalPrice = purchase.tickets
                .filter(ticket => ticket.event === eventId.value)
                .reduce((sum, ticket) => sum + ticket.ticketType.price, 0);
            return {
                ...purchase,
                purchaseId: purchase.id, // Assuming 'id' is the name of the property
                totalPrice
            };
        });
    console.log('Filtered Events:', filteredEvents.value); // Debug log to check the output
}

function performAction(purchaseId) {
    const purchaseDetails = purchases.value.find(purchase => purchase.id === purchaseId);
    if (purchaseDetails) {
        selectedPurchase.value = purchaseDetails;
        showModal.value = true; // Open the modal with the details
    }
}

function closeModal() {
    showModal.value = false; // Close the modal
}

function backToSalesReport() {
    router.push({ name: 'sales_report' });
}
</script>

<template>
    <div>
        <button @click="backToSalesReport">Takaisin</button>
        <h1>Myyntitapahtumat: {{ eventId }}</h1>
        <table class="table">
            <thead>
                <tr>
                    <th>Aika</th>
                    <th>#</th>
                    <th>Summa (€)</th>
                    <th>Toiminnot</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="(event, index) in filteredEvents" :key="event.purchaseId">
                    <td>{{ event.purchaseDate }}</td>
                    <td>{{ event.purchaseId }}</td>
                    <td>{{ event.totalPrice.toFixed(2) }}</td>
                    <td>
                        <button @click="performAction(event.purchaseId)">Näytä</button>
                    </td>
                </tr>
            </tbody>
        </table>
        <div v-if="showModal" class="modal">
            <div class="modal-content">
                <span class="close" @click="closeModal">&times;</span>
                <h3>Myyntitapahtuman {{ selectedPurchase.id }} tiedot</h3>
                <table class="details-table">
                    <thead>
                        <tr>
                            <th>Lippu ID</th>
                            <th>Lippu</th>
                            <th>Hinta (€)</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="ticket in selectedPurchase.tickets" :key="ticket.id">
                            <td>{{ ticket.id }}</td>
                            <td>{{ ticket.ticketType.name }}</td>
                            <td>{{ ticket.ticketType.price.toFixed(2) }}</td>
                            <td>{{ ticket.used ? 'Käytetty' : 'Käyttämätön' }}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</template>

<style scoped>
table {
    width: 100%;
    margin-top: 20px;
    border-collapse: collapse;
}
th, td {
    border: 1px solid #ddd;
    padding: 8px;
}
.modal {
    position: fixed;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
}
.modal-content {
    background-color: white;
    padding: 20px;
    border-radius: 5px;
    width: 50%;
}
.close {
    float: right;
    font-size: 28px;
    cursor: pointer;
}
.details-table th {
    background-color: #f2f2f2;
}
</style>

