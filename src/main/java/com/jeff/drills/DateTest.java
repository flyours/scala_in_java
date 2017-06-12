package com.jeff.drills;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class DateTest {
    private static Logger logger = LoggerFactory.getLogger(DateTest.class);

    public static void main(String[] args) {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd");
        String text = date.format(formatter);

        logger.debug(text);
        LocalDate parsedDate = LocalDate.parse(text, formatter);
        logger.debug("parsedDate ={}", parsedDate);

        logger.debug("Instant.now().toEpochMilli() ={}", Instant.now().toEpochMilli());
        logger.debug("new Date().getTime() = {}", new Date().getTime());

        String manufactureDate = "2017/06/10";
        try {
            Date d = new SimpleDateFormat("yyyy/MM/dd").parse(manufactureDate);
            logger.debug("d = {}", d);
            logger.debug("d.getTime() = {}", d.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
