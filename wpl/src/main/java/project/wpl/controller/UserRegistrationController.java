package project.wpl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import project.wpl.exception.InputValidationException;
import project.wpl.model.BankAccount;
import project.wpl.model.UserRegistry;
import project.wpl.repository.BankAccountRepository;
import project.wpl.repository.RegistrationRepository;
import project.wpl.service.UserRegistryServiceImpl;

import javax.validation.Valid;
import java.util.Map;


@RestController
public class UserRegistrationController {



 @Autowired
 private UserRegistryServiceImpl userRegistryServiceImpl;
 
 
 @Autowired
   private BankAccountRepository bankAccountRepository;
   
   
    @PostMapping(value =  "/userRegistration", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createDataSet(@Valid @RequestBody UserRegistry userRegistry, @RequestParam Map<String, String> params) throws Exception {
      
    	
    	
    	try {
    	userRegistryServiceImpl.createNewUser(userRegistry);
       }
       catch(InputValidationException e) {
    	   return new ResponseEntity(e.getMessage(),HttpStatus.FORBIDDEN);
       }
       
       //System.out.println("Test");
       
        //System.out.println(userRegistry.getUsername());
       // registrationRepository.findAll().forEach(x -> System.out.println(x));
        return new ResponseEntity("Test Success",HttpStatus.ACCEPTED);
    }

    @PostMapping(value =  "/addBankAccount", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addBankAccount(@Valid @RequestBody BankAccount bankAccount, @RequestParam Map<String, String> params) throws Exception {


        //TODO read request body and process
       // System.out.println(userRegistry.getUsername());
      bankAccountRepository.save(bankAccount);
       // registrationRepository.findAll().forEach(x -> System.out.println(x));
        return new ResponseEntity("Test Success",HttpStatus.ACCEPTED);
    }

    
    
   /* @GetMapping("/findall")
    public List<UserRegistry> findAll(){

        List<UserRegistry> userRegistries = registrationRepository.findAll();
        return userRegistries;
    }*/
}
