package project.wpl.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.validation.Valid;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.wpl.exception.InsufficientFundsException;
import project.wpl.exception.InsufficientStocksException;
import project.wpl.exception.ResourceNotFoundException;
import project.wpl.exception.StocksNotFoundException;
import project.wpl.model.*;
import project.wpl.repository.*;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

  @Autowired
  private BankAccountRepository bankAccountRepository;

  @Autowired
  private RegistrationRepository registrationRepository;

  @Autowired
  private UserShareRepository userShareRepository;

  @Autowired
  private StockListRepository stockListRepository;

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
        Optional<BankAccount> findByAccountNumber = bankAccountRepository.findById(buyStock.getAccountNumber());
        double updatedBalance=accountBalanceUpdate(buyStock,findByAccountNumber,"buy");
        if(updatedBalance<0)
            throw new InsufficientFundsException("Insufficent funds");
        findByAccountNumber.get().setBalance(updatedBalance);
        bankAccountRepository.save(findByAccountNumber.get());
       UserShare findUserShareBySymbol = userShareRepository.findBySymbol(buyStock.getSymbol());
       UserShare userShare=new UserShare();
       if(findUserShareBySymbol != null)
       {
                      int quantity=findUserShareBySymbol.getQuantity();
                      int updatedQuantity= quantity+buyStock.getNumberOfUnits();
                      findUserShareBySymbol.setQuantity(updatedQuantity);
                      userShareRepository.save(findUserShareBySymbol);
       }
       else{
                     userShare.setSymbol(buyStock.getSymbol());
                     userShare.setUsername(username);
                     userShare.setQuantity(buyStock.getNumberOfUnits());
                     userShare.setCompany(buyStock.getCompany_name());
                     System.out.println("before saving");
                     userShareRepository.save(userShare);
           }
    }

    public void stockSell(BuyStock buyStock, String username) throws InsufficientStocksException {
        Optional<BankAccount> findByAccountNumber = bankAccountRepository.findById(buyStock.getAccountNumber());
        double updatedBalance=accountBalanceUpdate(buyStock,findByAccountNumber,"sell");
       findByAccountNumber.get().setBalance(updatedBalance);
        bankAccountRepository.save(findByAccountNumber.get());
        UserShare findUserShareBySymbol = userShareRepository.findBySymbol(buyStock.getSymbol());
        int quantity=findUserShareBySymbol.getQuantity();
        if(buyStock.getNumberOfUnits()>quantity)
            throw new InsufficientStocksException("Insufficent stocks");
        int updatedQuantity= quantity-buyStock.getNumberOfUnits();
        findUserShareBySymbol.setQuantity(updatedQuantity);
        userShareRepository.save(findUserShareBySymbol);
    }




    private double accountBalanceUpdate(BuyStock buyStock, Optional<BankAccount> findByAccountNumber, String func) {
        int accountNumber= buyStock.getAccountNumber();
        //Optional<CurrentPrice> findByCurrentPrice = currentPriceRepository.findById(buyStock.getSymbol());
       double stockPrice=currentValue(buyStock.getSymbol());
        double totalPrice = buyStock.getNumberOfUnits()*stockPrice;
        double currentAccountBalance=findByAccountNumber.get().getBalance();
        double updateBalance=0.0;
        if(func.equals("buy"))
           updateBalance=currentAccountBalance-totalPrice;
        else if(func.equals("sell"))
            updateBalance=currentAccountBalance+totalPrice;
        System.out.println("updated Balance"+updateBalance);
        return updateBalance;
    }

    public String getProfileInfo(String username) {
        List<UserShare> byUsername = userShareRepository.findByUsername(username);
        List<String> symbolList =new ArrayList<String>();
        double stockPrice=0.0;
        double totalStockPrice=0.0;
        UserDetailOutput userDetailOutput = new UserDetailOutput();
        Gson gson = new Gson();
        for(int i=0;i<byUsername.size();i++)
        {
              String symbol = byUsername.get(i).getSymbol();
              stockPrice=currentValue(symbol);
              totalStockPrice =stockPrice*byUsername.get(i).getQuantity();
              userDetailOutput.setNetWorth(totalStockPrice);
              userDetailOutput.setQuantity(byUsername.get(i).getQuantity());
              userDetailOutput.setUsername(username);
              userDetailOutput.setSymbol(symbol);
              //System.out.println("total Stock price " +userDetailOutput.getNetWorth());
        }
        JsonElement json = gson.toJsonTree(userDetailOutput);
        System.out.println("json is "+json.toString());
        return json.toString();


    }

    public List<String> listStocks() throws  StocksNotFoundException{

        Iterable<StocksList> allStocks = stockListRepository.findAll();
        StocksList stocksList=new StocksList();
        List<StocksList> list=new ArrayList<StocksList>();
        Gson gson = new Gson();
        List<String> jsonList=new ArrayList<String>();
         for(StocksList stocks: allStocks){
             stocksList.setRegion(stocks.getRegion());
             stocksList.setCompany_name(stocks.getCompany_name());
             stocksList.setStock_type(stocks.getStock_type());
             stocksList.setSymbol(stocks.getSymbol());
             JsonElement json = gson.toJsonTree(stocksList);
             jsonList.add(json.toString());
         }
         if(jsonList==null) {
             throw new StocksNotFoundException("No Stocks Found");
         }

        return jsonList;
    }

    public double currentValue(String symbol){
        String output = "";
        double stockValue = 0.0;
        try {
            URL url = new URL("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol="+symbol+"&apikey=YNNNXAMXZNPWGTUD");

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            int code = con.getResponseCode();
            System.out.println(code);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            output = content.toString();
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(output).getAsJsonObject();
            LocalDate today = new LocalDate();
            if(today.getDayOfWeek() == 6)
                today = today.minusDays(1);
            else if(today.getDayOfWeek() == 7)
                today = today.minusDays(2);

            stockValue = jsonObject.get("Time Series (Daily)").getAsJsonObject().get(today.toString()).getAsJsonObject().get("4. close").getAsDouble();
            //json = jsonObject.toString();
        }catch (Exception e){

        }

        return stockValue;
    }



}
