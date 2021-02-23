#include "cast.h"

tresult FUnknown_queryInterface(FUnknown *this_ptr, TUID _iid, void **obj) {
    return cast(this_ptr)->queryInterface(_iid, obj);
}

uint32 FUnknown_addRef(FUnknown *this_ptr) {
    return cast(this_ptr)->addRef();
}

uint32 FUnknown_release(FUnknown *this_ptr) {
    return cast(this_ptr)->release();
}
