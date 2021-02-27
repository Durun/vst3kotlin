#include "tests.h"

int main() {
	test_types();
	test_MessageQueue();
	test_GlobalStore();
	test_ByteQueue();
	test_LongStore();
	test_LongArrayStore();
    test_IParameterChanges();
	return 0;
}
