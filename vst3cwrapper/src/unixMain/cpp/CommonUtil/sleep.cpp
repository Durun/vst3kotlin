#include <unistd.h>
#include "CommonUtil//sleep.h"

void CommonUtil::sleep(int millisecond) {
    usleep(millisecond * 1000);
}