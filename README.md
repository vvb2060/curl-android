# curl Android

curl tool and libcurl static library prefab for android.

Supports TLS, powered by [BoringSSL](https://github.com/vvb2060/BoringSSL_Android).

Supports HTTP/2, powered by [nghttp2](https://github.com/nghttp2/nghttp2).

Supports HTTP/3, powered by BoringSSL backend [ngtcp2](https://github.com/ngtcp2/ngtcp2) and [nghttp3](https://github.com/ngtcp2/nghttp3).

By default, use system built-in CA certificate store, and use system built-in DNS.


If you only want to use curl tool, unzip apk, extract `libcurl.so` and rename it to `curl`.

## Integration

Gradle:

```gradle
implementation 'io.github.vvb2060.ndk:curl:8.3.0'
```

This library is [Prefab](https://google.github.io/prefab/), so you will need to enable it in your project (Android Gradle Plugin 4.1+):

```gradle
android {
    ...
    buildFeatures {
        ...
        prefab = true
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

## Version

### 8.3.0
- curl 8.3.0
- nghttp2 1.56.0
- nghttp3 0.15.0
- ngtcp2 0.19.1

### 8.2.1
- curl 8.2.1
- nghttp2 1.55.1
- nghttp3 0.14.0
- ngtcp2 0.18.0

### 8.1.1
- curl 8.1.1
- nghttp2 1.53.0
- nghttp3 0.11.0
- ngtcp2 0.15.0

### 8.1.0
- curl 8.1.0
- nghttp2 1.53.0
- nghttp3 0.11.0
- ngtcp2 0.15.0

### 8.0.1
- curl 8.0.1
- nghttp2 1.52.0
- nghttp3 0.9.0
- ngtcp2 0.13.1

### 7.88.1
- curl 7.88.1
- nghttp2 1.52.0
- nghttp3 0.9.0
- ngtcp2 0.13.1

### 7.87.0
- curl 7.87.0
- nghttp2 1.51.0
- nghttp3 0.8.0
- ngtcp2 0.11.0

### 7.86.0
- curl 7.86.0
- nghttp2 1.50.0
- nghttp3 0.7.1
- ngtcp2 0.10.0

### 7.85.1
- curl 7.85.0
- nghttp2 1.49.0
- nghttp3 0.7.0
- ngtcp2 0.8.0

### 7.85.0
**aar file upload failed, please use 7.85.1**
- curl 7.85.0
- nghttp2 1.49.0
- nghttp3 0.7.0
- ngtcp2 0.8.0

### 7.84.0
- curl 7.84.0 with [my patch](https://github.com/curl/curl/pull/9056)
- nghttp2 1.48.0
- nghttp3 0.5.0
- ngtcp2 0.6.0

### 7.83.0-h3
- curl 7.83.0 with [my patch](https://github.com/curl/curl/pull/8789)
- nghttp2 1.47.0
- nghttp3 0.4.0
- ngtcp2 0.4.0

### 7.83.0-h2
- curl 7.83.0
- nghttp2 1.47.0

### 7.83.0
- curl 7.83.0

### 7.75.0
- curl 7.75.0
