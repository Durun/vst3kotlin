#include "pluginDir.h"
#include "loadVst3.h"
#include "loadFactory.h"

#include "base/FUID.h"
#include "base/IPluginFactory.h"
#include <pluginterfaces/vst/ivstaudioprocessor.h>

#include <assert.h>

int test_createInstance() {
	auto path = pluginDir("hostchecker.vst3");
	auto handle = loadVst3(path);
	auto factory = IPluginFactory_new(loadFactory(handle));

	auto iid = FUID();
	FUID_new_string(&iid, "42043F99B7DA453CA569E79D9AAEC33D");
	auto cid = FUID();
	FUID_new_string(&cid, "23FC190E02DD4499A8D2230E50617DA3");

	Steinberg::Vst::IAudioProcessor* obj = nullptr;

	auto result = IPluginFactory_createInstance(
		factory,cid, iid, reinterpret_cast<void**>(&obj));

	switch (result) {
		case Steinberg::kResultTrue:
			break;
		case Steinberg::kNoInterface:
			printf("NoInterface.\n");
			assert(false);
			break;
		default:
			printf("Failed: %d\n", result);
			assert(false);
			break;
	}
	auto latency = obj->getLatencySamples();
	printf("latency: %d samples\n", latency);
	assert(latency == 256);

	obj->release();
	IPluginFactory_release(factory);
	closeVst3(handle);
}
