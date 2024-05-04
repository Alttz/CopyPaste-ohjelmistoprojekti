<script setup lang="ts">
import { Http } from '@/http/http';
import router from '@/router';
import { ref } from 'vue';

let username = ref("");
let password = ref("");
let message = ref("");
let storage = ref(Http.parseJWT(localStorage.getItem("token")))
let isTokenStillValid = ref(false)

//this is used to check if user has still valid token stored.
//If so he is logged in automaticaly with that token
if (localStorage.getItem("token") !== null) {
    const temp_token = localStorage.getItem("token")
    isTokenStillValid.value = Http.checkTokenValidity(temp_token)
    if(isTokenStillValid) {
        Http.setToken(temp_token)
        router.push('/landing')
    }

}


async function Login() {
    message.value = '';  // Clear previous messages
    let loginSuccessful = await Http.login(username.value, password.value);
    if (!loginSuccessful) {
        message.value = "Käyttäjätunnus tai salasana on väärä";
    } else {
        window.location.reload();
    }
}

</script>

<template>
    <div class="container-fluid">
      <div>
          <h4>Kirjautuminen</h4>
          <hr>
          <input type="text" v-model="username" placeholder="Käyttäjätunnus"><br>
          <input type="password" v-model="password" placeholder="Salasana"><br>
          <button v-on:click="Login">Kirjaudu sisään</button>
          <p>{{ message }}</p>

          <p style="margin-top:150px; color:purple;">Token: {{storage}}</p>
          <p style="color:purple">isTokenStillValid: {{isTokenStillValid}}</p>
      </div>
    </div>
</template>

<style scoped>
    input, button {
        margin-top: 20px;
        width: 20%;
        min-height: 30px;
    }
</style>
