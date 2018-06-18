package com.jeff.drills.type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TypeVariableTest<K extends Item & Test2, V>{
    private static final Logger LOGGER=LoggerFactory.getLogger(TypeVariableTest.class);
    K key;
    V value;
    public static void main(String[] args) throws Exception
    {
        TypeVariable[] types = TypeVariableTest.class.getTypeParameters();
        for(TypeVariable t : types){
            LOGGER.info("getGenericDeclaration: {}",t.getGenericDeclaration());
            LOGGER.info("typevariable,name:{},bounds:{}",
                    t.getName(),
                    Arrays.stream(t.getBounds()).map(Object::toString).collect(Collectors.toList()));
        }
    }
}

class Item{
    private String appId;
    private String key;
    private String merchantId;
    private Integer shopId;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    @Override
    public String toString() {
        return "Item{" +
                "appId='" + appId + '\'' +
                ", key='" + key + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", shopId=" + shopId +
                '}';
    }
}

interface Test2{

}