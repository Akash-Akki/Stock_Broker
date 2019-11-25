package project.wpl.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.wpl.exception.ResourceNotFoundException;
import project.wpl.model.BankAccount;
import project.wpl.model.Role;
import project.wpl.model.UserRegistry;
import project.wpl.repository.BankAccountRepository;
import project.wpl.repository.RegistrationRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

  @Autowired
  private BankAccountRepository bankAccountRepository;

  @Autowired
  private RegistrationRepository registrationRepository;

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

  public void createBankAccount(@Valid BankAccount bankAccount, String username) {
    // TODO Auto-generated method stub
    System.out.println("account balance " + bankAccount.getBalance());
    System.out.println("username isssss bankaccount " + username);
    bankAccount.setUser_name(username);
    bankAccountRepository.save(bankAccount);
  }


  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
    UserRegistry user = registrationRepository.findByUsername(userName);
    if (user == null)
      throw new UsernameNotFoundException(userName);

    Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
    for (Role role : user.getRoles()) {
      grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
    }

    return new org.springframework.security.core.userdetails.User(user.getUsername(),
        user.getPasswd(), grantedAuthorities);

  }
}
