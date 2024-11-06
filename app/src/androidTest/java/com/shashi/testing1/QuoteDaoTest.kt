package com.shashi.testing1

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import androidx.arch.core.executor.testing.InstantTaskExecutorRule


class QuoteDaoTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    lateinit var quoteDatabase: QuoteDatabase
    lateinit var quotesDao: QuotesDao

    //    Room provides an in memory db, so once our testing is done, the db disappears
    @Before
    fun setUp() {
        quoteDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            QuoteDatabase::class.java
        ).allowMainThreadQueries().build()

        quotesDao = quoteDatabase.quoteDao()
    }

    @Test
    fun insertQuote_expectedSingleQuote() = runBlocking {
        val quote = Quote(0, "This is test quote", "Shashi")
        quotesDao.insertQuote(quote)

        val result = quotesDao.getQuotes().getOrAwaitValue()

        Assert.assertEquals(1, result.size)
        Assert.assertEquals("This is test quote2", result[0].text)

    }

    @Test
    fun deleteQuote_expectedNoresults() = runBlocking {
        val quote = Quote(0, "This is test quote", "Shashi")
        quotesDao.insertQuote(quote)
        quotesDao.delete()

        val result = quotesDao.getQuotes().getOrAwaitValue()

        Assert.assertEquals(0, result.size)

    }

    @After
    fun tearDown() {
        quoteDatabase.close()
    }

}