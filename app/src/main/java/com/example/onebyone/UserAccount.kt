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

    fun getPyear(): String? {
        return pyear
    }

    fun setPyear(pyear: String?) {
        this.pyear = pyear
    }

    fun getPmonth(): String? {
        return pmonth
    }

    fun setPmonth(pmonth: String?) {
        this.pmonth = pmonth
    }

    fun getPdate(): String? {
        return pdate
    }

    fun setPdate(pdate: String?) {
        this.pdate = pdate
    }


}