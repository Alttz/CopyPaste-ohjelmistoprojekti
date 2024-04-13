
//import { HttpRoutes } from "./http_routes"
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
	static apiurl = "http://projekti-ticketguru-tiimi4.rahtiapp.fi/";
	static username:string = ""
	static password:string = ""
	static async get(url: string) {
		const authHeader = this.getAuthHeader();
		try {
			const response = await axios.get(this.apiurl + url, { headers: authHeader });
			return response.data;
		} catch (error) {
			// Handle error
			console.error("GET request failed:", error);
			throw error;
		}
	}

	static async post(url: string, data: any) {
		const authHeader = this.getAuthHeader();
		try {
			const response = await axios.post(this.apiurl + url, data, { headers: authHeader });
			return response.data;
		} catch (error) {
			// Handle error
			console.error("POST request failed:", error);
			throw error;
		}
	}

	static async delete(url: string) {
		const authHeader = this.getAuthHeader();
		try {
			const response = await axios.delete(this.apiurl + url, { headers: authHeader });
			return response.data;
		} catch (error) {
			// Handle error
			console.error("DELETE request failed:", error);
			throw error;
		}
	}

	static async update(url: string, data: any) {
		const authHeader = this.getAuthHeader();
		try {
			const response = await axios.put(this.apiurl + url, data, { headers: authHeader });
			return response.data;
		} catch (error) {
			// Handle error
			console.error("UPDATE request failed:", error);
			throw error;
		}
	}

	private static getAuthHeader() {
		const encodedCredentials = Buffer.from(this.username + ":" + this.password).toString('base64');
		return { Authorization: `Basic ${encodedCredentials}` };
	}
	static async login(username:string,password:string) {

		var temp = this.apiurl+"login/"
		var resp = (await axios.post<any>(temp,{
			username: username,
			password: password
		})).data;


		if (resp["success"] == true) {

			//Localstorage is bit annoying sometimes and don't like to delete things.
			localStorage.removeItem("username")
			localStorage.removeItem("password")
			localStorage.clear()

			//this.auth_header = {"headers":{"Authorization": `Basic ${this.encoded_credentials}`}}


			localStorage.setItem("token",resp["data"]["token"])
			console.log(localStorage.getItem("token"))
			return resp
		}
	}
}
