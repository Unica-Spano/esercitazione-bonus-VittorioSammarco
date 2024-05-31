package com.example.esercitazionebonus;

import java.io.Serializable;
import java.util.Calendar;

public class Person implements Serializable {
    private String name, surname, email;
    private Calendar birthday;

    public String getName() {return name;}
    public String getSurname() {return surname;}
    public String getEmail() {return email;}
    public Calendar getBirthday() {return birthday;}
    public void setName(String name) {this.name = name;}
    public void setSurname(String surname) {this.surname = surname;}
    public void setEmail(String email) {this.email = email;}
    public void setBirthday(Calendar birhtday) {this.birthday = birhtday;}
}
