package com.certified.covid19response.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Result(
    val noOfSevereSymptoms: Int,
    val noOfLessSymptoms: Int,
    val noOfMostSymptoms: Int,
    val totalNoOfSymptoms: Float,
    val severeSymptoms: String = "",
    val lessSymptoms: String = "",
    val mostSymptoms: String = "",
    var severePercent: Float = 0F,
    var lessPercent: Float = 0F,
    var mostPercent: Float = 0F,
    var feeling: String = "",
    var message: String = ""
) : Parcelable {
    init {
        severePercent = (noOfSevereSymptoms / totalNoOfSymptoms) * 100
        lessPercent = (noOfLessSymptoms / totalNoOfSymptoms) * 100
        mostPercent = (noOfMostSymptoms / totalNoOfSymptoms) * 100

        message = when {
            noOfSevereSymptoms >= 1 -> {
                "Seek immediate medical attention if you have serious symptoms. Always call before visiting your doctor or health facility."
            }
            mostPercent > lessPercent -> {
                "People with mild  symptoms who are otherwise healthy should manage their symptoms at home."
            }
            lessPercent > mostPercent -> {
                "On average it takes 5 - 6 days from when someone is infected with the virus for symptoms to show, however it can take up to 14 days."
            }
            else -> {
                "E be like say your own don finish o. As a matter of fact, death is lurking around you. You no fit survive boss."
            }
        }
    }
}