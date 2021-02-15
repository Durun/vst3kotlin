
#include <stdio.h>
#include <unistd.h>
#include <string>

#if (__cplusplus >= 201707L)
#include <filesystem>
using namespace std;
using Path = std::filesystem::path;
#else
#include <experimental/filesystem>
using namespace std::experimental;
using Path = std::experimental::filesystem::path;
#endif

const std::string pluginDir(const char* path) {
    auto ret = std::string("../../../../../interop/src/commonTest/resources/vst3/");
    ret.append(path);
    return ret;
}

std::string getSOPath(const std::string& inPath) {
    auto n = inPath.substr(inPath.find_last_of('/') + 1);
    auto moduleName = n.substr(0, n.find_last_of('.'));
    auto buf = std::move(inPath);
    buf.append("/Contents/x86_64-linux");
    buf.append("/");
    buf.append(moduleName);
    buf.append(".so");
    return std::move(buf);
}


int sandbox() {
    char buf[256];
    getcwd(buf, 256);
    printf("cwd: %s\n", buf);

    const std::string path = pluginDir("again.vst3");
    std::string error;

    auto soPath = getSOPath(path);
    printf("so: %s", soPath.c_str());

    //auto factory = module->getFactory();
    return 0;
}
