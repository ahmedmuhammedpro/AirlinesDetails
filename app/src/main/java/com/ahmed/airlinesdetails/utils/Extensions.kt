package com.ahmed.airlinesdetails.utils

import androidx.navigation.NavController

fun NavController.popStackAllInstance(destination: Int, inclusive: Boolean): Boolean {
    var popped: Boolean
    while (true) {
        popped = popBackStack(destination, inclusive)
        if (!popped) {
            val f = currentDestination?.id
            break
        }
    }

    return popped
}

fun String.toIntOrFalse(): Boolean {
    val isInt = toIntOrNull()
    return isInt != null
}

fun <T> List<T>.copyRange(start: Int = 0, exclusiveEnd: Int): ArrayList<T> {

    val newList = ArrayList<T>()

    if (start > size - 1 || exclusiveEnd <= 0 || start >= exclusiveEnd)
        return newList

    var validEnd = exclusiveEnd
    if (size < exclusiveEnd) {
        validEnd = size
    }

    for (i in start until validEnd) {
        newList.add(get(i))
    }

    return newList
}