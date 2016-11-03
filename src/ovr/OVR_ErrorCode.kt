package ovr

import com.sun.jna.Pointer
import com.sun.jna.Structure
import ovr.OvrSuccessType.*
import java.util.*

/**
 * Created by GBarbieri on 03.11.2016.
 */

/** API call results are represented at the highest level by a single ovrResult.    */
typealias OvrResult = Int

/** \brief Indicates if an ovrResult indicates success.
 *
 *  Some functions return additional successful values other than ovrSucces and require usage of this macro to indicate successs.   */
fun OVR_SUCCESS(result: Int) = result >= 0

/** \brief Indicates if an ovrResult indicates an unqualified success.
 *
 *  This is useful for indicating that the code intentionally wants to check for result == ovrSuccess as opposed to OVR_SUCCESS(), which checks for
 *  result >= ovrSuccess.   */
fun OVR_UNQUALIFIED_SUCCESS(result: Int) = result == ovrSuccess.i

/** \brief Indicates if an ovrResult indicates failure. */
fun OVR_FAILURE(result: Int) = !OVR_SUCCESS(result)

// Success is a value greater or equal to 0, while all error types are negative values.
enum class OvrSuccessType(@JvmField val i: Int) {

    /** This is a general success result. Use OVR_SUCCESS to test for success.  */
    ovrSuccess(0)
}

/** Public success types
 *  Success is a value greater or equal to 0, while all error types are negative values.    */
enum class OvrSuccessTypes(@JvmField val i: Int) {

    /** Returned from a call to SubmitFrame. The call succeeded, but what the app rendered will not be visible on the HMD. Ideally the app should continue calling
     *  SubmitFrame, but not do any rendering. When the result becomes ovrSuccess, rendering should continue as usual.  */
    ovrSuccess_NotVisible(1000),
    ovrSuccess_BoundaryInvalid(1001), ///   < Boundary is invalid due to sensor change or was not setup.
    ovrSuccess_DeviceUnavailable(1002)  /// < Device is not available for the requested operation.
}

// Public error types
enum class OvrErrorType(@JvmField var i: Int) {

    /* General errors */
    ovrError_MemoryAllocationFailure(-1000), ///        < Failure to allocate memory.
    ovrError_InvalidSession(-1002), ///                 < Invalid ovrSession parameter provided.
    ovrError_Timeout(-1003), ///                        < The operation timed out.
    ovrError_NotInitialized(-1004), ///                 < The system or component has not been initialized.
    ovrError_InvalidParameter(-1005), ///               < Invalid parameter provided. See error info or log for details.
    ovrError_ServiceError(-1006), ///                   < Generic service error. See error info or log for details.
    ovrError_NoHmd(-1007), ///                          < The given HMD doesn't exist.
    ovrError_Unsupported(-1009), ///                    < Function call is not supported on this hardware/software
    ovrError_DeviceUnavailable(-1010), ///              < Specified device type isn't available.
    ovrError_InvalidHeadsetOrientation(-1011), ///      < The headset was in an invalid orientation for the requested operation (e.g. vertically oriented during ovr_RecenterPose).
    ovrError_ClientSkippedDestroy(-1012), ///           < The client failed to call ovr_Destroy on an active session before calling ovr_Shutdown. Or the client crashed.
    ovrError_ClientSkippedShutdown(-1013), ///          < The client failed to call ovr_Shutdown or the client crashed.
    ovrError_ServiceDeadlockDetected(-1014), ///        < The service watchdog discovered a deadlock.
    ovrError_InvalidOperation(-1015), ///               < Function call is invalid for object's current state

    /* Audio error range, reserved for Audio errors. */
    ovrError_AudioDeviceNotFound(-2001), ///            < Failure to find the specified audio device.
    ovrError_AudioComError(-2002), ///                  < Generic COM error.

    /* Initialization errors. */
    ovrError_Initialize(-3000), ///                     < Generic initialization error.
    ovrError_LibLoad(-3001), ///                        < Couldn't load LibOVRRT.
    ovrError_LibVersion(-3002), ///                     < LibOVRRT version incompatibility.
    ovrError_ServiceConnection(-3003), ///              < Couldn't connect to the OVR Service.
    ovrError_ServiceVersion(-3004), ///                 < OVR Service version incompatibility.
    ovrError_IncompatibleOS(-3005), ///                 < The operating system version is incompatible.
    ovrError_DisplayInit(-3006), ///                    < Unable to initialize the HMD display.
    ovrError_ServerStart(-3007), ///                    < Unable to start the server. Is it already running?
    ovrError_Reinitialization(-3008), ///               < Attempting to re-initialize with a different version.
    ovrError_MismatchedAdapters(-3009), ///             < Chosen rendering adapters between client and service do not match
    ovrError_LeakingResources(-3010), ///               < Calling application has leaked resources
    ovrError_ClientVersion(-3011), ///                  < Client version too old to connect to service
    ovrError_OutOfDateOS(-3012), ///                    < The operating system is out of date.
    ovrError_OutOfDateGfxDriver(-3013), ///             < The graphics driver is out of date.
    ovrError_IncompatibleGPU(-3014), ///                < The graphics hardware is not supported
    ovrError_NoValidVRDisplaySystem(-3015), ///         < No valid VR display system found.
    ovrError_Obsolete(-3016), ///                       < Feature or API is obsolete and no longer supported.
    ovrError_DisabledOrDefaultAdapter(-3017), ///       < No supported VR display system found, but disabled or driverless adapter found.
    ovrError_HybridGraphicsNotSupported(-3018), ///     < The system is using hybrid graphics (Optimus, etc...), which is not support.
    ovrError_DisplayManagerInit(-3019), ///             < Initialization of the DisplayManager failed.
    ovrError_TrackerDriverInit(-3020), ///              < Failed to get the interface for an attached tracker
    ovrError_LibSignCheck(-3021), ///                   < LibOVRRT signature check failure.
    ovrError_LibPath(-3022), ///                        < LibOVRRT path failure.
    ovrError_LibSymbols(-3023), ///                     < LibOVRRT symbol resolution failure.

    /* Rendering errors */
    ovrError_DisplayLost(-6000), ///                    < In the event of a system-wide graphics reset or cable unplug this is returned to the app.
    ovrError_TextureSwapChainFull(-6001), ///           < ovr_CommitTextureSwapChain was called too many times on a texture swapchain without calling submit to use the chain.
    ovrError_TextureSwapChainInvalid(-6002), ///        < The ovrTextureSwapChain is in an incomplete or inconsistent state. Ensure ovr_CommitTextureSwapChain was called at least once first.
    ovrError_GraphicsDeviceReset(-6003), ///            < Graphics device has been reset (TDR, etc...)
    ovrError_DisplayRemoved(-6004), ///                 < HMD removed from the display adapter
    ovrError_ContentProtectionNotAvailable(-6005), ///  <Content protection is not available for the display
    ovrError_ApplicationInvisible(-6006), ///           < Application declared itself as an invisible type and is not allowed to submit frames.
    ovrError_Disallowed(-6007), ///                     < The given request is disallowed under the current conditions.
    ovrError_DisplayPluggedIncorrectly(-6008), ///      < Display portion of HMD is plugged into an incompatible port (ex: IGP)

    /* Fatal errors */
    ovrError_RuntimeException(-7000), ///               < A runtime exception occurred. The application is required to shutdown LibOVR and re-initialize it before this error state will be cleared.

    /* Calibration errors */
    ovrError_NoCalibration(-9000), ///                  < Result of a missing calibration block
    ovrError_OldVersion(-9001), ///                     < Result of an old calibration block
    ovrError_MisformattedBlock(-9002), ///              < Result of a bad calibration block due to lengths

    /* Other errors */

}

/** Provides information about the last error.
 *  \see ovr_GetLastErrorInfo   */
open class OvrErrorInfo : Structure {

    @JvmField var result = 0               ///< The result from the last API call that generated an error ovrResult.
    @JvmField var errorString = ""     ///< A UTF8-encoded null-terminated English string describing the problem. The format of this string is subject to change in future versions.

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("result", "errorString")

    constructor(result: Int, errorString: String) {

        this.result = result
        this.errorString = errorString
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrErrorInfo(), Structure.ByReference
    class ByValue : OvrErrorInfo(), Structure.ByValue
}