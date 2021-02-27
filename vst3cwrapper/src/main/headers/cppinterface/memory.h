#pragma once

#include <cstdio>
#include <cstdlib>

template<typename T>
T *alloc() {
    auto ptr = reinterpret_cast<T *>(malloc(sizeof(T)));
    if (ptr == nullptr) {
        fprintf(stderr, "Failed to allocate T.\n");
        return nullptr;
    }
    return ptr;
}

template<typename T>
T *allocArray(int length) {
    auto ptr = reinterpret_cast<T *>(malloc(sizeof(T) * length));
    if (ptr == nullptr) {
        fprintf(stderr, "Failed to allocate array of T (length=%d).\n", length);
        return nullptr;
    }
    return ptr;
}

template<typename S, typename V>
S *alloc() {
    auto ptr = reinterpret_cast<S *>(malloc(sizeof(S)));
    if (ptr == nullptr) {
        fprintf(stderr, "Failed to allocate Struct.");
        return nullptr;
    }

    ptr->vtable = reinterpret_cast<V *>(malloc(sizeof(V)));
    if (ptr->vtable == nullptr) {
        fprintf(stderr, "Failed to allocate vtable.");
        return nullptr;
    }

    return ptr;
}

template<typename S>
void Struct_free(S *ptr) {
    free(ptr->vtable);
    free(ptr);
}
