package com.ahmed.airlinesdetails.utils

import org.junit.Assert.*
import org.junit.Test

class ExtensionsTest {

    @Test
    fun stringToIntOrFalse_whenNonStringNumber_returnFalse() {

        // Given a non string numeric
        val str = "Value"

        // When the String.toIntOrFalse executed
        val result = str.toIntOrFalse()

        // Then the result is false
        assertEquals(result, false)
    }

    @Test
    fun stringToIntOrFalse_whenStringNumberString_returnTrue() {

        // Given a string numeric
        val str = "85478485"

        // When the String.toIntOrFalse executed
        val result = str.toIntOrFalse()

        // Then the result is true
        assertEquals(result, true)
    }

    @Test
    fun copyRange_whenStartGreaterThanSize_returnEmptyList() {

        // Given a list of 4 string
        val list = listOf("value1", "value2", "value3", "value4")

        // When List<T>.copyRange is executed with start = 20 and exclusiveEnd = 40
        val result = list.copyRange(20, 40)

        // Then new copied list should be empty
        assertEquals(0, result.size)
    }

    @Test
    fun copyRange_whenExclusiveEndLessThanOrEqualsZero_returnEmpty() {

        // Given a list of 4 string
        val list = listOf("value1", "value2", "value3", "value4")

        // When List<T>.copyRange is executed with start = 20 and exclusiveEnd = 0
        val result = list.copyRange(20, 0)

        // Then new copied list should be empty
        assertEquals(0, result.size)
    }

    @Test
    fun copyRange_whenStartGreaterThanOrEqualsExclusiveEnd_returnEmpty() {

        // Given a list of 4 string
        val list = listOf("value1", "value2", "value3", "value4")

        // When List<T>.copyRange is executed with start = 20 and exclusiveEnd = 20
        val result = list.copyRange(20, 20)

        // Then new copied list should be empty
        assertEquals(0, result.size)
    }

    @Test
    fun copyRange_returnNewList() {

        // Given a list of 4 string
        val list = listOf("value1", "value2", "value3", "value4")

        // When List<T>.copyRange is executed with start = 0 and exclusiveEnd = 3
        val result = list.copyRange(exclusiveEnd = 3)

        // Then new copied shouldn't be empty
        assert(result.isNotEmpty())

        // Then new copied should be copied correctly
        result.forEachIndexed { index, str ->
            assertEquals(list[index], str)
        }
    }

}