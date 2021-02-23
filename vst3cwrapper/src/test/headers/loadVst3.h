#pragma once

#include <string>

/**
 * @param path .so file
 * @return void* handler
 */
void* loadVst3(const std::string& path);
void closeVst3(void* handle);
