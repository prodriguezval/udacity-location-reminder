package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RemindersDatabase

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun testInsertAndGetData() = runBlockingTest {

        val data = ReminderDTO(
            "some title",
            "a description",
            "test location",
            345.00,
            654.00
        )

        database.reminderDao().saveReminder(data)

        val loadedDataList = database.reminderDao().getReminders()

        assertThat(loadedDataList.size, `is`(1))

        val loadedData = loadedDataList.first()
        assertThat(loadedData.id, `is`(data.id))
        assertThat(loadedData.title, `is`(data.title))
        assertThat(loadedData.description, `is`(data.description))
        assertThat(loadedData.location, `is`(data.location))
        assertThat(loadedData.latitude, `is`(data.latitude))
        assertThat(loadedData.longitude, `is`(data.longitude))

    }
}