package copypaste.ticketguru.web.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

//This needs to be made into RestController not a normal Controller
@Controller
public class AuthController {

    private final String rooturl = "/api/authorize";
    @RequestMapping(rooturl)
    @ResponseBody
    public String welcome() {
        return "This is the auth page where user is rediricted bu WebSecurityConfig";
    }
}
