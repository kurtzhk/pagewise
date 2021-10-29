package com.pagewisegroup.pagewise

data class PWClass(val name: String, val assignments: List<Assignment>)

// Object that tracks a Student and their information including names, reading speed, and their classes.
class Student(classes: List<PWClass>, name: String, reading_speed: Double) {
    fun updateReadingSpeed() {
        TODO("Implementation will likely be something along the lines of taking the median or" +
                "mean of the completion speed of all completed assignments, maybe weighting more" +
                "recent assignments more heavily")
    }
}