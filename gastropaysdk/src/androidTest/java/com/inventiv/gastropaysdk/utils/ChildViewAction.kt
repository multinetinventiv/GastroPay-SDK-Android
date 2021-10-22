package com.inventiv.gastropaysdk.utils

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import org.hamcrest.Matcher

class ChildViewAction(id: Int) : ViewAction {

    var id1: Int = id

    @Override
    override fun getConstraints(): Matcher<View>? {
        return null
    }

    @Override
    override fun getDescription(): String {
        return "Click on a child view with specified id."
    }

    @Override
    override fun perform(uiController: UiController, view: View) {
        val v = view.findViewById<View>(id1)
        v.performClick();
    }
}