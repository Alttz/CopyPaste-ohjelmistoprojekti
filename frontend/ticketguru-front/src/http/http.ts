import axios from "axios"

/*
Kirjoitin tän classin alunperin toimimaan JWT authin kanssa.
Nyt me joudutaan handlaamaan tää erilailla sillä kaikki muut ryhmät käyttää basic authia
Ei sillälailla hirveän erilainen mutta pitää tutkia miten basic authi toimii, että miten passataan
tunnukset joka requestin mukana järkevästi


login sivun scripti tekee niin, että se tallentaa selaimen localstorageen tarivtut auth tiedot ja sitten
tässä classissa vaan haetaan ne kun tarvitaan
*/

export class Http {
	static apiurl: string = "http://127.0.0.1:8080/api/"
	static token: string|undefined = undefined
	static auth_header: any|undefined = undefined

	
	static async get(request_address:string) {

		const resp = axios.get(this.apiurl+request_address,this.auth_header)
		.catch(function (error) {
			if (error.response) {
				// The request was made and the server responded with a status code
				// that falls out of the range of 2xx
				return error.response

			} else if (error.request) {
				// The request was made but no response was received
				// `error.request` is an instance of XMLHttpRequest in the browser and an instance of
				// http.ClientRequest in node.js
				console.log(error.request);
			} else {
				// Something happened in setting up the request that triggered an Error
				console.log('Error', error.message);
		  }
		  console.log(error.config);
		});
		return resp



	}

	static async post(route:string,values:any) {
		//values should be a dict
		var resp = (await axios.post<any>(route,values)).data;
		return resp
	}

	static async update(route:string,values:any){

	}

	static async delete(request_address:string){

		var resp = (await axios.delete<any>(route,this.auth_header))


	}
	
	/*Näillä methodeilal voi toteuttaa JWT kirjautumisen ja tokenin käynnissäolon validoinnin. (checkAuth() on hieman buginen)*/
	/*
	static async login(username:string,password:string) {
		if (this.token != undefined) {return true}
		var temp = this.apiurl+"login/"
		var resp = (await axios.post<any>(temp,{
			username: username,
			password: password
		})).data;

		if (resp["success"] == true) {
			localStorage.removeItem("token")
			localStorage.clear()

			this.token = resp["data"]["token"]
			this.auth_header = {"headers":{"Authorization": `Bearer ${this.token}`}}
			localStorage.setItem("token",resp["data"]["token"])
			console.log(localStorage.getItem("token"))
			return resp
		}
		return false
	}

	static async checkAuth() {
		//If token is not defined load token from local stroage and set auth headers
		if (this.token == undefined) {
			var token = localStorage.getItem("token")
			if (token) {
				this.token = token
				this.auth_header = {"headers":{"Authorization": `Bearer ${this.token}`}}
			}
		}

		//Call api's router for checking validity of current auth token.
		//msg	"Token has expired"
		var url = this.apiurl+"validate/"
		var resp = axios.get(url,this.auth_header)
		.catch(function (error) {
			if (error.response) {
			  // The request was made and the server responded with a status code
			  // that falls out of the range of 2xx
			  return error.response.status
			  //console.log(error.response.data);
			  //console.log(error.response.status);
			  //console.log(error.response.headers);
			}
		});
		return resp
	}
	*/
}
