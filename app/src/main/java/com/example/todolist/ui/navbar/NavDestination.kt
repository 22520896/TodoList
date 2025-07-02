package com.example.todolist.ui.navbar

import com.example.todolist.R

enum class NavDes(val route: String, val label: String, val icon: Int = 0) {
//    HOME("home", "Nhiệm vụ", R.drawable.ic_home),
    CALENDAR("calendar", "Lịch", R.drawable.ic_calendar),
    ADD("add", "Thêm", R.drawable.ic_plus),
    REPORT("report", "Báo cáo", R.drawable.ic_chart),
    SETTING("setting", "Cài đặt", R.drawable.ic_setting),
    TODO("home/todo", "Nhiệm vụ", R.drawable.ic_home),
    NOTE("home/note", "Ghi chú"),
    NOTE_EDIT("edit_note", "Chỉnh sửa ghi chú")
}
