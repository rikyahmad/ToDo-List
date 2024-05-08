package com.staygrateful.todolistapp
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.staygrateful.todolistapp.data.local.dao.TaskDao
import com.staygrateful.todolistapp.data.local.database.TaskDatabase
import com.staygrateful.todolistapp.data.model.Task
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class TaskDaoTest {

    private lateinit var taskDao: TaskDao
    private lateinit var database: TaskDatabase

    @Before
    fun createDatabase() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, TaskDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        taskDao = database.taskDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetTask() = runBlocking {
        val task = Task(1001, "Test Task", "Description", System.currentTimeMillis(), false)
        taskDao.insertTask(task)
        val allTasks = taskDao.getAllTasks("")
        assert(allTasks.contains(task))
    }

    @Test
    @Throws(Exception::class)
    fun deleteTask() = runBlocking {
        val task = Task(1001, "Test Task", "Description", System.currentTimeMillis(), false)
        taskDao.insertTask(task)
        taskDao.deleteTask(task)
        val allTasks = taskDao.getAllTasks("")
        assert(!allTasks.contains(task))
    }
}
