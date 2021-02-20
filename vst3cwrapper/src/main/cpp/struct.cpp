#include "struct.h"

#include <cstdio>
#include <cstdlib>

SIComponentHandler* SIComponentHandler_alloc() {
    auto ptr = reinterpret_cast<SIComponentHandler*>(malloc(sizeof(SIComponentHandler)));
    if (ptr == nullptr) {
        fprintf(stderr, "Failed to allocate SIComponentHandler.");
        return nullptr;
    }

    ptr->vtable = reinterpret_cast<IComponentHandlerVTable*>(malloc(sizeof(IComponentHandlerVTable)));
    if (ptr->vtable == nullptr) {
        fprintf(stderr, "Failed to allocate IComponentHandlerVTable.");
        return nullptr;
    }

    return ptr;
}

void SIComponentHandler_free(SIComponentHandler* ptr) {
	free(ptr->vtable);
	free(ptr);
}

SIComponentHandler2* SIComponentHandler2_alloc() {
    auto ptr = reinterpret_cast<SIComponentHandler2*>(malloc(sizeof(SIComponentHandler2)));
    if (ptr == nullptr) {
        fprintf(stderr, "Failed to allocate SIComponentHandler2.");
        return nullptr;
    }

    ptr->vtable = reinterpret_cast<IComponentHandler2VTable*>(malloc(sizeof(IComponentHandler2VTable)));
    if (ptr->vtable == nullptr) {
        fprintf(stderr, "Failed to allocate IComponentHandler2VTable.");
        return nullptr;
    }

    return ptr;
}

void SIComponentHandler2_free(SIComponentHandler2* ptr) {
    free(ptr->vtable);
    free(ptr);
}
