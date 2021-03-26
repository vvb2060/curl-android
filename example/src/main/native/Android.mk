LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE           := vvb2060
LOCAL_SRC_FILES        := http3.c
LOCAL_STATIC_LIBRARIES := curl_static
include $(LOCAL_PATH)/build-executable.mk

$(call import-module,prefab/curl)
