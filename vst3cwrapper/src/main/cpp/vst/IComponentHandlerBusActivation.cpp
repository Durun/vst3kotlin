#include "cast.h"
// [IComponentHandlerBusActivation] member functions
tresult PLUGIN_API IComponentHandlerBusActivation_requestBusActivation(IComponentHandlerBusActivation* this_ptr, MediaType type, BusDirection dir, int32 index, TBool state) {
    return cast(this_ptr)->requestBusActivation(type, dir, index, state);
}
