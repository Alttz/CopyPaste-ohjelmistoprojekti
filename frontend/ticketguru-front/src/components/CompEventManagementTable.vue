<script setup lang="ts">
import { type Ref, ref, onMounted, computed } from 'vue';
import { Http } from '@/http/http';
import router from '@/router';

const emit = defineEmits(['addToCart'])

const props = defineProps<{
    toFetch: string,
    allowClick?: boolean,
    clickRedirectTarget?: string,
    allowEdit?: boolean,
    showDeleted?: boolean,
    shownColumns?: any,
    allowSearch?: boolean,
    passedComponents?: any
}>()

const itemsPerPage: Ref<number> = ref(15);
const currentPage: Ref<number> = ref(1);
var items: Ref<any[]> = ref([]);
var totalPages: Ref<number> = ref(0);

onMounted(async () => {
    const temp = await Http.get(props.toFetch);
    items.value = temp.sort((b, a) => new Date(a.date) - new Date(b.date));
    totalPages.value = Math.ceil(items.value.length / itemsPerPage.value);
});

const paginatedItems = computed(() => {
    const startIndex = (currentPage.value - 1) * itemsPerPage.value;
    return items.value.slice(startIndex, startIndex + itemsPerPage.value);
});

function nextPage() {
    if (currentPage.value < totalPages.value) currentPage.value++;
}

function prevPage() {
    if (currentPage.value > 1) currentPage.value--;
}

console.log(props)

function clickAction(id: number, event: any) {
    if (props.allowClick) {
        console.log(router)
        router.push({ name: "event", query: { id: id } })
    }
}

function addNewEvent() {
    router.push({ name: 'createevent' });
}

function editEvent(id: number) {
    console.log('Editing event with ID:', id);
    router.push({ name: 'editevent', params: { id } });
}

function viewTicketTypes(id: number) {
    router.push({ name: 'ticketTypes', params: { id } });
}

function viewReport(id: number) {
    router.push({ name: 'sales_report', params: { id } });
}

function formatDate(dateString) {
    const date = new Date(dateString);
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0'); // Month is 0-indexed
    const year = date.getFullYear();
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');

    return `${day}.${month}.${year} klo ${hours}.${minutes}`;
}
</script>



<template>
	<button class="btn btn-success" @click="addNewEvent">Lisää uusi tapahtuma</button>
	<table class="table table-bordered border-primary">
		<thead class="thead-dark">
			<tr>
				<th>Aika</th>
				<th>Kaupunki</th>
				<th>Kuvaus</th>
				<th>Toiminnot</th>
			</tr>
		</thead>
		<tbody v-if="editMode">
			<tr v-for="data in paginatedItems" :key="data.id">
				<td v-for="d in data">
					<input class="form-control" :value="d">
				</td>
			</tr>
		</tbody>

		<tbody v-if="!editMode">
			<tr v-for="data in paginatedItems" :key="data.id" @click="clickAction(data['id'], $event)">
				<td>{{ formatDate(data['date']) }}</td>
				<td>{{ data['city'] }}</td>
				<td>{{ data['name'] }}</td>
				<td>
					<button class="btn btn-primary btn-sm" @click="editEvent(data.id)">Muokkaa</button>
					<button class="btn btn-info btn-sm" @click="viewTicketTypes(data.id)">Lipputyypit</button>
					<button class="btn btn-warning btn-sm" @click="viewReport(data.id)">Raportti</button>
				</td>
			</tr>
		</tbody>
	</table>
	<nav aria-label="Page navigation">
		<div class="pagination">
			<a class="page-item" :class="{ disabled: currentPage === 1 }">
				<button class="page-link" @click="prevPage">Previous</button>
			</a>
			<span class="page-status">{{ currentPage }} / {{ totalPages }}</span>
			<a class="page-item" :class="{ disabled: currentPage === totalPages }">
				<button class="page-link" @click="nextPage">Next</button>
			</a>
		</div>
	</nav>
</template>


<style scoped>

.pagination .page-link {
    margin: 8px;
    border-radius: 5px; /* rounded corners for links */
}

.pagination .active .page-link {
    background-color: #007bff; /* active link color */
    color: white;
}

.pagination .disabled .page-link {
    color: grey;
    pointer-events: none; /* prevent clicks on disabled links */
}

.pagination .page-link:hover {
    background-color: #0056b3; /* hover state color */
    color: white;
}
.search-button {
	margin-bottom: 25px;
}

* {
	margin: 1px solid black;
}
</style>