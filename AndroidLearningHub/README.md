# Android Learning Hub

A single, connected Android app (Java) that implements **every practical exercise** from the iMCA Android Development syllabus (Units 1–4) as one coherent product — not 20 separate demo apps.

## Opening the project

1. Open **Android Studio** (Hedgehog/Iguana or newer) → `File → Open` → select the `AndroidLearningHub` folder.
2. Let Gradle sync. Android Studio will auto-generate the Gradle wrapper if it's missing — accept that prompt.
3. Run on an emulator with **Play Store image** (needed for Google Maps + Location) or a physical device, API 24+.

## One thing you must do before Maps will work

Open `app/src/main/AndroidManifest.xml` and replace:
```xml
<meta-data android:name="com.google.android.geo.API_KEY" android:value="YOUR_GOOGLE_MAPS_API_KEY_HERE" />
```
with a real key from Google Cloud Console (enable **Maps SDK for Android**). Every other screen works with zero configuration.

## App flow

```
LoginActivity  →  HubActivity (RecyclerView menu)  →  13 feature screens
     ↓
SignUpActivity
```

Login and Sign Up both write to the same `SharedPreferences` used across the app. From the Hub, every card launches a feature screen with an **explicit Intent** — this is itself the Unit 3 "navigating between activities" practical, applied consistently instead of only in one throwaway demo.

## Practical → file map

| Syllabus practical | Screen / class |
|---|---|
| Hello World | `HubActivity` (post-login landing) |
| Activity Lifecycle | `LifecycleActivity` |
| Custom dialogs & AlertDialog | `DialogShowcaseActivity` |
| Camera & gallery access | `MediaPickerActivity` |
| Multi-element UI layout | `UiPlaygroundActivity` |
| WebView | `WebViewActivity` + `assets/help.html` |
| ImageView/GalleryView | `MediaPickerActivity` + `ImageAdapter` |
| Input validation | `SignUpActivity` (live) / `LoginActivity` (submit-time) |
| Login screen | `LoginActivity` |
| Explicit & implicit intents (share/call/email/camera) | `QuickActionsActivity` |
| Calculator | `CalculatorActivity` |
| ListView + ArrayAdapter | `NotesListActivity` |
| RecyclerView for dynamic lists | `ExpenseAdapter` (used in `ExpenseTrackerActivity`) |
| Fragments / multi-pane | `SettingsActivity` + `SettingsMasterFragment` + `SettingsDetailFragment` |
| Music/media player | `MediaPlayerActivity` (`res/raw/sample_track.wav` — swap in your own) |
| SharedPreferences | `LoginActivity` (remember-me), `SettingsMasterFragment` (theme/currency) |
| Runtime permissions | `PermissionUtils` + camera/location/contacts screens |
| Google Maps + current location | `MapsActivity` |
| Contacts via ContentProvider | `ContactsActivity` + `ContactAdapter` |
| SQLite CRUD | `ExpenseDbHelper` |
| Mini-project (Expense Tracker) | `ExpenseTrackerActivity` — ties RecyclerView + SQLite + SharedPreferences + dialogs + validation together |

## Design language

Minimal, light, single accent color (`#0A84FF`), rounded 14–16dp corners, generous whitespace, no neon or stock Material defaults — deliberately closer to iOS/HIG-style polish than a typical Android tutorial UI, per the brief.

## Known gaps / next steps

- **Room** (the syllabus's alternate persistence library to raw SQLite) isn't implemented — the mini-project intentionally uses `SQLiteOpenHelper` since the syllabus lists SQLite as the CRUD practical's target. Room can be added as a second `ExpenseDbHelper` variant if your instructor wants both compared.
- The launcher icon is a simple placeholder vector — swap `drawable/ic_launcher_foreground.xml` for your own mark.
- `MediaPlayerActivity` ships with a generated 3-second tone so the project runs out of the box; replace `res/raw/sample_track.wav` with a real track.
