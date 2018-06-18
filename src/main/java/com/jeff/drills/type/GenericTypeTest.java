package com.jeff.drills.type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.stream.Collectors;

public class GenericTypeTest<K extends Item & Test2, V> extends STestInterface<Item>  {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericTypeTest.class);
    K key;
    V value;

    STestInterface<K>[][][] keys;
    public static void main(String[] args) throws Exception {
        //ParameterizedType
        Type genericSuperclass = GenericTypeTest.class.getGenericSuperclass();
        LOGGER.info("genericSuperclass: {}, is ParameterizedType: {}",genericSuperclass, genericSuperclass instanceof ParameterizedType);

        //TypeVariable
        TypeVariable[] types = GenericTypeTest.class.getTypeParameters();
        for (TypeVariable t : types) {
            LOGGER.info("getGenericDeclaration: {}", t.getGenericDeclaration());
            LOGGER.info("typevariable,name:{},bounds:{}",
                    t.getName(),
                    Arrays.stream(t.getBounds()).map(Object::toString).collect(Collectors.toList()));
        }

        //GenericArrayType
        Type type = GenericTypeTest.class.getDeclaredField("keys").getGenericType();
        //获取泛型数组中元素的类型
        //无论从左向右有几个[]并列，getGenericComponentType仅仅脱去最右边的[]之后剩下的内容就作为这个方法的返回值。
        System.out.println(((GenericArrayType)type).getGenericComponentType());
    }
}

class Item {
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

interface Test2 {

}

class STestInterface<T> {

}