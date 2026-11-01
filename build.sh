#!/usr/bin/env bash
# ==============================================================================
# KeyOS - Build & Patch Automation Script
# Maintainer: KeyOS (@KeyOSdev)
# Repository: https://github.com/KeyOSdev/PixelBoard
# ==============================================================================

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

REBUILD_PATCH=false
INPUT_APK="input/gboard.apk"
for arg in "$@"; do
    case "$arg" in
        -r|--rebuild|--compile|-c)
            REBUILD_PATCH=true
            ;;
        -h|--help)
            echo "Usage: ./build.sh [options] [input_apk]"
            echo "Options:"
            echo "  -r, --rebuild    Recompile patch bundle from keyos-patches/ using Gradle"
            echo "  -h, --help       Show this help message"
            echo ""
            echo "By default, ./build.sh runs 100% offline using the pre-bundled patches/KeyOS.mpp."
            exit 0
            ;;
        *)
            if [[ -f "$arg" ]]; then
                INPUT_APK="$arg"
            fi
            ;;
    esac
done

PATCH_VERSION=$(grep -E "^version\s*=" keyos-patches/gradle.properties | cut -d'=' -f2 | tr -d ' ')
MPP_PATH="patches/KeyOS.mpp"

if [[ "$REBUILD_PATCH" == true ]] || [[ ! -f "$MPP_PATH" ]]; then
    echo "=== 🎹 Compiling PixelBoard Patch Bundle from Source ==="
    cd keyos-patches
    ./gradlew :patches:buildAndroid
    cd "$SCRIPT_DIR"

    COMPILED_MPP="keyos-patches/patches/build/libs/patches-${PATCH_VERSION}.mpp"
    if [[ ! -f "$COMPILED_MPP" ]]; then
        COMPILED_MPP=$(ls keyos-patches/patches/build/libs/patches-*.mpp 2>/dev/null | grep -v 'javadoc' | grep -v 'sources' | head -n 1)
    fi
    if [[ -z "${COMPILED_MPP:-}" ]] || [[ ! -f "$COMPILED_MPP" ]]; then
        echo "❌ Error: Patch bundle failed to build at keyos-patches/patches/build/libs/"
        exit 1
    fi
    mkdir -p patches
    cp "$COMPILED_MPP" "$MPP_PATH"
    if [[ -n "${PATCH_VERSION:-}" ]]; then
        cp "$COMPILED_MPP" "patches/patches-${PATCH_VERSION}.mpp"
    fi
    echo "✅ Patch bundle compiled & synced to: $MPP_PATH"
else
    echo "📦 Using local patch bundle: $MPP_PATH (100% offline)"
fi

# Check for Java 21 to run patcher.jar
JAVA_BIN=""
for candidate in \
    "${JAVA_HOME:-}/bin/java" \
    "/Applications/Android Studio.app/Contents/jbr/Contents/Home/bin/java" \
    "/opt/homebrew/opt/openjdk@21/bin/java" \
    /opt/homebrew/Cellar/openjdk@21/*/libexec/openjdk.jdk/Contents/Home/bin/java \
    "/opt/homebrew/opt/openjdk/bin/java" \
    "$(/usr/libexec/java_home -v 21 2>/dev/null)/bin/java" \
    "$(which java 2>/dev/null)"; do
    if [[ -x "$candidate" ]]; then
        candidate_ver=$("$candidate" -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
        if [[ "$candidate_ver" -ge 21 ]]; then
            JAVA_BIN="$candidate"
            break
        fi
    fi
done

if [[ -z "$JAVA_BIN" ]]; then
    echo "⚠️ Note: patcher.jar requires Java 21+."
    echo "   Please install Java 21 (e.g. 'brew install openjdk@21') or specify JAVA_HOME."
    exit 1
fi
echo "☕ Using Java 21+: $JAVA_BIN"

if [[ ! -f "$INPUT_APK" ]]; then
    echo "❌ Error: Place a stock Gboard APK at $INPUT_APK or pass its path as an argument."
    exit 1
fi

echo "=== 🔨 Applying Patches to $INPUT_APK ==="
mkdir -p output
mkdir -p tools/patcher-data

KEYSTORE_PATH="tools/patcher-data/keyos.keystore"
if [[ ! -f "$KEYSTORE_PATH" ]]; then
    echo "🔑 Generating KeyOS release keystore..."
    keytool -genkeypair \
        -keystore "$KEYSTORE_PATH" \
        -alias keyos \
        -keyalg RSA \
        -keysize 2048 \
        -validity 10000 \
        -storepass keyoskey \
        -keypass keyoskey \
        -dname "CN=KeyOS, OU=KeyOS, O=KeyOS, L=Unknown, ST=Unknown, C=US" \
        >/dev/null 2>&1
fi

"$JAVA_BIN" -jar tools/patcher.jar patch \
    --patches="$MPP_PATH" \
    --options-file=options.json \
    --striplibs=arm64-v8a \
    --signer="KeyOS" \
    --keystore="$KEYSTORE_PATH" \
    --keystore-password=keyoskey \
    --keystore-entry-alias=keyos \
    --keystore-entry-password=keyoskey \
    --temporary-files-path="tools/patcher-data/tmp" \
    -o=output/KeyOS.apk \
    -r=output/patching-result.json \
    "$INPUT_APK"

if [[ ! -s "output/KeyOS.apk" ]]; then
    echo "❌ Error: APK patching failed; no output/KeyOS.apk was produced." >&2
    exit 1
fi

if [[ -f "output/patching-result.json" ]]; then
    GBOARD_VER=$(grep -o '"packageVersion":"[^"]*"' output/patching-result.json | cut -d'"' -f4 | cut -d'-' -f1)
    GBOARD_SHORT_VER=$(echo "$GBOARD_VER" | cut -d'.' -f1-3)
    if [[ -n "$GBOARD_VER" ]]; then
        cp output/KeyOS.apk "output/KeyOS-${GBOARD_VER}.apk"
        echo "📦 Versioned APK saved to: output/KeyOS-${GBOARD_VER}.apk"
    fi
    if [[ -n "$GBOARD_SHORT_VER" ]] && [[ "$GBOARD_SHORT_VER" != "$GBOARD_VER" ]]; then
        cp output/KeyOS.apk "output/KeyOS-${GBOARD_SHORT_VER}.apk"
        echo "📦 Short-versioned APK saved to: output/KeyOS-${GBOARD_SHORT_VER}.apk"
    fi
fi
if [[ -n "${PATCH_VERSION:-}" ]]; then
    cp output/KeyOS.apk "output/KeyOS-${PATCH_VERSION}.apk"
fi

echo "🎉 Successfully built output/KeyOS.apk!"
