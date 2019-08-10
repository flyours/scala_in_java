package com.jeff.drills;


import com.alibaba.fastjson.JSON;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JsonPermormanceTest {
    public static void main(String[] args) {
        List<JsonTestObject> objects=new ArrayList<>();
        for(int i=0;i<100000;i++) {
            objects.add(new JsonTestObject(10+i,"name"+i,new Date(), BigDecimal.valueOf(1000+i)));
        }
        long jsonsize=0;
        System.out.println("-------------------------------------------json-------------------");
        long start = System.currentTimeMillis();
        for(JsonTestObject jsonTestObject: objects){
            String tmp= jsonTestObject.toString();
            jsonsize+=tmp.length();
        }
        long end = System.currentTimeMillis();

        System.out.println(start);
        System.out.println(end);
        System.out.println(end-start);
        System.out.println("result:"+jsonsize);

        System.out.println("-------------------------------------------tostring-------------------");
        long tostringSize=0;
        start = System.currentTimeMillis();
        for(JsonTestObject jsonTestObject: objects){
            String tmp= JSON.toJSONString(jsonTestObject);
            tostringSize+=tmp.length();
        }
        end = System.currentTimeMillis();

        System.out.println(start);
        System.out.println(end);
        System.out.println(end-start);
        System.out.println("result:"+tostringSize);

    }

    public static class JsonTestObject{
        private Integer age;
        private String name;
        private Date birthDay;
        private BigDecimal salary;

        public JsonTestObject(Integer age, String name, Date birthDay, BigDecimal salary) {
            this.age = age;
            this.name = name;
            this.birthDay = birthDay;
            this.salary = salary;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getBirthDay() {
            return birthDay;
        }

        public void setBirthDay(Date birthDay) {
            this.birthDay = birthDay;
        }

        public BigDecimal getSalary() {
            return salary;
        }

        public void setSalary(BigDecimal salary) {
            this.salary = salary;
        }

        @Override
        public String toString() {
            return "JsonTestObject{" +
                    "age=" + age +
                    ", name='" + name + '\'' +
                    ", birthDay=" + birthDay +
                    ", salary=" + salary +
                    '}';
        }
    }
}
