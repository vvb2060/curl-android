LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)
include $(LOCAL_PATH)/curl/lib/Makefile.inc
LOCAL_MODULE            := curl_static
LOCAL_SRC_FILES         := $(addprefix curl/lib/,$(CSOURCES))
LOCAL_C_INCLUDES        := $(LOCAL_PATH)/curl/include $(LOCAL_PATH)/curl/lib
ifeq ($(TARGET_ARCH_ABI),armeabi-v7a)
    LOCAL_C_INCLUDES += $(LOCAL_PATH)/config32
else ifeq ($(TARGET_ARCH_ABI),arm64-v8a)
    LOCAL_C_INCLUDES += $(LOCAL_PATH)/config64
else ifeq ($(TARGET_ARCH_ABI),x86)
    LOCAL_C_INCLUDES += $(LOCAL_PATH)/config32
else ifeq ($(TARGET_ARCH_ABI),x86_64)
    LOCAL_C_INCLUDES += $(LOCAL_PATH)/config64
endif
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/curl/include
LOCAL_CFLAGS            := -DHAVE_CONFIG_H -DBUILDING_LIBCURL
LOCAL_STATIC_LIBRARIES  := ssl_static
# https://gist.github.com/vvb2060/56d5b8fda2553f36938b2b72b1390114/f8bb9882cbff921ba0dc643e5d15beb93b87700e
STATIC_LIBRARY_STRIP    := true
include $(BUILD_STATIC_LIBRARY)

$(call import-module,prefab/boringssl)
