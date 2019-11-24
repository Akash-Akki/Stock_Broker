package project.wpl.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "registration", schema = "public")
public class UserRegistry {

  @Column(name = "username")
  private String username;

  @Column(name = "passwd")
  private String passwd;

  private String passwordConfirm;

  @Column(name = "security_qn")
  private String security_qn;

  @Column(name = "security_qn_ans")
  private String security_qn_ans;

  @Column(name = "address")
  private String address;

  @Column(name = "email")
  private String email;

  @Id
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPasswd() {
    return passwd;
  }

  public void setPasswd(String passwd) {
    this.passwd = passwd;
  }

  @Transient
  public String getPasswordConfirm() {
    return passwordConfirm;
  }

  public void setPasswordConfirm(String passwordConfirm) {
    this.passwordConfirm = passwordConfirm;
  }

  public String getSecurity_qn() {
    return security_qn;
  }

  public void setSecurity_qn(String security_qn) {
    this.security_qn = security_qn;
  }

  public String getSecurity_qn_ans() {
    return security_qn_ans;
  }

  public void setSecurity_qn_ans(String security_qn_ans) {
    this.security_qn_ans = security_qn_ans;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

}

