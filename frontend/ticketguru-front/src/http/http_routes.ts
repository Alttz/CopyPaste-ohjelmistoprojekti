


export class HttpRoutes {
    static base_route = "http://127.0.0.1:8080/api/"
    static routing_to_use:string = "copypaste"
    static routes = {
        "base_route": "",
        "login":"",
        "events":"",
        "tickets":"",
        "users":"",
        "purchases":""
        
    }
    
    static get(value:string) {
        return this.routes[value]
    }
    

}
