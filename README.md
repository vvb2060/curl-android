# libcurl Android

curl static library prefab for android.

Supports SSL, powered by [BoringSSL](https://github.com/vvb2060/BoringSSL_Android).
Supports HTTP/2. powered by [nghttp2](https://github.com/nghttp2/nghttp2).
For HTTP/3 support, wait for curl support for BoringSSL-based [ngtcp2](https://github.com/ngtcp2/ngtcp2).
No other protocols supported.
By default, use system built-in CA certificate store, and use system built-in DNS.

## Integration

Gradle:

```gradle
implementation 'io.github.vvb2060.ndk:curl:7.83.0-h2'
```

This library is [Prefab](https://google.github.io/prefab/), so you will need to enable it in your project (Android Gradle Plugin 4.1+):

```gradle
android {
    ...
    buildFeatures {
        ...
        prefab true
    }
}
```

## Usage

### ndk-build

you can use `curl_static` in your `Android.mk`. 
For example, if your application defines `libapp.so` and it uses `curl_static`, your `Android.mk` file should include the following:

```makefile
include $(CLEAR_VARS)
LOCAL_MODULE           := app
LOCAL_SRC_FILES        := app.cpp
LOCAL_STATIC_LIBRARIES := curl_static
include $(BUILD_SHARED_LIBRARY)

# If you don't need your project to build with NDKs older than r21, you can omit
# this block.
ifneq ($(call ndk-major-at-least,21),true)
    $(call import-add-path,$(NDK_GRADLE_INJECTED_IMPORT_PATH))
endif

$(call import-module,prefab/curl)
```

### CMake

you can use `curl_static` in your `CMakeLists.txt`. 
For example, if your application defines `libapp.so` and it uses `curl_static`, your `CMakeLists.txt` file should include the following:

```cmake
add_library(app SHARED app.cpp)

# Add these two lines.
find_package(curl REQUIRED CONFIG)
target_link_libraries(app curl::curl_static)
```
