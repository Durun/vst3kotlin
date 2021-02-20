#include "cppinterface/memory.h"
#include "cppinterface/IComponentHandler.h"

SIComponentHandler* SIComponentHandler_alloc() {
    return alloc<SIComponentHandler, IComponentHandlerVTable>();
}
SIComponentHandler2* SIComponentHandler2_alloc() {
    return alloc<SIComponentHandler2, IComponentHandler2VTable>();
}
void SIComponentHandler_free(SIComponentHandler* ptr) {
    Struct_free(ptr);
}
void SIComponentHandler2_free(SIComponentHandler2* ptr) {
    Struct_free(ptr);
}
