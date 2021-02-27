#include <cstdio>
#include <cassert>

#include "cppinterface/IParameterChanges.h"
#include <pluginterfaces/vst/ivstparameterchanges.h>

int test_IParameterChanges() {
    fprintf(stderr, "--test_IParameterChanges--\n");
    auto sParams = SIParameterChanges_alloc(8, 8);

    fprintf(stderr, "init IParameterChanges...\n");
    SIParameterChanges_init(sParams);

    auto params = reinterpret_cast<Steinberg::Vst::IParameterChanges *>(sParams);

    int32 qIndex;
    const uint32 paramID = 1;

    fprintf(stderr, "addParameterData...\n");
    Steinberg::Vst::IParamValueQueue* queue = params->addParameterData(paramID, qIndex);
    fprintf(stderr, "paramID=%lu, qIndex=%ld\n", paramID, qIndex);
    fprintf(stderr, "getParameterId(): %lu, getPointCount(): %ld\n",
            queue->getParameterId(), queue->getPointCount());

    assert(params->getParameterCount() == 1);
    assert(params->getParameterData(0) == queue);
    assert(queue->getParameterId() == paramID);
    assert(queue->getPointCount() == 0);


    fprintf(stderr, "addParameterData...\n");
    queue = params->addParameterData(paramID, qIndex);
    fprintf(stderr, "paramID=%lu, qIndex=%ld\n", paramID, qIndex);
    fprintf(stderr, "getParameterId(): %lu, getPointCount(): %ld\n",
            queue->getParameterId(), queue->getPointCount());

    assert(params->getParameterCount() == 2);
    assert(params->getParameterData(1) == queue);
    assert(queue->getParameterId() == paramID);
    assert(queue->getPointCount() == 0);

    int32 index;
    const int32 offset = 1;
    const ParamValue value = 0.5;
    fprintf(stderr, "addPoint...\n");
    queue->addPoint(offset, value, index);

    int32 outOffset;
    ParamValue outValue;
    fprintf(stderr, "getPoint...\n");
    queue->getPoint(index, outOffset, outValue);
    fprintf(stderr, "offset: %lu, value: %f\n",
            outOffset, outValue);
    assert(outOffset == offset);
    assert(outValue == value);
    params->release();
    return 0;
}

