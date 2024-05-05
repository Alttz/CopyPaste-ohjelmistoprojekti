import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import { Http } from './http/http'

import VueDatePicker from '@vuepic/vue-datepicker';
import '@vuepic/vue-datepicker/dist/main.css'

const app = createApp(App)

app.component('VueDatePicker', VueDatePicker);
app.use(createPinia())
app.use(router)

app.mount('#app')



/*
//Uses for JWT auth validation
//FIXME: If session is left on enough to expire you need to login 2 times to get valid token
const resp:any = await Http.checkAuth()
console.log(resp)
if(resp == 401) {
    router.push("/login")
    localStorage.removeItem("token")
}
*/
