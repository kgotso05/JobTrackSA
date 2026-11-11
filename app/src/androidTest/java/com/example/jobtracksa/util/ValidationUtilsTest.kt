package com.example.jobtracksa.util

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidationUtilsTest {

    @Test
    fun validCompanyName_returnsTrue() {

        val result =
            ValidationUtils.isValidCompanyName("FNB")

        assertTrue(result)
    }

    @Test
    fun emptyCompanyName_returnsFalse() {

        val result =
            ValidationUtils.isValidCompanyName("")

        assertFalse(result)
    }

    @Test
    fun validJobTitle_returnsTrue() {

        val result =
            ValidationUtils.isValidJobTitle(
                "Software Developer"
            )

        assertTrue(result)
    }

    @Test
    fun validEmail_returnsTrue() {

        val result =
            ValidationUtils.isValidEmail(
                "student@example.com"
            )

        assertTrue(result)
    }

    @Test
    fun invalidEmail_returnsFalse() {

        val result =
            ValidationUtils.isValidEmail(
                "studentexample.com"
            )

        assertFalse(result)
    }

    @Test
    fun passwordWithEightCharacters_returnsTrue() {

        val result =
            ValidationUtils.isValidPassword(
                "Test1234"
            )

        assertTrue(result)
    }

    @Test
    fun shortPassword_returnsFalse() {

        val result =
            ValidationUtils.isValidPassword(
                "12345"
            )

        assertFalse(result)
    }

    @Test
    fun matchingPasswords_returnsTrue() {

        val result =
            ValidationUtils.passwordsMatch(
                "Test1234",
                "Test1234"
            )

        assertTrue(result)
    }

    @Test
    fun differentPasswords_returnsFalse() {

        val result =
            ValidationUtils.passwordsMatch(
                "Test1234",
                "Test5678"
            )

        assertFalse(result)
    }

    @Test
    fun validDate_returnsTrue() {

        val result =
            ValidationUtils.isValidDate(
                "2026-10-15"
            )

        assertTrue(result)
    }

    @Test
    fun impossibleDate_returnsFalse() {

        val result =
            ValidationUtils.isValidDate(
                "2026-99-99"
            )

        assertFalse(result)
    }

    @Test
    fun invalidDateFormat_returnsFalse() {

        val result =
            ValidationUtils.isValidDate(
                "15/10/2026"
            )

        assertFalse(result)
    }

    @Test
    fun validHttpsUrl_returnsTrue() {

        val result =
            ValidationUtils.isValidUrl(
                "https://www.example.com/job"
            )

        assertTrue(result)
    }

    @Test
    fun invalidUrl_returnsFalse() {

        val result =
            ValidationUtils.isValidUrl(
                "not a website"
            )

        assertFalse(result)
    }

    @Test
    fun emptyOptionalUrl_returnsTrue() {

        val result =
            ValidationUtils.isValidUrl("")

        assertTrue(result)
    }
}