#include "cast.h"

// [IMessage] member functions
FIDString PLUGIN_API IMessage_getMessageID(IMessage* this_ptr) {
	return cast(this_ptr)->getMessageID();
}
void PLUGIN_API IMessage_setMessageID(IMessage* this_ptr, FIDString id){
	return cast(this_ptr)->setMessageID(id);
}
IAttributeList* PLUGIN_API IMessage_getAttributes(IMessage* this_ptr) {
	auto ret = cast(this_ptr)->getAttributes();
	return cast(ret);
}
