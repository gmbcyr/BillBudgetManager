package com.gmb.bbm2.data.model

/**
 * Created by GMB on 3/25/2018.
 */
class BudgetLineFB {

    private var id: String? = null
    private var datestart: Long? = null
    private var dateend: Long? = null
    private var montantmax: Double? = null
    private var montantprojecte: Double? = null
    private var montantreel: Double? = null
    private var ch1: String? = null
    private var ch2: String? = null
    private var ch3: String? = null
    private var idcat: String? = null
    private var month: Int = 0
    private var year: Int = 0
    private var idStatement: String? = null


    constructor(id: String?, idcat: String?, month: Int, year: Int, idStatement: String?) {
        this.id = id
        this.idcat = idcat
        this.month = month
        this.year = year
        this.idStatement = idStatement

        datestart=System.currentTimeMillis()
        dateend=System.currentTimeMillis()

        montantmax=0.0
        montantprojecte=0.0
        montantreel=0.0


    }

    constructor(id: String?, montantmax: Double?, montantprojecte: Double?, montantreel: Double?, idcat: String?, month: Int, year: Int, idStatement: String?) {
        this.id = id
        this.montantmax = montantmax
        this.montantprojecte = montantprojecte
        this.montantreel = montantreel
        this.idcat = idcat
        this.month = month
        this.year = year
        this.idStatement = idStatement
    }




}