package com.test.memoapp.calendar.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

@Composable
fun CalendarScreen() {
    CalendarContent()
}

@Composable
fun CalendarContent() {
    val currentMonth = remember { mutableStateOf(YearMonth.now(ZoneId.of("Asia/Seoul"))) }
    val startMonth = remember { currentMonth.value.minusMonths(600) }
    val endMonth = remember { currentMonth.value.plusMonths(600) }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }
    val selectDay = remember { mutableStateOf(LocalDateTime.now().dayOfMonth) }

    val state = rememberCalendarState(
        startMonth = startMonth,
        firstVisibleMonth = currentMonth.value,
        endMonth = endMonth,
        firstDayOfWeek = firstDayOfWeek
    )

    LaunchedEffect(state) {
        snapshotFlow { state.firstVisibleMonth }
            .collect { calendarMonth ->
                currentMonth.value = calendarMonth.yearMonth
            }
    }

    Scaffold { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            DaysOfWeekTitle(currentMonth, daysOfWeek = daysOfWeekFromLocale())
            HorizontalCalendar(
                state = state,
                dayContent = { day ->
                    Day(
                        day = day,
                        isSelected = day.date.dayOfMonth == selectDay.value,
                        hasSchedule = false,
                        isTodoDone = false
                    ) {
                        selectDay.value = day.date.dayOfMonth
                    }
                },
            )
        }
    }
}

@Preview
@Composable
fun PreviewContent() {
    CalendarContent()
}

@Composable
fun Day(
    day: CalendarDay,
    isSelected: Boolean = false,
    hasSchedule: Boolean = false,  // 일정이 있는가?
    isTodoDone: Boolean = false,    // 오늘 할 일을 다 끝냈는가?
    onClick: (CalendarDay) -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f) // 정사각형 모양
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = day.position == DayPosition.MonthDate,
                onClick = { onClick(day)}
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .background(
                        if (isSelected) {
                            Color.Cyan.copy(alpha = 0.3f)
                        } else Color.Transparent, shape = CircleShape
                    ).size(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day.date.dayOfMonth.toString(),
                    color = if (day.position == DayPosition.MonthDate) Color.Black else Color.LightGray,
                    style = MaterialTheme.typography.bodyMedium,

                    )
            }

            // 일정/Todo 상태 표시 (하단에 작은 점들)
            Row(modifier = Modifier.padding(top = 2.dp)) {
                if (hasSchedule) {
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .background(Color.Red, CircleShape)
                    )
                }
                Spacer(modifier = Modifier.width(2.dp))
                if (isTodoDone) {
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .background(Color.Green, CircleShape)
                    )
                }
            }
        }
    }
}

fun daysOfWeekFromLocale(): List<DayOfWeek> {
    val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
    var daysOfWeek = DayOfWeek.values()
    // 시작 요일에 맞게 순서 재배열
    if (firstDayOfWeek != DayOfWeek.MONDAY) {
        val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
        val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
        daysOfWeek = rhs + lhs
    }
    return daysOfWeek.toList()
}

@Composable
fun DaysOfWeekTitle(month: MutableState<YearMonth>, daysOfWeek: List<DayOfWeek>) {
    Column() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { month.value = month.value.minusMonths(1) }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBackIos, contentDescription = "")
            }
            Text(
                text = "${month.value}",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
            )
            IconButton(onClick = { month.value = month.value.plusMonths(1) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = ""
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp), // 상하 여백
        ) {
            for (dayOfWeek in daysOfWeek) {
                Text(
                    modifier = Modifier.weight(1f), // 7등분
                    textAlign = TextAlign.Center,
                    text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN), // "월", "화" 등
                    style = MaterialTheme.typography.labelMedium,
                    color = when (dayOfWeek) {
                        DayOfWeek.SUNDAY -> Color.Red    // 일요일은 빨간색
                        DayOfWeek.SATURDAY -> Color.Blue // 토요일은 파란색
                        else -> Color.Gray               // 평일은 회색
                    }
                )
            }
        }
    }
}