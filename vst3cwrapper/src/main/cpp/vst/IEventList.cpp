#include "cast.h"

// [IEventList] member functions
int32 PLUGIN_API IEventList_getEventCount(IEventList* this_ptr) {
    return cast(this_ptr)->getEventCount();
}
tresult PLUGIN_API IEventList_getEvent(IEventList* this_ptr, int32 index, Event* e) {
    return cast(this_ptr)->getEvent(index, *cast(e));
}
tresult PLUGIN_API IEventList_addEvent(IEventList* this_ptr, Event* e) {
	return cast(this_ptr)->addEvent(*cast(e));
}
