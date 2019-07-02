package com.etrusted.interview.demo.rest.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NotNull
public class OrderRequest {
  @NotBlank
  private String shopURL;
  @NotBlank
  private String orderReference;
  @NotBlank
  private String firstName;
  @NotBlank
  private String lastName;
  @NotBlank
  private String email;

  public String getShopURL() {
    return shopURL;
  }

  public void setShopURL(String shopURL) {
    this.shopURL = shopURL;
  }

  public String getOrderReference() {
    return orderReference;
  }

  public void setOrderReference(String orderReference) {
    this.orderReference = orderReference;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}