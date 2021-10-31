package com.pagewisegroup.pagewise

// id fields should only be populated when reading from or writing to database.
data class PWClass(val name: String, val assignments: ArrayList<Assignment> = ArrayList(), var id: Long? = null)

// Object that tracks a Student and their information including names, reading speed, and their classes.
class Student(var name: String, var reading_speed: Double, var id: Long? = null) {
    val classes = ArrayList<PWClass>()
    fun updateReadingSpeed() {
        TODO("Implementation will likely be something along the lines of taking the median or" +
                "mean of the completion speed of all completed assignments, maybe weighting more" +
                "recent assignments more heavily")
    }

    override fun toString(): String {
        return "name: $name read speed: $reading_speed id: $id"
    }
}