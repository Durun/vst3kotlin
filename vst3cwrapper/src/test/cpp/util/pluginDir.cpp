#include "pluginDir.h"

const std::string pluginDir(const char* path) {
    printf("Plugin: %s\n", path);
    auto ret = std::string("../../../../../interop/src/commonTest/resources/vst3/");
    ret.append(path);
    return ret.c_str();
}
