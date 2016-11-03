package ovr

import com.sun.jna.ptr.FloatByReference
import com.sun.jna.ptr.IntByReference
import org.junit.Test

/**
 * Created by GBarbieri on 02.11.2016.
 */

class Test {

    @Test fun init() {

        loadNatives()

        //val initParams = OvrInitParams(OvrInitFlags.ovrInit_RequestVersion.i, OVR_MINOR_VERSION, null, 0, 0)
        val initParams = OvrInitParams.ByReference()
        initParams.Flags = OvrInitFlags.ovrInit_RequestVersion.i
        initParams.RequestedMinorVersion = OVR_MINOR_VERSION

        val result = ovr_Initialize(initParams)
        assert(OVR_SUCCESS(result), { "Failed to initialize libOVR." })
    }
}