# 🧮 CalciDon — Next-Gen Android Calculator & Mathematical Suite

[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Android](https://img.shields.io/badge/Android-API%2024%20--%2036-3DDC84?logo=android&logoColor=white)](https://developer.android.com)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose%20%7C%20Material%203-4285F4?logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Gradle](https://img.shields.io/badge/Gradle-9.3.1-02303A?logo=gradle&logoColor=white)](https://gradle.org)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![Download APK](https://img.shields.io/badge/Download-CalciDon.apk-3DDC84?logo=android&logoColor=white)](https://github.com/abhisheksinghcodebase/CalciDon/raw/main/CalciDon.apk)

**CalciDon** is a modern, feature-packed, and beautifully designed Android calculation suite built with **Jetpack Compose** and **Material 3**. Engineered for performance, clarity, and precision, CalciDon blends a high-precision scientific calculator, an interactive formula reference & quadratic equation solver, an extensive multi-category unit converter, and persistent calculation history with favorites.

---

## 🌟 Key Features

### 1. 🖩 Advanced Scientific Calculator
- **Standard & Advanced Math**: High-precision arithmetic, exponents ($x^y$), square roots ($\sqrt{x}$), cube roots ($\sqrt[3]{x}$), factorials ($n!$), percentages, and modulus.
- **Trigonometric & Hyperbolic Functions**: Full support for `sin`, `cos`, `tan`, inverse (`asin`, `acos`, `atan`), and hyperbolic (`sinh`, `cosh`, `tanh`).
- **Angle Modes**: Instant one-tap toggle between **Degrees (DEG)** and **Radians (RAD)**.
- **Mathematical Constants**: Built-in constants including $\pi$ (Pi), $e$ (Euler's Number), $\phi$ (Golden Ratio), and $c$ (Speed of Light).
- **Smart Expression Parsing**: Tokenized recursive-descent engine supporting implicit multiplication (e.g., `2π`, `5(4+1)`, `3sin(90)`).
- **Real-Time Result Preview**: Live calculation preview appears dynamically as you type.
- **Memory Operations**: Instant access to `MC`, `MR`, `M+`, `M-`.

### 2. 📐 Formulas, Constants & Quadratic Solver
- **Physics & Mathematical Constants**: Fast reference for Universal Gravitation ($G$), Planck’s Constant ($h$), Speed of Light ($c$), Electron Charge ($e$), and more.
- **Formula Library**: Categorized geometry, physics, and algebraic formulas with mathematical explanations.
- **Quadratic Equation Solver**: Interactive solver for quadratic equations ($ax^2 + bx + c = 0$) providing both real and complex roots with step-by-step discriminant analysis.

### 3. 🔄 Comprehensive Unit Converter
Instant bi-directional conversions across essential measurement units:
- **Length**: Kilometers, Meters, Centimeters, Millimeters, Miles, Yards, Feet, Inches.
- **Weight & Mass**: Kilograms, Grams, Milligrams, Pounds, Ounces, Metric Tonnes.
- **Temperature**: Celsius, Fahrenheit, Kelvin.
- **Area**: Square Meters, Square Kilometers, Square Feet, Acres, Hectares.
- **Volume**: Liters, Milliliters, Gallons, Cubic Meters.
- **Speed**: m/s, km/h, mph, Knots.
- **Digital Storage**: Bits, Bytes, KB, MB, GB, TB.
- **Time**: Seconds, Minutes, Hours, Days, Weeks.

### 4. 📜 Calculation History & Favorites
- **Room DB Offline Persistence**: Every evaluated expression is automatically saved locally.
- **Favorites**: Star and bookmark frequent or critical calculations.
- **Search & Filter**: Real-time search across expressions and results.
- **Recall & Reuse**: Tap any historical item to load the expression or result directly back onto the calculator display.
- **Clear All**: One-tap history management with confirmation safety.

### 5. 🎨 Aesthetic Material 3 Theming
- **Dynamic & Curated Themes**: Seamlessly switch between System Default, Dark/AMOLED Mode, and vibrant custom color palettes.
- **Fluid Micro-Animations**: Smooth keypad ripple effects, tactile vibrations, and responsive typography.

---

## 🛠️ Architecture & Tech Stack

```
CalciDon/
├── app/src/main/java/com/example/
│   ├── MainActivity.kt                # Main activity entry point & navigation scaffold
│   ├── data/                          # Data Layer (Room DB, DAOs, Entities, Repository)
│   │   ├── AppDatabase.kt
│   │   ├── CalculationDao.kt
│   │   ├── CalculationEntity.kt
│   │   └── HistoryRepository.kt
│   ├── engine/                        # Calculation & Parsing Engines
│   │   ├── ExpressionEvaluator.kt     # Custom AST Tokenizer & Recursive Descent Parser
│   │   ├── FormulaReference.kt        # Physical constants & scientific formulas
│   │   └── UnitConverterEngine.kt     # Multi-category unit conversion formulas
│   ├── ui/                            # Presentation Layer
│   │   ├── CalculatorViewModel.kt     # Unidirectional StateFlow ViewModel
│   │   ├── components/                # Modular Compose components (Display, Keypad)
│   │   ├── screens/                   # App screens (Calculator, UnitConverter, Formulas, History)
│   │   └── theme/                     # Material 3 Color schemes, Typography & Theming
```

- **Language**: [Kotlin 2.2.10](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with [Material Design 3](https://m3.material.io/)
- **Architecture**: MVVM (Model-View-ViewModel) + Clean Architecture + StateFlow
- **Database**: [Room 2.7.0](https://developer.android.com/training/data-storage/room) with Kotlin Symbol Processing (KSP)
- **Concurrency**: Kotlin Coroutines & Flow
- **Build System**: Android Gradle Plugin (AGP 9.1.1) + Gradle 9.3.1 + Java 17

---

## 📥 Download & Installation

### 📱 Option 1: Direct APK Download (Recommended for Mobile)

You can download and install the pre-compiled Android APK directly from GitHub on your phone without needing Android Studio or Gradle:

<div align="center">

[![Download CalciDon APK](https://img.shields.io/badge/⬇️%20Download-CalciDon.apk-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://github.com/abhisheksinghcodebase/CalciDon/raw/main/CalciDon.apk)

👉 **[Direct Download Link (CalciDon.apk)](https://github.com/abhisheksinghcodebase/CalciDon/raw/main/CalciDon.apk)** &bull; **[Browse in Repository](CalciDon.apk)**

</div>

#### 📋 APK Specification
| Property | Value |
| :--- | :--- |
| **Package File** | [`CalciDon.apk`](CalciDon.apk) |
| **Direct Download** | [Click to Download APK](https://github.com/abhisheksinghcodebase/CalciDon/raw/main/CalciDon.apk) |
| **Minimum OS** | Android 7.0 (API Level 24 - Nougat) |
| **Target OS** | Android 16 (API Level 36) |
| **Package Name** | `com.aistudio.calcidon.xklpqr` |
| **Architecture** | Universal (ARM64, ARMv7, x86, x86_64) |

#### 📲 Step-by-Step Android Installation Guide
1. **Download**: Tap the [Download CalciDon.apk](https://github.com/abhisheksinghcodebase/CalciDon/raw/main/CalciDon.apk) button or link above on your Android device.
2. **Open Downloads**: Once downloaded, tap the completion notification or open your **Files** / **Downloads** app and locate `CalciDon.apk`.
3. **Allow Installation from Unknown Sources** *(if prompted)*:
   - When prompted that your device does not allow installing unknown apps from this source, tap **Settings**.
   - Enable **"Allow from this source"** for your browser or file manager.
4. **Install & Launch**:
   - Tap **Install** and wait for the installation to finish.
   - Tap **Open** to start using **CalciDon**!

---

### 💻 Option 2: Install via ADB (For Developers)

With your Android device or emulator connected and **USB Debugging** enabled:

```powershell
# Install the APK to connected device
adb install -r CalciDon.apk

# Launch the app directly
adb shell am start -n com.aistudio.calcidon.xklpqr/com.example.MainActivity
```

---

### 🛠️ Option 3: Build from Source

#### Prerequisites
- **JDK 17+** (e.g. Microsoft OpenJDK 17 or Eclipse Adoptium Temurin 17)
- **Android SDK** (API 24 to API 36)
- **Android Studio** (Ladybug / Meerkat or later) or CLI tools

#### Build Steps
1. **Clone the repository**:
   ```bash
   git clone https://github.com/abhisheksinghcodebase/CalciDon.git
   cd CalciDon
   ```

2. **Build Debug APK**:
   ```powershell
   ./gradlew.bat assembleDebug
   ```

3. **Locate Generated APK**:
   The compiled APK is placed at:
   - Root directory: `CalciDon.apk`
   - Build output: `app/build/outputs/apk/debug/app-debug.apk`

---

## 👨‍💻 Author & Connect

**Abhishek Kumar (Abhishek Singh)**  
*Full-Stack Developer & AI Engineer*

- **GitHub**: [@abhisheksinghcodebase](https://github.com/abhisheksinghcodebase)
- **LinkedIn**: [@abhisheksinghcode](https://www.linkedin.com/in/abhisheksinghcode/)
- **Repository**: [https://github.com/abhisheksinghcodebase/CalciDon](https://github.com/abhisheksinghcodebase/CalciDon)

---

## 📄 License
This project is licensed under the [Apache License 2.0](LICENSE).
