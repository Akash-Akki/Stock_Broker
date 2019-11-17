package project.wpl.service;

import java.util.Base64;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.wpl.constants.Constants;
import project.wpl.exception.InputValidationException;
import project.wpl.exception.ResourceNotFoundException;
import project.wpl.model.UserRegistry;
import project.wpl.repository.RegistrationRepository;

@Service
public class UserRegistryServiceImpl {


  @Autowired
  private RegistrationRepository registrationRepository;


  public void createNewUser(@Valid UserRegistry userRegistry) {
    validate(userRegistry);
    userRegistry.setPasswd(Base64.getEncoder().encodeToString(userRegistry.getPasswd().getBytes()));
    registrationRepository.save(userRegistry);
  }


  public void updateUserInformation(@Valid UserRegistry userRegistry, String username) {
    // TODO Auto-generated method stub

    Optional<UserRegistry> findByIdResult = registrationRepository.findById(username);
    System.out.println("username " + username);

    if (findByIdResult.isPresent()) {
      findByIdResult.get().setEmail(userRegistry.getEmail());
      findByIdResult.get().setAddress(userRegistry.getAddress());
      registrationRepository.save(findByIdResult.get());
    } else {
      throw new ResourceNotFoundException("Username not found");
    }

  }

  public void validate(@Valid UserRegistry userRegistry) throws InputValidationException {
    if (!userRegistry.getPasswordConfirm().equals(userRegistry.getPasswd())) {
      throw new InputValidationException("This password doesn't match with the above passsword");
    }
    if (!Constants.set.contains(userRegistry.getSecurity_qn())) {
      throw new InputValidationException(
          "Security question doesn't match with the available security quesitons");
    }
  }



}
