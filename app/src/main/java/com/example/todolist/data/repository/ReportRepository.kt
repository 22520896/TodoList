package com.example.todolist.data.repository
import com.example.todolist.data.dao.ReportDao
import com.example.todolist.data.dao.TodoDao
import com.example.todolist.data.entity.Report
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ReportRepository @Inject constructor(
    private val dao: ReportDao,
    private val todoDao: TodoDao
){
    fun getReports(yearMonth: String): Flow<Report?> = dao.getByMonth(yearMonth)

    suspend fun updateReportFor(date: LocalDate) {
        val monthStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM"))
        val todos = todoDao.getTodosByMonthOnce(monthStr)
        val total = todos.size
        val completed = todos.count { it.isDone }
        val incompleted = total - completed

        val report = Report(
            yearMonth = monthStr,
            total = total,
            completed = completed,
            incompleted = incompleted,
        )
        dao.insertOrUpdate(report)
    }

}