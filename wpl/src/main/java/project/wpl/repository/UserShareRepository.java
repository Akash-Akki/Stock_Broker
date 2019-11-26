package project.wpl.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.wpl.model.UserShare;

import java.util.Optional;

@Repository
public interface UserShareRepository extends CrudRepository<UserShare,Long> {

    UserShare findBySymbol(String symbol);
    //UserShare findAllByCompanyName(String company_name);
}
