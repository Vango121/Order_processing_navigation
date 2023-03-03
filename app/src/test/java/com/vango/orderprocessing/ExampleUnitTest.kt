package com.vango.orderprocessing

import org.junit.Test

import org.junit.Assert.*
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun blank_fields_test() {
        val viewModel: MainViewModel = mock()
        whenever(viewModel.checkIfCredentialsAreValid("testtest", "")).thenReturn(false)
        whenever(viewModel.checkIfCredentialsAreValid("", "testtest")).thenReturn(false)
        whenever(viewModel.checkIfCredentialsAreValid("", "")).thenReturn(false)
        whenever(viewModel.checkIfCredentialsAreValid("testtest", "testtest")).thenReturn(true)
    }
    @Test
    fun credentials_length_test() {
        val viewModel: MainViewModel = mock()
        whenever(viewModel.checkIfCredentialsAreValid("test", "haslo")).thenReturn(false)
        whenever(viewModel.checkIfCredentialsAreValid("username", "1234")).thenReturn(false)
        whenever(viewModel.checkIfCredentialsAreValid("t", "t")).thenReturn(false)
        whenever(viewModel.checkIfCredentialsAreValid("testtest", "testtest")).thenReturn(true)
    }
}