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

## Implemented screens

`app/presentation/screen/` — splash, onboarding, auth (login / sign-up),
home, product detail, cart, orders + order detail (QR-code receipt),
account / profile. Deep-link URI routing is wired through a central
navigator. Use cases live in `:core:domain` (auth, orders, products, …)
backed by Supabase.

## Build

JDK 17 required.

```bash
./gradlew assembleDebug     # debug APK
./gradlew :app:build        # full module build
./gradlew lint
./gradlew test
./gradlew clean
```

### Signing setup

Both build types are signed with a release keystore. Builds read everything
from `local.properties` (git-ignored):

```properties
sdk.dir=/path/to/Android/sdk

supabase.url=https://<project>.supabase.co
supabase.anonKey=<publishable anon key>

store=../key.jks            # resolved relative to :app → repo root
storePass=<store password>
keyAlias=shopverse
keyPass=<key password>
```

Generate the keystore once at the repo root (it is git-ignored):

```bash
keytool -genkeypair -v -keystore key.jks -alias shopverse \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -storepass <store password> -keypass <key password> \
  -dname "CN=Pooya Jalali, O=ShopVerse, C=US"
```

## Release pipeline (fastlane + GitHub Actions)

`.github/workflows/release.yml` runs on every `v*` tag push (or manually
via *Actions → Release → Run workflow*). It builds the signed release APK
with fastlane and publishes a GitHub Release with the APK attached.

Fastlane lanes (`fastlane/Fastfile`):

```bash
bundle exec fastlane android build     # clean + assembleRelease (signed APK)
bundle exec fastlane android release   # build + create GitHub release with APK
```

Required repository **Actions secrets**:

| Secret              | Value                                      |
| ------------------- | ------------------------------------------ |
| `KEYSTORE_BASE64`   | `base64 -i key.jks`                        |
| `STORE_PASSWORD`    | keystore store password                    |
| `KEY_ALIAS`         | key alias (`shopverse`)                    |
| `KEY_PASSWORD`      | key password                               |
| `SUPABASE_URL`      | Supabase project URL                       |
| `SUPABASE_ANON_KEY` | Supabase publishable anon key              |

(`GITHUB_TOKEN` is provided automatically by Actions.)

Cutting a release:

```bash
git tag v1.0.0
git push origin v1.0.0
```

Note: the workflow builds the commit the tag points to — after pushing a
fix, re-point the tag (`git tag -d`, push the deletion, re-tag) rather than
re-running the failed workflow, which would reuse the old commit.
