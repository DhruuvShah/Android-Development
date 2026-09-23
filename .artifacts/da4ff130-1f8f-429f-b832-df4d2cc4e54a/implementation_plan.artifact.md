# Fix Build Issues in RecyclerView Project

The current project has several configuration issues that prevent it from building, including invalid SDK/AGP versions and an environment issue with `JAVA_HOME`.

## User Review Required

> [!IMPORTANT]
> **Environment Issue:** The system's `JAVA_HOME` environment variable is pointing to an invalid directory (`C:\Program Files\Java\jdk-20`). You should update this to point to a valid JDK installation (like the one bundled with Android Studio).

> [!WARNING]
> **Invalid Versions:** The project is configured with `compileSdk 37` and `AGP 9.4.1`, which are either non-existent or unstable. I will downgrade these to stable versions (SDK 35 and AGP 8.7.2).

## Proposed Changes

### Build Configuration

#### [MODIFY] [libs.versions.toml](file:///D:/Dhruv/iMCA/Sem-5/Mobile-Application-Development/Android-Projects/RecyclerView/gradle/libs.versions.toml)
Update AGP and other dependency versions to stable releases.

#### [MODIFY] [build.gradle.kts](file:///D:/Dhruv/iMCA/Sem-5/Mobile-Application-Development/Android-Projects/RecyclerView/app/build.gradle.kts)
Fix `compileSdk` and `targetSdk` syntax and values.

#### [MODIFY] [gradle-wrapper.properties](file:///D:/Dhruv/iMCA/Sem-5/Mobile-Application-Development/Android-Projects/RecyclerView/gradle/wrapper/gradle-wrapper.properties)
Downgrade Gradle to a stable version (e.g., 8.10.2) to avoid download timeouts and compatibility issues.

## Verification Plan

### Automated Tests
- Run `./gradlew assembleDebug` to verify the build completes successfully (after fixing `JAVA_HOME`).

### Manual Verification
- Verify that the project opens in Android Studio without sync errors.
