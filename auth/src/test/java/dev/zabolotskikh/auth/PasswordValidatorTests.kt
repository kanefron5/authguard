package dev.zabolotskikh.auth

import dev.zabolotskikh.auth.data.repository.PasswordValidator
import org.junit.Assert
import org.junit.Test

class PasswordValidatorTests {
    @Test
    fun `test empty`() {
        Assert.assertTrue(PasswordValidator().check(""))
    }

    @Test
    fun `test less 8 symbols`() {
        Assert.assertFalse(PasswordValidator().check("Qq!4567"))
        Assert.assertFalse(PasswordValidator().check("1"))
    }

    @Test
    fun `test correct`() {
        Assert.assertTrue(PasswordValidator().check("Qq!45678"))
    }

    @Test
    fun `test symbols`() {
        val symbols = arrayOf(
            '!',
            '@',
            '#',
            '$',
            '%',
            '^',
            '&',
            '*',
            '(',
            ')',
            '-',
            '_',
            '+',
            '=',
            ';',
            ':',
            '\'',
            '"',
            '\\',
            '/',
            '|',
            '?',
            '`',
            '~',
            '[',
            ']',
            '{',
            '}',
            '<',
            '>',
            ',',
            '.'
        )
        symbols.forEach {
            val s = "Qq${it}45678"
            println(s)
            Assert.assertTrue(PasswordValidator().check(s))
        }
    }

    @Test
    fun testNumbers() {
        repeat(10) {
            val s = "Qq${it}!qqqqqq"
            println(s)
            Assert.assertTrue(PasswordValidator().check(s))
        }
    }

    @Test
    fun testLowercase() {
        for (i in 97..122) {
            val s = "QQ${i.toChar()}!1QQQQQ"
            println(s)
            Assert.assertTrue(PasswordValidator().check(s))
        }
    }
    @Test
    fun testUppercase() {
        for (i in 65..90) {
            val s = "qq${i.toChar()}!1qqqqq"
            println(s)
            Assert.assertTrue(PasswordValidator().check(s))
        }
    }
}