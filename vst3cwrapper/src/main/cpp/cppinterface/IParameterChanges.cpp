#include "cppinterface/IParameterChanges.h"
#include "base/result.h"

/*
// for debug
static void show(SIParamValueQueue *this_ptr) {
    fprintf(stderr, "  [SIParamValueQueue (%p)]\n", this_ptr);
    fprintf(stderr, "  vtable: %p\n", this_ptr->vtable);
    fprintf(stderr, "  refCount: %ld\n", this_ptr->refCount);
    fprintf(stderr, "  _pointCount: %ld\n", this_ptr->_pointCount);
    fprintf(stderr, "  _id: %ld\n", this_ptr->_id);
    fprintf(stderr, "  _sampleOffset: %p\n", this_ptr->_sampleOffset);
    fprintf(stderr, "  _value: %p\n", this_ptr->_value);
}

static void show(SIParameterChanges *this_ptr) {
    fprintf(stderr, "[SIParameterChanges (%p)]\n", this_ptr);
    fprintf(stderr, "vtable: %p\n", this_ptr->vtable);
    fprintf(stderr, "refCount: %ld\n", this_ptr->refCount);
    fprintf(stderr, "_paramCount: %ld\n", this_ptr->_paramCount);
    fprintf(stderr, "_params: %p\n", this_ptr->_params);
    for (int i = 0; i < MaxParamValueQueue; ++i) {
        show(&(this_ptr->_params[i]));
    }
}
*/

static SIParamValueQueue *cast(IParamValueQueue *this_ptr) {
    return reinterpret_cast<SIParamValueQueue *>(this_ptr);
}

static SIParameterChanges *cast(IParameterChanges *this_ptr) {
    return reinterpret_cast<SIParameterChanges *>(this_ptr);
}
static IParamValueQueue *cast(SIParamValueQueue *this_ptr) {
    return reinterpret_cast<IParamValueQueue *>(this_ptr);
}

static IParameterChanges *cast(SIParameterChanges *this_ptr) {
    return reinterpret_cast<IParameterChanges *>(this_ptr);
}

ParamID SIParamValueQueue_getParameterId(IParamValueQueue *this_ptr) {
    return cast(this_ptr)->_id;
}

int32 SIParamValueQueue_getPointCount(IParamValueQueue *this_ptr) {
    return cast(this_ptr)->_pointCount;
}

tresult SIParamValueQueue_getPoint(
        IParamValueQueue *this_ptr, int32 index, int32 *sampleOffset/*out*/, ParamValue *value/*out*/) {
    auto ptr = cast(this_ptr);
    if (index < 0 || ptr->_pointCount - 1 < index) return kInvalidArgument;
    *sampleOffset = ptr->_sampleOffset[index];
    *value = ptr->_value[index];
    return kResultTrue;
}

tresult SIParamValueQueue_addPoint(
        IParamValueQueue *this_ptr, int32 sampleOffset, ParamValue value, int32 *index/*out*/) {
    SIParamValueQueue *ptr = cast(this_ptr);
    if (MaxPointPerFrame <= ptr->_pointCount) return kOutOfMemory;
    int32 newIndex = ptr->_pointCount;
    *index = newIndex;
    ptr->_sampleOffset[newIndex] = sampleOffset;
    ptr->_value[newIndex] = value;
    ptr->_pointCount++;
    return kResultTrue;
}

int32 SIParameterChanges_getParameterCount(IParameterChanges *this_ptr) {
    auto ptr = cast(this_ptr);
    return ptr->_paramCount;
}

IParamValueQueue *SIParameterChanges_getParameterData(IParameterChanges *this_ptr, int32 index) {
    auto ptr = cast(this_ptr);
    //if (index < 0 || ptr->_paramCount - 1 < index) return kInvalidArgument;
    return cast(&(ptr->_params[index]));
}

IParamValueQueue *SIParameterChanges_addParameterData(
        IParameterChanges *this_ptr, const ParamID *id, int32 *index/*out*/) {
    auto ptr = cast(this_ptr);
    if (MaxParamValueQueue <= ptr->_paramCount) return reinterpret_cast<IParamValueQueue *>(kOutOfMemory);

    auto newIndex = ptr->_paramCount;
    *index = newIndex;
    ptr->_paramCount++;
    SIParamValueQueue *paramPtr = &(ptr->_params[newIndex]);
    SIParamValueQueue_init(paramPtr);
    paramPtr->_id = *id;
    return reinterpret_cast<IParamValueQueue *>(paramPtr);
}

void SIParamValueQueue_init(SIParamValueQueue *ptr) {
    ptr->vtable = &IParamValueQueueVTable_def;
    ptr->refCount = 1;
    ptr->_pointCount = 0;
}

void SIParameterChanges_init(SIParameterChanges *ptr) {
    ptr->vtable = &IParameterChangesVTable_def;
    ptr->refCount = 1;
    ptr->_paramCount = 0;
    for (int i = 0; i < MaxParamValueQueue; ++i) {
        SIParamValueQueue_init(reinterpret_cast<SIParamValueQueue *>(&(ptr->_params[i])));
    }
}


uint32 SIParamValueQueue_addRef(void *this_ptr) {
    auto ptr = reinterpret_cast<SIParamValueQueue *>(this_ptr);
    return ptr->refCount++;
}

uint32 SIParamValueQueue_release(void *this_ptr) {
    auto ptr = reinterpret_cast<SIParamValueQueue *>(this_ptr);
    ptr->refCount--;
    if (ptr->refCount <= 0) {
        //SIParamValueQueue_free(ptr);
        return 0;
    } else {
        return ptr->refCount;
    }
}

uint32 SIParameterChanges_addRef(void *this_ptr) {
    auto ptr = reinterpret_cast<SIParameterChanges *>(this_ptr);
    return ptr->refCount++;
}

uint32 SIParameterChanges_release(void *this_ptr) {
    auto ptr = reinterpret_cast<SIParameterChanges *>(this_ptr);
    ptr->refCount--;
    if (ptr->refCount <= 0) {
        SIParameterChanges_free(ptr);
        return 0;
    } else {
        return ptr->refCount;
    }
}