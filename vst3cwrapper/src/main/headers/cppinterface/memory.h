#pragma once

#include <cstdio>
#include <cstdlib>

template <typename S, typename V>
S* alloc() {
    auto ptr = reinterpret_cast<S*>(malloc(sizeof(S)));
    if (ptr == nullptr) {
        fprintf(stderr, "Failed to allocate Struct.");
        return nullptr;
    }

    ptr->vtable = reinterpret_cast<V*>(malloc(sizeof(V)));
    if (ptr->vtable == nullptr) {
        fprintf(stderr, "Failed to allocate vtable.");
        return nullptr;
    }

    return ptr;
}

template <typename S>
void Struct_free(S* ptr) {
    free(ptr->vtable);
    free(ptr);
}
