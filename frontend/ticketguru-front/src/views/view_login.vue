<script setup lang="ts">
import { Http } from '@/http/http';
import router from '@/router';
import { ref } from 'vue';

let username = ref("");
let password = ref("");
let message = ref("");
const error = ref('');

async function Login() {
  error.value = '';  // Clear previous errors on new request
  try {
    let loginSuccessful = await Http.login(username.value, password.value);
    if (!loginSuccessful) {
      message.value = "Invalid username or password";
      return false;
    } else {
      // Assuming login credentials are validated and correct
      router.push('/test');
    }
  } catch (error) {
    message.value = "Login failed: " + error.message;
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
          <p>{{ message }}</p>
        </CompBox>
      </div>
    </div>
</template>

<style scoped>
    input, button {
        margin-top: 20px;
        width: 100%;
        min-height: 30px;
    }
</style>
