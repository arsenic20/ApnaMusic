# ApnaMusic 🎵

ApnaMusic is a modern music streaming application built using **Jetpack Compose** and **ExoPlayer**. It provides an intuitive UI for browsing albums, playing tracks, and managing playlists with seamless navigation. The app follows **MVVM Clean Architecture** and leverages **Dagger-Hilt** for dependency injection.

## 📌 Features

### 🎨 Home Screen
- **Top Bar with Navigation**
- **Auto-Slider & Carousel View** for displaying albums and artist collections.
- **Lazy Column for Smooth Scrolling** through content.

### 📜 Playlist Screen
- Displays **tracks of a selected album** or artist.
- Uses **Coil** for lightweight image loading (chosen over Glide since animations are not required).
- **Efficient Data Binding** with ViewModels.

### 🎵 Music Player Screen
- **ExoPlayer Integration** for seamless music playback.
- **Music Controls:** Play, Pause, Next, Previous.
- **Progress Bar** based on track duration.
- **Fisher-Yates Algorithm** for shuffling tracks.

## 🏗️ Architecture
- **MVVM (Model-View-ViewModel)** pattern for maintainability.
- **Dagger-Hilt** for Dependency Injection.
- **Navigation Component** (`NavHost`) for seamless screen transitions.
- **Repository Pattern** for data handling.

## 🛠️ Tech Stack
- **Kotlin & Jetpack Compose** for UI.
- **ExoPlayer** for media playback.
- **Coil** for efficient image loading.
- **Retrofit** for networking.
- **Navigation Component** for seamless navigation.

## 🚀 Getting Started
1. Clone the repository:
   ```sh
   git clone https://github.com/arsenic20/ApnaMusic.git
