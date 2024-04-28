<script setup lang="ts">
import { type Ref, ref } from 'vue';
import { Http } from '@/http/http';
import router from '@/router';

const emit = defineEmits(['addToCart'])


const props = defineProps<{
	toFetch: string
	allowClick?: boolean
	clickRedirectTarget?: string
	allowEdit?: boolean
	showDeleted?: boolean
	shownColumns?: any
	allowSearch?: boolean
	passedComponents?: any
}>()

var editMode: Ref<boolean> = ref(false)
var temp = (await Http.get(props.toFetch))
var items: Ref<any> = ref(temp)

console.log(props)

function clickAction(id: number, event: any) {
	if (props.allowClick) {
		console.log(router)
		//router.push(props.what+"/"+id)
		//router.push(props.what+"/"+id,{state:{id:id}})
		router.push({ name: "event", query: { id: id } })
	}
}

function addNewEvent() {
    router.push({ name: 'createevent' });
}

function editEvent(id: number) {
	console.log('Editing event with ID:', id);  // This should log the correct id
  router.push({ name: 'editevent', params: { id } });
}

function viewTicketTypes(id: number) {
    router.push({ name: 'ticketTypes', params: { id } });
}

//: before passed prop means it's literal type not a string for example. (list can be passed this way)
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
			<tr v-for="data in items">
				<td v-for="d in data">
					<input className="form-control" :value="d">
				</td>
			</tr>
		</tbody>

		<tbody v-if="!editMode">
			<tr v-for="data in items" @click="clickAction(data['id'], $event)">
				<td>{{ data['date'] }}</td>
				<td>{{ data['city'] }}</td>
				<td>{{ data['name'] }}</td>
				<td>
					<button class="btn btn-primary btn-sm" @click="editEvent(data.id)">Muokkaa</button>
					<button class="btn btn-info btn-sm" @click="viewTicketTypes(data.id)">Lipputyypit</button>
					<button class="btn btn-warning btn-sm" @click="generateReport(data)">Raportti</button>
				</td>
			</tr>
		</tbody>
	</table>
</template>


<style scoped>
.search-button {
	margin-botton: 25px;
}

* {
	margin: 1px solid black;
}
</style>
