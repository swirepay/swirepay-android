package com.swirepay.swirepay_sdk_playground

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.swirepay.android_sdk.retrofit.DateDeSerializer
import org.junit.Before
import java.io.File
import java.util.*

open class BaseClass {
    lateinit var gsonBuilder : GsonBuilder
    lateinit var gson : Gson
    @Before
    fun setup(){
        gsonBuilder = GsonBuilder().apply { registerTypeAdapter(Date::class.java ,
            DateDeSerializer()
        ) }
        gson =gsonBuilder.create()
    }

    /**
     * @return File of the given path
     * @param path path of the file
     * example : json/payment_session.json
     */
    fun getResource(path : String) : File{
        return File(this.javaClass.classLoader?.getResource(path)!!.path)
    }

}