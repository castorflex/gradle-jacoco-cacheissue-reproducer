package com.jacocoissueapplication

import org.junit.Assert.assertEquals
import org.junit.Test


class MyClassTest {
    @Test
    fun testSimpleMethod() {
        assertEquals(MyClass().simpleMethod(), 2)
    }
}
