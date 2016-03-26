package com.jeff.java;

import com.jeff.scala.DualLogger;

public class JavaLogger extends DualLogger {
    public void logIt(String m) {
        log(m);
    }

    public static void main(String[] args) {
        new JavaLogger().logIt("zhang jeff");
    }
}
