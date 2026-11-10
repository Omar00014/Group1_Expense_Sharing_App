# Repository Guidelines & AI Agent Rules

## Project Overview
- **Project Name**: Payment_App
- **Application Package**: `com.example.payment_app`
- **Platform**: Native Android (Kotlin)
- **UI Framework**: Jetpack Compose with Material 3 (Compose BOM)
- **Min SDK**: 24 | **Target/Compile SDK**: 37

---

## 🏛️ Architecture & Component Design
- **UI & State**:
  - Use Jetpack Compose exclusively for all UI features. Do not use legacy XML layout views.
  - Follow unidirectional data flow (UDF). State flows down, events flow up.
  - Expose UI state from ViewModels using Kotlin `StateFlow` or `SharedFlow`. Collect in Compose using `collectAsStateWithLifecycle()`.
- **Concurrency**:
  - Use Kotlin Coroutines and Structured Concurrency.
  - Avoid hardcoding `Dispatchers.IO` or `Dispatchers.Default` inside ViewModels/Repositories; inject `CoroutineDispatcher` instances for testability.
- **Dependency Injection**:
  - Prefer modern DI frameworks (e.g., Hilt / Koin) when adding dependency injection to new packages or modules.

---

## 🎨 Code Style & Conventions
- **Idiomatic Kotlin**:
  - Prefer immutable properties (`val`) over mutable ones (`var`).
  - Use `sealed interface` or `sealed class` for UI states (e.g., `Loading`, `Success`, `Error`).
- **Compose Best Practices**:
  - Keep Composables stateless when possible; hoist state to parent Composables or ViewModels.
  - Ensure all customizable Composables accept a default `modifier: Modifier = Modifier` as the first optional parameter.
  - Always include `@Preview` composables with sample/placeholder data for previewing UI in Android Studio.
  - Respect system edge-to-edge layout insets.
- **Naming Conventions**:
  - Composable functions: `PascalCase` (e.g., `PaymentHeader`, `TransactionRow`).
  - ViewModel classes: `[Feature]ViewModel` (e.g., `PaymentViewModel`).
  - State objects: `[Feature]UiState` (e.g., `PaymentUiState`).

---

## 📁 Directory Structure
```text
app/src/main/java/com/example/payment_app/
├── MainActivity.kt        # Main Entry Point & Host Activity
└── ui/
    └── theme/             # Material 3 Color, Type, & Theme definitions
```
*When adding new features, organize code by feature package (e.g., `com.example.payment_app.feature.payment`).*

---

## 🛠️ Build, Test, & Lint Commands
- **Assemble Debug APK**: `./gradlew :app:assembleDebug`
- **Run Unit Tests**: `./gradlew :app:testDebugUnitTest`
- **Run Android Instrumentation Tests**: `./gradlew :app:connectedDebugAndroidTest`
- **Clean Project**: `./gradlew clean`

---

## ⚠️ Critical Guardrails & Rules
1. **Never Hardcode Secrets**: Do not place API keys, private tokens, or payment credentials directly in Kotlin files. Use Gradle `BuildConfig` or secret properties files.
2. **Preserve Editor Sync**: Do not use shell redirection or direct disk edits outside IDE tools.
3. **Write Unit Tests**: Any new business logic, domain mappers, or ViewModels must include corresponding unit tests under `app/src/test/`.
