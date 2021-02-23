#include "cast.h"

// [IMidiMapping] member functions
tresult PLUGIN_API IMidiMapping_getMidiControllerAssignment(IMidiMapping* this_ptr, int32 busIndex, int16 channel, CtrlNumber midiControllerNumber, ParamID* id /*out*/) {
    return cast(this_ptr)->getMidiControllerAssignment(busIndex, channel, midiControllerNumber, *id);
}
