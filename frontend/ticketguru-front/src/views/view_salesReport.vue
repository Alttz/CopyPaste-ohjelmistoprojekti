<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { Http } from '@/http/http';

const route = useRoute();
const router = useRouter();
const eventId = ref(Number(route.params.id));
const event = ref(null);
const ticketTypes = ref([]);
const totalRevenue = ref(0);

onMounted(async () => {
    if (!eventId.value) {
        console.error('Event ID is required');
        return;
    }

    try {
        const eventResponse = await Http.get(`/events/${eventId.value}`); // Fetch event details
        event.value = eventResponse[0];
        const purchases = await Http.get('/purchases');
        processPurchases(purchases);
    } catch (error) {
        console.error('Failed to load data:', error);
    }
});

function processPurchases(purchases) {
    const revenueMap = new Map();

    for (const purchase of purchases) {
        for (const ticket of purchase.tickets) {
            if (ticket.event !== eventId.value) continue;
            const { name, price } = ticket.ticketType;
            if (!revenueMap.has(name)) {
                revenueMap.set(name, { name, count: 0, totalRevenue: 0, id: ticket.ticketType.id });
            }
            let current = revenueMap.get(name);
            current.totalRevenue += price;
            current.count += 1;
        }
    }

    ticketTypes.value = Array.from(revenueMap.values());
    totalRevenue.value = ticketTypes.value.reduce((acc, type) => acc + type.totalRevenue, 0);
}

function backToEvents() {
    router.push({ name: 'event_management' });
}

function goToSalesEvents() {
    router.push({ name: 'sales_events' });
}

</script>

<template>
    <div>
        <br>
        <button @click="backToEvents">Takaisin</button>
        <h3>Myyntiraportti | {{ event?.name }}</h3> 
        <table class="table">
            <thead>
                <tr>
                    <th>Lipputyyppi</th>
                    <th>Kpl</th>
                    <th>Yhteensä</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="ticketType in ticketTypes" :key="ticketType.id">
                    <td>{{ ticketType.name }}</td>
                    <td>{{ ticketType.count }}</td>
                    <td>{{ ticketType.totalRevenue.toFixed(2) }} €</td>
                </tr>
                <tr>
                    <th>Yhteensä</th>
                    <td>{{ ticketTypes.reduce((sum, type) => sum + type.count, 0) }}</td>
                    <td>{{ totalRevenue.toFixed(2) }}</td>
                </tr>
            </tbody>
        </table>
        <button @click="goToSalesEvents">Myyntitapahtumat</button>
    </div>
</template>




<style scoped>
table {
    width: 500px;
    margin-top: 20px;
}

button {
	margin: 3px;
}
</style>
