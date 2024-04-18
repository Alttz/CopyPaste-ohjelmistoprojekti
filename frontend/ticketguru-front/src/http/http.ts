
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
	static apiurl = "https://copypaste-ohjelmistoprojekti-copypaste-ticketguru.rahtiapp.fi/api";

	private static token: string | null = null;

	/*
	static setCredentials(username: string, password: string) {
		this.token = btoa(username + ':' + password);  // Encode credentials only once
	}
	*/

	static async get(url: string) {
		try {
			const response = await axios.get(this.apiurl + url, {
				headers: this.getAuthHeader(),
			});
			return response.data;
		} catch (error) {
			// Handle error
			console.error("Get request failed:", error);
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
		return { Authorization: `Bearer ${this.token}` };
	}

	static async login(username: string, password: string) {
			try {
				const response = await axios.post(`${this.apiurl}/login`, {
					username: username,
					password: password
				});

				const token = response.data.token;
				if (token) {
					this.token = token;
					return true;
				} else {
					console.error("No token received.");
					return false;
				}
			} catch (error) {
				console.error("Authentication failed:", error);
				return false;
			}
		}

}
