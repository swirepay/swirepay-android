package com.swirepay.android_sdk

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test_base24_conversion(){
        val result = Utility.getBase24String("sk_test_xkNDG8VLfNYEqOMVvrMho98K60NGkuyQ")
        assertEquals(result , "c2tfdGVzdF94a05ERzhWTGZOWUVxT01WdnJNaG85OEs2ME5Ha3V5UQ==")
    }

}