package com.certified.covid19response

fun main() {
    val result = Result(2, 2, 1, 5f)
    println("severe percent: ${result.severePercent}")
    println("less percent: ${result.lessPercent}")
    println("most percent: ${result.mostPercent}")
}

data class Result(
    val noOfSevereSymptoms: Int,
    val noOfLessSymptoms: Int,
    val noOfMostSymptoms: Int,
    val totalNoOfSymptoms: Float,
    val severeSymptoms: String = "",
    val lessSymptoms: String = "",
    val mostSymptoms: String = "",
    var severePercent: Float = 1f,
    var lessPercent: Float = 1f,
    var mostPercent: Float = 1f,
) {
    init {
        severePercent = (noOfSevereSymptoms / totalNoOfSymptoms) * 100
        lessPercent = (noOfLessSymptoms / totalNoOfSymptoms) * 100
        mostPercent = (noOfMostSymptoms / totalNoOfSymptoms) * 100
    }

    val message = when {
        severePercent >= 1 -> {
            "Seek immediate medical attention if you have serious symptoms. Always call before visiting your doctor or health facility."
        }
        lessPercent >= 1 -> {
            "People w"
        }
        mostPercent >= 1 -> {
            ""
        }
        else -> {
            "E be like say your own don finish o. As a matter of fact, death is lurking around you. You no fit survive boss."
        }
    }
}