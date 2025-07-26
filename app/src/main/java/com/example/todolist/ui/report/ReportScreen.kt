package com.example.todolist.ui.report

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.todolist.ui.todo.HorizontalMonthCalendar
import com.example.todolist.viewmodel.CommonViewModel
import com.example.todolist.viewmodel.ReportViewModel

@Composable
fun ReportScreen(
    reportViewModel: ReportViewModel,
    color: Color,
) {
    val selectedDate by reportViewModel.selectedDate.collectAsStateWithLifecycle()
    val report by reportViewModel.report.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Báo cáo tháng",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
        )

        HorizontalMonthCalendar(
            color = color,
            selectedDate = selectedDate,
            onMonthSelected = { reportViewModel.setDate(it) },
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Text(
            text = "Tổng quan",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 25.dp)
        )

        report?.let {
            DonutChart(report = it)

            Spacer(modifier = Modifier.height(16.dp))

            ReportCats(report = it)
        } ?: run {
            Text("Không có dữ liệu", color = Color.Gray)
        }
    }
}