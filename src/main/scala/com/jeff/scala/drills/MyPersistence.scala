package com.jeff.scala.drills


import javax.persistence.Id

import scala.annotation.meta.{beanGetter, getter}
import scala.beans.BeanProperty

//meta annotation; use: javap -v a/b/c.class will get RuntimeVisibleAnnotations if the annotation is RetentionPolicy.RUNTIME
class MyPersistence {
    @(Id@beanGetter @getter)
    @BeanProperty var x = 0
}

