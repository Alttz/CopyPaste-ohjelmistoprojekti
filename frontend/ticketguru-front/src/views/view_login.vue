<script setup lang="ts">

import CompBox from '@/components/CompBox.vue';



import { Http,} from '@/http/http';
import router from '@/router';
import { type Ref, ref } from 'vue';


let username : Ref<string> = ref("")
let password : Ref<string> = ref("")
let message : Ref<string> = ref("")

//username.value
//username returs ref odject except in vue template
async function Login() {
  let resp = await Http.login(username.value,password.value)
  if (resp == false) {
    message.value = "Invalid username or password"
    return resp
  } else {
    router.push('/events')
  }
}

</script>

<template>

    <div class="container-fluid">
      <div>
        <CompBox>
          <h4>Login</h4>
          <hr>
          <input type="text" v-model="username" placeholder="username"><br>
          <input type="password" v-model="password" placeholder="password"><br>
          <button v-on:click="Login">Login</button>
          <p>{{message}}</p>
        </CompBox>
      </div>
    </div>

</template>


<style scoped>

	input,button {
    margin-top: 20px;
    width: 100%;
    min-height: 30px;
  }

</style>
	