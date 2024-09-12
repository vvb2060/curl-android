#include <malloc.h>
#include <stddef.h>

extern "C++" {

namespace std {
using ::size_t;
}

}

void* operator new(std::size_t size) {
    return malloc(size);
}

void* operator new[](std::size_t size) {
    return malloc(size);
}

void operator delete(void* ptr) {
    free(ptr);
}

void operator delete[](void* ptr) {
    free(ptr);
}
