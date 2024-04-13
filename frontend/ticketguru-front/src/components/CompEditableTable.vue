<script setup lang="ts">
	import axios from 'axios';
	import { type Ref, ref } from 'vue';
	import { Http } from '@/http/http';
	import router from '@/router';



	const props = defineProps<{
		toFetch:string
		allowClick?: boolean
		allowEdit?: boolean
		showDeleted?: boolean
		shownColumns?: any
		allowSearch?: boolean
	}>()

	var editMode : Ref<boolean> = ref(false)
	var temp = (await Http.get(props.toFetch))
	var items : Ref<any> = ref(temp)
	var keys : Ref<any> = ref(Object.keys(temp[0]))
	
	//Tell user if we are in edit mode
	var editText : Ref<any> = ref("Edit")



	function toggleEdit(event:any) {
		console.log(editMode.value)
		if (props.allowEdit) {
			if (editMode.value) {
				saveData()
				editMode.value = false
				editText.value = "Edit"
			}  else {
				editMode.value = true
				editText.value = "Save"
			}
			return editMode
		}
	}

	function saveData() {}


	//: before passed prop means it's literal type not a string for example. (list can be passed this way)
</script>


<template>
	<table class="table table-bordered border-primary">
		<thead class="thead-dark">
			<tr>
				<th v-for="key in keys">{{key}}</th>
			</tr>
		</thead>
		<tbody v-if="editMode">
			<tr v-for="data in items">
				<td v-for="d in data">
					<input className="form-control" :value="d">
				</td>
			</tr>
		</tbody>

		<tbody v-else>
			<tr v-for="data in items">
				<td v-for="d in data">{{d}}</td>
			</tr>
		</tbody>
	</table>

	<div class="col-md-4" v-if="props.allowEdit === true">
		<button class="btn" :class="{'btn-success': editMode, 'btn-warning':!editMode}" @click="toggleEdit($event)">{{editText}}</button>
	</div>
</template>


<style scoped>
	.search-button {
		margin-botton: 25px;
	}

</style>


