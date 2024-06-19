# To-Do List App

## Overview

This is a simple To-Do List application developed using Android Studio. The app allows users to manage tasks with features like task creation, updating, and deletion. Each task can include a title, subtitle, task notes, image, URL, priority, and category. The application also includes a splash screen and an onboarding screen for new users.

## Features

- **Create, Update, and Delete Tasks**: Manage tasks efficiently with full CRUD (Create, Read, Update, Delete) operations.
- **Task Details**: Customize tasks with optional fields such as subtitle, note text, image, URL, priority, and category.
- **Organize by Category**: Categorize tasks under different categories like University, Home, Work, and Other.
- **Set Task Priorities**: Assign priorities to tasks for better organization.
- **Onboarding and Splash Screens**: Guide new users through the app's features with an onboarding screen and present a splash screen on app launch.
- **Persistent Storage**: Utilizes Room Database for local storage of tasks.

## Skills and Libraries Used

- **Kotlin**: Main programming language used for development.
- **Jetpack Compose**: Modern UI toolkit for building native Android UIs.
- **Android Architecture Components**:
  - Room: Persistence library for local database operations.
  - ViewModel: Manage UI-related data in a lifecycle-conscious way.
  - StateFlow: Provides a convenient way to emit state updates in a reactive manner.
- **MVVM Architecture**: Architecture pattern used to separate the UI and business logic, leveraging ViewModel for managing UI-related data.
- **Dependency Injection**: 
  - Hilt: Standard library to incorporate Dagger dependency injection into an Android application.
- **Navigation**: 
  - Navigation Compose: Tool to manage navigation within a Jetpack Compose app.
- **Material Design Components**: 
  - Used for UI components and theming.
- **Coil**: 
  - Image loading library for Android.
- **SwipeRefreshLayout**: 
  - Android widget for implementing swipe-to-refresh UX pattern.

## Video Demo

[Watch the video demo of the To-Do List App](

https://github.com/Omar-Saeid11/To-Do-List/assets/93147693/fc8f5e06-be69-43b4-91ea-5144a3cc61ee

)

## Getting Started

### Prerequisites

- Android Studio 4.0 or higher
- Android device or emulator running Android 5.0 (Lollipop) or higher

### Installation

1. **Clone the repository:**

   ```sh
   git clone https://github.com/Omar-Saeid11/To-Do-List.git
