package com.jugarte.gourmet

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

fun printLibHello() {
    print("Hola")
}

class Deserializer<T : Any>(private val javaClassName: Class<T>) : ResponseDeserializable<T> {
    override fun deserialize(content: String): T = Gson().fromJson(content, javaClassName)
}