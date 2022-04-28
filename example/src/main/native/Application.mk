APP_CFLAGS     := -Wall -Wextra -Werror
APP_CFLAGS     += -fvisibility=hidden -fvisibility-inlines-hidden
APP_CONLYFLAGS := -std=c18
APP_LDFLAGS    := -Wl,-exclude-libs,ALL
APP_STL        := none
