LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE           := vvb2060
LOCAL_SRC_FILES        := http.c
LOCAL_STATIC_LIBRARIES := curl_static
LOCAL_LDFLAGS          := -fPIE
include $(LOCAL_PATH)/build-executable.mk

#include $(LOCAL_PATH)/../../../../curl/src/main/native/Android.mk
$(call import-module,prefab/curl)
