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

SIParameterChanges *SIParameterChanges_alloc(int32 maxParams, int32 maxPoints) {
    auto ptr = alloc<SIParameterChanges>();
    ptr->_params = allocArray<SIParamValueQueue>(maxParams);
    ptr->maxParams = maxParams;
    if (ptr->_params == nullptr) { exit(1); }

    for (int i = 0; i < maxParams; ++i) {
        ptr->_params[i].maxPoints = maxPoints;
        ptr->_params[i]._sampleOffset = allocArray<int32>(maxPoints);
        ptr->_params[i]._value = allocArray<ParamValue>(maxPoints);
        if (ptr->_params[i]._sampleOffset == nullptr) { exit(1); }
        if (ptr->_params[i]._value == nullptr) { exit(1); }
    }
    return ptr;
}

void SIParameterChanges_free(SIParameterChanges *ptr) {
    for (int i = 0; i < ptr->maxParams; ++i) {
        free(ptr->_params[i]._sampleOffset);
        free(ptr->_params[i]._value);
    }
    free(ptr->_params);
    free(ptr);
}