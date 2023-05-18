APP_CFLAGS     := -Wall -Wextra
APP_CFLAGS     += -Wno-builtin-macro-redefined -D__FILE__=__FILE_NAME__
APP_CONLYFLAGS := -std=c2x
APP_STL        := none

ifneq ($(NDK_DEBUG),1)
APP_CFLAGS     += -flto -Werror -Wno-error=constant-logical-operand
APP_LDFLAGS    += -flto
endif
