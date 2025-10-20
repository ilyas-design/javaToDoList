# Smart Tasks - Android Todo List App

A modern Android application for intelligent task management with AI-powered organization. Features a beautiful welcome page and comprehensive task management capabilities.

## Features

- **Welcome Page**: Beautiful welcome screen with the Smart Tasks logo
- **Task Management**: Add, edit, and delete tasks with full CRUD operations
- **AI-Powered Organization**: Rule-based AI that organizes tasks by importance, priority, and due date
- **Modern UI**: Material Design 3 components with custom branding
- **Responsive Layout**: Optimized for different screen sizes
- **Local Database**: Room persistence for offline task storage
- **Priority & Importance**: Categorize tasks with priority (Low, Medium, High) and importance levels
- **Due Dates**: Set and track task deadlines
- **Brand Colors**: Orange and yellow color scheme matching the logo

## Project Structure

```
app/
├── src/main/
│   ├── java/org/example/smarttasks/
│   │   ├── MainActivity.java              # Welcome activity
│   │   ├── TaskListActivity.java          # Main task list
│   │   ├── AddEditTaskActivity.java       # Add/edit task form
│   │   ├── Task.java                      # Task model (Room entity)
│   │   ├── TaskDao.java                   # Data access object
│   │   ├── TaskDatabase.java              # Room database
│   │   ├── TaskRepository.java            # Repository pattern
│   │   ├── TaskViewModel.java             # ViewModel for UI
│   │   ├── TaskOrganizerAI.java           # AI organization logic
│   │   ├── TaskAdapter.java               # RecyclerView adapter
│   │   └── Converters.java                # Room type converters
│   ├── res/
│   │   ├── layout/
│   │   │   ├── activity_main.xml          # Welcome page layout
│   │   │   ├── activity_task_list.xml     # Task list layout
│   │   │   ├── activity_add_edit_task.xml # Add/edit task layout
│   │   │   └── task_item.xml              # Task item layout
│   │   ├── menu/
│   │   │   └── add_task_menu.xml          # Menu for save action
│   │   ├── values/
│   │   │   ├── strings.xml                # String resources
│   │   │   ├── colors.xml                 # Color definitions
│   │   │   └── themes.xml                 # App theme
│   │   ├── drawable/
│   │   │   └── main_logo.png              # Smart Tasks logo
│   │   └── mipmap-*/                      # App icons for different densities
│   └── AndroidManifest.xml                # Android manifest
├── src/test/
│   └── java/org/example/smarttasks/
│       ├── TaskOrganizerAITest.java       # Unit tests for AI
│       └── TaskViewModelTest.java         # Unit tests for ViewModel
├── build.gradle                           # App-level build configuration
└── proguard-rules.pro                     # ProGuard rules
```

## Requirements

- Android Studio Arctic Fox or later
- Android SDK 24+ (Android 7.0)
- Java 8+

## Building the App

1. Open the project in Android Studio
2. Sync the project with Gradle files
3. Build the project (Build → Make Project)
4. Run on an emulator or physical device

## How to Use

1. **Launch the App**: Start with the welcome screen
2. **Navigate to Tasks**: Tap "Commencer" to enter the task list
3. **Add Tasks**: Use the floating action button to add new tasks
4. **Organize Tasks**: Tasks are automatically organized by AI based on priority, importance, and due date
5. **Edit/Delete**: Tap on any task to edit or delete it

## AI Organization

The app uses a rule-based AI system that:
- Prioritizes tasks by importance (High > Medium > Low)
- Then by priority (High > Medium > Low)
- Finally by due date (earliest first)

## Testing

Run unit tests with:
```bash
./gradlew test
```

Run instrumentation tests with:
```bash
./gradlew connectedAndroidTest
```

## Customization

The app uses a custom color scheme based on the Smart Tasks logo:
- Primary Orange: `#FF6B35`
- Secondary Yellow: `#FFD23F`
- Dark Orange: `#E55A2B`
- Light Orange: `#FF8C42`

## Dependencies

- AndroidX Libraries (AppCompat, ConstraintLayout, RecyclerView, CardView)
- Room (for database persistence)
- ViewModel & LiveData (for UI data management)
- Material Components (for modern UI)

## License

This project is part of the Smart Tasks application suite.
