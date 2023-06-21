package com.demo.demofirestore.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.ref.WeakReference

open class BaseViewModel<N> : ViewModel() {

    lateinit var mNavigator: WeakReference<N>


    private var gson: Gson? = null

    private val goBack = MutableLiveData<String>()

    fun getGoBackStatus(): MutableLiveData<String> {
        return goBack
    }
    fun getNavigator(): N? {
        return mNavigator.get()
    }

    fun setNavigator(navigator: N) {
        mNavigator = WeakReference(navigator)
    }
    protected open fun <T> getStringFromObject(requestObject: T): Map<String, String> {
        gson = Gson()
        val jsonString: String = gson!!.toJson(requestObject)
        val mapType = object : TypeToken<Map<String?, String?>?>() {}.type
        return gson!!.fromJson(jsonString, mapType)
    }
}