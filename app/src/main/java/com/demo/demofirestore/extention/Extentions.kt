package com.demo.demofirestore.extention

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.dialog(
    title: String,
    msg: String,
    positiveText: String,
    negativeText: String,
    positiveClick: (() -> Unit)? = null,
    negativeClick: (() -> Unit)? = null,
    dialogView: View? = null,
) {
    val builder = AlertDialog.Builder(this)
    builder.setView(dialogView)

    builder.setView(dialogView)
        .setTitle(title)
        .setMessage(msg)
        .setPositiveButton(positiveText)
        { _, _ -> positiveClick?.invoke() }
        .setNegativeButton(negativeText)
        { _, _ -> negativeClick?.invoke() }
        .create()
        .show()
}

fun Context.hideKeyboard() {
    this as Activity
    val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}