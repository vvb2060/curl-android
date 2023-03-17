APP_CFLAGS     := -Wall -Wextra -Wmost
APP_CFLAGS     += -Wno-builtin-macro-redefined -D__FILE__=__FILE_NAME__
APP_CONLYFLAGS := -std=c2x
APP_STL        := none

ifneq ($(NDK_DEBUG),1)
APP_CFLAGS     += -flto -Werror
APP_CFLAGS     += -fvisibility=hidden -fvisibility-inlines-hidden
APP_LDFLAGS    += -flto -Wl,--exclude-libs,ALL
endif
