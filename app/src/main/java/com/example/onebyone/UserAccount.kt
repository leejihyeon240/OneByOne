package com.example.onebyone

class UserAccount {

    private val uid: String? = null
    private val email: String? = null
    private val pwd: String? = null
    private var name: String? = null
    private var input: Int = 0
    private var output: Int = 0
    private var total: Int = 0


    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getInput(): Int {
        return input
    }

    fun getOutput(): Int {
        return output
    }


    fun getTotal(): Int {
        return input-output
    }
}