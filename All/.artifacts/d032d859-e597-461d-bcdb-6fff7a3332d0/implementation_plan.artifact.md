# Fix App Crash on Launch

The app is crashing because `CardViewActivity` is defined as the launcher activity in `AndroidManifest.xml`, but the class itself is missing from the project. This results in a `ClassNotFoundException`.

## Proposed Changes

### [Android Manifest]

#### [MODIFY] [AndroidManifest.xml](file:///D:/Dhruv/iMCA/Sem-5/Mobile-Application-Development/Android-Projects/IMCADDivision/app/src/main/AndroidManifest.xml)
- Change the launcher activity from the non-existent `CardViewActivity` to `MainActivity`.
- Set `MainActivity` as `exported="true"` so it can be launched.
- Remove the entry for `CardViewActivity`.

## Verification Plan

### Automated Tests
- Run `app:assembleDebug` to ensure the project still builds.
- Deploy the app to the device and verify it launches `MainActivity` without crashing.

### Manual Verification
- Verify the app opens the login page (`MainActivity`) successfully.
