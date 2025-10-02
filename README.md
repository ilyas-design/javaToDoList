# Smart Tasks - Android Todo List App

A modern Android application for intelligent task management with a beautiful welcome page featuring the Smart Tasks logo.

## Features

- **Welcome Page**: Beautiful welcome screen with the Smart Tasks logo
- **Modern UI**: Material Design 3 components with custom branding
- **Responsive Layout**: Optimized for different screen sizes
- **Brand Colors**: Orange and yellow color scheme matching the logo

## Project Structure

```
app/
├── src/main/
│   ├── java/org/example/smarttasks/
│   │   └── MainActivity.java          # Main activity with welcome page
│   ├── res/
│   │   ├── layout/
│   │   │   └── activity_main.xml      # Welcome page layout
│   │   ├── values/
│   │   │   ├── strings.xml            # String resources
│   │   │   ├── colors.xml             # Color definitions
│   │   │   └── themes.xml             # App theme
│   │   ├── drawable/
│   │   │   └── main_logo.png          # Smart Tasks logo
│   │   └── mipmap-*/                  # App icons for different densities
│   └── AndroidManifest.xml            # Android manifest
├── build.gradle.kts                   # App-level build configuration
└── proguard-rules.pro                 # ProGuard rules
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

## Welcome Page Features

- **Logo Display**: Large Smart Tasks logo prominently displayed
- **App Title**: "Welcome to Smart Tasks" with custom typography
- **Subtitle**: "Your intelligent task management companion"
- **Description**: Brief app description
- **Get Started Button**: Material Design button to proceed to main app

## Customization

The app uses a custom color scheme based on the Smart Tasks logo:
- Primary Orange: `#FF6B35`
- Secondary Yellow: `#FFD23F`
- Dark Orange: `#E55A2B`
- Light Orange: `#FF8C42`

## Next Steps

The welcome page is ready! You can now:
1. Add navigation to the main task list activity
2. Implement user authentication
3. Add onboarding screens
4. Create the main task management features

## License

This project is part of the Smart Tasks application suite.
