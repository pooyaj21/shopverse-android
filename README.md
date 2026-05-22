# shopverse-android

## Module layout

```
:app                     Android application — ShopVerseApplication wires Koin.
:core:shared             JVM library — AppResult, DispatcherProvider, EnvironmentConfiguration.
:core:model              Android library — BaseModel + Meta + Pagination (entity base types).
:core:preferences        Android library — EncryptedSharedPreferences + SharedPref wrapper + Koin DI.
:core:service            Android library — service interfaces live here. Empty Koin module.
:core:data               Android library — repository interfaces + impls live here. DispatcherProvider binding.
:core:domain             Android library — UseCases live here. Koin module includes data.
:build-logic:convention  Included build — Gradle convention plugins.
```

Dependency direction (modules never depend "upward"):

```
app → domain → data → { service, model, preferences }, all share :core:shared
```

## Koin module graph

`ShopVerseApplication` starts Koin with `domainDiModule`, which transitively includes:

```
domainDiModule
  └── dataDiModule
        ├── serviceDiModule
        └── preferencesDiModule
```

Each layer owns its own `di/DI.kt` and only includes the layer directly beneath it.

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

This is the architectural shell only. Every module compiles and the Koin
graph is wired end-to-end, but no domain entities, services, repositories,
or use cases are implemented yet. Coming per
`../shopverse-idea/week-2-android-foundation.md` and
`week-3-android-polish.md`:

- Domain entities in `:core:model` (Product, Cart, Order, …)
- Retrofit / Ktor services in `:core:service`
- Room database + DAOs and repository impls in `:core:data`
- Use cases in `:core:domain`
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
