<div align="center">

# KeyOS

**Enhanced Gboard Mod for a cleaner, faster and smarter typing experience. [No Root Required]**

<p>
  <img src="https://img.shields.io/badge/Android-10%20to%2016-3DDC84?style=flat-square&logo=android&logoColor=white" alt="Android Support"/>
  <img src="https://img.shields.io/badge/Architecture-arm64--v8a-blue?style=flat-square" alt="Arch Support"/>
  <img src="https://img.shields.io/badge/Base%20App-Gboard%20v18.5.3-orange?style=flat-square&logo=google" alt="Base Gboard"/>
  <img src="https://img.shields.io/badge/Root-Not%20Required-success?style=flat-square" alt="No Root"/>
  <img src="https://img.shields.io/badge/License-GPL--3.0-lightgrey?style=flat-square" alt="License"/>
</p>

</div>

---

## Overview

KeyOS is a modified build of Gboard designed for side-by-side installation. It keeps the familiar Gboard feel while adding extra features and improvements. 

It installs with its own package ID (`com.key.board`) so you can use it alongside your stock Gboard without replacing or uninstalling it.

---

## Key Features

- **Rambler Voice Typing** – Smarter, cleaner voice to text
- **AI Writing Tools** – Proofread, rephrase and adjust tone on the fly
- **Advanced Spell Checker** – Better suggestions and grammar support
- **Scan Text (OCR)** – Extract text using your camera
- **Clipboard History** – Extended history with more control
- **Material 3 Expressive Keys** – Modern pill-shaped keycaps
- **Emoji Palette V2** – Cleaner emoji layout with smarter tabs
- **Smart Compose & Suggestions** – More accurate predictions
- **Instant Settings Apply** – Most toggles work without restarting the keyboard
- **Side-by-Side Coexistence** – Safe and easy to install

---

## Installation

### Option 1: Install APK
1. Download the `KeyOS` APK
2. Allow installation from your file manager/browser if prompted
3. Go to `Settings > System > Languages & input > On-screen keyboard > Manage keyboards`
4. Enable `KeyOS`
5. Switch to `KeyOS` from the keyboard picker and start using it

### Option 2: Build from Source
1. Place your stock `Gboard v18.5.3 arm64-v8a` APK as `input/gboard.apk`
2. Run the build script:

```bash
./build.sh
```

3. The patched APK will be generated in `output/KeyOS.apk`.

### Build and download from GitHub Actions

The normal **Build & Test KeyOS Patches** workflow creates only a `.mpp` patch
bundle; it cannot create an APK without a stock Gboard input APK.

To create and publish an installable APK:

1. Open **Actions > Build KeyOS APK > Run workflow**.
2. Provide an HTTPS URL for a compatible stock Gboard `arm64-v8a` APK.
3. Optionally provide its SHA-256 checksum.
4. Enter a release tag, such as `v1.3.2`.
5. After the run succeeds, download `KeyOS.apk` from the workflow's **Artifacts**
   section or from the created GitHub **Release**.

The workflow validates the downloaded APK, creates `output/KeyOS.apk`, uploads
it as an Actions artifact, and attaches it to the selected GitHub Release. Do
not commit the stock APK to this repository.

---

## Compatibility

| Property | Value |
| :--- | :--- |
| **Package Name** | `com.key.board` |
| **Base Version** | Gboard `18.5.3.993608717-release-arm64-v8a` |
| **Also Compatible** | `18.4.1`, `18.3.1`, `18.0.3` |
| **Architecture** | `arm64-v8a` |
| **Android** | Android 10 – Android 16 |
| **Root** | Not required |

---

## Disclaimer

This project is for personal and educational use only. "Google", "Gboard", "Android" and related names are trademarks of Google LLC. This project is not affiliated with, endorsed or sponsored by Google LLC in any way.
