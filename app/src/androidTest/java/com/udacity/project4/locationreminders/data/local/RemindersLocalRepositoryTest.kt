package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RemindersDatabase
    private lateinit var repository: RemindersLocalRepository

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).build()

        repository = RemindersLocalRepository(database.reminderDao())
    }

    @After
    fun cleanUp() = database.close()

    @Test
    fun testInsertAndGetData() = runBlocking {

        val data = ReminderDTO(
            "title 123",
            "some description",
            "I'm lost help",
            354.00,
            453.00
        )

        repository.saveReminder(data)

        val result = repository.getReminder(data.id)

        result as Result.Success
        assertNotNull(result.data)

        val loadedData = result.data
        assertThat(loadedData.id, `is`(data.id))
        assertThat(loadedData.title, `is`(data.title))
        assertThat(loadedData.description, `is`(data.description))
        assertThat(loadedData.location, `is`(data.location))
        assertThat(loadedData.latitude, `is`(data.latitude))
        assertThat(loadedData.longitude, `is`(data.longitude))
    }

    @Test
    fun testDataNotFound_returnError() = runBlocking {
        val result = repository.getReminder("7777")
        val error = (result is Result.Error)
        assertThat(error, `is`(true))
        result as Result.Error
        assertThat(result.message, `is`("Reminder not found!"))
    }
}