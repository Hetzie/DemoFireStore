package com.demo.demofirestore.ui.main

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.demo.demofirestore.R
import com.demo.demofirestore.base.BaseViewModel
import com.demo.demofirestore.databinding.DeleteDialogBinding
import com.demo.demofirestore.databinding.UpdateDialogBinding
import com.demo.demofirestore.extention.dialog
import com.demo.demofirestore.extention.showToast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainViewModel@Inject constructor(
    var context: Context):BaseViewModel<Any>() {
    val name = MutableLiveData<String>()
    var email = MutableLiveData<String>()
    private var db = Firebase.firestore
    var collection = "Random"
    private lateinit var updateDialogBinding: UpdateDialogBinding

    fun addData(context: Context) {

        val student = hashMapOf(
            "name" to name.value.toString(),
            "email" to email.value.toString()
        )
        if (!name.value.isNullOrEmpty()) {
            if (!email.value.isNullOrEmpty()) {

                val dbData = db.collection(collection).document(name.value!!)


                dbData.get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        val document = it.result
                        if (document.exists()) {
                            context.showToast("Record already exist.")
                        } else {
                            dbData.set(student)
                                .addOnSuccessListener {
                                    context.showToast("Data Added.")
                                    viewModelScope.launch {
                                        withContext(Dispatchers.Main){
                                            name.postValue("")
                                            email.postValue("")
                                        }
                                    }
//                                    name.value = ""
//                                    email.value = ""
                                }.addOnFailureListener {
                                    context.showToast("Not able to add this record.")
                                }
                        }
                    }
                }
            } else {
                context.showToast("Email field is required")
            }
        } else {
            context.showToast("Name field is required")
        }
    }

    fun updateParticularRecord(context: Context, nameParticular: String, currentEmail: String) {

        val inflater = LayoutInflater.from(context)
        updateDialogBinding =
            DataBindingUtil.inflate(inflater,
                R.layout.update_dialog,
                null,
                false)
        updateDialogBinding.etUpdateName.isClickable = false
        updateDialogBinding.etUpdateName.isEnabled = false
        updateDialogBinding.etUpdateName.isFocusable = false
        updateDialogBinding.etUpdateName.setText(nameParticular)
        updateDialogBinding.etUpdateEmail.setText(currentEmail)

        Log.e("UPDATE", "Particular")
        context.dialog("Update Data",
            "by name",
            "Update",
            "Cancel",
            {
                    if (!updateDialogBinding.etUpdateEmail.text.isNullOrEmpty()) {
                        val updatedInfo = mapOf(
                            "email" to updateDialogBinding.etUpdateEmail.text.toString()
                        )
                        db.collection(collection).document(nameParticular)
                            .update(updatedInfo).addOnSuccessListener {
                                context.showToast("Record updated Successfully")
                            }.addOnFailureListener {
                                context.showToast("Not able to update this record.")
                            }
                    } else {
                        context.showToast("Email field is required")
                    }

            }, {}, updateDialogBinding.root)
    }

    fun deleteParticularData(context: Context, nameParticular: String){
        db.collection(collection).document(nameParticular).delete()
            .addOnSuccessListener {
                context.showToast("Record deleted")
            }.addOnFailureListener {
                context.showToast("Not able to delete this record.")
            }
    }
}