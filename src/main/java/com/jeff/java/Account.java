package com.jeff.java;

import com.jeff.scala.Persistable;
import com.jeff.scala.Persistable$class;

public class Account implements Persistable<Account> {
    @Override
    public void log(String msg){
        Persistable$class.log(this,msg);
    }
    @Override
    public Account getEntity() {
        log("getEntity");
        return this;
    }
    @Override
    public Account save() {
        return (Account) Persistable$class.save(this);
    }

    public static void main(String[] args) {
        new Account().save();
    }
}