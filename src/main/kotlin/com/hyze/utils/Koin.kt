package com.hyze.utils

import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.java.KoinJavaComponent.getKoin

/**
 * @author Greg Hibberd <greg@greghibberd.com>
 * @since March 26, 2020
 */

inline fun <reified T> get(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): T = getKoin().get(qualifier, parameters)

fun getProperty(key: String): String = getKoin().getProperty(key)!!

@Suppress("UNCHECKED_CAST")
fun <T> getProperty(key: String, defaultValue: T): T = getKoin().getProperty(key, defaultValue.toString()) as T

inline fun <reified T : Any> inject(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): Lazy<T> = getKoin().inject(qualifier, parameters = parameters)