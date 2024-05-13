
//import { HttpRoutes } from "./http_routes"
import axios from "axios"

/*
Yksinkertaisuudessaan Wrapper luokka axioxelle joka trackaa oikeat post locationit
ja authentificationit
*/

export class Http {
	static apiurl = "https://copypaste-ohjelmistoprojekti-copypaste-ticketguru.rahtiapp.fi/api";

	private static token: string | null = localStorage.getItem("token");

	static isAuthenticated() {
		return this.token !== null && this.checkTokenValidity(this.token);
	  }
	

	static checkTokenValidity(passed_token:string) {
		try {
			const tokenData = JSON.parse(atob(passed_token.split('.')[1])); // Decode JWT token
			const expirationTime = tokenData.exp * 1000; // Convert expiration time to milliseconds
			const currentTime = Date.now(); // Current time in milliseconds

			return currentTime <= expirationTime;

		} catch (error) {
			console.log("Error decoding token: " + error.message)
		}

	}

	static parseJWT(passed_token) {
		try {
			return JSON.parse(atob(passed_token.split('.')[1]));

		} catch (error) {
			console.log("Error decoding token: " + error.message)
		}

	}

	static setToken(passed_token) {
		this.token = passed_token
	}

	/*
	static setCredentials(username: string, password: string) {
		this.token = btoa(username + ':' + password);  // Encode credentials only once
	}
	*/

	static async get(url: string) {
		try {
			const response = await axios.get(this.apiurl + url, { headers: this.getAuthHeader(),});
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

	static async put(url: string, data: any) {
		const authHeader = this.getAuthHeader();
		try {
			const response = await axios.put(this.apiurl + url, data, { headers: authHeader });
			return response.data;
		} catch (error) {
			// Handle error
			console.error("PUT request failed:", error);
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
					localStorage.setItem("token", token);


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
