LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)
include $(LOCAL_PATH)/curl/lib/Makefile.inc
LOCAL_MODULE            := curl_static
LOCAL_SRC_FILES         := $(addprefix curl/lib/,$(CSOURCES))
LOCAL_C_INCLUDES        := $(LOCAL_PATH)/curl/include $(LOCAL_PATH)/curl/lib $(LOCAL_PATH)/include
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/curl/include
LOCAL_CFLAGS            := -DHAVE_CONFIG_H -DBUILDING_LIBCURL
LOCAL_STATIC_LIBRARIES  := ssl_static
# https://gist.github.com/vvb2060/56d5b8fda2553f36938b2b72b1390114
LOCAL_STATIC_OBJCOPY    := true
LOCAL_OBJCOPY_MODE      := --strip-unneeded -R .comment
include $(BUILD_STATIC_LIBRARY)

$(call import-module,prefab/boringssl)
