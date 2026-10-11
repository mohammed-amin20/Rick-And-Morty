<div align="center">

# 🛸 Rick & Morty — Android

### Explore the multiverse — a modern Android showcase built with Jetpack Compose, Clean Architecture & MVI

[![Kotlin](https://img.shields.io/badge/Kotlin-2.3.20-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-BOM%202026.03.01-4285F4?logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Material 3](https://img.shields.io/badge/Material%203-UI-6750A4?logo=materialdesign&logoColor=white)](https://m3.material.io)
[![AGP](https://img.shields.io/badge/AGP-9.0.1-3DDC84?logo=androidstudio&logoColor=white)](https://developer.android.com/build)
[![API](https://img.shields.io/badge/API-26%2B-brightgreen?logo=android)](https://developer.android.com)
[![Architecture](https://img.shields.io/badge/Architecture-Clean%20%2B%20MVI-blueviolet)](#-architecture)
[![Room](https://img.shields.io/badge/Room-2.8.4-orange)](https://developer.android.com/training/data-storage/room)
[![Coil](https://img.shields.io/badge/Coil-3.4.0-6E4AFF)](https://coil-kt.github.io/coil/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](#-license)

A sleek Android client for [**The Rick and Morty API**](https://rickandmortyapi.com), featuring a
cinematic character list, shared-element navigation into a rich detail screen, offline caching,
and a handcrafted Rick & Morty–inspired design system in both light **and** dark mode.

</div>

---

## 📖 Table of Contents

- [✨ Features](#-features)
- [📸 Screenshots](#-screenshots)
- [🧰 Tech Stack](#-tech-stack)
- [🏗️ Architecture](#️-architecture)
- [🔄 Data Flow](#-data-flow)
- [📂 Project Structure](#-project-structure)
- [🎨 Design System](#-design-system)
- [🚀 Getting Started](#-getting-started)
- [🧪 Testing](#-testing)
- [🗺️ Roadmap](#️-roadmap)
- [📄 License](#-license)
- [👤 Author](#-author)

---

## ✨ Features

| | |
|---|---|
| 🎬 **Cinematic list** | Full-width 16:9 character cards with image scrims, status badges and press-scale feedback. |
| ♾️ **Infinite scroll** | Paged loading driven by `LazyListState`, guarded against duplicate requests. |
| 🔎 **Debounced search** | Live search across the multiverse with a 450 ms debounce and instant clear. |
| 🪄 **Shared-element morph** | The image and name smoothly morph from card → hero as you navigate to the detail screen. |
| 🧬 **Rich detail** | Status/species chips plus Profile, Origin, Location and Appearances (episode) sections. |
| 💀 **Graceful states** | Shimmer skeletons, an animated portal loader, and empty/error states with retry. |
| 💾 **Offline cache** | Room-backed cache with network-first strategy and transparent fallback. |
| 🌗 **Light & dark** | Full Rick & Morty palette for both themes, following the system setting. |
| 🏛️ **Clean + MVI** | Clear layer separation with a unidirectional `Action → State` flow. |
| 👁️ **Previewable** | Compose previews for **every** screen state in **both** light and dark. |

---

## 📸 Screenshots

> Add your captures to [`docs/screenshots/`](docs/screenshots) using the file names below — they'll render automatically.

| List · Light | List · Dark |
| :---: | :---: |
| ![Character list light](docs/screenshots/list-light.png) | ![Character list dark](docs/screenshots/list-dark.png) |

| Detail · Light | Detail · Dark |
| :---: | :---: |
| ![Character detail light](docs/screenshots/detail-light.png) | ![Character detail dark](docs/screenshots/detail-dark.png) |

---

## 🧰 Tech Stack

| Category | Technology | Version |
| :--- | :--- | :--- |
| **Language** | Kotlin | `2.3.20` |
| **UI** | Jetpack Compose · Material 3 | BOM `2026.03.01` |
| **Architecture** | Clean Architecture + MVI | — |
| **DI** | Hilt (Dagger) | `2.59.2` |
| **Async** | Kotlin Coroutines + Flow | — |
| **Networking** | Retrofit · OkHttp Logging | `3.0.0` · `5.3.2` |
| **Serialization** | Gson · kotlinx.serialization | `3.0.0` · `1.10.0` |
| **Persistence** | Room | `2.8.4` |
| **Images** | Coil 3 | `3.4.0` |
| **Navigation** | Navigation Compose (type-safe) | `2.9.0` |
| **Build** | AGP · Gradle · KSP | `9.0.1` · `9.1.0` · `2.3.12` |
| **SDK** | min / target / compile | `26` / `36` / `36` |
| **Toolchain** | JDK / JVM target | `21` / `11` |

---

## 🏗️ Architecture

The app follows **Clean Architecture** with an **MVI** presentation layer. Dependencies point inward:
the UI knows only about state and actions, the repository hides where data comes from, and the data
sources are swappable.

```mermaid
flowchart TD
    subgraph Presentation
        UI["Compose Screen<br/>(Stateless Content)"]
        VM["ViewModel (MVI)<br/>State · Action"]
    end
    subgraph Data
        REPO["Repository<br/>network-first + cache fallback"]
        REMOTE["Remote — Retrofit API<br/>RickAndMortyAPI"]
        LOCAL["Local — Room DB<br/>CharacterDao · Converters"]
    end

    UI -- "onAction(Action)" --> VM
    VM -- "State (StateFlow)" --> UI
    VM --> REPO
    REPO --> REMOTE
    REPO --> LOCAL
```

**MVI loop** — every user intent is an `Action`, transformed into an immutable `State`:

```mermaid
sequenceDiagram
    participant U as User
    participant S as Screen
    participant V as ViewModel
    participant R as Repository
    U->>S: types "rick"
    S->>V: onAction(OnSearchChanged("rick"))
    V->>V: debounce(450ms) · distinctUntilChanged
    V->>R: getCharactersByName(page, "rick")
    R-->>V: Result.Success(characters)
    V-->>S: State(characters, loading = false)
    S-->>U: recomposes the list
```

**Layers**

- **Presentation** — `list/` & `detail/` each expose a stateless `*Content` composable (previewable)
  plus a stateful screen, `State`, `Action` and a Hilt `ViewModel`.
- **Data** — `repository/` orchestrates `data_source/` (Retrofit + Room) and `mappers/` convert DTOs
  to Room entities. `util/` holds the `Result<T, Error>` sealed types.
- **DI** — `di/RickAndMortyModule.kt` provides the API, database and repository singletons.

---

## 🔄 Data Flow

1. The user types in the search field → `OnSearchChanged` updates state.
2. The ViewModel debounces the query, then calls `Repository.getCharactersByName(page, name)`.
3. The repository hits the **network first**; on failure it **falls back to Room** cache.
4. Network results are **upserted** into Room and emitted back as `State`.
5. The list recomposes; scrolling near the end emits `OnNextPageRequested` for the next page.

---

## 📂 Project Structure

<details>
<summary><b>Expand the module tree</b></summary>

```text
app/src
├── debug/java/…/presentation/preview/ScreenPreviews.kt   # Light & dark previews for all states
└── main/java/com/mohammed/rickandmorty
    ├── MainActivity.kt                                   # SharedTransitionLayout + NavHost
    ├── RickAndMortyApp.kt                                # @HiltAndroidApp
    ├── ui/theme/                                         # Color · Shape · Theme · Type
    └── fueature/rick_and_morty
        ├── data
        │   ├── data_source
        │   │   ├── local/db                              # RoomDatabase · Converters
        │   │   │   ├── dao/CharacterDao.kt
        │   │   │   ├── entity/Character.kt
        │   │   │   ├── Converters.kt
        │   │   │   └── RickAndMortyDB.kt
        │   │   └── remote/rick_and_morty_api             # Retrofit service + DTOs
        │   │       ├── RickAndMortyAPI.kt
        │   │       └── dto/ (Character, CharactersDto, Info, Location, Origin)
        │   ├── mappers/RickAndMortyMappers.kt
        │   ├── repository/RickAndMortyRepository.kt
        │   └── util/ (Error.kt, Result.kt)
        ├── di/RickAndMortyModule.kt
        └── presentation
            ├── components/                               # CharacterCard, Chips, LoadingPortal, …
            ├── detail/                                   # Character detail (Screen · State · Action · VM)
            ├── list/                                     # Character list (Screen · State · Action · VM)
            └── navigation/                               # Type-safe Routes + NavHost
```

</details>

---

## 🎨 Design System

A Rick & Morty–inspired palette that adapts to light and dark.

| Swatch | Token | Hex | Usage |
| :---: | :--- | :--- | :--- |
| ![bg](https://img.shields.io/badge/-272B33-272B33?style=flat-square) | `RamBackground` | `#272B33` | App background (dark) |
| ![surface](https://img.shields.io/badge/-3C3E44-3C3E44?style=flat-square) | `RamSurface` | `#3C3E44` | Cards & sheets |
| ![elev](https://img.shields.io/badge/-45484E-45484E?style=flat-square) | `RamSurfaceElevated` | `#45484E` | Chips & elevated surfaces |
| ![blue](https://img.shields.io/badge/-00B5CC-00B5CC?style=flat-square) | `PortalBlue` | `#00B5CC` | Primary accent |
| ![green](https://img.shields.io/badge/-97CE4C-97CE4C?style=flat-square) | `PortalGreen` | `#97CE4C` | Secondary accent |
| ![glow](https://img.shields.io/badge/-B2DF28-B2DF28?style=flat-square) | `PortalGlow` | `#B2DF28` | Tertiary glow |
| ![alive](https://img.shields.io/badge/-55CC44-55CC44?style=flat-square) | `StatusAlive` | `#55CC44` | Alive characters |
| ![dead](https://img.shields.io/badge/-D63D2E-D63D2E?style=flat-square) | `StatusDead` | `#D63D2E` | Dead characters |

Bold, tight typography (`displaySmall`, `headlineSmall`, …) and rounded, expressive shapes are defined
in `ui/theme/Type.kt` and `ui/theme/Shape.kt`.

---

## 🚀 Getting Started

**Prerequisites**

- Android Studio (latest stable) with **JDK 21**
- Android SDK with **API 36** platform & build-tools
- An internet connection (the app talks to `rickandmortyapi.com`)

**Clone & run**

```bash
git clone https://github.com/mohammed-amin20/Rick-And-Morty.git
cd Rick-And-Morty
```

Open the project in Android Studio and hit **Run ▶**, or from the CLI:

```bash
# Build a debug APK
./gradlew :app:assembleDebug

# Build & install on a connected device/emulator
./gradlew :app:installDebug
```

> The debug APK is written to `app/build/outputs/apk/debug/`.

---

## 🧪 Testing

```bash
# Unit tests
./gradlew test

# Instrumented tests (requires a device/emulator)
./gradlew connectedAndroidTest
```

Compose previews live in `app/src/debug/…/presentation/preview/ScreenPreviews.kt` and cover the list
and detail screens in **loading, content, empty, pagination and error** states — in **both** light and
dark mode.

---

## 🗺️ Roadmap

- [ ] Paging 3 integration for the character list
- [ ] Offline-first single source of truth (Flow from Room)
- [ ] Search filters (status, species, gender)
- [ ] Episodes & locations browsing tabs
- [ ] UI snapshot tests for the preview states
- [ ] Downloadable Rick & Morty font

---

## 📄 License

Released under the **MIT License**. See [LICENSE](LICENSE) for details.

```
MIT License · Copyright (c) 2026 mohammed-amin20
```

---

## 👤 Author

<div align="center">

**Mohammed Amin** — [@mohammed-amin20](https://github.com/mohammed-amin20)

Made with 🛸 & ☕ — powered by [The Rick and Morty API](https://rickandmortyapi.com)

</div>
