package com.swirepay.android_sdk

import com.swirepay.android_sdk.ui.base.BaseActivity
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
    fun testRedirectCondition(){
        //pass -> https://www.swirepay.com/?sp-session-id=setupsession-f489d9a22dfc4017940b5bd954c8de7c
        //fail -> https://staging-secure.swirepay.com/setup-session?key=c2tfdGVzdF94a05ERzhWTGZOWUVxT01WdnJNaG85OEs2ME5Ha3V5UQ==&redirectUrl=undefined&ss=setupsession-f489d9a22dfc4017940b5bd954c8de7c&secret=setupsession-f489d9a22dfc4017940b5bd954c8de7c_fdcc657af9a44ce98a81b8fa5dcd236f

        val passUrl = "https://www.swirepay.com/?sp-session-id=setupsession-f489d9a22dfc4017940b5bd954c8de7c"
        assertTrue(BaseActivity.isThisFinalUrl(passUrl))

        val failUrl = "https://staging-secure.swirepay.com/setup-session?key=c2tfdGVzdF94a05ERzhWTGZOWUVxT01WdnJNaG85OEs2ME5Ha3V5UQ==&redirectUrl=undefined&ss=setupsession-f489d9a22dfc4017940b5bd954c8de7c&secret=setupsession-f489d9a22dfc4017940b5bd954c8de7c_fdcc657af9a44ce98a81b8fa5dcd236f"
        assertFalse(BaseActivity.isThisFinalUrl(failUrl))
    }

}