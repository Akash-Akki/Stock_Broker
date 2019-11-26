package project.wpl.controller;

import java.security.Principal;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.wpl.exception.InputValidationException;
import project.wpl.exception.InsufficientFundsException;
import project.wpl.exception.ResourceNotFoundException;
import project.wpl.exception.SessionNotFoundException;
import project.wpl.model.BankAccount;
import project.wpl.model.BuyStock;
import project.wpl.model.TransferInfo;
import project.wpl.model.UserRegistry;
import project.wpl.repository.BankAccountRepository;
import project.wpl.service.UserDetailServiceImpl;
import project.wpl.service.UserRegistryServiceImpl;
import project.wpl.service.UserValidator;


@RestController
public class UserRegistrationController {

  @Autowired
  private UserRegistryServiceImpl userRegistryServiceImpl;

  @Autowired
  private UserDetailServiceImpl userDetailService;

  @Autowired
  private BankAccountRepository bankAccountRepository;

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserValidator userValidator;

  @PostMapping(value = "/userRegistration", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity createDataSet(@Valid @RequestBody UserRegistry userRegistry,
                                      @RequestParam Map<String, String> params, BindingResult bindingResult) throws Exception {
    try {
      System.out.println("Creating user");
       userValidator.validate(userRegistry,bindingResult);
       if(bindingResult.hasErrors())
       {
         return new ResponseEntity("invalid username or password - username should be minimum of 6 characters and password should be minimum of 8 characters ",HttpStatus.FORBIDDEN);
       }


      userRegistryServiceImpl.createNewUser(userRegistry);
    } catch (InputValidationException e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
    }
    // registrationRepository.findAll().forEach(x -> System.out.println(x));
    return new ResponseEntity("user registration Success", HttpStatus.ACCEPTED);
  }


  @PutMapping(value = "/updateUserInfo", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity updateDataSet(@Valid @RequestBody UserRegistry userRegistry,
      @RequestParam Map<String, String> params, HttpSession session) throws Exception {
    // System.out.println("size " + params.size());
    String username = (String) session.getAttribute("username");

    try {
      if (username == null) {
        throw new SessionNotFoundException("User is Not logged in");
      }
      userDetailService.updateUserInformation(userRegistry, username);

      // System.out.println("size " + params.size());
    } catch (ResourceNotFoundException e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
    } catch (SessionNotFoundException e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
    }
    // registrationRepository.findAll().forEach(x -> System.out.println(x));
    return new ResponseEntity("update Info Success", HttpStatus.ACCEPTED);
  }



  @PostMapping(value = "/addBankAccount", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity addBankAccount(@Valid @RequestBody BankAccount bankAccount,
      @RequestParam Map<String, String> params, HttpSession session) throws Exception {
    String username = (String) session.getAttribute("username");
    try {

      if (username == null) {
        throw new SessionNotFoundException("User Not logged in");
      }
    } catch (SessionNotFoundException e) {
      return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
    }
    userDetailService.createBankAccount(bankAccount, username);
   // System.out.println("username is addbannkaccount " + username);
    // registrationRepository.findAll().forEach(x -> System.out.println(x));
    return new ResponseEntity("Add Bank Account success", HttpStatus.ACCEPTED);
  }

  @PostMapping(value="/transferMoney", consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity moneyTransfer(@Valid @RequestBody TransferInfo transferInfo,
                                      @RequestParam Map<String ,String> params,HttpSession session)
  {
     try {
       userDetailService.transferMoney(transferInfo);
     }
     catch(InsufficientFundsException e)
     {
       return new ResponseEntity(e.getMessage(),HttpStatus.FORBIDDEN);
     }
      return new ResponseEntity("sucesfully amount transferred",HttpStatus.ACCEPTED);
  }


  @PostMapping(value="/buy",consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity buyStock(@Valid @RequestBody BuyStock buyStock,@RequestParam Map<String,String> params,HttpSession session)
  {
    String username = (String) session.getAttribute("username");
   try {
     userDetailService.stockBuy(buyStock,username);
   }
   catch(InsufficientFundsException e){
     return new ResponseEntity(e.getMessage(),HttpStatus.FORBIDDEN);
   }
     return new ResponseEntity("stock bought succesfully",HttpStatus.ACCEPTED);
  }





  @PostMapping(value = "/forgotPassword", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity forgotPassword(@Valid @RequestBody UserRegistry userRegistry,
      @RequestParam Map<String, String> params) throws Exception {
    JSONObject entities = userRegistryServiceImpl.validateSecurityQuestion(userRegistry);
    return new ResponseEntity<Object>(entities, HttpStatus.OK);
  }

  @PutMapping(value = "/resetPassword", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity resetPassword(@Valid @RequestBody UserRegistry userRegistry,
      @RequestParam Map<String, String> params) throws Exception {
    userRegistryServiceImpl.passwordReset(userRegistry);
    return new ResponseEntity("reset password sucess", HttpStatus.OK);
  }

  @PostMapping("/login")
  public String login(@Valid @RequestBody UserRegistry userRegistry, HttpServletRequest request,
      Principal principal) {
    System.out.println("logging in");
    // System.out.println("principal user " + principal.getName());
    UserDetails userDetails = userDetailsService.loadUserByUsername(userRegistry.getUsername());
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
        new UsernamePasswordAuthenticationToken(userDetails, userRegistry.getPasswd(),
            userDetails.getAuthorities());
    System.out.println("Auth test");
    authenticationManager.authenticate(usernamePasswordAuthenticationToken);

    if (usernamePasswordAuthenticationToken.isAuthenticated()) {
      // create session
      HttpSession session = request.getSession();
      session.setAttribute("username", userRegistry.getUsername());

      SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
      System.out.println(String.format("Auto login %s successfully!", userRegistry.getUsername()));
    } else
      return "Username or password invalid";

    return "login";
  }



  @GetMapping(value = "/customlogout")
  public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
    // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    HttpSession session = request.getSession(false);
    SecurityContextHolder.clearContext();
    session = request.getSession();
    if (session != null) {
      session.invalidate();
      return "logged out";
    }

    return "error";

  }

  @GetMapping({"/", "/welcome"})
  public String welcome(Model model, Principal principal) {
    System.out.println(principal.getName());
    return "welcome";
  }



  /*
   * @GetMapping("/findall") public List<UserRegistry> findAll(){
   * 
   * List<UserRegistry> userRegistries = registrationRepository.findAll(); return userRegistries; }
   */
}
