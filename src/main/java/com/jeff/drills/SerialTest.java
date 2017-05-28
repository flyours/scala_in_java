package com.jeff.drills;


import com.jeff.scala.drills.UserProtocol.LogOut;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerialTest {
    public static void main(String[] args) throws Exception{
        ObjectInputStream oi=new ObjectInputStream(new FileInputStream("logout.bin"));
        LogOut lo= (LogOut) oi.readObject();
        System.out.println(lo.mmeToken());

        ObjectOutputStream oo=new ObjectOutputStream(new FileOutputStream("logout.bin2"));
        oo.writeObject(new LogOut("1234abcd"));
        oo.flush();
    }
}

