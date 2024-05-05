<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { Http } from '@/http/http';

const route = useRoute();
const router = useRouter();
const eventId = ref(Number(route.params.id));
const purchases = ref([]);
const event = ref(null);
const filteredEvents = ref([]);
const showModal = ref(false);
const selectedPurchase = ref(null);

const currentPage = ref(1);
const itemsPerPage = ref(15);
const pageCount = computed(() => Math.ceil(filteredEvents.value.length / itemsPerPage.value));
const paginatedEvents = computed(() => {
    const start = (currentPage.value - 1) * itemsPerPage.value;
    const end = start + itemsPerPage.value;
    return filteredEvents.value.slice(start, end);
});

function prevPage() {
    if (currentPage.value > 1) {
        currentPage.value--;
    }
}

function nextPage() {
    if (currentPage.value < pageCount.value) {
        currentPage.value++;
    }
}

onMounted(async () => {
    try {
        const eventResponse = await Http.get(`/events/${eventId.value}`);
        event.value = eventResponse[0];
        const response = await Http.get('/purchases');
        purchases.value = response;
        filterEvents();
    } catch (error) {
        console.error('Failed to load purchases:', error);
    }
});

function filterEvents() {
    filteredEvents.value = purchases.value
        .filter(purchase => purchase.tickets.some(ticket => ticket.event === eventId.value))
        .map(purchase => {
            const totalPrice = purchase.tickets
                .filter(ticket => ticket.event === eventId.value)
                .reduce((sum, ticket) => sum + ticket.ticketType.price, 0);
            return {
                ...purchase,
                purchaseId: purchase.id,
                totalPrice,
                purchaseDate: new Date(purchase.purchaseDate)
            };
        })
        .sort((b, a) => a.purchaseDate - b.purchaseDate);
}

function performAction(purchaseId) {
    const purchaseDetails = purchases.value.find(purchase => purchase.id === purchaseId);
    if (purchaseDetails) {
        selectedPurchase.value = purchaseDetails;
        showModal.value = true;
    }
}

function closeModal() {
    showModal.value = false;
}

function backToSalesReport() {
    router.push({ name: 'sales_report' });
}

function formatDate(dateString) {
    const date = new Date(dateString);
    date.setHours(date.getHours() + 3);
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const year = date.getFullYear();
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    return `${day}.${month}.${year} klo ${hours}:${minutes}`;
}
</script>



<template>
    <div>
        <button @click="backToSalesReport">Takaisin</button>
        <h1>Myyntitapahtumat | {{ event?.name }}</h1>
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
                <tr v-for="(event, index) in paginatedEvents" :key="event.purchaseId">
                    <td>{{ formatDate(event.purchaseDate) }}</td>
                    <td>{{ event.purchaseId }}</td>
                    <td>{{ event.totalPrice.toFixed(2) }}</td>
                    <td>
                        <button @click="performAction(event.purchaseId)">Näytä</button>
                    </td>
                </tr>
            </tbody>
        </table>
        <nav aria-label="Page navigation">
            <div class="pagination">
                <a class="page-item" :class="{ disabled: currentPage === 1 }">
                    <button class="page-link" @click="prevPage">Previous</button>
                </a>
                <span class="page-status">{{ currentPage }} / {{ pageCount }}</span>
                <a class="page-item" :class="{ disabled: currentPage === pageCount }">
                    <button class="page-link" @click="nextPage">Next</button>
                </a>
            </div>
        </nav>
        <div v-if="showModal" class="modal">
            <div class="modal-content">
                <span class="close" @click="closeModal">&times;</span>
                <h3>Myyntitapahtuman {{ selectedPurchase.id }} tiedot</h3>
                <table class="details-table">
                    <thead>
                        <tr>
                            <th>Lippu id</th>
                            <th>Lippukoodi</th>
                            <th>Lipputyyppi</th>
                            <th>Hinta (€)</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="ticket in selectedPurchase.tickets" :key="ticket.id">
                            <td>{{ ticket.id }}</td>
                            <td>{{ ticket.uuid }}</td>
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
    width: 500px;
    margin-top: 20px;
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

.details-table {
    width: 100%;
}

.details-table th {
    background-color: #f2f2f2;
}
</style>

