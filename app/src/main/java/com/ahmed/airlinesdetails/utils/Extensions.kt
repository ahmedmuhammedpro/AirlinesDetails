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