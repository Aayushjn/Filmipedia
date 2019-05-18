package com.aayush.filmipedia

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        val appContext = ApplicationProvider.getApplicationContext<Context>()

        assertEquals("com.aayush.filmipedia", appContext.packageName)
    }
}
