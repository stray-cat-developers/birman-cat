package org.straycats.birmancat.utils

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

fun invokeAny(clazz: Any, arg: String, data: Any): Any {
    val props = clazz::class.memberProperties.find { it.name == arg }
    props!!.isAccessible = true
    if (props is KMutableProperty<*>) {
        props.setter.call(clazz, data)
    }
    return clazz
}

fun invokeFunc(clazz: KClass<*>, functionName: String): KFunction<*>? {
    val func = clazz.declaredMemberFunctions.find { it.name == functionName }
    func?.let {
        it.isAccessible = true
    }
    return func
}

fun invokeId(clazz: Any, id: Long, invoke: Boolean = true): Any {
    if (invoke.not()) {
        return clazz
    }

    val props = clazz::class.memberProperties.find { it.name == "id" }
    props!!.isAccessible = true
    if (props is KMutableProperty<*>) {
        props.setter.call(clazz, id)
    }
    return clazz
}
