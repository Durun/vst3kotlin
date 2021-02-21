#include "cast.h"

// [IConnectionPoint] member functions
tresult PLUGIN_API IConnectionPoint_connect(IConnectionPoint* this_ptr, IConnectionPoint* other) {
    return cast(this_ptr)->connect(cast(other));
}
tresult PLUGIN_API IConnectionPoint_disconnect(IConnectionPoint* this_ptr, IConnectionPoint* other) {
    return cast(this_ptr)->disconnect(cast(other));
}
tresult PLUGIN_API IConnectionPoint_notify(IConnectionPoint* this_ptr, IMessage* message) {
    return cast(this_ptr)->notify(cast(message));
}
