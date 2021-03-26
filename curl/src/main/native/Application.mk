APP_CFLAGS     := -Wall -Wextra -Wshadow -Werror
APP_CFLAGS     += -fvisibility=hidden -fvisibility-inlines-hidden -fno-stack-protector
APP_CFLAGS     += -Wno-builtin-macro-redefined -D__FILE__=__FILE_NAME__
APP_CONLYFLAGS := -std=c18
APP_LDFLAGS    := -Wl,-exclude-libs,ALL -Wl,--gc-sections
APP_STL        := none
