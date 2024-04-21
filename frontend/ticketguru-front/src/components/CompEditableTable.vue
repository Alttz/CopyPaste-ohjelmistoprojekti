<script setup lang="ts">
	import axios from 'axios';
	import { type Ref, ref } from 'vue';
	import { Http } from '@/http/http';
	import router from '@/router';

	import CompBox from '@/components/CompBox.vue'
    import CompSelect from '@/components/CompSelect.vue'


	const emit = defineEmits(['addToCart'])

	const props = defineProps<{
		toFetch:string
		allowClick?: boolean
		clickRedirectTarget?: string
		allowEdit?: boolean
		showDeleted?: boolean
		shownColumns?: any
		allowSearch?: boolean
		passedComponents?: any
	}>()

	var editMode : Ref<boolean> = ref(false)
	var temp = (await Http.get(props.toFetch))
	var items : Ref<any> = ref(temp)
	var keys : Ref<any> = ref(Object.keys(temp[0]))



	var passedComponents : Ref<any> = ref(props.passedComponents)

	//Tell user if we are in edit mode
	var editText : Ref<any> = ref("Edit")

	console.log(props)


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

	function clickAction(id:number,event:any) {
		if (props.allowClick) {
			console.log(router)
			//router.push(props.what+"/"+id)
			//router.push(props.what+"/"+id,{state:{id:id}})
			router.push({name:"event",query:{id:id}})
		}
	}

	//Let's preprocess what we are gonna add to the cart
	//Not everything is needed overthere
	function addToCart(ticket) {
		//$emit('addToCart',data)



		let processed_data = {
			"id":ticket['id'],
			"date":ticket['date'],
			"place":ticket['place'],
			"city":ticket['city'],
			"name":ticket['name'],
			"ticketType":"Seniori",
			"Price":"0 €"
		}


		emit("addToCart",processed_data)
		//alert(ticket["city"])

	}

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
			<tr v-for="data in items" >
				<td v-for="d in data">
					<input className="form-control" :value="d">
				</td>
			</tr>
		</tbody>

		<tbody v-else>
			<tr v-for="data in items" @click="clickAction(data['id'],$event)">
				<td v-for="d in data">
					<template v-if="Array.isArray(d)">
						<!-- Render something different for array -->
						<CompSelect :todisplay=d></CompSelect>
					</template>
					<template v-else>
						<!-- Render span for non-array values -->
						<span>{{ d }}</span>
					</template>
				</td>

				<td>
					<input type="number" value="1" min="1" :max="data['ticketCount']">

				</td>
				<td>
					<button @click="addToCart(data)">Add to Cart</button>
				</td>
			</tr>
		</tbody>
	</table>


	<p>{{data}}</p>
	<div class="col-md-4" v-if="props.allowEdit === true">
		<button class="btn" :class="{'btn-success': editMode, 'btn-warning':!editMode}" @click="toggleEdit($event)">{{editText}}</button>
	</div>

	<div>


	</div>
</template>


<style scoped>
	.search-button {
		margin-botton: 25px;
	}

	* {
		margin: 1px solid black;
	}

</style>


