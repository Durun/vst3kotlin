#include "cast.h"

// [IAttributeList] member functions
tresult PLUGIN_API IAttributeList_setInt(IAttributeList* this_ptr, AttrID id, int64 value) {
    return cast(this_ptr)->setInt(id, value);
}
tresult PLUGIN_API IAttributeList_getInt(IAttributeList* this_ptr, AttrID id, int64* value) {
    return cast(this_ptr)->getInt(id, *value);
}
tresult PLUGIN_API IAttributeList_setFloat(IAttributeList* this_ptr, AttrID id, double value) {
    return cast(this_ptr)->setFloat(id, value);
}
tresult PLUGIN_API IAttributeList_getFloat(IAttributeList* this_ptr, AttrID id, double* value) {
    return cast(this_ptr)->getFloat(id, *value);
}
tresult PLUGIN_API IAttributeList_setString(IAttributeList* this_ptr, AttrID id, const TChar* string) {
    return cast(this_ptr)->setString(id, string);
}
tresult PLUGIN_API IAttributeList_getString(IAttributeList* this_ptr, AttrID id, TChar* string, uint32 sizeInBytes) {
    return cast(this_ptr)->getString(id, string, sizeInBytes);
}
tresult PLUGIN_API IAttributeList_setBinary(IAttributeList* this_ptr, AttrID id, const void* data, uint32 sizeInBytes) {
    return cast(this_ptr)->setBinary(id, data, sizeInBytes);
}
tresult PLUGIN_API IAttributeList_getBinary(IAttributeList* this_ptr, AttrID id, const void** data, uint32* sizeInBytes) {
    return cast(this_ptr)->getBinary(id, *data, *sizeInBytes);
}
