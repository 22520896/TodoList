# 📅 TodoList App

A personal task management application built with **Kotlin**, using **Jetpack Compose**, **MVVM architecture**, and **Room Database**. The app allows users to create, view, and manage tasks (Todos), events,  notes, focus sessions, and monthly productivity reports.


## 📌 Features
- **Task Management**: Create, view, edit, and delete tasks with start/end times, reminders, priority, completion status, and focus mode.
- **Event Management**: Add view, edit, and delete events by calendar.
- **Note Management**: Create, edit, and delete personal notes.
- **Focus Mode**: Enable a timer to maintain focus on specific tasks.
- **Customer Management**: Tracks registered customer details (for members who can buy on credit and receive discounts).
- **Monthly Report**: Track completed and pending tasks monthly for productivity analysis.
- **User Settings**: Customize date/time formats, interface colors, and reminder tones.

---

## 🛠️ Technologies Used
- **Room Database**: Local SQLite database with Flow for real-time data.
- **Jetpack Compose**: Modern UI framework for flexible screens.
- **Hilt**: Dependency Injection for managing components.
- **Kotlin Coroutines & Flow**: Asynchronous task and data flow handling.
- **Java Time API**: Date/time management with user-defined formats.
- **Accompanist Permissions**: Handles runtime permissions (e.g., notifications).
- **RingtoneManager**: Manages custom notification sounds.
- **Material 3**: Supports Dynamic Colors and dark/light themes.

---

## 📱 Screens
<div align="center">

<table style="width: 100%; border-collapse: collapse; text-align: center;">
  <thead>
    <tr>
      <th style="text-align: center;">Screen</th>
      <th style="text-align: center;">Preview</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td style="text-align: center; vertical-align: middle;">Todo</td>
      <td style="text-align: center; vertical-align: middle;"><img src="assets/todo.jpg" width="250"/></td>
    </tr>
    <tr>
      <td style="text-align: center; vertical-align: middle;">Add/Edit Todo</td>
      <td style="text-align: center; vertical-align: middle;"><img src="assets/addTodo.jpg" width="250"/></td>
    </tr>
    <tr>
      <td style="text-align: center; vertical-align: middle;">Calendar</td>
      <td style="text-align: center; vertical-align: middle;"><img src="assets/event.jpg" width="250"/></td>
    </tr>
    <tr>
      <td style="text-align: center; vertical-align: middle;">Add/Edit Event</td>
      <td style="text-align: center; vertical-align: middle;"><img src="assets/addEvent.jpg" width="250"/></td>
    </tr>
    <tr>
      <td style="text-align: center; vertical-align: middle;">Note</td>
      <td style="text-align: center; vertical-align: middle;"><img src="assets/note.jpg" width="250"/></td>
    </tr>
    <tr>
      <td style="text-align: center; vertical-align: middle;">Add/Edit Note</td>
      <td style="text-align: center; vertical-align: middle;"><img src="assets/addNote.jpg" width="250"/></td>
    </tr>
    <tr>
      <td style="text-align: center; vertical-align: middle;">Report</td>
      <td style="text-align: center; vertical-align: middle;"><img src="assets/report.jpg" width="250"/></td>
    </tr>
    <tr>
      <td style="text-align: center; vertical-align: middle;">Setting</td>
      <td style="text-align: center; vertical-align: middle;"><img src="assets/setting.jpg" width="250"/></td>
    </tr>  
    <tr>
      <td style="text-align: center; vertical-align: middle;">Focus Mode</td>
      <td style="text-align: center; vertical-align: middle;"><img src="assets/focus.jpg" width="250"/></td>
    </tr>  
  </tbody>
</table>

</div>

---

## 🚀 Getting Started

### ⚙️ Requirements
- Android Studio (latest version)

### 📦 Installation

1. Clone the repository:
```
git clone https://github.com/22520896/TodoList.git
```

2. Open project in Android Studio.

3. Sync Gradle and download dependencies.

4. Connect a device or use an emulator to run the app.
