package com.example.meteoapp.db.model

import java.util.*

sealed class Month(private val monthNumber: Int) {
    var temperature: Double? = null
    fun getValue() = monthNumber + 1

    override fun toString(): String {
        val objectName = this::class.simpleName.toString()
        return objectName.substring(0, 1) + objectName.substring(1, 3)
            .toLowerCase(Locale.getDefault())
    }

    object JANUARY : Month(0)
    object FEBRUARY : Month(1)
    object MARCH : Month(2)
    object APRIL : Month(3)
    object MAY : Month(4)
    object JUNE : Month(5)
    object JULY : Month(6)
    object AUGUST : Month(7)
    object SEPTEMBER : Month(8)
    object OCTOBER : Month(9)
    object NOVEMBER : Month(10)
    object DECEMBER : Month(11)
    object UNKNOWN : Month(-1)
}