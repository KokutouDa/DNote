package com.kokutouda.dnote.extension

import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by apple on 2017/7/5.
 */

object DelegateExt {
    fun <T> notNullSingleValueVar() = NotNullSingleValueVar<T>()
}

//为null时抛出异常，有值时再赋值一次也抛出异常
class NotNullSingleValueVar<T>  {

    var value: T? = null

    //获取的值为null时抛出异常
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value ?: throw IllegalStateException("${property.name} not initialed")
    }

    //值已经初始化时抛出异常，否则赋值
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = if (this.value == null) value
        else throw IllegalStateException("${property.name} already initialized")
    }
}