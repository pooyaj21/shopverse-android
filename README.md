# shopverse-android

Native Kotlin Android client for ShopVerse. Architecture mirrors the
internal `Provin` project: convention-plugin-driven multi-module setup
with Clean Architecture layering.

## Module layout

```
:app                     Android application — only ShopVerseApplication.kt for now.
:core:shared             JVM library — coroutine helpers, AppResult, DispatcherProvider.
:core:model              Android library — domain entities / DTOs.
:core:preferences        Android library — Repository interfaces for persistent storage.
:core:service            Android library — network / service interfaces.
:core:data               Android library — Retrofit + Room impls, DI bindings.
:core:domain             Android library — UseCases + Koin module.
:build-logic:convention  Included build — Gradle convention plugins.
```

Dependency direction (modules never depend "upward"):

```
app → domain → data → { service, preferences, model }, all share :core:shared
```

## Convention plugins (`build-logic/convention`)

Apply these in module `build.gradle.kts` instead of duplicating Android/Kotlin DSL:

| Plugin id                | Used by                          | What it does                                                 |
| ------------------------ | -------------------------------- | ------------------------------------------------------------ |
| `app.android.application`| `:app`                           | Applies AGP app + Kotlin Android, sets compile/min/target SDK, JVM 17, desugaring on, creates `stage` build type, includes `:core:shared` + coroutines |
| `app.android.library`    | all `:core:*` Android libraries  | Same as above but for libraries (+ `consumerProguardFiles`)  |
| `app.jvm.library`        | `:core:shared`                   | Kotlin JVM-only library, no Android deps                     |
| `app.koin`               | JVM/Android libraries needing Koin core    | Adds `koin-core`                                   |
| `app.koin.android`       | Android libraries needing Koin Android     | Adds `koin-android`                                |

SDK levels and JVM target are centralised in
`build-logic/convention/.../Defaults.kt` — update there, not in individual modules.

## What's NOT in here yet (intentional)

This is just the shell. No API clients, no use cases, no Room database,
no screens. Coming in later weeks per `../shopverse-idea/week-2-android-foundation.md`
and `week-3-android-polish.md`:

- Retrofit + Supabase wiring in `:core:service` / `:core:data`
- Domain `UseCase` pairs in `:core:domain`
- Compose screens + ViewModels in `:app/presentation/`
- MainActivity + nav graph

## Build

JDK 17 required.

```bash
./gradlew assembleDebug     # debug APK (won't be installable until an Activity is added)
./gradlew :app:build        # full module build
./gradlew lint
./gradlew test
./gradlew clean
```
