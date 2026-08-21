# Add Login and Register Functionality (Intent-based)

This plan adds a Login and Register flow to the Fairness Engine app. Registration data will be passed back to the Login screen using `Intent` extras. All other app functionalities will be disabled by isolating the `AddRoommatesActivity` and making the new Login screen the launcher.

## User Review Required

> [!IMPORTANT]
> Since we are not using `SharedPreferences` as requested, the "registered" user data will only persist for the current session (i.e., if the app is fully closed and cleared from memory, you will need to register again to log in). This is the limitation of using only `Intent` extras.

## Proposed Changes

### [Authentication Component]

#### [NEW] [activity_login.xml](file:///D:/Dhruv/iMCA/Sem-5/Mobile-Application-Development/Android-Projects/FairnessEngine/app/src/main/res/layout/activity_login.xml)
Layout for the login screen, matching the app's dark theme and UI style.
- Email and Password fields.
- Login and Register buttons.

#### [NEW] [activity_register.xml](file:///D:/Dhruv/iMCA/Sem-5/Mobile-Application-Development/Android-Projects/FairnessEngine/app/src/main/res/layout/activity_register.xml)
Layout for the registration screen.
- Full Name, Email, and Password fields.
- Submit button.

#### [NEW] [LoginActivity.java](file:///D:/Dhruv/iMCA/Sem-5/Mobile-Application-Development/Android-Projects/FairnessEngine/app/src/main/java/com/example/fairnessengine/LoginActivity.java)
Handles login logic. It will receive registration data from `RegisterActivity` via `Intent` extras and compare it with user input.

#### [NEW] [RegisterActivity.java](file:///D:/Dhruv/iMCA/Sem-5/Mobile-Application-Development/Android-Projects/FairnessEngine/app/src/main/java/com/example/fairnessengine/RegisterActivity.java)
Handles registration logic. It will pass the entered email and password back to `LoginActivity`.

---

### [App Configuration & Isolation]

#### [MODIFY] [AndroidManifest.xml](file:///D:/Dhruv/iMCA/Sem-5/Mobile-Application-Development/Android-Projects/FairnessEngine/app/src/main/AndroidManifest.xml)
- Change the launcher activity from `MainActivity` to `LoginActivity`.
- Register the new `LoginActivity` and `RegisterActivity`.

#### [MODIFY] [AddRoommatesActivity.java](file:///D:/Dhruv/iMCA/Sem-5/Mobile-Application-Development/Android-Projects/FairnessEngine/app/src/main/java/com/example/fairnessengine/AddRoommatesActivity.java)
- Hide the bottom navigation bar permanently.
- Disable navigation to `HomeDashboardActivity` from the "Let's go" button.

## Verification Plan

### Manual Verification
1. Launch the app (should open `LoginActivity`).
2. Click "Register".
3. Fill in Full Name, Email, and Password. Click "Submit".
4. Enter the **same** email and password in the `LoginActivity`.
5. Click "Login" (should open `AddRoommatesActivity`).
6. Verify that the bottom navigation is hidden and you cannot navigate to other parts of the app.
7. Try entering wrong credentials to verify the error message.
