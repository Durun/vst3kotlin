#include "cppinterface/memory.h"
#include "cppinterface/IComponentHandler.h"
#include "cppinterface/IParameterChanges.h"

SIComponentHandler *SIComponentHandler_alloc() {
    return alloc<SIComponentHandler, IComponentHandlerVTable>();
}

SIComponentHandler2 *SIComponentHandler2_alloc() {
    return alloc<SIComponentHandler2, IComponentHandler2VTable>();
}

void SIComponentHandler_free(SIComponentHandler *ptr) {
    Struct_free(ptr);
}

void SIComponentHandler2_free(SIComponentHandler2 *ptr) {
    Struct_free(ptr);
}

SIParameterChanges *SIParameterChanges_alloc() {
    auto ptr = alloc<SIParameterChanges>();
    auto params = allocArray<SIParamValueQueue>(MaxParamValueQueue);
    ptr->_params = params;
    if (ptr->_params == nullptr) { exit(1); }

    for (int i = 0; i < MaxParamValueQueue; ++i) {
        params[i]._sampleOffset = allocArray<int32>(MaxPointPerFrame);
        params[i]._value = allocArray<ParamValue>(MaxPointPerFrame);
        if (params[i]._sampleOffset == nullptr) { exit(1); }
        if (params[i]._value == nullptr) { exit(1); }
    }
    return ptr;
}

void SIParamValueQueue_free(SIParamValueQueue *ptr) {
    free(ptr->_sampleOffset);
    free(ptr->_value);
    free(ptr);
}

void SIParameterChanges_free(SIParameterChanges *ptr) {
    SIParamValueQueue_free(reinterpret_cast<SIParamValueQueue *>(ptr->_params));
    free(ptr);
}