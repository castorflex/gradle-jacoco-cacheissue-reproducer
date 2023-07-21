package com.jacocoissueapplication

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class MyClassTest {
    @Test
    fun testSimpleMethod() {
        assertEquals(MyClass().simpleMethod(), 2)
    }
}
