package com.pagewisegroup.pagewise

// id fields should only be populated when reading from or writing to database.
data class PWClass(var id: Long?, val name: String, val assignments: List<Assignment>)

// Object that tracks a Student and their information including names, reading speed, and their classes.
class Student(var id: Long? = null, val classes: List<PWClass>, var name: String, var reading_speed: Double) {
    fun updateReadingSpeed() {
        TODO("Implementation will likely be something along the lines of taking the median or" +
                "mean of the completion speed of all completed assignments, maybe weighting more" +
                "recent assignments more heavily")
    }
}