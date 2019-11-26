package project.wpl.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.naming.InsufficientResourcesException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.wpl.exception.InsufficientFundsException;
import project.wpl.exception.ResourceNotFoundException;
import project.wpl.model.*;
import project.wpl.repository.BankAccountRepository;
import project.wpl.repository.CurrentPriceRepository;
import project.wpl.repository.RegistrationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import project.wpl.repository.UserShareRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

  @Autowired
  private BankAccountRepository bankAccountRepository;

  @Autowired
  private RegistrationRepository registrationRepository;

  @Autowired
  private CurrentPriceRepository currentPriceRepository;

  @Autowired
  private UserShareRepository userShareRepository;

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
   @Transactional
    public void transferMoney(TransferInfo transferInfo) throws  InsufficientFundsException{

           Optional<BankAccount> findByIdResult = bankAccountRepository.findById(transferInfo.getToAccounNumber());
           Optional<BankAccount> findFromAccountById = bankAccountRepository.findById(transferInfo.getFromAccountNumber());
           double fromAcccountBalance = findFromAccountById.get().getBalance();

           if(fromAcccountBalance<transferInfo.getAmountToTransfer()) throw new InsufficientFundsException("Insufficent funds");
           double balance = findByIdResult.get().getBalance();
           double updatedBalance = balance + transferInfo.getAmountToTransfer();
           findByIdResult.get().setBalance(updatedBalance);
           bankAccountRepository.save(findByIdResult.get());


           double updatedFromAccountBalance = fromAcccountBalance - transferInfo.getAmountToTransfer();
           findFromAccountById.get().setBalance(updatedFromAccountBalance);
           bankAccountRepository.save(findFromAccountById.get());
    }

    @Transactional
    public void stockBuy(BuyStock buyStock,String username) throws InsufficientFundsException{
        int accountNumber= buyStock.getAccountNumber();
        Optional<CurrentPrice> findByCurrentPrice = currentPriceRepository.findById(buyStock.getSymbol());
        double stockPrice=findByCurrentPrice.get().getPrice();
        double totalPrice = buyStock.getNumberOfUnits()*stockPrice;
        Optional<BankAccount> findByAccountNumber = bankAccountRepository.findById(accountNumber);
        double currentAccountBalance=findByAccountNumber.get().getBalance();
          if(currentAccountBalance-totalPrice<0)
               throw new InsufficientFundsException("Insufficent funds");
          double updateBalance=currentAccountBalance-totalPrice;
        findByAccountNumber.get().setBalance(updateBalance);
        bankAccountRepository.save(findByAccountNumber.get());

       UserShare findUserShareBySymbol = userShareRepository.findBySymbol(buyStock.getSymbol());

       if(findUserShareBySymbol != null)
           {
                     System.out.println("result is "+findUserShareBySymbol.toString());
                      /*int quantity=findUserShareBySymbol.get().getQuantity();
                      int updatedQuantity= quantity+buyStock.getNumberOfUnits();
                      findUserShareBySymbol.get().setQuantity(updatedQuantity);
                      userShareRepository.save(findUserShareBySymbol.get());*/
           }
           else{
               System.out.println("find By Symbol is "+ buyStock.getSymbol());

                  findUserShareBySymbol.setSymbol(buyStock.getSymbol());
                  findUserShareBySymbol.setCompany(buyStock.getCompany_name());
                  findUserShareBySymbol.setQuantity(buyStock.getNumberOfUnits());
                  findUserShareBySymbol.setUsername(username);
                  userShareRepository.save(findUserShareBySymbol);
           }
    }
}
