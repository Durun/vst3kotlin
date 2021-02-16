#include "loadVst3.h"
#include "getFunctionPointer.h"
#include <dlfcn.h>

// private
static std::string getSOPath(const std::string& inPath) {
    auto n = inPath.substr(inPath.find_last_of('/') + 1);
    auto moduleName = n.substr(0, n.find_last_of('.'));
    auto buf = std::move(inPath);
    buf.append("/Contents/x86_64-linux");
    buf.append("/");
    buf.append(moduleName);
    buf.append(".so");
    return std::move(buf);
}

// public
void* loadVst3(const std::string& path) {
    auto modulePath = getSOPath(path);
    printf("dlopen: %s\n", modulePath.c_str());
    auto module = dlopen(modulePath.c_str(), RTLD_LAZY);
    if (!module) {
        printf("dlopen failed: %s\n", modulePath.c_str());
        return nullptr;
    }
    return module;
}
void closeVst3(void* handle) {
    if (handle) {
        if (auto moduleExit = getFunctionPointer<ModuleExitFunc>(handle, "ModuleExit")) {
            moduleExit();
        }
        dlclose(handle);
    }
}
