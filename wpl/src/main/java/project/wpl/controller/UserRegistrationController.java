package project.wpl.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import project.wpl.model.UserRegistry;
import project.wpl.response.UserRegistrationResponse;

import javax.validation.Valid;
import java.util.Map;


@RestController
public class UserRegistrationController {

    private static Logger logger = LoggerFactory.getLogger(UserRegistrationController.class);

    @PostMapping(value =  "/userRegistration", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createDataSet(@Valid @RequestBody UserRegistry userRegistry,  @RequestParam Map<String, String> params) throws Exception {
        //TODO read request body and process
        UserRegistrationResponse registrationResponse = new UserRegistrationResponse();
        return new ResponseEntity(registrationResponse, HttpStatus.ACCEPTED);
       //System dfkfs
    }
}
