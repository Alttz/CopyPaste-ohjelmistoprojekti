import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import { Http } from './http/http'

const app = createApp(App)

app.use(createPinia())
app.use(router)

app.mount('#app')




//FIXME: If session is left on enough to expire you need to login 2 times to get valid token
const resp:any = await Http.checkAuth()
console.log(resp)
if(resp == 401) {
    router.push("/login")
    localStorage.removeItem("token")
}
