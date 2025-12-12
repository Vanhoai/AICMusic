# 🎵 ICMusic

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com/reference)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-orange.svg)](https://kotlinlang.org/)
[![Compose](https://img.shields.io/badge/Jetpack-Compose-blue.svg)](https://developer.android.com/jetpack/compose)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

ICMusic is a modern Android application that allows users to download and listen to music from
YouTube, featuring a beautiful user interface and smooth user experience.

![thumbnail](https://raw.githubusercontent.com/Vanhoai/ICMusic/thumbnail.png)

## ✨ Key Features

- 🎬 Direct music downloads from YouTube
- 🎵 Feature-rich music player
- 📱 Modern UI with Jetpack Compose
- 📥 Easy download management
- 🎨 Material Design 3 with dark mode support
- 📋 Create and manage playlists
- 🔄 Local data synchronization

## 🛠️ Technology Stack

- **Kotlin** - Primary programming language
- **Jetpack Compose** - Modern UI toolkit
- **Material Design 3** - Design system
- **Kotlin Coroutines & Flow** - Asynchronous programming
- **Hilt** - Dependency injection
- **Room Database** - Local storage
- **ExoPlayer** - Music playback
- **Retrofit** - Networking
- **Coil** - Image loading and caching
- **YouTube Data API** - Video search and download
- **Clean Architecture** with MVVM pattern

## 📱 Screenshots

<div style="display: flex;">
    <img src="/api/placeholder/200/400" width="200" alt="Home Screen"/>
    <img src="/api/placeholder/200/400" width="200" alt="Player Screen"/>
    <img src="/api/placeholder/200/400" width="200" alt="Library Screen"/>
</div>

## ⚙️ System Requirements

- Android 6.0 (API level 23) or higher
- 50MB of free storage
- Internet connection

## 🚀 Installation

1. Clone the repository:

```bash
git clone https://github.com/yourusername/ICMusic.git
```

2. Open the project in Android Studio

3. Add your YouTube API key to `local.properties`:

```properties
YOUTUBE_API_KEY=your_api_key_here
```

4. Build and run the application

## 📖 Usage Guide

### Downloading Music

1. Open the app and tap the search icon
2. Enter a song name or paste a YouTube URL
3. Select the desired song
4. Tap the download button and wait for completion

### Playing Music

- Tap any song to play
- Use control buttons for play/pause, next, previous
- Swipe up on the mini player to open full-screen player
- Create playlists by long-pressing a song and selecting "Add to playlist"

## 🤝 Contributing

We welcome all contributions! If you'd like to contribute:

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

ICMusic is distributed under the MIT License. See `LICENSE` for more information.

## 📞 Contact

- Website: [icmusic.com](https://icmusic.com)
- Email: support@icmusic.com
- Twitter: [@ICMusic](https://twitter.com/ICMusic)
- Facebook: [ICMusic](https://facebook.com/ICMusic)

## 🙏 Acknowledgments

Thanks to the open-source libraries used in this project:

- [ExoPlayer](https://github.com/google/ExoPlayer)
- [Coil](https://github.com/coil-kt/coil)
- [Retrofit](https://github.com/square/retrofit)
- [Hilt](https://dagger.dev/hilt/)

## 🔒 Privacy & Security

ICMusic takes your privacy seriously. We:

- Only request necessary permissions
- Don't collect personal data
- Store all data locally on your device
- Use secure HTTPS connections
- Never share your information with third parties

## 🐛 Bug Reporting

Found a bug? Please help us improve by reporting it:

1. Go to the Issues tab
2. Click "New Issue"
3. Select "Bug Report"
4. Fill in the template with as much detail as possible

## 🚀 Upcoming Features

- Offline mode
- Crossfade between tracks
- Advanced equalizer
- Cloud backup
- Lyrics integration
- Social sharing features