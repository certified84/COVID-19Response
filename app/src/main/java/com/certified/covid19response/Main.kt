package com.certified.covid19response

fun main() {
    val button = Button()
    button.clickListener = object : OnClickListener {
        override fun onClick() {
            TODO("Not yet implemented")
        }
    }
}

class User() : OnClickListener {
    var name: String = "Samson"

    override fun onClick() {
//        super.onClick()
    }
}

class Button : View(), OnClickListener {
    override fun onClick() {
//        super.onClick()
    }
}

interface OnClickListener {
    fun onClick()
}

abstract class View {
    lateinit var clickListener: OnClickListener

    fun click() {
        clickListener.onClick()
    }
}