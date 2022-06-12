package com.example.onebyone

class UserAccount {

    private val uid: String? = null
    private val email: String? = null
    private val pwd: String? = null
    private var name: String? = null
    private var input: Int = 0
    private var output: Int = 0
    private var total: Int = 0


    private var pyear: String? = null
    private var pmonth: String? = null
    private var pdate: String? = null


    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }
}