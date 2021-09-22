package com.ahmed.airlinesmodel.entities

interface Mapper<T> {
    fun map(): T?
}