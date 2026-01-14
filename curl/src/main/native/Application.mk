APP_CFLAGS     := -Wall -Wextra
APP_CFLAGS     += -Werror -Wno-error=constant-logical-operand
APP_CFLAGS     += -Wno-builtin-macro-redefined -D__FILE__=__FILE_NAME__
APP_CONLYFLAGS := -std=c2x
APP_LDFLAGS    := -Wl,--icf=all
APP_STL        := c++_static

ifeq ($(enableLTO),1)
APP_CFLAGS     += -flto
APP_LDFLAGS    += -flto
endif
