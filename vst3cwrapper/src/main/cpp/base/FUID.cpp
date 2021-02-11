#include "cast.h"

// [FUID] constructors
FUID *FUID_new_int(uint32 i1, uint32 i2, uint32 i3, uint32 i4) {
	auto ret = new Steinberg::FUID(i1, i2, i3, i4);
	return cast(ret);
}
// [FUID] member functions
bool FUID_generate(FUID *this_ptr) {
	return cast(this_ptr)->generate();
}
bool FUID_isValid(FUID *this_ptr) {
	return cast(this_ptr)->isValid();
}
FUID *FUID_set(FUID *this_ptr, const FUID *f) {
	const auto other = cast(f);
	Steinberg::FUID ret = cast(this_ptr)->operator=(*other);
	return reinterpret_cast<FUID *>(this_ptr);
}
bool FUID_equals(FUID *this_ptr, const FUID *f) {
	const auto other = cast(f);
	return cast(this_ptr)->operator==(*other);
}
bool FUID_lessThan(FUID *this_ptr, const FUID *f) {
	const auto other = cast(f);
	return cast(this_ptr)->operator<(*other);
}
uint32 FUID_getLong1(FUID *this_ptr) {
	return cast(this_ptr)->getLong1();
}
uint32 FUID_getLong2(FUID *this_ptr) {
	return cast(this_ptr)->getLong2();
}
uint32 FUID_getLong3(FUID *this_ptr) {
	return cast(this_ptr)->getLong3();
}
uint32 FUID_getLong4(FUID *this_ptr) {
	return cast(this_ptr)->getLong4();
}
void FUID_from4Int(FUID *this_ptr, uint32 d1, uint32 d2, uint32 d3, uint32 d4) {
	return cast(this_ptr)->from4Int(d1, d2, d3, d4);
}
void FUID_to4Int(FUID *this_ptr, uint32 *d1, uint32 *d2, uint32 *d3, uint32 *d4) {
	return cast(this_ptr)->to4Int(*d1, *d2, *d3, *d4);
}
void FUID_toString(FUID *this_ptr, char8 *string) {
	return cast(this_ptr)->toString(string);
}
bool FUID_fromString(FUID *this_ptr, const char8 *string) {
	return cast(this_ptr)->fromString(string);
}
void FUID_toRegistryString(FUID *this_ptr, char8 *string) {
	return cast(this_ptr)->toRegistryString(string);
}
bool FUID_fromRegistryString(FUID *this_ptr, const char8 *string) {
	return cast(this_ptr)->fromRegistryString(string);
}
void *FUID_toTUID(FUID *this_ptr, TUID result) {
	cast(this_ptr)->toTUID(result);
}
void FUID_print(FUID *this_ptr, char8 *string, int32 style) {
	cast(this_ptr)->print(string, style);
}
