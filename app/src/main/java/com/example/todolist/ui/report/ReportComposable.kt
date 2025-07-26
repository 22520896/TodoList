package com.example.todolist.ui.report

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.R
import com.example.todolist.data.entity.Report

@Composable
fun DonutChart(report: Report) {
    val total = report.total.coerceAtLeast(1) // tránh chia 0
    val completedPercent = report.completed * 100f / total
    val incompletedPercent = report.incompleted * 100f / total

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
        PieChart(
            values = listOf(
                completedPercent,
                incompletedPercent,
            ),
            colors = listOf(
                Color(0xFF4CAF50),
                Color(0xFFFFC107),
            ),
            size = 160.dp,
            thickness = 26.dp
        )

        Text(
            text = "${report.total.toString()} nhiệm vụ",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }

    Spacer((Modifier.height(8.dp)))

    Row(
        modifier = Modifier.padding(top = 10.dp).fillMaxWidth(0.6f),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Legend("Chưa hoàn thành", Color(0xFFFFC107))
        Legend("Hoàn thành", Color(0xFF4CAF50))


    }
}

@Composable
fun Legend(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(12.dp)
                .background(color, shape = CircleShape)
        )
        Spacer(Modifier.width(4.dp))
        Text(label, fontSize = 12.sp)
    }
}

@Composable
fun PieChart(
    values: List<Float>,
    colors: List<Color>,
    size: Dp = 200.dp,
    thickness: Dp = 30.dp
) {
    val proportions = values.map { it / values.sum() }

    val sweepAngles = proportions.map { it * 360f }

    Canvas(modifier = Modifier.size(size)) {
        var startAngle = -90f
        for (i in sweepAngles.indices) {
            drawArc(
                color = colors[i],
                startAngle = startAngle,
                sweepAngle = sweepAngles[i],
                useCenter = false,
                style = Stroke(width = thickness.toPx(), cap = StrokeCap.Butt)
            )
            startAngle += sweepAngles[i]
        }
    }
}

@Composable
fun ReportCats(report: Report) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CatStat(R.drawable.img3, report.incompleted, Color(0xFFFFC107))
        CatStat(R.drawable.img3, report.completed, Color(0xFF4CAF50))
    }
}

@Composable
fun CatStat(imageRes: Int, count: Int, badgeColor: Color) {
    Box(contentAlignment = Alignment.TopEnd) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier.size(72.dp)
        )
            Box(
                modifier = Modifier
                    .offset(x = 4.dp, y = (-4).dp)
                    .size(20.dp)
                    .background(badgeColor, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = count.toString(),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
    }
}

