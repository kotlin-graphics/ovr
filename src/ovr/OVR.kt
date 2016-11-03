package ovr

import com.sun.deploy.security.SessionCertStore
import com.sun.jna.Native
import com.sun.jna.NativeLibrary
import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.ptr.IntByReference
import com.sun.jna.ptr.PointerByReference
import java.util.*
import ovr.OvrButton.*
import ovr.OvrEyeType.*
import ovr.OvrHandType.*
import ovr.OvrResult

/**
 * Created by GBarbieri on 02.11.2016.
 */

/********************************************************************************/
/**
\file      OVR_CAPI.h
\brief     C Interface to the Oculus PC SDK tracking and rendering library.
\copyright Copyright 2014 Oculus VR, LLC All Rights reserved.
 ************************************************************************************/

// TODO wrong expectation, PRODUCT_VERSION has been removed
// Versioned file expectations.
//     Windows: LibOVRRT<BIT_DEPTH>_<PRODUCT_VERSION>_<MAJOR_VERSION>.dll
//          Example: LibOVRRT64_1_1.dll -- LibOVRRT 64 bit, product 1, major version 1, minor/patch/build numbers unspecified in the name.
//     Mac:     LibOVRRT_<PRODUCT_VERSION>.framework/Versions/<MAJOR_VERSION>/LibOVRRT_<PRODUCT_VERSION>
//          We are not presently using the .framework bundle's Current directory to hold the version number. This may change.
//     Linux:   libOVRRT<BIT_DEPTH>_<PRODUCT_VERSION>.so.<MAJOR_VERSION>
//          The file on disk may contain a minor version number, but a symlink is used to map this major-only version to it.
fun loadNatives() = Native.register(NativeLibrary.getInstance("LibOVRRT${System.getProperty("sun.arch.data.model")}_$OVR_MAJOR_VERSION"))

//------------------------------------------------------------------------------------------------------------------------------------------------------------------
// ***** ovrBool

typealias OvrBool = Byte   ///< Boolean type
typealias OvrBool_ByReference = Byte   ///< Boolean type
const val ovrFalse = 0.toByte()     ///< ovrBool value of false.
const val ovrTrue = 1.toByte()      ///< ovrBool value of true.

//------------------------------------------------------------------------------------------------------------------------------------------------------------------
// ***** Simple Math Structures

/** A RGBA color with normalized float components.  */
open class OvrColorf : Structure {

    @JvmField var r = 0f
    @JvmField var g = 0f
    @JvmField var b = 0f
    @JvmField var a = 0f

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("r", "g", "b", "a")

    constructor(r: Float, g: Float, b: Float, a: Float) {
        this.r = r
        this.g = g
        this.b = b
        this.a = a
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrColorf(), Structure.ByReference
    class ByValue : OvrColorf(), Structure.ByValue
}

/** A 2D vector with integer components.    */
open class OvrVector2i : Structure {

    @JvmField var x = 0
    @JvmField var y = 0

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("w", "h")

    constructor(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrVector2i(), Structure.ByReference
    class ByValue : OvrVector2i(), Structure.ByValue
}

/** A 2D size with integer components.    */
open class OvrSizei : Structure {

    @JvmField var w = 0
    @JvmField var h = 0

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("w", "h")

    constructor(w: Int, h: Int) {
        this.w = w
        this.h = h
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrSizei(), Structure.ByReference
    class ByValue : OvrSizei(), Structure.ByValue
}

/** A 2D rectangle with a position and size. All components are integers.    */
open class OvrRecti : Structure {

    @JvmField var pos = OvrVector2i()
    @JvmField var size = OvrSizei()

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("pos", "size")

    constructor(pos: OvrVector2i, size: OvrSizei) {
        this.pos = pos
        this.size = size
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrRecti(), Structure.ByReference
    class ByValue : OvrRecti(), Structure.ByValue
}

/** A quaternion rotation.    */
open class OvrQuatf : Structure {

    @JvmField var x = 0f
    @JvmField var y = 0f
    @JvmField var z = 0f
    @JvmField var w = 0f

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("x", "y", "z", "w")

    constructor(x: Float, y: Float, z: Float, w: Float) {
        this.x = x
        this.y = y
        this.z = z
        this.w = w
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrQuatf(), Structure.ByReference
    class ByValue : OvrQuatf(), Structure.ByValue
}

/** A 2D vector with float components.    */
open class OvrVector2f : Structure {

    @JvmField var x = 0f
    @JvmField var y = 0f

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("x", "y")

    constructor(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrVector2f(), Structure.ByReference
    class ByValue : OvrVector2f(), Structure.ByValue
}

/** A 3D vector with float components.    */
open class OvrVector3f : Structure {

    @JvmField var x = 0f
    @JvmField var y = 0f
    @JvmField var z = 0f

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("x", "y", "z")

    constructor(x: Float, y: Float, z: Float) {
        this.x = x
        this.y = y
        this.z = z
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrVector3f(), Structure.ByReference
    class ByValue : OvrVector3f(), Structure.ByValue
}

/** A 4x4 matrix with float elements.    */
open class OvrMatrix4f : Structure {

    @JvmField var m = FloatArray(4 * 4)

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("m")

    constructor(m: FloatArray) {
        if (m.size != this.m.size) throw IllegalArgumentException("Wrong array size !")
        this.m = m
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrMatrix4f(), Structure.ByReference
    class ByValue : OvrMatrix4f(), Structure.ByValue
}

/** Position and orientation together.    */
open class OvrPosef : Structure {

    @JvmField var ovrQuatf = OvrQuatf()
    @JvmField var ovrVector3f = OvrVector3f()

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("ovrQuatf", "ovrVector3f")

    constructor(ovrQuatf: OvrQuatf, ovrVector3f: OvrVector3f) {
        this.ovrQuatf = ovrQuatf
        this.ovrVector3f = ovrVector3f
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrPosef(), Structure.ByReference
    class ByValue : OvrPosef(), Structure.ByValue
}

/**  A full pose (rigid body) configuration with first and second derivatives.
 *
 *  Body refers to any object for which ovrPoseStatef is providing data.
 *  It can be the HMD, Touch controller, sensor or something else. The context depends on the usage of the struct.    */
open class OvrPoseStatef : Structure {

    @JvmField var thePose = OvrPosef()                  ///< Position and orientation.
    @JvmField var angularVelocity = OvrVector3f()       ///< Angular velocity in radians per second.
    @JvmField var linearVelocity = OvrVector3f()        ///< Velocity in meters per second.
    @JvmField var angularAcceleration = OvrVector3f()   ///< Angular acceleration in radians per second per second.
    @JvmField var linearAcceleration = OvrVector3f()    ///< Acceleration in meters per second per second.
    @JvmField var pad0 = ByteArray(4)                   ///< \internal struct pad.
    @JvmField var timeInSeconds = 0.0                   ///< Absolute time that this pose refers to. \see ovr_GetTimeInSeconds

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("thePose", "angularVelocity", "linearVelocity", "angularAcceleration", "linearAcceleration",
            "pad0", "timeInSeconds")

    constructor(thePose: OvrPosef, angularVelocity: OvrVector3f, linearVelocity: OvrVector3f, angularAcceleration: OvrVector3f, linearAcceleration: OvrVector3f,
                timeInSeconds: Double) {
        this.thePose = thePose
        this.angularVelocity = angularVelocity
        this.linearVelocity = linearVelocity
        this.angularAcceleration = angularAcceleration
        this.linearAcceleration = linearAcceleration
        this.timeInSeconds = timeInSeconds
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrPoseStatef(), Structure.ByReference
    class ByValue : OvrPoseStatef(), Structure.ByValue
}

/**  Describes the up, down, left, and right angles of the field of view.
 *
 *   Field Of View (FOV) tangent of the angle units.
 *   \note For a standard 90 degree vertical FOV, we would have: { UpTan = tan(90 degrees / 2), DownTan = tan(90 degrees / 2) }.   */
open class OvrFovPort : Structure {

    @JvmField var upTan = 0f    ///< The tangent of the angle between the viewing vector and the top edge of the field of view.
    @JvmField var downTan = 0f  ///< The tangent of the angle between the viewing vector and the bottom edge of the field of view.
    @JvmField var leftTan = 0f  ///< The tangent of the angle between the viewing vector and the left edge of the field of view.
    @JvmField var rightTan = 0f ///< The tangent of the angle between the viewing vector and the right edge of the field of view.

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("upTan", "downTan", "leftTan", "rightTan")

    constructor(upTan: Float, downTan: Float, leftTan: Float, rightTan: Float) {
        this.upTan = upTan
        this.downTan = downTan
        this.leftTan = leftTan
        this.rightTan = rightTan
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrFovPort(), Structure.ByReference
    class ByValue : OvrFovPort(), Structure.ByValue
}


//------------------------------------------------------------------------------------------------------------------------------------------------------------------
// ***** HMD Types

/** Enumerates all HMD types that we support.
 *
 * The currently released developer kits are ovrHmd_DK1 and ovrHmd_DK2. The other enumerations are for internal use only. */
enum class OvrHmdType(@JvmField val i: Int) {

    ovrHmd_None(0),
    ovrHmd_DK1(3),
    ovrHmd_DKHD(4),
    ovrHmd_DK2(6),
    ovrHmd_CB(8),
    ovrHmd_Other(9),
    ovrHmd_E3_2015(10),
    ovrHmd_ES06(11),
    ovrHmd_ES09(12),
    ovrHmd_ES11(13),
    ovrHmd_CV1(14);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** HMD capability bits reported by device.     */
enum class OvrHmdCaps(@JvmField val i: Int) {

    // Read-only flags
    ovrHmdCap_DebugDevice(0x0010); ///< <B>(read only)</B> Specifies that the HMD is a virtual debug device.

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}


/** Tracking capability bits reported by the device.
 *  Used with ovr_GetTrackingCaps.  */
enum class OvrTrackingCaps(@JvmField val i: Int) {

    ovrTrackingCap_Orientation(0x0010), ///         < Supports orientation tracking (IMU).
    ovrTrackingCap_MagYawCorrection(0x0020), ///    < Supports yaw drift correction via a magnetometer or other means.
    ovrTrackingCap_Position(0x0040); ///            < Supports positional tracking.

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}


/** Specifies which eye is being used for rendering.
 *  This type explicitly does not include a third "NoStereo" monoscopic option, as such is not required for an HMD-centered API.    */
enum class OvrEyeType(@JvmField val i: Int) {

    ovrEye_Left(0), ///             < The left eye, from the viewer's perspective.
    ovrEye_Right(1), ///            < The right eye, from the viewer's perspective.
    ovrEye_Count(2); ///            < \internal Count of enumerated elements.

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** Specifies the coordinate system ovrTrackingState returns tracking poses in.
 *  Used with ovr_SetTrackingOriginType()    */
enum class OvrTrackingOrigin(@JvmField val i: Int) {

    /** \brief Tracking system origin reported at eye (HMD) height
     * \details Prefer using this origin when your application requires matching user's current physical head pose to a virtual head pose without any regards to
     * the height of the floor. Cockpit-based, or 3rd-person experiences are ideal candidates.
     * When used, all poses in ovrTrackingState are reported as an offset transform from the profile calibrated or recentered HMD pose.
     * It is recommended that apps using this origin type call ovr_RecenterTrackingOrigin prior to starting the VR experience, but notify the user before doing so
     * to make sure the user is in a comfortable pose, facing a comfortable direction.  */
    ovrTrackingOrigin_EyeLevel(0),

    /** \brief Tracking system origin reported at floor height
     * \details Prefer using this origin when your application requires the physical floor height to match the virtual floor height, such as standing experiences.
     * When used, all poses in ovrTrackingState are reported as an offset transform from the profile calibrated floor pose. Calling ovr_RecenterTrackingOrigin
     * will recenter the X & Z axes as well as yaw, but the Y-axis (i.e. height) will continue to be reported using the floor height as the origin for all poses.   */
    ovrTrackingOrigin_FloorLevel(1),

    ovrTrackingOrigin_Count(2); ///             < \internal Count of enumerated elements.

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** Identifies a graphics device in a platform-specific way.
 *  For Windows this is a LUID type.    */
open class OvrGraphicsLuid : Structure {

    // Public definition reserves space for graphics API-specific implementation
    @JvmField var reserved = ""

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("reserved")

    constructor(reserved: String) {
        this.reserved = reserved
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrGraphicsLuid(), Structure.ByReference
    class ByValue : OvrGraphicsLuid(), Structure.ByValue
}

/** This is a complete descriptor of the HMD.    */
open class OvrHmdDesc : Structure {

    @JvmField var type = 0                         ///< The type of HMD.
    fun type() = OvrHmdType.of(type)
    @JvmField var pad0 = ByteArray(4)               ///< \internal struct paddding.
    @JvmField var productName = ""                  ///< UTF8-encoded product identification string (e.g. "Oculus Rift DK1").
    @JvmField var manufacturer = ""                 ///< UTF8-encoded HMD manufacturer identification string.
    @JvmField var vendorId = 0.toShort()            ///< HID (USB) vendor identifier of the device.
    @JvmField var productId = 0.toShort()           ///< HID (USB) product identifier of the device.
    @JvmField var serialNumber = ""                 ///< HMD serial number.
    @JvmField var firmwareMajor = 0.toShort()       ///< HMD firmware major version.
    @JvmField var firmwareMinor = 0.toShort()       ///< HMD firmware minor version.
    @JvmField var availableHmdCaps = 0              ///< Capability bits described by ovrHmdCaps which the HMD currently supports.
    @JvmField var defaultHmdCaps = 0                ///< Capability bits described by ovrHmdCaps which are default for the current Hmd.
    @JvmField var availableTrackingCaps = 0         ///< Capability bits described by ovrTrackingCaps which the system currently supports.
    @JvmField var defaultTrackingCaps = 0           ///< Capability bits described by ovrTrackingCaps which are default for the current system.
    @JvmField var defaultEyeFov = Array(ovrEye_Count.i, { OvrFovPort() })  ///< Defines the recommended FOVs for the HMD.
    @JvmField var maxEyeFov = Array(ovrEye_Count.i, { OvrFovPort() })      ///< Defines the maximum FOVs for the HMD.
    @JvmField var resolution = OvrSizei()           ///< Resolution of the full HMD screen (both eyes) in pixels.
    @JvmField var displayRefreshRate = 0f           ///< Nominal refresh rate of the display in cycles per second at the time of HMD creation.
    @JvmField var pad1 = ByteArray(4)               ///< \internal struct paddding.

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("type", "pad0", "productName", "manufacturer", "vendorId", "productId", "serialNumber", "firmwareMajor",
            "firmwareMinor", "availableHmdCaps", "defaultHmdCaps", "availableTrackingCaps", "defaultTrackingCaps", "defaultEyeFov", "maxEyeFov", "resolution",
            "displayRefreshRate", "pad1")

    constructor(type: OvrHmdType, productName: String, manufacturer: String, vendorId: Short, productId: Short, serialNumber: String, firmwareMajor: Short,
                firmwareMinor: Short, availableHmdCaps: Int, defaultHmdCaps: Int, availableTrackingCaps: Int, defaultTrackingCaps: Int,
                defaultEyeFov: Array<OvrFovPort>, maxEyeFov: Array<OvrFovPort>, resolution: OvrSizei, displayRefreshRate: Float)
    : this(type.i, productName, manufacturer, vendorId, productId, serialNumber, firmwareMajor, firmwareMinor, availableHmdCaps, defaultHmdCaps,
            availableTrackingCaps, defaultTrackingCaps, defaultEyeFov, maxEyeFov, resolution, displayRefreshRate)

    constructor(type: Int, productName: String, manufacturer: String, vendorId: Short, productId: Short, serialNumber: String, firmwareMajor: Short,
                firmwareMinor: Short, availableHmdCaps: Int, defaultHmdCaps: Int, availableTrackingCaps: Int, defaultTrackingCaps: Int,
                defaultEyeFov: Array<OvrFovPort>, maxEyeFov: Array<OvrFovPort>, resolution: OvrSizei, displayRefreshRate: Float) {
        this.type = type
        this.productName = productName
        this.manufacturer = manufacturer
        this.vendorId = vendorId
        this.productId = productId
        this.serialNumber = serialNumber
        this.firmwareMajor = firmwareMajor
        this.firmwareMinor = firmwareMinor
        this.availableHmdCaps = availableHmdCaps
        this.defaultHmdCaps = defaultHmdCaps
        this.availableTrackingCaps = availableTrackingCaps
        this.defaultTrackingCaps = defaultTrackingCaps
        if (defaultEyeFov.size != this.defaultEyeFov.size) throw IllegalArgumentException("Wrong array size !")
        this.defaultEyeFov = defaultEyeFov
        if (maxEyeFov.size != this.maxEyeFov.size) throw IllegalArgumentException("Wrong array size !")
        this.maxEyeFov = maxEyeFov
        this.resolution = resolution
        this.displayRefreshRate = displayRefreshRate
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrHmdDesc(), Structure.ByReference
    class ByValue : OvrHmdDesc(), Structure.ByValue
}

// TODO check
/// Used as an opaque pointer to an OVR session.
typealias OvrSession = Pointer
typealias OvrSession_ByReference = PointerByReference

/// Used as an opaque pointer to an OVR session.
//typedef struct ovrHmdStruct* ovrSession;
/** Bit flags describing the current status of sensor tracking.
 *  The values must be the same as in enum StatusBits
 *  \see ovrTrackingState   */
enum class OvrStatusBits(val i: Int) {

    ovrStatus_OrientationTracked(0x0001), ///   < Orientation is currently tracked (connected and in use).
    ovrStatus_PositionTracked(0x0002); ///      < Position is currently tracked (false if out of range).

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/**  Specifies the description of a single sensor.
 *  \see ovr_GetTrackerDesc     */
open class OvrTrackerDesc : Structure {

    @JvmField var frustumHFovInRadians = 0f      ///< Sensor frustum horizontal field-of-view (if present).
    @JvmField var frustumVFovInRadians = 0f      ///< Sensor frustum vertical field-of-view (if present).
    @JvmField var frustumNearZInMeters = 0f      ///< Sensor frustum near Z (if present).
    @JvmField var frustumFarZInMeters = 0f       ///< Sensor frustum far Z (if present).

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("frustumHFovInRadians", "frustumVFovInRadians", "frustumNearZInMeters", "frustumFarZInMeters")

    constructor(frustumHFovInRadians: Float, frustumVFovInRadians: Float, frustumNearZInMeters: Float, frustumFarZInMeters: Float) {

        this.frustumHFovInRadians = frustumHFovInRadians
        this.frustumVFovInRadians = frustumVFovInRadians
        this.frustumNearZInMeters = frustumNearZInMeters
        this.frustumFarZInMeters = frustumFarZInMeters
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrTrackerDesc(), Structure.ByReference
    class ByValue : OvrTrackerDesc(), Structure.ByValue
}

/**  Specifies sensor flags.
 *  /see ovrTrackerPose */
enum class OvrTrackerFlags(@JvmField val i: Int) {

    ovrTracker_Connected(0x0020), ///       < The sensor is present, else the sensor is absent or offline.
    ovrTracker_PoseTracked(0x0004);      ///< The sensor has a valid pose, else the pose is unavailable. This will only be set if ovrTracker_Connected is set.

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/**  Specifies the pose for a single sensor.     */
open class OvrTrackerPose : Structure {

    @JvmField var trackerFlags = 0      ///     < ovrTrackerFlags.
    @JvmField var pose = OvrPosef()         /// < The sensor's pose. This pose includes sensor tilt (roll and pitch). For a leveled coordinate system use LeveledPose.
    @JvmField var leveledPose = OvrPosef()  /// < The sensor's leveled pose, aligned with gravity. This value includes position and yaw of the sensor, but not roll and pitch. It can be used as a reference point to render real-world objects in the correct location.
    @JvmField var pad0 = ByteArray(4)  ///      < \internal struct pad.

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("trackerFlags", "pose", "leveledPose", "pad0")

    constructor(trackerFlags: Int, pose: OvrPosef, leveledPose: OvrPosef) {

        this.trackerFlags = trackerFlags
        this.pose = pose
        this.leveledPose = leveledPose
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrTrackerPose(), Structure.ByReference
    class ByValue : OvrTrackerPose(), Structure.ByValue
}

/** Tracking state at a given absolute time (describes predicted HMD pose, etc.).
 *  Returned by ovr_GetTrackingState.
 *  \see ovr_GetTrackingState     */
open class OvrTrackingState : Structure {

    /// Predicted head pose (and derivatives) at the requested absolute time.
    @JvmField var headPose = OvrPoseStatef()

    /// HeadPose tracking status described by ovrStatusBits.
    @JvmField var statusFlags = 0

    /** The most recent calculated pose for each hand when hand controller tracking is present.
     *  HandPoses[ovrHand_Left] refers to the left hand and HandPoses[ovrHand_Right] to the right hand.
     *  These values can be combined with ovrInputState for complete hand controller information. */
    @JvmField var handPoses = Array(2, { OvrPoseStatef() })

    /** HandPoses status flags described by ovrStatusBits.
     *  Only ovrStatus_OrientationTracked and ovrStatus_PositionTracked are reported.   */
    @JvmField var handStatusFlags = IntArray(2) //TODO wiki

    /** The pose of the origin captured during calibration.
     *  Like all other poses here, this is expressed in the space set by ovr_RecenterTrackingOrigin, and so will change every time that is called. This pose can be
     *  used to calculate where the calibrated origin lands in the new recentered space.
     *  If an application never calls ovr_RecenterTrackingOrigin, expect this value to be the identity pose and as such will point respective origin based on
     *  ovrTrackingOrigin requested when calling ovr_GetTrackingState.  */
    @JvmField var calibratedOrigin = OvrPosef()

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("headPose", "statusFlags", "handPoses", "handStatusFlags", "calibratedOrigin")

    constructor(headPose: OvrPoseStatef, statusFlags: Int, handPoses: Array<OvrPoseStatef>, handStatusFlags: Array<OvrStatusBits>, calibratedOrigin: OvrPosef)
    : this(headPose, statusFlags, handPoses, handStatusFlags.map { it.i }.toIntArray(), calibratedOrigin)

    constructor(headPose: OvrPoseStatef, statusFlags: Int, handPoses: Array<OvrPoseStatef>, handStatusFlags: IntArray, calibratedOrigin: OvrPosef) {

        this.headPose = headPose
        this.statusFlags = statusFlags
        if (handPoses.size != this.handPoses.size) throw IllegalArgumentException("Wrong array size !")
        this.handPoses = handPoses
        if (handStatusFlags.size != this.handStatusFlags.size) throw IllegalArgumentException("Wrong array size !")
        this.handStatusFlags = handStatusFlags
        this.calibratedOrigin = calibratedOrigin
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrTrackingState(), Structure.ByReference
    class ByValue : OvrTrackingState(), Structure.ByValue
}

/** Rendering information for each eye. Computed by ovr_GetRenderDesc() based on the specified FOV. Note that the rendering viewport is not included here as it
 *  can be specified separately and modified per frame by passing different Viewport values in the layer structure.
 *  \see ovr_GetRenderDesc  */
open class OvrEyeRenderDesc : Structure {

    @JvmField var eye = 0                        ///            < The eye index to which this instance corresponds.
    fun eye() = OvrEyeType.of(eye)
    @JvmField var fov = OvrFovPort()                        /// < The field of view.
    @JvmField var distortedViewport = OvrRecti()          ///   < Distortion viewport.
    @JvmField var pixelsPerTanAngleAtCenter = OvrVector2f() /// < How many display pixels will fit in tan(angle) = 1.
    @JvmField var hmdToEyeOffset = OvrVector3f()             ///< Translation of each eye, in meters.

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("eye", "fov", "distortedViewport", "pixelsPerTanAngleAtCenter", "hmdToEyeOffset")

    constructor(eye: OvrEyeType, fov: OvrFovPort, distortedViewport: OvrRecti, pixelsPerTanAngleAtCenter: OvrVector2f, hmdToEyeOffset: OvrVector3f)
    : this(eye.i, fov, distortedViewport, pixelsPerTanAngleAtCenter, hmdToEyeOffset)

    constructor(eye: Int, fov: OvrFovPort, distortedViewport: OvrRecti, pixelsPerTanAngleAtCenter: OvrVector2f, hmdToEyeOffset: OvrVector3f) {

        this.eye = eye
        this.fov = fov
        this.distortedViewport = distortedViewport
        this.pixelsPerTanAngleAtCenter = pixelsPerTanAngleAtCenter
        this.hmdToEyeOffset = hmdToEyeOffset
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrEyeRenderDesc(), Structure.ByReference
    class ByValue : OvrEyeRenderDesc(), Structure.ByValue
}

/** Projection information for ovrLayerEyeFovDepth.
 *  Use the utility function ovrTimewarpProjectionDesc_FromProjection to generate this structure from the application's projection matrix.
 *  \see ovrLayerEyeFovDepth, ovrTimewarpProjectionDesc_FromProjection  */
open class OvrTimewarpProjectionDesc : Structure {

    @JvmField var projection22 = 0f     ///< Projection matrix element [2][2].
    @JvmField var projection23 = 0f     ///< Projection matrix element [2][3].
    @JvmField var projection32 = 0f     ///< Projection matrix element [3][2].

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("projection22", "projection23", "projection32")

    constructor(projection22: Float, projection23: Float, projection32: Float) {

        this.projection22 = projection22
        this.projection23 = projection23
        this.projection32 = projection32
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrTimewarpProjectionDesc(), Structure.ByReference
    class ByValue : OvrTimewarpProjectionDesc(), Structure.ByValue
}

/** Contains the data necessary to properly calculate position info for various layer types.
 *  - HmdToEyeOffset is the same value pair provided in ovrEyeRenderDesc.
 *  - HmdSpaceToWorldScaleInMeters is used to scale player motion into in-application units.
 *  In other words, it is how big an in-application unit is in the player's physical meters.
 *  For example, if the application uses inches as its units then HmdSpaceToWorldScaleInMeters would be 0.0254.
 *  Note that if you are scaling the player in size, this must also scale. So if your application units are inches, but you're shrinking the player to half their
 *  normal size, then HmdSpaceToWorldScaleInMeters would be 0.0254*2.0.
 *  \see ovrEyeRenderDesc, ovr_SubmitFrame  */
open class OvrViewScaleDesc : Structure {

    @JvmField var hmdToEyeOffset = Array(ovrEye_Count.i, { OvrVector3f() })   ///< Translation of each eye.
    @JvmField var hmdSpaceToWorldScaleInMeters = 0f   ///                                   < Ratio of viewer units to meter units.

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("hmdToEyeOffset", "hmdSpaceToWorldScaleInMeters")

    constructor(hmdToEyeOffset: Array<OvrVector3f>, hmdSpaceToWorldScaleInMeters: Float) {

        if (hmdToEyeOffset.size != this.hmdToEyeOffset.size) throw IllegalArgumentException("Wrong array size !")
        this.hmdToEyeOffset = hmdToEyeOffset
        this.hmdSpaceToWorldScaleInMeters = hmdSpaceToWorldScaleInMeters
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrViewScaleDesc(), Structure.ByReference
    class ByValue : OvrViewScaleDesc(), Structure.ByValue
}

//------------------------------------------------------------------------------------------------------------------------------------------------------------------
// ***** Platform-independent Rendering Configuration

/** The type of texture resource.
 *  \see ovrTextureSwapChainDesc    */
enum class OvrTextureType(@JvmField val i: Int) {

    ovrTexture_2D(0), ///< 2D textures.
    ovrTexture_2D_External(1), ///< External 2D texture. Not used on PC
    ovrTexture_Cube(2), ///< Cube maps. Not currently supported on PC.
    ovrTexture_Count(3);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** The bindings required for texture swap chain.
 *  All texture swap chains are automatically bindable as shader input resources since the Oculus runtime needs this to read them.
 *  \see ovrTextureSwapChainDesc    */
enum class OvrTextureBindFlags(@JvmField val i: Int) {

    ovrTextureBind_None(0),
    ovrTextureBind_DX_RenderTarget(0x0001), ///< The application can write into the chain with pixel shader
    ovrTextureBind_DX_UnorderedAccess(0x0002), ///< The application can write to the chain with compute shader
    ovrTextureBind_DX_DepthStencil(0x0004)    ///< The chain buffers can be bound as depth and/or stencil buffers
}

/** The format of a texture.
 *  \see ovrTextureSwapChainDesc    */
enum class OvrTextureFormat {

    OVR_FORMAT_UNKNOWN,
    OVR_FORMAT_B5G6R5_UNORM, ///< Not currently supported on PC. Would require a DirectX 11.1 device.
    OVR_FORMAT_B5G5R5A1_UNORM, ///< Not currently supported on PC. Would require a DirectX 11.1 device.
    OVR_FORMAT_B4G4R4A4_UNORM, ///< Not currently supported on PC. Would require a DirectX 11.1 device.
    OVR_FORMAT_R8G8B8A8_UNORM,
    OVR_FORMAT_R8G8B8A8_UNORM_SRGB,
    OVR_FORMAT_B8G8R8A8_UNORM,
    OVR_FORMAT_B8G8R8A8_UNORM_SRGB, ///< Not supported for OpenGL applications
    OVR_FORMAT_B8G8R8X8_UNORM, ///< Not supported for OpenGL applications
    OVR_FORMAT_B8G8R8X8_UNORM_SRGB, ///< Not supported for OpenGL applications
    OVR_FORMAT_R16G16B16A16_FLOAT,
    OVR_FORMAT_D16_UNORM,
    OVR_FORMAT_D24_UNORM_S8_UINT,
    OVR_FORMAT_D32_FLOAT,
    OVR_FORMAT_D32_FLOAT_S8X24_UINT,

    // Added in 1.5 compressed formats can be used for static layers
    OVR_FORMAT_BC1_UNORM,
    OVR_FORMAT_BC1_UNORM_SRGB,
    OVR_FORMAT_BC2_UNORM,
    OVR_FORMAT_BC2_UNORM_SRGB,
    OVR_FORMAT_BC3_UNORM,
    OVR_FORMAT_BC3_UNORM_SRGB,
    OVR_FORMAT_BC6H_UF16,
    OVR_FORMAT_BC6H_SF16,
    OVR_FORMAT_BC7_UNORM,
    OVR_FORMAT_BC7_UNORM_SRGB;

    @JvmField val i = ordinal

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** Misc flags overriding particular behaviors of a texture swap chain
 *  \see ovrTextureSwapChainDesc    */
enum class OvrTextureMiscFlags(@JvmField val i: Int) {

    ovrTextureMisc_None(0),

    /** DX only: The underlying texture is created with a TYPELESS equivalent of the format specified in the texture desc. The SDK will still access the texture
     *  using the format specified in the texture desc, but the app can create views with different formats if this is specified.   */
    ovrTextureMisc_DX_Typeless(0x0001),

    /** DX only: Allow generation of the mip chain on the GPU via the GenerateMips call. This flag requires that RenderTarget binding also be specified.    */
    ovrTextureMisc_AllowGenerateMips(0x0002),

    /** Texture swap chain contains protected content, and requires HDCP connection in order to display to HMD. Also prevents mirroring or other redirection of any
     *  frame containing this contents  */
    ovrTextureMisc_ProtectedContent(0x0004)
}


/** Description used to create a texture swap chain.
 *  \see ovr_CreateTextureSwapChainDX
 *  \see ovr_CreateTextureSwapChainGL   */
open class OvrTextureSwapChainDesc : Structure {

    @JvmField var type = 0
    fun type() = OvrTextureType.of(type)
    @JvmField var format = 0
    fun format() = OvrTextureFormat.of(format)
    @JvmField var arraySize = 0      ///        < Only supported with ovrTexture_2D. Not supported on PC at this time.
    @JvmField var width = 0
    @JvmField var height = 0
    @JvmField var mipLevels = 0
    @JvmField var sampleCount = 0    ///        < Current only supported on depth textures
    @JvmField var staticImage = 0.toByte()  /// < Not buffered in a chain. For images that don't change
    @JvmField var miscFlags = 0      ///        < ovrTextureFlags
    @JvmField var bindFlags = 0      ///        < ovrTextureBindFlags. Not used for GL.

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("type", "format", "arraySize", "width", "height", "mipLevels", "sampleCount", "staticImage", "miscFlags",
            "bindFlags")

    constructor(type: OvrTextureType, format: OvrTextureFormat, arraySize: Int, width: Int, height: Int, mipLevels: Int, sampleCount: Int, staticImage: Boolean,
                miscFlags: Int, bindFlags: Int)
    : this(type.i, format.i, arraySize, width, height, mipLevels, sampleCount, (if (staticImage) 1 else 0).toByte(), miscFlags, bindFlags)

    constructor(type: Int, format: Int, arraySize: Int, width: Int, height: Int, mipLevels: Int, sampleCount: Int, staticImage: Byte, miscFlags: Int, bindFlags: Int) {

        this.type = type
        this.format = format
        this.arraySize = arraySize
        this.width = width
        this.height = height
        this.mipLevels = mipLevels
        this.sampleCount = sampleCount
        this.staticImage = staticImage
        this.miscFlags = miscFlags
        this.bindFlags = bindFlags
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrTextureSwapChainDesc(), Structure.ByReference
    class ByValue : OvrTextureSwapChainDesc(), Structure.ByValue
}

/** Description used to create a mirror texture.
 *  \see ovr_CreateMirrorTextureDX
 *  \see ovr_CreateMirrorTextureGL  */
open class OvrMirrorTextureDesc : Structure {

    @JvmField var format = 0
    fun format() = OvrTextureFormat.of(format)
    @JvmField var width = 0
    @JvmField var height = 0
    @JvmField var miscFlags = 0      ///< ovrTextureFlags

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("format", "width", "height", "miscFlags")

    constructor(format: OvrTextureFormat, width: Int, height: Int, miscFlags: Int) : this(format.i, width, height, miscFlags)

    constructor(format: Int, width: Int, height: Int, miscFlags: Int) {

        this.format = format
        this.width = width
        this.height = height
        this.miscFlags = miscFlags
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrMirrorTextureDesc(), Structure.ByReference
    class ByValue : OvrMirrorTextureDesc(), Structure.ByValue
}

typealias OvrTextureSwapChain = Pointer
typealias OvrTextureSwapChain_ByReference = PointerByReference
typealias ovrMirrorTexture = Pointer
typealias ovrMirrorTexture_ByReference = PointerByReference

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------

/** Describes button input types.
 *  Button inputs are combined; that is they will be reported as pressed if they are pressed on either one of the two devices.
 *  The ovrButton_Up/Down/Left/Right map to both XBox D-Pad and directional buttons.
 *  The ovrButton_Enter and ovrButton_Return map to Start and Back controller buttons, respectively.    */
enum class OvrButton(@JvmField val i: Int) {

    ovrButton_A(0x00000001), /// A button on XBox controllers and right Touch controller. Select button on Oculus Remote.
    ovrButton_B(0x00000002), /// B button on XBox controllers and right Touch controller. Back button on Oculus Remote.
    ovrButton_RThumb(0x00000004), /// Right thumbstick on XBox controllers and Touch controllers. Not present on Oculus Remote.
    ovrButton_RShoulder(0x00000008), /// Right shoulder button on XBox controllers. Not present on Touch controllers or Oculus Remote.

    ovrButton_X(0x00000100), /// X button on XBox controllers and left Touch controller. Not present on Oculus Remote.
    ovrButton_Y(0x00000200), /// Y button on XBox controllers and left Touch controller. Not present on Oculus Remote.
    ovrButton_LThumb(0x00000400), /// Left thumbstick on XBox controllers and Touch controllers. Not present on Oculus Remote.
    ovrButton_LShoulder(0x00000800), /// Left shoulder button on XBox controllers. Not present on Touch controllers or Oculus Remote.

    ovrButton_Up(0x00010000), /// Up button on XBox controllers and Oculus Remote. Not present on Touch controllers.
    ovrButton_Down(0x00020000), /// Down button on XBox controllers and Oculus Remote. Not present on Touch controllers.
    ovrButton_Left(0x00040000), /// Left button on XBox controllers and Oculus Remote. Not present on Touch controllers.
    ovrButton_Right(0x00080000), /// Right button on XBox controllers and Oculus Remote. Not present on Touch controllers.
    ovrButton_Enter(0x00100000), /// Start on XBox 360 controller. Menu on XBox One controller and Left Touch controller. Should be referred to as the Menu button in user-facing documentation.
    ovrButton_Back(0x00200000), /// Back on Xbox 360 controller. View button on XBox One controller. Not present on Touch controllers or Oculus Remote.
    ovrButton_VolUp(0x00400000), /// Volume button on Oculus Remote. Not present on XBox or Touch controllers.
    ovrButton_VolDown(0x00800000), /// Volume button on Oculus Remote. Not present on XBox or Touch controllers.
    ovrButton_Home(0x01000000), /// Home button on XBox controllers. Oculus button on Touch controllers and Oculus Remote.

    // Bit mask of all buttons that are for private usage by Oculus
    ovrButton_Private(ovrButton_VolUp or ovrButton_VolDown or ovrButton_Home),

    // Bit mask of all buttons on the right Touch controller
    ovrButton_RMask(ovrButton_A or ovrButton_B or ovrButton_RThumb or ovrButton_RShoulder),

    // Bit mask of all buttons on the left Touch controller
    ovrButton_LMask(ovrButton_X or ovrButton_Y or ovrButton_LThumb or ovrButton_LShoulder or ovrButton_Enter);

    infix fun or(other: OvrButton) = i or other.i
}

infix fun Int.or(other: OvrButton) = this or other.i

/** Describes touch input types.
 *  These values map to capacitive touch values reported ovrInputState::Touch.
 *  Some of these values are mapped to button bits for consistency. */
enum class OvrTouch(@JvmField val i: Int) {

    ovrTouch_A(ovrButton_A),
    ovrTouch_B(ovrButton_B),
    ovrTouch_RThumb(ovrButton_RThumb),
    ovrTouch_RThumbRest(0x00000008),
    ovrTouch_RIndexTrigger(0x00000010),

    // Bit mask of all the button touches on the right controller
    ovrTouch_RButtonMask(ovrTouch_A or ovrTouch_B or ovrTouch_RThumb or ovrTouch_RThumbRest or ovrTouch_RIndexTrigger),

    ovrTouch_X(ovrButton_X),
    ovrTouch_Y(ovrButton_Y),
    ovrTouch_LThumb(ovrButton_LThumb),
    ovrTouch_LThumbRest(0x00000800),
    ovrTouch_LIndexTrigger(0x00001000),

    // Bit mask of all the button touches on the left controller
    ovrTouch_LButtonMask(ovrTouch_X or ovrTouch_Y or ovrTouch_LThumb or ovrTouch_LThumbRest or ovrTouch_LIndexTrigger),

    // Finger pose state
    // Derived internally based on distance, proximity to sensors and filtering.
    ovrTouch_RIndexPointing(0x00000020),
    ovrTouch_RThumbUp(0x00000040),

    // Bit mask of all right controller poses
    ovrTouch_RPoseMask(ovrTouch_RIndexPointing or ovrTouch_RThumbUp),

    ovrTouch_LIndexPointing(0x00002000),
    ovrTouch_LThumbUp(0x00004000),

    // Bit mask of all left controller poses
    ovrTouch_LPoseMask(ovrTouch_LIndexPointing or ovrTouch_LThumbUp);

    constructor(button: OvrButton) : this(button.i)

    infix fun or(other: OvrTouch) = i or other.i
}

infix fun Int.or(other: OvrTouch) = this or other.i

/** Describes the Touch Haptics engine.
 *  Currently, those values will NOT change during a session.   */
open class OvrTouchHapticsDesc : Structure {

    // Haptics engine frequency/sample-rate, sample time in seconds equals 1.0/sampleRateHz
    @JvmField var sampleRateHz = 0
    // Size of each Haptics sample, sample value range is [0, 2^(Bytes*8)-1]
    @JvmField var sampleSizeInBytes = 0

    /** Queue size that would guarantee Haptics engine would not starve for data
     *  Make sure size doesn't drop below it for best results   */
    @JvmField var queueMinSizeToAvoidStarvation = 0

    // Minimum, Maximum and Optimal number of samples that can be sent to Haptics through ovr_SubmitControllerVibration
    @JvmField var submitMinSamples = 0
    @JvmField var submitMaxSamples = 0
    @JvmField var submitOptimalSamples = 0

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("sampleRateHz", "sampleSizeInBytes", "queueMinSizeToAvoidStarvation", "submitMinSamples",
            "submitMaxSamples", "submitOptimalSamples")

    constructor(sampleRateHz: Int, sampleSizeInBytes: Int, queueMinSizeToAvoidStarvation: Int, submitMinSamples: Int, submitMaxSamples: Int,
                submitOptimalSamples: Int) {

        this.sampleRateHz = sampleRateHz
        this.sampleSizeInBytes = sampleSizeInBytes
        this.queueMinSizeToAvoidStarvation = queueMinSizeToAvoidStarvation
        this.submitMinSamples = submitMinSamples
        this.submitMaxSamples = submitMaxSamples
        this.submitOptimalSamples = submitOptimalSamples
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrTouchHapticsDesc(), Structure.ByReference
    class ByValue : OvrTouchHapticsDesc(), Structure.ByValue
}

/** Specifies which controller is connected; multiple can be connected at once. */
enum class OvrControllerType(@JvmField val i: Int) {

    ovrControllerType_None(0x00),
    ovrControllerType_LTouch(0x01),
    ovrControllerType_RTouch(0x02),
    ovrControllerType_Touch(0x03),
    ovrControllerType_Remote(0x04),
    ovrControllerType_XBox(0x10),

    ovrControllerType_Active(0xff);     ///< Operate on or query whichever controller is active.

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** Haptics buffer submit mode  */
enum class OvrHapticsBufferSubmitMode(@JvmField val i: Int) {

    // Enqueue buffer for later playback
    ovrHapticsBufferSubmit_Enqueue(0);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** Haptics buffer descriptor, contains amplitude samples used for Touch vibration  */
open class OvrHapticsBuffer : Structure {

    @JvmField var samples = Pointer(0L)
    @JvmField var samplesCount = 0
    @JvmField var submitMode = 0
    fun submitMode() = OvrHapticsBufferSubmitMode.of(submitMode)


    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("samples", "samplesCount", "submitMode")

    constructor(samples: Pointer, samplesCount: Int, submitMode: OvrHapticsBufferSubmitMode) : this(samples, samplesCount, submitMode.i)

    constructor(samples: Pointer, samplesCount: Int, submitMode: Int) {

        this.samples = samples
        this.samplesCount = samplesCount
        this.submitMode = submitMode
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrHapticsBuffer(), Structure.ByReference
    class ByValue : OvrHapticsBuffer(), Structure.ByValue
}

/** State of the Haptics playback for Touch vibration   */
open class OvrHapticsPlaybackState : Structure {

    // Remaining space available to queue more samples
    @JvmField var remainingQueueSpace = 0

    // Number of samples currently queued
    @JvmField var samplesQueued = 0

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("remainingQueueSpace", "samplesQueued")

    constructor(remainingQueueSpace: Int, samplesQueued: Int) {

        this.remainingQueueSpace = remainingQueueSpace
        this.samplesQueued = samplesQueued
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrHapticsPlaybackState(), Structure.ByReference
    class ByValue : OvrHapticsPlaybackState(), Structure.ByValue
}

/** Position tracked devices    */
enum class OvrTrackedDeviceType(@JvmField val i: Int) {

    ovrTrackedDevice_HMD(0x0001),
    ovrTrackedDevice_LTouch(0x0002),
    ovrTrackedDevice_RTouch(0x0004),
    ovrTrackedDevice_Touch(0x0006),
    ovrTrackedDevice_All(0xFFFF)
}

/** Boundary types that specified while using the boundary system   */
enum class OvrBoundaryType(@JvmField val i: Int) {

    // Outer boundary - closely represents user setup walls
    ovrBoundary_Outer(0x0001),

    // Play area - safe rectangular area inside outer boundary which can optionally be used to restrict user interactions and motion.
    ovrBoundary_PlayArea(0x0100)
}

/** Boundary system look and feel   */
open class OvrBoundaryLookAndFeel : Structure {

    // Boundary color (alpha channel is ignored)
    @JvmField var color = OvrColorf()

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("color")

    constructor(color: OvrColorf) {

        this.color = color
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrBoundaryLookAndFeel(), Structure.ByReference
    class ByValue : OvrBoundaryLookAndFeel(), Structure.ByValue
}

/** Provides boundary test information  */
open class OvrBoundaryTestResult : Structure {

    // True if the boundary system is being triggered. Note that due to fade in/out effects this may not exactly match visibility.
    @JvmField var isTriggering = 0.toByte()

    // Distance to the closest play area or outer boundary surface.
    @JvmField var closestDistance = 0f

    // Closest point on the boundary surface.
    @JvmField var closestPoint = OvrVector3f()

    // Unit surface normal of the closest boundary surface.
    @JvmField var closestPointNormal = OvrVector3f()

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("isTriggering", "closestDistance", "closestPoint", "closestPointNormal")

    constructor(isTriggering: OvrBool, closestDistance: Float, closestPoint: OvrVector3f, closestPointNormal: OvrVector3f) {

        this.isTriggering = isTriggering
        this.closestDistance = closestDistance
        this.closestPoint = closestPoint
        this.closestPointNormal = closestPointNormal
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrBoundaryTestResult(), Structure.ByReference
    class ByValue : OvrBoundaryTestResult(), Structure.ByValue
}

/** Provides names for the left and right hand array indexes.
 *  \see ovrInputState, ovrTrackingState    */
enum class OvrHandType(@JvmField var i: Int) {

    ovrHand_Left(0),
    ovrHand_Right(1),
    ovrHand_Count(2)
}

/** ovrInputState describes the complete controller input state, including Oculus Touch, and XBox gamepad. If multiple inputs are connected and used at the same
 *  time, their inputs are combined.    */
open class OvrInputState : Structure {

    /** System type when the controller state was last updated. */
    @JvmField var timeInSeconds = 0.0

    /** Values for buttons described by ovrButton.  */
    @JvmField var buttons = 0

    /** Touch values for buttons and sensors as described by ovrTouch.  */
    @JvmField var touches = 0

    /** Left and right finger trigger values (ovrHand_Left and ovrHand_Right), in the range 0.0 to 1.0f.
     *  Returns 0 if the value would otherwise be less than 0.1176, for ovrControllerType_XBox.
     *  This has been formally named simply "Trigger". We retain the name IndexTrigger for backwards code compatibility.
     *  User-facing documentation should refer to it as the Trigger.    */
    @JvmField var indexTrigger = FloatArray(OvrHandType.ovrHand_Count.i)

    /** Left and right hand trigger values (ovrHand_Left and ovrHand_Right), in the range 0.0 to 1.0f.
     *  This has been formally named "Grip Button". We retain the name HandTrigger for backwards code compatibility.
     *  User-facing documentation should refer to it as the Grip Button or simply Grip. */
    @JvmField var handTrigger = FloatArray(OvrHandType.ovrHand_Count.i)

    /** Horizontal and vertical thumbstick axis values (ovrHand_Left and ovrHand_Right), in the range -1.0f to 1.0f.
     *  Returns a deadzone (value 0) per each axis if the value on that axis would otherwise have been between -.2746 to +.2746, for ovrControllerType_XBox */
    @JvmField var thumbstick = Array(ovrHand_Count.i, { OvrVector2f() })

    /** The type of the controller this state is for.   */
    @JvmField var controllerType = 0

    fun controllerType() = OvrControllerType.of(controllerType)

    /** Left and right finger trigger values (ovrHand_Left and ovrHand_Right), in the range 0.0 to 1.0f.
     *  Does not apply a deadzone.  Only touch applies a filter.
     *  This has been formally named simply "Trigger". We retain the name IndexTrigger for backwards code compatibility.
     *  User-facing documentation should refer to it as the Trigger.
     *  Added in 1.7    */
    @JvmField var indexTriggerNoDeadzone = FloatArray(ovrHand_Count.i)

    /** Left and right hand trigger values (ovrHand_Left and ovrHand_Right), in the range 0.0 to 1.0f.
     *  Does not apply a deadzone. Only touch applies a filter.
     *  This has been formally named "Grip Button". We retain the name HandTrigger for backwards code compatibility.
     *  User-facing documentation should refer to it as the Grip Button or simply Grip.
     *  Added in 1.7    */
    @JvmField var handTriggerNoDeadzone = FloatArray(ovrHand_Count.i)

    /** Horizontal and vertical thumbstick axis values (ovrHand_Left and ovrHand_Right), in the range -1.0f to 1.0f
     *  Does not apply a deadzone or filter.
     *  Added in 1.7    */
    @JvmField var thumbstickNoDeadzone = Array(ovrHand_Count.i, { OvrVector2f() })

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("timeInSeconds", "buttons", "touches", "indexTrigger", "handTrigger", "thumbstick", "controllerType",
            "indexTriggerNoDeadzone", "handTriggerNoDeadzone", "thumbstickNoDeadzone")

    constructor(timeInSeconds: Double, buttons: Int, touches: Int, indexTrigger: FloatArray, handTrigger: FloatArray, thumbstick: Array<OvrVector2f>,
                controllerType: OvrControllerType, indexTriggerNoDeadzone: FloatArray, handTriggerNoDeadzone: FloatArray, thumbstickNoDeadzone: Array<OvrVector2f>)
    : this(timeInSeconds, buttons, touches, indexTrigger, handTrigger, thumbstick, controllerType.i, indexTriggerNoDeadzone, handTriggerNoDeadzone,
            thumbstickNoDeadzone)

    constructor(timeInSeconds: Double, buttons: Int, touches: Int, indexTrigger: FloatArray, handTrigger: FloatArray, thumbstick: Array<OvrVector2f>,
                controllerType: Int, indexTriggerNoDeadzone: FloatArray, handTriggerNoDeadzone: FloatArray, thumbstickNoDeadzone: Array<OvrVector2f>) {

        this.timeInSeconds = timeInSeconds
        this.buttons = buttons
        this.touches = touches
        if (indexTrigger.size != this.indexTrigger.size) throw IllegalArgumentException("Wrong array size !")
        this.indexTrigger = indexTrigger
        if (handTrigger.size != this.handTrigger.size) throw IllegalArgumentException("Wrong array size !")
        this.handTrigger = handTrigger
        if (thumbstick.size != this.thumbstick.size) throw IllegalArgumentException("Wrong array size !")
        this.thumbstick = thumbstick
        this.controllerType = controllerType
        if (indexTriggerNoDeadzone.size != this.indexTriggerNoDeadzone.size) throw IllegalArgumentException("Wrong array size !")
        this.indexTriggerNoDeadzone = indexTriggerNoDeadzone
        if (handTriggerNoDeadzone.size != this.handTriggerNoDeadzone.size) throw IllegalArgumentException("Wrong array size !")
        this.handTriggerNoDeadzone = handTriggerNoDeadzone
        if (thumbstickNoDeadzone.size != this.thumbstickNoDeadzone.size) throw IllegalArgumentException("Wrong array size !")
        this.thumbstickNoDeadzone = thumbstickNoDeadzone
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrInputState(), Structure.ByReference
    class ByValue : OvrInputState(), Structure.ByValue
}

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------
// ***** Initialize structures

/** Initialization flags.
 *  \see ovrInitParams, ovr_Initialize  */
enum class OvrInitFlags(@JvmField val i: Int) {

    /** When a debug library is requested, a slower debugging version of the library will run which can be used to help solve problems in the library and debug
     *  application code.   */
    ovrInit_Debug(0x00000001),

    /** When a version is requested, the LibOVR runtime respects the RequestedMinorVersion field and verifies that the RequestedMinorVersion is supported.
     *  Normally when you specify this flag you simply use OVR_MINOR_VERSION for ovrInitParams::RequestedMinorVersion, though you could use a lower version
     *  than OVR_MINOR_VERSION to specify previous version behavior.    */
    ovrInit_RequestVersion(0x00000004),

    /** These bits are writable by user code.   */
    ovrinit_WritableBits(0x00ffffff)
}

/** Logging levels
 *  \see ovrInitParams, ovrLogCallback  */
enum class OvrLogLevel(@JvmField var i: Int) {

    ovrLogLevel_Debug(0), ///   < Debug-level log event.
    ovrLogLevel_Info(1), ///    < Info-level log event.
    ovrLogLevel_Error(2)  ///   < Error-level log event.
}

/** Parameters for ovr_Initialize.
 *  \see ovr_Initialize */
open class OvrInitParams : Structure {

    /** Flags from ovrInitFlags to override default behavior.
     *  Use 0 for the defaults. */
    @JvmField var Flags = 0

    /** Requests a specific minor version of the LibOVR runtime.
     *  Flags must include ovrInit_RequestVersion or this will be ignored and OVR_MINOR_VERSION will be used. If you are directly calling the LibOVRRT version
     *  of ovr_Initialize in the LibOVRRT DLL then this must be valid and include ovrInit_RequestVersion.   */
    @JvmField var RequestedMinorVersion = 0

    /** User-supplied log callback function, which may be called at any time asynchronously from multiple threads until ovr_Shutdown completes.
     *  Use NULL to specify no log callback.    */
    //TODO check ovrLogCallback LogCallback;
    @JvmField var LogCallback: Pointer? = null

    /** User-supplied data which is passed as-is to LogCallback. Typically this is used to store an application-specific pointer which is read in the callback
     *  function.   */
    //TODO check uintptr_t      UserData;
    @JvmField var UserData = 0

    /** Relative number of milliseconds to wait for a connection to the server before failing. Use 0 for the default timeout.   */
    @JvmField var ConnectionTimeoutMS = 0

    @JvmField var Pad0 = ByteArray(4) ///< \internal

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("Flags", "RequestedMinorVersion", "LogCallback", "UserData", "ConnectionTimeoutMS", "Pad0")

    constructor(Flags: Int, RequestedMinorVersion: Int, LogCallback: Pointer?, UserData: Int, ConnectionTimeoutMS: Int) {

        this.Flags = Flags
        this.RequestedMinorVersion = RequestedMinorVersion
        this.LogCallback = LogCallback
        this.UserData = UserData
        this.ConnectionTimeoutMS = ConnectionTimeoutMS
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrInitParams(), Structure.ByReference
    class ByValue : OvrInitParams(), Structure.ByValue
}

// ----------------------------------------------------------------------------------------------------------------------------------------------------------------
// ***** API Interfaces


/** Initializes LibOVR
 *
 *  Initialize LibOVR for application usage. This includes finding and loading the LibOVRRT shared library. No LibOVR API functions, other than
 *  ovr_GetLastErrorInfo and ovr_Detect, can be called unless ovr_Initialize succeeds. A successful call to ovr_Initialize must be eventually followed by a call
 *  to ovr_Shutdown. ovr_Initialize calls are idempotent.
 *  Calling ovr_Initialize twice does not require two matching calls to ovr_Shutdown.
 *  If already initialized, the return value is ovr_Success.
 *
 *  LibOVRRT shared library search order:
 *       -# Current working directory (often the same as the application directory).
 *       -# Module directory (usually the same as the application directory, but not if the module is a separate shared library).
 *       -# Application directory
 *       -# Development directory (only if OVR_ENABLE_DEVELOPER_SEARCH is enabled, which is off by default).
 *       -# Standard OS shared library search location(s) (OS-specific).
 *
 *  \param params Specifies custom initialization options. May be NULL to indicate default options when using the CAPI shim. If you are directly calling the
 *  LibOVRRT version of ovr_Initialize in the LibOVRRT DLL then this must be valid and include ovrInit_RequestVersion.
 *  \return Returns an ovrResult indicating success or failure. In the case of failure, use ovr_GetLastErrorInfo to get more information. Example failed results
 *  include:
 *      - ovrError_Initialize: Generic initialization error.
 *      - ovrError_LibLoad: Couldn't load LibOVRRT.
 *      - ovrError_LibVersion: LibOVRRT version incompatibility.
 *      - ovrError_ServiceConnection: Couldn't connect to the OVR Service.
 *      - ovrError_ServiceVersion: OVR Service version incompatibility.
 *      - ovrError_IncompatibleOS: The operating system version is incompatible.
 *      - ovrError_DisplayInit: Unable to initialize the HMD display.
 *      - ovrError_ServerStart:  Unable to start the server. Is it already running?
 *      - ovrError_Reinitialization: Attempted to re-initialize with a different version.
 *
 *  <b>Example code</b>
 *      \code{.cpp}
 *          ovrInitParams initParams = { ovrInit_RequestVersion, OVR_MINOR_VERSION, NULL, 0, 0 };
 *          ovrResult result = ovr_Initialize(&initParams);
 *          if(OVR_FAILURE(result)) {
 *              ovrErrorInfo errorInfo;
 *              ovr_GetLastErrorInfo(&errorInfo);
 *              DebugLog("ovr_Initialize failed: %s", errorInfo.ErrorString);
 *              return false;
 *          }
 *          [...]
 *      \endcode
 *
 *  \see ovr_Shutdown   */
external fun ovr_Initialize(params: OvrInitParams.ByReference): OvrResult

/** Shuts down LibOVR
 *
 *  A successful call to ovr_Initialize must be eventually matched by a call to ovr_Shutdown.
 *  After calling ovr_Shutdown, no LibOVR functions can be called except ovr_GetLastErrorInfo or another ovr_Initialize. ovr_Shutdown invalidates all pointers,
 *  references, and created objects previously returned by LibOVR functions. The LibOVRRT shared library can be unloaded by ovr_Shutdown.
 *
 *  \see ovr_Initialize */
external fun ovr_Shutdown()

/** Returns information about the most recent failed return value by the current thread for this library.
 *
 *  This function itself can never generate an error.
 *  The last error is never cleared by LibOVR, but will be overwritten by new errors.
 *  Do not use this call to determine if there was an error in the last API call as successful API calls don't clear the last ovrErrorInfo.
 *  To avoid any inconsistency, ovr_GetLastErrorInfo should be called immediately after an API function that returned a failed ovrResult, with no other API
 *  functions called in the interim.
 *
 *  \param[out] errorInfo The last ovrErrorInfo for the current thread.
 *  \see ovrErrorInfo   */
external fun ovr_GetLastErrorInfo(errorInfo: OvrErrorInfo.ByReference);

/** Returns the version string representing the LibOVRRT version.
 *
 *  The returned string pointer is valid until the next call to ovr_Shutdown.
 *
 *  Note that the returned version string doesn't necessarily match the current OVR_MAJOR_VERSION, etc., as the returned string refers to the LibOVRRT shared
 *  library version and not the locally compiled interface version.
 *
 *  The format of this string is subject to change in future versions and its contents should not be interpreted.
 *
 *  \return Returns a UTF8-encoded null-terminated version string.   */
external fun ovr_GetVersionString(): String

/** Writes a message string to the LibOVR tracing mechanism (if enabled).
 *
 *  This message will be passed back to the application via the ovrLogCallback if it was registered.
 *
 *  \param[in ] level One of the ovrLogLevel constants.
 *  \param[in ] message A UTF8-encoded null-terminated string.
 *  \return returns the strlen of the message or a negative value if the message is too large.
 *
 *  \see ovrLogLevel, ovrLogCallback    */
fun ovr_TraceMessage(level: OvrLogLevel, message: String) = ovr_TraceMessage(level.i, message)

external fun ovr_TraceMessage(level: Int, message: String): Int


/** Identify client application info.
 *
 *  The string is one or more newline-delimited lines of optional info indicating engine name, engine version, engine plugin name, engine plugin version, engine
 *  editor. The order of the lines is not relevant. Individual lines are optional. A newline is not necessary at the end of the last line.
 *  Call after ovr_Initialize and before the first call to ovr_Create.
 *  Each value is limited to 20 characters. Key names such as 'EngineName:' 'EngineVersion:' do not count towards this limit.
 *
 *  \param[in] identity Specifies one or more newline-delimited lines of optional info:
 *              EngineName: %s\n
 *              EngineVersion: %s\n
 *              EnginePluginName: %s\n
 *              EnginePluginVersion: %s\n
 *              EngineEditor: <boolean> ('true' or 'false')\n
 *
 *  <b>Example code</b>
 *      \code{.cpp}
 *      ovr_IdentifyClient("EngineName: Unity\n"
 *                         "EngineVersion: 5.3.3\n"
 *                         "EnginePluginName: OVRPlugin\n"
 *                         "EnginePluginVersion: 1.2.0\n"
 *                         "EngineEditor: true");
 *      \endcode    */
external fun ovr_IdentifyClient(identity: String): OvrResult

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------
/** @name HMD Management
 *
 *  Handles the enumeration, creation, destruction, and properties of an HMD (head-mounted display).*/

/** Returns information about the current HMD .
 *
 *  ovr_Initialize must have first been called in order for this to succeed, otherwise ovrHmdDesc::Type will be reported as ovrHmd_None.
 *
 *  \param[ in ] session Specifies an ovrSession previously returned by ovr_Create, else NULL in which case this function detects whether an HMD is present and
 *  returns its info if so.
 *
 *  \return Returns an ovrHmdDesc. If the hmd is NULL and ovrHmdDesc::Type is ovrHmd_None then no HMD is present .  */
external fun ovr_GetHmdDesc(session: OvrSession): OvrHmdDesc.ByValue

/** Returns the number of attached trackers .
 *
 *  The number of trackers may change at any time, so this function should be called before use as opposed to once on startup.
 *
 *  \param[ in ] session Specifies an ovrSession previously returned by ovr_Create. *
 *  \return Returns unsigned int count. */
external fun ovr_GetTrackerCount(session: OvrSession): Int

/** Returns a given attached tracker description .
 *
 *  ovr_Initialize must have first been called in order for this to succeed, otherwise the returned trackerDescArray will be zero -initialized.The data returned
 *  by this function can change at runtime .
 *
 *  \param[ in ] session Specifies an ovrSession previously returned by ovr_Create. *
 *  \param[ in ] trackerDescIndex Specifies a tracker index. The valid indexes are in the range of 0 to the tracker count returned by ovr_GetTrackerCount . *
 *  \return Returns ovrTrackerDesc. An empty ovrTrackerDesc will be returned if trackerDescIndex is out of range. *
 *  \see ovrTrackerDesc, ovr_GetTrackerCount    */
external fun ovr_GetTrackerDesc(session: OvrSession, trackerDescIndex: Int): OvrTrackerDesc

/** Creates a handle to a VR session.
 *
 *  Upon success the returned ovrSession must be eventually freed with ovr_Destroy when it is no longer needed .
 *  A second call to ovr_Create will result in an error return value if the previous session has not been destroyed.
 *
 *  \param[out ] pSession Provides a pointer to an ovrSession which will be written to upon success.
 *  \param[out ] luid Provides a system specific graphics adapter identifier that locates which graphics adapter has the HMD attached . This must match the
 *      adapter used by the application or no rendering output will be possible.This is important for stability on multi - adapter systems . An application that
 *      simply chooses the default adapter will not run reliably on multi - adapter systems .
 *  \return Returns an ovrResult indicating success or failure. Upon failure the returned ovrSession will be NULL .
 *
 *  <b>Example code</b>
 *      \code{.cpp}
 *          ovrSession session;
 *          ovrGraphicsLuid luid;
 *          ovrResult result = ovr_Create (&session, &luid);
 *          if (OVR_FAILURE(result))
 *             ...
 *      \endcode
 *
 *  \see ovr_Destroy    */
external fun ovr_Create(pSession: OvrSession_ByReference, pLuid: OvrGraphicsLuid.ByReference): OvrResult

/** Destroys the session.
 *
 *  \param[ in ] session Specifies an ovrSession previously returned by ovr_Create.
 *  \see ovr_Create */
external fun ovr_Destroy(session: OvrSession)

/** Specifies status information for the current session.
 *
 *  \see ovr_GetSessionStatus   */
open class OvrSessionStatus : Structure {

    @JvmField var isVisible: OvrBool = ovrFalse     ///    < True if the process has VR focus and thus is visible in the HMD.
    fun isVisible() = isVisible == ovrTrue
    @JvmField var hmdPresent: OvrBool = ovrFalse   ///     < True if an HMD is present.
    fun hmdPresent() = hmdPresent == ovrTrue
    @JvmField var hmdMounted: OvrBool = ovrFalse   ///     < True if the HMD is on the user's head.
    fun hmdMounted() = isVisible == ovrTrue
    @JvmField var displayLost: OvrBool = ovrFalse  ///     < True if the session is in a display-lost state. See ovr_SubmitFrame.
    fun displayLost() = displayLost == ovrTrue
    @JvmField var shouldQuit: OvrBool = ovrFalse   ///     < True if the application should initiate shutdown.
    fun shouldQuit() = shouldQuit == ovrTrue
    @JvmField var shouldRecenter: OvrBool = ovrFalse  ///  < True if UX has requested re-centering. Must call ovr_ClearShouldRecenterFlag or ovr_RecenterTrackingOrigin.
    fun shouldRecenter() = shouldRecenter == ovrTrue

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("isVisible", "hmdPresent", "hmdMounted", "displayLost", "shouldQuit", "shouldRecenter")

    constructor(isVisible: Boolean, hmdPresent: Boolean, hmdMounted: Boolean, displayLost: Boolean, shouldQuit: Boolean, shouldRecenter: Boolean)
    : this(if (isVisible) ovrTrue else ovrFalse, if (hmdPresent) ovrTrue else ovrFalse, if (hmdMounted) ovrTrue else ovrFalse,
            if (displayLost) ovrTrue else ovrFalse, if (shouldQuit) ovrTrue else ovrFalse, if (shouldRecenter) ovrTrue else ovrFalse)

    constructor(isVisible: OvrBool, hmdPresent: OvrBool, hmdMounted: OvrBool, displayLost: OvrBool, shouldQuit: OvrBool, shouldRecenter: OvrBool) {

        this.isVisible = isVisible
        this.hmdPresent = hmdPresent
        this.hmdMounted = hmdMounted
        this.displayLost = displayLost
        this.shouldQuit = shouldQuit
        this.shouldRecenter = shouldRecenter
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrSessionStatus(), Structure.ByReference
    class ByValue : OvrSessionStatus(), Structure.ByValue
}

/** Returns status information for the application.
 *
 *  \param[in] session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[out] sessionStatus Provides an ovrSessionStatus that is filled in.
 *
 *  \return Returns an ovrResult indicating success or failure. In the case of failure, use ovr_GetLastErrorInfo to get more information.
//          Return values include but aren't limited to:
 *      - ovrSuccess: Completed successfully.
 *      - ovrError_ServiceConnection: The service connection was lost and the application must destroy the session.     */
external fun ovr_GetSessionStatus(session: OvrSession, sessionStatus: OvrSessionStatus.ByReference): OvrResult

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------
/** @name Tracking
 *
 *  Tracking functions handle the position, orientation, and movement of the HMD in space.
 *
 *  All tracking interface functions are thread-safe, allowing tracking state to be sampled from different threads. */


/** Sets the tracking origin type
 *
 *  When the tracking origin is changed, all of the calls that either provide or accept ovrPosef will use the new tracking origin provided .
 *
 *  \param[ in ] session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[ in ] origin Specifies an ovrTrackingOrigin to be used for all ovrPosef
 *
 *  \return Returns an ovrResult indicating success or failure. In the case of failure, use ovr_GetLastErrorInfo to get more information.
 *
 *  \see ovrTrackingOrigin, ovr_GetTrackingOriginType   */
fun ovr_SetTrackingOriginType(session: OvrSession, origin: OvrTrackingOrigin) = ovr_SetTrackingOriginType(session, origin.i)

external fun ovr_SetTrackingOriginType(session: OvrSession, origin: Int): OvrResult


/** Gets the tracking origin state
 *
 *  \param[ in ] session Specifies an ovrSession previously returned by ovr_Create.
 *
 *  \return Returns the ovrTrackingOrigin that was either set by default, or previous set by the application.
 *
 *  \see ovrTrackingOrigin, ovr_SetTrackingOriginType   */
fun ovr_GetTrackingOriginType_(session: OvrSession) = OvrTrackingOrigin.of(ovr_GetTrackingOriginType(session))

external fun ovr_GetTrackingOriginType(session: OvrSession): Int

/** Re-centers the sensor position and orientation.
 *
 *  This resets the (x,y,z) positional components and the yaw orientation component.
 *  The Roll and pitch orientation components are always determined by gravity and cannot be redefined. All future tracking will report values relative to this
 *  new reference position.
 *  If you are using ovrTrackerPoses then you will need to call ovr_GetTrackerPose after this, because the sensor position(s) will change as a result of this.
 *
 *  The headset cannot be facing vertically upward or downward but rather must be roughly level otherwise this function will fail with
 *  ovrError_InvalidHeadsetOrientation.
 *
 *  For more info, see the notes on each ovrTrackingOrigin enumeration to understand how recenter will vary slightly in its behavior based on the current
 *  ovrTrackingOrigin setting.
 *
 *  \param[in] session Specifies an ovrSession previously returned by ovr_Create.
 *
 *  \return Returns an ovrResult indicating success or failure. In the case of failure, use ovr_GetLastErrorInfo to get more information. Return values include
 *  but aren't limited to:
 *      - ovrSuccess: Completed successfully.
 *      - ovrError_InvalidHeadsetOrientation: The headset was facing an invalid direction when
 *        attempting recentering, such as facing vertically.
 *
 *  \see ovrTrackingOrigin, ovr_GetTrackerPose  */
external fun ovr_RecenterTrackingOrigin(session: OvrSession): OvrResult

/** Clears the ShouldRecenter status bit in ovrSessionStatus.
 *
 *  Clears the ShouldRecenter status bit in ovrSessionStatus, allowing further recenter requests to be detected. Since this is automatically done by
 *  ovr_RecenterTrackingOrigin, this is only needs to be called when application is doing its own re-centering. */
external fun ovr_ClearShouldRecenterFlag(session: OvrSession)

/** Returns tracking state reading based on the specified absolute system time.
 *
 *  Pass an absTime value of 0.0 to request the most recent sensor reading. In this case both PredictedPose and SamplePose will have the same value.
 *
 *  This may also be used for more refined timing of front buffer rendering logic, and so on.
 *  This may be called by multiple threads.
 *
 *  \param[in] session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[in] absTime Specifies the absolute future time to predict the return ovrTrackingState value. Use 0 to request the most recent tracking state.
 *  \param[in] latencyMarker Specifies that this call is the point in time where the "App-to-Mid-Photon" latency timer starts from. If a given ovrLayer provides
 *      "SensorSampleTime", that will override the value stored here.
 *  \return Returns the ovrTrackingState that is predicted for the given absTime.
 *
 *  \see ovrTrackingState, ovr_GetEyePoses, ovr_GetTimeInSeconds    */
fun ovr_GetTrackingState(session: OvrSession, absTime: Double, latencyMarker: Boolean)
        = ovr_GetTrackingState(session, absTime, if (latencyMarker) ovrTrue else ovrFalse)

external fun ovr_GetTrackingState(session: OvrSession, absTime: Double, latencyMarker: OvrBool): OvrTrackingState


/** Returns the ovrTrackerPose for the given attached tracker.
 *
 *  \param[in] session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[in] trackerPoseIndex Index of the tracker being requested.
 *
 *  \return Returns the requested ovrTrackerPose. An empty ovrTrackerPose will be returned if trackerPoseIndex is out of range.
 *
 *  \see ovr_GetTrackerCount    */
external fun ovr_GetTrackerPose(session: OvrSession, trackerPoseIndex: Int): OvrTrackerPose


/** Returns the most recent input state for controllers, without positional tracking info.
 *
 *  \param[out ] inputState Input state that will be filled in .
 *  \param[in ] ovrControllerType Specifies which controller the input will be returned for.
 *  \return Returns ovrSuccess if the new state was successfully obtained.
 *
 *  \see ovrControllerType  */
fun ovr_GetInputState(session: OvrSession, controllerType: OvrControllerType, inputState: OvrInputState.ByReference)
        = ovr_GetInputState(session, controllerType.i, inputState)

external fun ovr_GetInputState(session: OvrSession, controllerType: Int, inputState: OvrInputState.ByReference): OvrResult


/** Returns controller types connected to the system OR'ed together.
 *
 *  \return A bitmask of ovrControllerTypes connected to the system.
 *
 *  \see ovrControllerType  */
external fun ovr_GetConnectedControllerTypes(session: OvrSession): Int


/** Gets information about Haptics engine for the specified Touch controller.
 *
 *  \param[in ] session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[in ] controllerType The controller to retrieve the information from.
 *
 *  \return Returns an ovrTouchHapticsDesc. */
fun ovr_GetTouchHapticsDesc(session: OvrSession, controllerType: OvrControllerType) = ovr_GetTouchHapticsDesc(session, controllerType.i)

external fun ovr_GetTouchHapticsDesc(session: OvrSession, controllerType: Int): OvrTouchHapticsDesc


/** Sets constant vibration (with specified frequency and amplitude) to a controller.
 *  Note: ovr_SetControllerVibration cannot be used interchangeably with ovr_SubmitControllerVibration.
 *
 *  This method should be called periodically, vibration lasts for a maximum of 2.5 seconds.
 *
 *  \param[in ] session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[in ] controllerType The controller to set the vibration to.
 *  \param[in ] frequency Vibration frequency. Supported values are: 0.0 (disabled), 0.5 and 1.0. Non valid values will be clamped.
 *  \param[in ] amplitude Vibration amplitude in the [0.0, 1.0] range.
 *  \return Returns an ovrResult for which OVR_SUCCESS(result) is false upon error and true upon success. Return values include but aren't limited to:
 *      - ovrSuccess: The call succeeded and a result was returned.
 *      - ovrSuccess_DeviceUnavailable: The call succeeded but the device referred to by controllerType is not available.   */
fun ovr_SetControllerVibration(session: OvrSession, controllerType: OvrControllerType, frequency: Float, amplitude: Float)
        = ovr_SetControllerVibration(session, controllerType.i, frequency, amplitude)

external fun ovr_SetControllerVibration(session: OvrSession, controllerType: Int, frequency: Float, amplitude: Float): OvrResult


/** Submits a Haptics buffer (used for vibration) to Touch (only) controllers.
 *  Note: ovr_SubmitControllerVibration cannot be used interchangeably with ovr_SetControllerVibration.
 *
 *  \param[in ] session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[in ] controllerType Controller where the Haptics buffer will be played.
 *  \param[in ] buffer Haptics buffer containing amplitude samples to be played.
 *  \return Returns an ovrResult for which OVR_SUCCESS(result) is false upon error and true upon success. Return values include but aren't limited to:
 *      - ovrSuccess: The call succeeded and a result was returned.
 *      - ovrSuccess_DeviceUnavailable: The call succeeded but the device referred to by controllerType is not available.*
 *
 *  \see ovrHapticsBuffer    */
fun ovr_SubmitControllerVibration(session: OvrSession, controllerType: OvrControllerType, buffer: OvrHapticsBuffer.ByReference)
        = ovr_SubmitControllerVibration(session, controllerType.i, buffer)

external fun ovr_SubmitControllerVibration(session: OvrSession, controllerType: Int, buffer: OvrHapticsBuffer.ByReference): OvrResult


/** Gets the Haptics engine playback state of a specific Touch controller.
 *
 *  \param[in] session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[in] controllerType Controller where the Haptics buffer wil be played.
 *  \param[in] outState State of the haptics engine.
 *  \return Returns an ovrResult for which OVR_SUCCESS(result) is false upon error and true upon success. Return values include but aren't limited to:
 *      - ovrSuccess: The call succeeded and a result was returned.
 *      - ovrSuccess_DeviceUnavailable: The call succeeded but the device referred to by controllerType is not available.
 *
 *  \see ovrHapticsPlaybackState    */
fun ovr_GetControllerVibrationState(session: OvrSession, controllerType: OvrControllerType, outState: OvrHapticsPlaybackState.ByReference)
        = ovr_GetControllerVibrationState(session, controllerType.i, outState)

external fun ovr_GetControllerVibrationState(session: OvrSession, controllerType: Int, outState: OvrHapticsPlaybackState.ByReference): OvrResult


/** Tests collision/proximity of position tracked devices (e.g. HMD and/or Touch) against the Boundary System.
 *  Note: this method is similar to ovr_BoundaryTestPoint but can be more precise as it may take into account device acceleration/momentum.
 *
 *  \param[in ] session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[in ] deviceBitmask Bitmask of one or more tracked devices to test.
 *  \param[in ] boundaryType Must be either ovrBoundary_Outer or ovrBoundary_PlayArea.
 *  \param[out ] outTestResult Result of collision/proximity test, contains information such as distance and closest point.
 *  \return Returns an ovrResult for which OVR_SUCCESS(result) is false upon error and true upon success. Return values include but aren't limited to:
 *      - ovrSuccess: The call succeeded and a result was returned.
 *      - ovrSuccess_BoundaryInvalid: The call succeeded but the result is not a valid boundary due to not being set up.
 *      - ovrSuccess_DeviceUnavailable: The call succeeded but the device referred to by deviceBitmask is not available.
 *
 *  \see ovrBoundaryTestResult  */
fun ovr_TestBoundary(session: OvrSession, deviceBitmask: OvrTrackedDeviceType, boundaryType: OvrBoundaryType, outTestResult: OvrBoundaryTestResult.ByReference)
        = ovr_TestBoundary(session, deviceBitmask.i, boundaryType.i, outTestResult)

external fun ovr_TestBoundary(session: OvrSession, deviceBitmask: Int, boundaryType: Int, outTestResult: OvrBoundaryTestResult.ByReference): OvrResult


/** Tests collision/proximity of a 3D point against the Boundary System.
 *
 *  \param[in ] session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[in ] point 3D point to test.
 *  \param[in ] singleBoundaryType Must be either ovrBoundary_Outer or ovrBoundary_PlayArea to test against
 *  \param[out ] outTestResult Result of collision/proximity test, contains information such as distance and closest point.
 *  \return Returns an ovrResult for which OVR_SUCCESS(result) is false upon error and true upon success. Return values include but aren't limited to:
 *      - ovrSuccess: The call succeeded and a result was returned.
 *      - ovrSuccess_BoundaryInvalid: The call succeeded but the result is not a valid boundary due to not being set up.
 *
 *  \see ovrBoundaryTestResult  */
fun ovr_TestBoundaryPoint(session: OvrSession, point: OvrVector3f.ByReference, singleBoundaryType: OvrBoundaryType,
                          outTestResult: OvrBoundaryTestResult.ByReference) = ovr_TestBoundaryPoint(session, point, singleBoundaryType.i, outTestResult)

external fun ovr_TestBoundaryPoint(session: OvrSession, point: OvrVector3f.ByReference, singleBoundaryType: Int, outTestResult: OvrBoundaryTestResult.ByReference)
        : OvrResult


/** Sets the look and feel of the Boundary System.
 *
 *  \param[in ] session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[in ] lookAndFeel Look and feel parameters.
 *  \return Returns ovrSuccess upon success.
 *  \see ovrBoundaryLookAndFeel  */
external fun ovr_SetBoundaryLookAndFeel(session: OvrSession, lookAndFeel: OvrBoundaryLookAndFeel.ByReference): OvrResult

/** Resets the look and feel of the Boundary System to its default state.
 *
 *  \param[in ] session Specifies an ovrSession previously returned by ovr_Create.
 *  \return Returns ovrSuccess upon success.
 *  \see ovrBoundaryLookAndFeel */
external fun ovr_ResetBoundaryLookAndFeel(session: OvrSession): OvrResult

/** Gets the geometry of the Boundary System's "play area" or "outer boundary" as 3D floor points.
 *
 *  \param[in] session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[in] boundaryType Must be either ovrBoundary_Outer or ovrBoundary_PlayArea.
 *  \param[out] outFloorPoints Array of 3D points (in clockwise order) defining the boundary at floor height (can be NULL to retrieve only the number of points).
 *  \param[out] outFloorPointsCount Number of 3D points returned in the array.
 *  \return Returns an ovrResult for which OVR_SUCCESS(result) is false upon error and true upon success. Return values include but aren't limited to:
 *      - ovrSuccess: The call succeeded and a result was returned.
 *      - ovrSuccess_BoundaryInvalid: The call succeeded but the result is not a valid boundary due to not being set up.    */
fun ovr_GetBoundaryGeometry(session: OvrSession, boundaryType: OvrBoundaryType, outFloorPoints: OvrVector3f.ByReference, outFloorPointsCount: IntByReference)
        = ovr_GetBoundaryGeometry(session, boundaryType.i, outFloorPoints, outFloorPointsCount)

external fun ovr_GetBoundaryGeometry(session: OvrSession, boundaryType: Int, outFloorPoints: OvrVector3f.ByReference, outFloorPointsCount: IntByReference)
        : OvrResult


/** Gets the dimension of the Boundary System's "play area" or "outer boundary".
 *
 *  \param[in ] session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[in ] boundaryType Must be either ovrBoundary_Outer or ovrBoundary_PlayArea.
 *  \param[out ] dimensions Dimensions of the axis aligned bounding box that encloses the area in meters (width, height and length).
 *  \return Returns an ovrResult for which OVR_SUCCESS(result) is false upon error and true upon success. Return values include but aren't limited to:
 *      - ovrSuccess: The call succeeded and a result was returned.
 *      - ovrSuccess_BoundaryInvalid: The call succeeded but the result is not a valid boundary due to not being set up.    */
fun ovr_GetBoundaryDimensions(session: OvrSession, boundaryType: OvrBoundaryType, outDimensions: OvrVector3f.ByReference)
        = ovr_GetBoundaryDimensions(session, boundaryType.i, outDimensions)

external fun ovr_GetBoundaryDimensions(session: OvrSession, boundaryType: Int, outDimensions: OvrVector3f.ByReference): OvrResult


/** Returns if the boundary is currently visible.
 *  Note: visibility is false if the user has turned off boundaries, otherwise, it's true if the app has requested boundaries to be visible or if any tracked
 *  device is currently triggering it. This may not exactly match rendering due to fade-in and fade-out effects.
 *
 *  \param[in ] session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[out ] outIsVisible ovrTrue, if the boundary is visible.
 *  \return Returns an ovrResult for which OVR_SUCCESS(result) is false upon error and true upon success. Return values include but aren't limited to:
 *      - ovrSuccess: Result was successful and a result was returned.
 *      - ovrSuccess_BoundaryInvalid: The call succeeded but the result is not a valid boundary due to not being set up.    */
external fun ovr_GetBoundaryVisible(session: OvrSession, outIsVisible: OvrBool_ByReference): OvrResult

/** Requests boundary to be visible.
 *
 *  \param[in ] session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[in ] visible forces the outer boundary to be visible. An application can't force it to be invisible, but can cancel its request by passing false.

 *  \return Returns ovrSuccess upon success.    */
fun ovr_RequestBoundaryVisible(session: OvrSession, visible: Boolean) = ovr_RequestBoundaryVisible(session, if (visible) ovrTrue else ovrFalse)

external fun ovr_RequestBoundaryVisible(session: OvrSession, visible: OvrBool): OvrResult


//-----------------------------------------------------------------------------------------------------------------------------------------------------------------
// @name Layers
//
///@{


/** Specifies the maximum number of layers supported by ovr_SubmitFrame.
 *
 *  /see ovr_SubmitFrame    */
const val ovrMaxLayerCount = 16

/** Describes layer types that can be passed to ovr_SubmitFrame.
 *  Each layer type has an associated struct, such as ovrLayerEyeFov.
 *
 *  \see ovrLayerHeader */
enum class OvrLayerType(@JvmField val i: Int) {

    ovrLayerType_Disabled(0), ///   < Layer is disabled.
    ovrLayerType_EyeFov(1), ///     < Described by ovrLayerEyeFov.
    ovrLayerType_Quad(3), ///       < Described by ovrLayerQuad. Previously called ovrLayerType_QuadInWorld.
    ///  enum 4 used to be ovrLayerType_QuadHeadLocked. Instead, use ovrLayerType_Quad with ovrLayerFlag_HeadLocked.
    ovrLayerType_EyeMatrix(5);  /// < Described by ovrLayerEyeMatrix.

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}


/** Identifies flags used by ovrLayerHeader and which are passed to ovr_SubmitFrame.
 *
 *  \see ovrLayerHeader  */
enum class OvrLayerFlags(@JvmField val i: Int) {

    /** ovrLayerFlag_HighQuality enables 4 x anisotropic sampling during the composition of the layer .
     *  The benefits are mostly visible at the periphery for high - frequency & high-contrast visuals.
     *  For best results consider combining this flag with an ovrTextureSwapChain that has mipmaps and instead of using arbitrary sized textures, prefer texture
     *  sizes that are powers-of-two.
     *  Actual rendered viewport and doesn't necessarily have to fill the whole texture. */
    ovrLayerFlag_HighQuality(0x01),

    /** ovrLayerFlag_TextureOriginAtBottomLeft: the opposite is TopLeft.
     *  Generally this is false for D3D, true for OpenGL.    */
    ovrLayerFlag_TextureOriginAtBottomLeft(0x02),

    /** Mark this surface as "headlocked", which means it is specified relative to the HMD and moves with it, rather than being specified relative to
     *  sensor / torso space and remaining still while the head moves.
     *  What used to be ovrLayerType_QuadHeadLocked is now ovrLayerType_Quad plus this flag.
     *  However the flag can be applied to any layer type to achieve a similar effect.   */
    ovrLayerFlag_HeadLocked(0x04)

}

/** Defines properties shared by all ovrLayer structs, such as ovrLayerEyeFov.
 *
 *  ovrLayerHeader is used as a base member in these larger structs.
 *  This struct cannot be used by itself except for the case that Type is ovrLayerType_Disabled.
 *
 *  \see ovrLayerType, ovrLayerFlags    */
open class OvrLayerHeader : OvrLayer_Union {

    @JvmField var type = 0  ///< Described by ovrLayerType.
    fun type() = OvrLayerType.of(type)
    @JvmField var flags = 0 ///< Described by ovrLayerFlags.

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("type", "flags")

    constructor(type: OvrLayerType, flags: Int) : this(type.i, flags)

    constructor(type: Int, flags: Int) {

        this.type = type
        this.flags = flags
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrLayerHeader(), Structure.ByReference
    class ByValue : OvrLayerHeader(), Structure.ByValue
}

/** Describes a layer that specifies a monoscopic or stereoscopic view.
 *  This is the kind of layer that's typically used as layer 0 to ovr_SubmitFrame, as it is the kind of layer used to render a 3D stereoscopic view.
 *
 *  Three options exist with respect to mono/stereo texture usage:
 *     - ColorTexture[0] and ColorTexture[1] contain the left and right stereo renderings, respectively.
 *          Viewport[0] and Viewport[1] refer to ColorTexture[0] and ColorTexture[1], respectively.
 *     - ColorTexture[0] contains both the left and right renderings, ColorTexture[1] is NULL, and Viewport[0] and Viewport[1] refer to sub-rects with
 *         ColorTexture[0].
 *     - ColorTexture[0] contains a single monoscopic rendering, and Viewport[0] and Viewport[1] both refer to that rendering.
 *
 *  \see ovrTextureSwapChain, ovr_SubmitFrame   */
open class OvrLayerEyeFov : OvrLayer_Union {

    /** Header.Type must be ovrLayerType_EyeFov.    */
    @JvmField var header = OvrLayerHeader()

    /** ovrTextureSwapChains for the left and right eye respectively.
     *  The second one of which can be NULL for cases described above. */
    @JvmField var colorTexture = Array(ovrEye_Count.i, { OvrTextureSwapChain(0L) });

    /** Specifies the ColorTexture sub-rect UV coordinates.
     *  Both Viewport[0] and Viewport[1] must be valid.  */
    @JvmField var viewport = Array(ovrEye_Count.i, { OvrRecti() })

    /** The viewport field of view. */
    @JvmField var fov = Array(ovrEye_Count.i, { OvrFovPort() })

    /** Specifies the position and orientation of each eye view, with the position specified in meters.
     *  RenderPose will typically be the value returned from ovr_CalcEyePoses, but can be different in special cases if a different head pose is used for
     *  rendering. */
    @JvmField var renderPose = Array(ovrEye_Count.i, { OvrPosef() })

    /** Specifies the timestamp when the source ovrPosef (used in calculating RenderPose) was sampled from the SDK. Typically retrieved by calling
     *  ovr_GetTimeInSeconds around the instant the application calls ovr_GetTrackingState
     *  The main purpose for this is to accurately track app tracking latency.   */
    @JvmField var sensorSampleTime = 0.0

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("header", "colorTexture", "viewport", "fov", "renderPose", "sensorSampleTime")

    constructor(header: OvrLayerHeader, colorTexture: Array<OvrTextureSwapChain>, viewport: Array<OvrRecti>, fov: Array<OvrFovPort>, renderPose: Array<OvrPosef>,
                sensorSampleTime: Double) {

        this.header = header
        if (colorTexture.size != this.colorTexture.size) throw IllegalArgumentException("Wrong array size !")
        this.colorTexture = colorTexture
        if (viewport.size != this.viewport.size) throw IllegalArgumentException("Wrong array size !")
        this.viewport = viewport
        if (fov.size != this.fov.size) throw IllegalArgumentException("Wrong array size !")
        this.fov = fov
        if (renderPose.size != this.renderPose.size) throw IllegalArgumentException("Wrong array size !")
        this.renderPose = renderPose
        this.sensorSampleTime = sensorSampleTime
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrLayerEyeFov(), Structure.ByReference
    class ByValue : OvrLayerEyeFov(), Structure.ByValue
}

/** Describes a layer that specifies a monoscopic or stereoscopic view.
 *  This uses a direct 3x4 matrix to map from view space to the UV coordinates.
 *  It is essentially the same thing as ovrLayerEyeFov but using a much lower level. This is mainly to provide compatibility with specific apps.
 *  Unless the application really requires this flexibility, it is usually better to use ovrLayerEyeFov.
 *
 *  Three options exist with respect to mono/stereo texture usage:
 *     - ColorTexture[0] and ColorTexture[1] contain the left and right stereo renderings, respectively.
 *          Viewport[0] and Viewport[1] refer to ColorTexture[0] and ColorTexture[1], respectively.
 *     - ColorTexture[0] contains both the left and right renderings, ColorTexture[1] is NULL, and Viewport[0] and Viewport[1] refer to sub-rects with
 *         ColorTexture[0].
 *     - ColorTexture[0] contains a single monoscopic rendering, and Viewport[0] and Viewport[1] both refer to that rendering.
 *
 *  \see ovrTextureSwapChain, ovr_SubmitFrame   */
open class OvrLayerEyeMatrix : Structure {

    /** Header.Type must be ovrLayerType_EyeMatrix.    */
    @JvmField var header = OvrLayerHeader()

    /** ovrTextureSwapChains for the left and right eye respectively.
     *  The second one of which can be NULL for cases described above.  */
    @JvmField var colorTexture = Array(ovrEye_Count.i, { OvrTextureSwapChain(0L) })

    /** Specifies the ColorTexture sub-rect UV coordinates.
     *  Both Viewport[0] and Viewport[1] must be valid.  */
    @JvmField var viewport = Array(ovrEye_Count.i, { OvrRecti() })

    /** Specifies the position and orientation of each eye view, with the position specified in meters.
     *  RenderPose will typically be the value returned from ovr_CalcEyePoses, but can be different in special cases if a different head pose is used for rendering. */
    @JvmField var renderPose = Array(ovrEye_Count.i, { OvrPosef() })

    /** Specifies the mapping from a view-space vector to a UV coordinate on the textures given above.
     *  P = (x,y,z,1)*Matrix
     *  TexU  = P.x/P.z
     *  TexV  = P.y/P.z  */
    @JvmField var matrix = Array(ovrEye_Count.i, { OvrMatrix4f() })

    /** Specifies the timestamp when the source ovrPosef (used in calculating RenderPose) was sampled from the SDK. Typically retrieved by calling
     *  ovr_GetTimeInSeconds around the instant the application calls ovr_GetTrackingState
     *  The main purpose for this is to accurately track app tracking latency.   */
    @JvmField var sensorSampleTime = 0.0

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("header", "colorTexture", "viewport", "renderPose", "matrix", "sensorSampleTime")

    constructor(header: OvrLayerHeader, colorTexture: Array<OvrTextureSwapChain>, viewport: Array<OvrRecti>, renderPose: Array<OvrPosef>,
                matrix: Array<OvrMatrix4f>, sensorSampleTime: Double) {

        this.header = header
        if (colorTexture.size != this.colorTexture.size) throw IllegalArgumentException("Wrong array size !")
        this.colorTexture = colorTexture
        if (viewport.size != this.viewport.size) throw IllegalArgumentException("Wrong array size !")
        this.viewport = viewport
        if (renderPose.size != this.renderPose.size) throw IllegalArgumentException("Wrong array size !")
        this.renderPose = renderPose
        if (matrix.size != this.matrix.size) throw IllegalArgumentException("Wrong array size !")
        this.matrix = matrix
        this.sensorSampleTime = sensorSampleTime
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrLayerEyeMatrix(), Structure.ByReference
    class ByValue : OvrLayerEyeMatrix(), Structure.ByValue
}

/** Describes a layer of Quad type, which is a single quad in world or viewer space.
 *  It is used for ovrLayerType_Quad. This type of layer represents a single
 *  object placed in the world and not a stereo view of the world itself.
 *
 *  A typical use of ovrLayerType_Quad is to draw a television screen in a room that for some reason is more convenient to draw as a layer than as part of the
 *  main view in layer 0. For example, it could implement a 3D popup GUI that is drawn at a higher resolution than layer 0 to improve fidelity of the GUI.
 *
 *  Quad layers are visible from both sides; they are not back-face culled.
 *
 *  \see ovrTextureSwapChain, ovr_SubmitFrame   */
open class OvrLayerQuad : OvrLayer_Union {

    /** Header.Type must be ovrLayerType_Quad. */
    @JvmField var header = OvrLayerHeader()

    /** Contains a single image, never with any stereo view.    */
    @JvmField var colorTexture = OvrTextureSwapChain(0L)

    /** Specifies the ColorTexture sub-rect UV coordinates. */
    @JvmField var viewport = OvrRecti()

    /** Specifies the orientation and position of the center point of a Quad layer type.
     *  The supplied direction is the vector perpendicular to the quad.
     *  The position is in real-world meters (not the application's virtual world, the physical world the user is in) and is relative to the "zero" position
     *  set by ovr_RecenterTrackingOrigin unless the ovrLayerFlag_HeadLocked flag is used.  */
    @JvmField var quadPoseCenter = OvrPosef()

    /** Width and height (respectively) of the quad in meters.  */
    @JvmField var quadSize = OvrVector2f()

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("header", "colorTexture", "viewport", "quadPoseCenter", "quadSize")

    constructor(header: OvrLayerHeader, colorTexture: OvrTextureSwapChain, viewport: OvrRecti, quadPoseCenter: OvrPosef, quadSize: OvrVector2f) {

        this.header = header
        this.colorTexture = colorTexture
        this.viewport = viewport
        this.quadPoseCenter = quadPoseCenter
        this.quadSize = quadSize
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrLayerQuad(), Structure.ByReference
    class ByValue : OvrLayerQuad(), Structure.ByValue
}

/** Union that combines ovrLayer types in a way that allows them to be used in a polymorphic way.   */
abstract class OvrLayer_Union : Structure {
    constructor() : super()
    constructor(peer: Pointer) : super(peer)
}

/** @name SDK Distortion Rendering -------------------------------------------------------------------------------------------------------------------------------
 *
 *  All of rendering functions including the configure and frame functions are not thread safe. It is OK to use ConfigureRendering on one thread and handle
 *  frames on another thread, but explicit synchronization must be done since functions that depend on configured state are not reentrant.
 *
 *  These functions support rendering of distortion by the SDK. */

/** TextureSwapChain creation is rendering API-specific.
 *  ovr_CreateTextureSwapChainDX and ovr_CreateTextureSwapChainGL can be found in the rendering API-specific headers, such as OVR_CAPI_D3D.h and OVR_CAPI_GL.h

 *  Gets the number of buffers in an ovrTextureSwapChain.
 *
 *  \param[in]  session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[in]  chain Specifies the ovrTextureSwapChain for which the length should be retrieved.
 *  \param[out] out_Length Returns the number of buffers in the specified chain.
 *
 *  \return Returns an ovrResult for which OVR_SUCCESS(result) is false upon error.
 *
 *  \see ovr_CreateTextureSwapChainDX, ovr_CreateTextureSwapChainGL */
external fun ovr_GetTextureSwapChainLength(session: OvrSession, chain: OvrTextureSwapChain, out_Length: IntByReference): OvrResult

/** Gets the current index in an ovrTextureSwapChain.
 *
 *  \param[in]  session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[in]  chain Specifies the ovrTextureSwapChain for which the index should be retrieved.
 *  \param[out] out_Index Returns the current (free) index in specified chain.
 *
 *  \return Returns an ovrResult for which OVR_SUCCESS(result) is false upon error.
 *
 *  \see ovr_CreateTextureSwapChainDX, ovr_CreateTextureSwapChainGL */
external fun ovr_GetTextureSwapChainCurrentIndex(session: OvrSession, chain: OvrTextureSwapChain, out_Index: IntByReference): OvrResult

/** Gets the description of the buffers in an ovrTextureSwapChain
 *
 *  \param[in ]  session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[in ]  chain Specifies the ovrTextureSwapChain for which the description should be retrieved.
 *  \param[out ] out_Desc Returns the description of the specified chain.
 *
 *  \return Returns an ovrResult for which OVR_SUCCESS(result) is false upon error.
 *
 *  \see ovr_CreateTextureSwapChainDX, ovr_CreateTextureSwapChainGL  */
external fun ovr_GetTextureSwapChainDesc(session: OvrSession, chain: OvrTextureSwapChain, out_Desc: OvrTextureSwapChainDesc.ByReference): OvrResult

/** Commits any pending changes to an ovrTextureSwapChain, and advances its current index
 *
 *  \param[in ]  session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[in ]  chain Specifies the ovrTextureSwapChain to commit.
 *
 *  \note When Commit is called, the texture at the current index is considered ready for use by the runtime, and further writes to it should be avoided. The
 *  swap chain's current index is advanced, providing there's room in the chain. The next time the SDK dereferences this texture swap chain, it will synchronize
 *  with the app's graphics context and pick up the submitted index, opening up room in the swap chain for further commits.
 *
 *  \return Returns an ovrResult for which OVR_SUCCESS(result) is false upon error.
 *          Failures include but aren't limited to:
 *      - ovrError_TextureSwapChainFull: ovr_CommitTextureSwapChain was called too many times on a texture swapchain without calling submit to use the chain.
 *
 *  \see ovr_CreateTextureSwapChainDX, ovr_CreateTextureSwapChainGL  */
external fun ovr_CommitTextureSwapChain(session: OvrSession, chain: OvrTextureSwapChain): OvrResult

/** Destroys an ovrTextureSwapChain and frees all the resources associated with it.
 *
 *  \param[in] session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[in] chain Specifies the ovrTextureSwapChain to destroy. If it is NULL then this function has no effect.
 *
 *  \see ovr_CreateTextureSwapChainDX, ovr_CreateTextureSwapChainGL */
external fun ovr_DestroyTextureSwapChain(session: OvrSession, chain: OvrTextureSwapChain)

/** MirrorTexture creation is rendering API-specific.
 *  ovr_CreateMirrorTextureDX and ovr_CreateMirrorTextureGL can be found in the rendering API-specific headers, such as OVR_CAPI_D3D.h and OVR_CAPI_GL.h

 *  Destroys a mirror texture previously created by one of the mirror texture creation functions.
 *
 *  \param[in ] session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[in ] mirrorTexture Specifies the ovrTexture to destroy. If it is NULL then this function has no effect.
 *
 *  \see ovr_CreateMirrorTextureDX, ovr_CreateMirrorTextureGL    */
external fun ovr_DestroyMirrorTexture(session: OvrSession, mirrorTexture: ovrMirrorTexture)

/** Calculates the recommended viewport size for rendering a given eye within the HMD with a given FOV cone.
 *
 *  Higher FOV will generally require larger textures to maintain quality.
 *  Apps packing multiple eye views together on the same texture should ensure there are at least 8 pixels of padding between them to prevent texture filtering
 *  and chromatic aberration causing images to leak between the two eye views.
 *
 *  \param[in ] session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[in ] eye Specifies which eye (left or right) to calculate for.
 *  \param[in ] fov Specifies the ovrFovPort to use.
 *  \param[in ] pixelsPerDisplayPixel Specifies the ratio of the number of render target pixels to display pixels at the center of distortion. 1.0 is the default
 *      value. Lower values can improve performance, higher values give improved quality.
 *
 *  <b>Example code</b>
 *      \code{.cpp}
 *          ovrHmdDesc hmdDesc = ovr_GetHmdDesc(session);
 *          ovrSizei eyeSizeLeft  = ovr_GetFovTextureSize(session, ovrEye_Left,  hmdDesc.DefaultEyeFov[ovrEye_Left],  1.0f);
 *          ovrSizei eyeSizeRight = ovr_GetFovTextureSize(session, ovrEye_Right, hmdDesc.DefaultEyeFov[ovrEye_Right], 1.0f);
 *      \endcode
 *
 *  \return Returns the texture width and height size.   */
fun ovr_GetFovTextureSize(session: OvrSession, eye: OvrEyeType, fov: OvrFovPort, pixelsPerDisplayPixel: Float)
        = ovr_GetFovTextureSize(session, eye.i, fov, pixelsPerDisplayPixel)

external fun ovr_GetFovTextureSize(session: OvrSession, eye: Int, fov: OvrFovPort, pixelsPerDisplayPixel: Float): OvrSizei


/** Computes the distortion viewport, view adjust, and other rendering parameters for the specified eye.
 *
 *  \param[in ] session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[in ] eyeType Specifies which eye (left or right) for which to perform calculations.
 *  \param[in ] fov Specifies the ovrFovPort to use.
 *
 *  \return Returns the computed ovrEyeRenderDesc for the given eyeType and field of view.
 *
 *  \see ovrEyeRenderDesc    */
fun ovr_GetRenderDesc(session: OvrSession, eyeType: OvrEyeType, fov: OvrFovPort) = ovr_GetRenderDesc(session, eyeType.i, fov)

external fun ovr_GetRenderDesc(session: OvrSession, eyeType: Int, fov: OvrFovPort): OvrEyeRenderDesc

/** Submits layers for distortion and display.
 *
 *  ovr_SubmitFrame triggers distortion and processing which might happen asynchronously.
 *  The function will return when there is room in the submission queue and surfaces are available. Distortion might or might not have completed.
 *
 *  \param[in] session Specifies an ovrSession previously returned by ovr_Create.
 *
 *  \param[in] frameIndex Specifies the targeted application frame index, or 0 to refer to one frame after the last time ovr_SubmitFrame was called.
 *
 *  \param[in] viewScaleDesc Provides additional information needed only if layerPtrList contains an ovrLayerType_Quad. If NULL, a default version is used based
 *      on the current configuration and a 1.0 world scale.
 *
 *  \param[in] layerPtrList Specifies a list of ovrLayer pointers, which can include NULL entries to indicate that any previously shown layer at that index is to
 *      not be displayed.
 *      Each layer header must be a part of a layer structure such as ovrLayerEyeFov or ovrLayerQuad, with Header.Type identifying its type. A NULL layerPtrList
 *      entry in the array indicates the absence of the given layer.
 *
 *  \param[in] layerCount Indicates the number of valid elements in layerPtrList. The maximum supported layerCount is not currently specified, but may be
 *      specified in a future version.
 *
 *  - Layers are drawn in the order they are specified in the array, regardless of the layer type.
 *
 *  - Layers are not remembered between successive calls to ovr_SubmitFrame. A layer must be specified in every call to ovr_SubmitFrame or it won't be displayed.
 *
 *  - If a layerPtrList entry that was specified in a previous call to ovr_SubmitFrame is passed as NULL or is of type ovrLayerType_Disabled, that layer is no
 *      longer displayed.
 *
 *  - A layerPtrList entry can be of any layer type and multiple entries of the same layer type are allowed. No layerPtrList entry may be duplicated
 *      (i.e. the same pointer as an earlier entry).
 *
 *  <b>Example code</b>
 *      \code{.cpp}
 *          ovrLayerEyeFov  layer0;
 *          ovrLayerQuad    layer1;
 *            ...
 *          ovrLayerHeader* layers[2] = { &layer0.Header, &layer1.Header };
 *          ovrResult result = ovr_SubmitFrame(session, frameIndex, nullptr, layers, 2);
 *      \endcode
 *
 *  \return Returns an ovrResult for which OVR_SUCCESS(result) is false upon error and true upon success. Return values include but aren't limited to:
 *      - ovrSuccess: rendering completed successfully.
 *      - ovrSuccess_NotVisible: rendering completed successfully but was not displayed on the HMD, usually because another application currently has ownership
 *          of the HMD. Applications receiving this result should stop rendering new content, but continue to call ovr_SubmitFrame periodically until it returns
 *          a value other than ovrSuccess_NotVisible.
 *      - ovrError_DisplayLost: The session has become invalid (such as due to a device removal) and the shared resources need to be released
 *          (ovr_DestroyTextureSwapChain), the session needs to destroyed (ovr_Destroy) and recreated (ovr_Create), and new resources need to be created
 *          (ovr_CreateTextureSwapChainXXX). The application's existing private graphics resources do not need to be recreated unless the new ovr_Create call
 *          returns a different GraphicsLuid.
 *      - ovrError_TextureSwapChainInvalid: The ovrTextureSwapChain is in an incomplete or inconsistent state.
 *          Ensure ovr_CommitTextureSwapChain was called at least once first.
 *
 *  \see ovr_GetPredictedDisplayTime, ovrViewScaleDesc, ovrLayerHeader  */
// TODO layerPtrList -> list of ovrLayer pointers
//external fun ovr_SubmitFrame(session:OvrSession, frameIndex:Long, viewScaleDesc:OvrViewScaleDesc.ByReference,
//ovrLayerHeader const * const * layerPtrList, unsigned int layerCount);

/**----------------------------------------------------------------------------------------------------------------------------------------------------------------
 * @name Frame Timing   */

/** Contains the performance stats for a given SDK compositor frame
 *
 *  All of the int fields can be reset via the ovr_ResetPerfStats call. */
open class OvrPerfStatsPerCompositorFrame : Structure {

    /** Vsync Frame Index - increments with each HMD vertical synchronization signal (i.e. vsync or refresh rate)
     *  If the compositor drops a frame, expect this value to increment more than 1 at a time.*/
    @JvmField var hmdVsyncIndex = 0

    /** Application stats ------------------------------------------------------------------------------------------- */

    /** Index that increments with each successive ovr_SubmitFrame call */
    @JvmField var appFrameIndex = 0

    /** If the app fails to call ovr_SubmitFrame on time, then expect this value to increment with each missed frame    */
    @JvmField var appDroppedFrameCount = 0

    /** Motion-to-photon latency for the application
     *  This value is calculated by either using the SensorSampleTime provided for the ovrLayerEyeFov or if that is not available, then the call to
     *  ovr_GetTrackingState which has latencyMarker set to ovrTrue */
    @JvmField var appMotionToPhotonLatency = 0f

    /** Amount of queue-ahead in seconds provided to the app based on performance and overlap of CPU & GPU utilization
     *  A value of 0.0 would mean the CPU & GPU workload is being completed in 1 frame's worth of time, while 11 ms (on the CV1) of queue ahead would indicate
     *  that the app's CPU workload for the next frame is overlapping the app's GPU workload for the current frame. */
    @JvmField var appQueueAheadTime = 0f

    /** Amount of time in seconds spent on the CPU by the app's render-thread that calls ovr_SubmitFrame
     *  Measured as elapsed time between from when app regains control from ovr_SubmitFrame to the next time the app calls ovr_SubmitFrame. */
    @JvmField var appCpuElapsedTime = 0f

    /** Amount of time in seconds spent on the GPU by the app
     *  Measured as elapsed time between each ovr_SubmitFrame call using GPU timing queries.    */
    @JvmField var appGpuElapsedTime = 0f

    /**  SDK Compositor stats --------------------------------------------------------------------------------------- */

    /** Index that increments each time the SDK compositor completes a distortion and timewarp pass
     *  Since the compositor operates asynchronously, even if the app calls ovr_SubmitFrame too late, the compositor will kick off for each vsync.  */
    @JvmField var compositorFrameIndex = 0

    /** Increments each time the SDK compositor fails to complete in time
     *  This is not tied to the app's performance, but failure to complete can be tied to other factors such as OS capabilities, overall available hardware
     *  cycles to execute the compositor in time and other factors outside of the app's control.    */
    @JvmField var compositorDroppedFrameCount = 0

    /** Motion-to-photon latency of the SDK compositor in seconds
     *  This is the latency of timewarp which corrects the higher app latency as well as dropped app frames.    */
    @JvmField var compositorLatency = 0f

    /** The amount of time in seconds spent on the CPU by the SDK compositor. Unless the VR app is utilizing all of the CPU cores at their peak performance,
     *  there is a good chance the compositor CPU times will not affect the app's CPU performance in a major way.   */
    @JvmField var compositorCpuElapsedTime = 0f

    /** The amount of time in seconds spent on the GPU by the SDK compositor. Any time spent on the compositor will eat away from the available GPU time for
     *  the app.    */
    @JvmField var compositorGpuElapsedTime = 0f

    /** The amount of time in seconds spent from the point the CPU kicks off the compositor to the point in time the compositor completes the distortion &
     *  timewarp on the GPU. In the event the GPU time is not available, expect this value to be -1.0f  */
    @JvmField var compositorCpuStartToGpuEndElapsedTime = 0f

    /** The amount of time in seconds left after the compositor is done on the GPU to the associated V-Sync time.
     *  In the event the GPU time is not available, expect this value to be -1.0f   */
    @JvmField var compositorGpuEndToVsyncElapsedTime = 0f

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("hmdVsyncIndex", "appFrameIndex", "appDroppedFrameCount", "appMotionToPhotonLatency",
            "appQueueAheadTime", "appCpuElapsedTime", "appGpuElapsedTime", "compositorFrameIndex", "compositorDroppedFrameCount", "compositorLatency",
            "compositorCpuElapsedTime", "compositorGpuElapsedTime", "compositorCpuStartToGpuEndElapsedTime", "compositorGpuEndToVsyncElapsedTime")

    constructor(hmdVsyncIndex: Int, appFrameIndex: Int, appDroppedFrameCount: Int, appMotionToPhotonLatency: Float, appQueueAheadTime: Float,
                appCpuElapsedTime: Float, appGpuElapsedTime: Float, compositorFrameIndex: Int, compositorDroppedFrameCount: Int, compositorLatency: Float,
                compositorCpuElapsedTime: Float, compositorGpuElapsedTime: Float, compositorCpuStartToGpuEndElapsedTime: Float,
                compositorGpuEndToVsyncElapsedTime: Float) {

        this.hmdVsyncIndex = hmdVsyncIndex
        this.appFrameIndex = appFrameIndex
        this.appDroppedFrameCount = appDroppedFrameCount
        this.appMotionToPhotonLatency = appMotionToPhotonLatency
        this.appQueueAheadTime = appQueueAheadTime
        this.appCpuElapsedTime = appCpuElapsedTime
        this.appGpuElapsedTime = appGpuElapsedTime
        this.compositorFrameIndex = compositorFrameIndex
        this.compositorDroppedFrameCount = compositorDroppedFrameCount
        this.compositorLatency = compositorLatency
        this.compositorCpuElapsedTime = compositorCpuElapsedTime
        this.compositorGpuElapsedTime = compositorGpuElapsedTime
        this.compositorCpuStartToGpuEndElapsedTime = compositorCpuStartToGpuEndElapsedTime
        this.compositorGpuEndToVsyncElapsedTime = compositorGpuEndToVsyncElapsedTime
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrPerfStatsPerCompositorFrame(), Structure.ByReference
    class ByValue : OvrPerfStatsPerCompositorFrame(), Structure.ByValue
}

/** Maximum number of frames of performance stats provided back to the caller of ovr_GetPerfStats   */
const val ovrMaxProvidedFrameStats = 5

/** This is a complete descriptor of the performance stats provided by the SDK
 *
 *  FrameStatsCount will have a maximum value set by ovrMaxProvidedFrameStats
 *  If the application calls ovr_GetPerfStats at the native refresh rate of the HMD then FrameStatsCount will be 1. If the app's workload happens to force
 *  ovr_GetPerfStats to be called at a lower rate, then FrameStatsCount will be 2 or more.
 *  If the app does not want to miss any performance data for any frame, it needs to ensure that it is calling ovr_SubmitFrame and ovr_GetPerfStats at a rate
 *  that is at least:
 *  "HMD_refresh_rate / ovrMaxProvidedFrameStats". On the Oculus Rift CV1 HMD, this will be equal to 18 times per second.
 *  If the app calls ovr_SubmitFrame at a rate less than 18 fps, then when calling ovr_GetPerfStats, expect AnyFrameStatsDropped to become ovrTrue while
 *  FrameStatsCount is equal to ovrMaxProvidedFrameStats.
 *
 *  The performance entries will be ordered in reverse chronological order such that the first entry will be the most recent one.
 *
 *  AdaptiveGpuPerformanceScale is an edge-filtered value that a caller can use to adjust the graphics quality of the application to keep the GPU utilization in
 *  check. The value is calculated as: (desired_GPU_utilization / current_GPU_utilization)
 *  As such, when this value is 1.0, the GPU is doing the right amount of work for the app.
 *  Lower values mean the app needs to pull back on the GPU utilization.
 *  If the app is going to directly drive render-target resolution using this value, then be sure to take the square-root of the value before scaling the
 *  resolution with it.
 *  Changing render target resolutions however is one of the many things an app can do increase or decrease the amount of GPU utilization.
 *  Since AdaptiveGpuPerformanceScale is edge-filtered and does not change rapidly (i.e. reports non-1.0 values once every couple of seconds) the app can make
 *  the necessary adjustments and then keep watching the value to see if it has been satisfied.
 *
 *  \see ovr_GetPerfStats, ovrPerfStatsPerCompositorFrame   */
open class OvrPerfStats : Structure {

    @JvmField var frameStats = Array(ovrMaxProvidedFrameStats, { OvrPerfStatsPerCompositorFrame() })
    @JvmField var frameStatsCount = 0
    @JvmField var anyFrameStatsDropped = ovrFalse
    @JvmField var adaptiveGpuPerformanceScale = 0f

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("frameStats", "frameStatsCount", "anyFrameStatsDropped", "adaptiveGpuPerformanceScale")

    constructor(frameStats: Array<OvrPerfStatsPerCompositorFrame>, frameStatsCount: Int, anyFrameStatsDropped: Boolean, adaptiveGpuPerformanceScale: Float)
    : this(frameStats, frameStatsCount, if (anyFrameStatsDropped) ovrTrue else ovrFalse, adaptiveGpuPerformanceScale)

    constructor(frameStats: Array<OvrPerfStatsPerCompositorFrame>, frameStatsCount: Int, anyFrameStatsDropped: OvrBool, adaptiveGpuPerformanceScale: Float) {

        if (frameStats.size != this.frameStats.size) throw IllegalArgumentException("Wrong array size !")
        this.frameStats = frameStats
        this.frameStatsCount = frameStatsCount
        this.anyFrameStatsDropped = anyFrameStatsDropped
        this.adaptiveGpuPerformanceScale = adaptiveGpuPerformanceScale
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : OvrPerfStatsPerCompositorFrame(), Structure.ByReference
    class ByValue : OvrPerfStatsPerCompositorFrame(), Structure.ByValue
}

/** Retrieves performance stats for the VR app as well as the SDK compositor.
 *
 *  If the app calling this function is not the one in focus (i.e. not visible in the HMD), then outStats will be zero'd out.
 *  New stats are populated after each successive call to ovr_SubmitFrame. So the app should call this function on the same thread it calls ovr_SubmitFrame,
 *  preferably immediately afterwards.
 *
 *  \param[in] session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[out] outStats Contains the performance stats for the application and SDK compositor
 *  \return Returns an ovrResult for which OVR_SUCCESS(result) is false upon error and true upon success.
 *
 *  \see ovrPerfStats, ovrPerfStatsPerCompositorFrame, ovr_ResetPerfStats   */
external fun ovr_GetPerfStats(session: OvrSession, outStats: OvrPerfStats.ByReference): OvrResult

/** Resets the accumulated stats reported in each ovrPerfStatsPerCompositorFrame back to zero.
 *
 *  Only the integer values such as HmdVsyncIndex, AppDroppedFrameCount etc. will be reset as the other fields such as AppMotionToPhotonLatency are independent
 *  timing values updated per-frame.
 *
 *  \param[in] session Specifies an ovrSession previously returned by ovr_Create.
 *  \return Returns an ovrResult for which OVR_SUCCESS(result) is false upon error and true upon success.
 *
 *  \see ovrPerfStats, ovrPerfStatsPerCompositorFrame, ovr_GetPerfStats */
external fun ovr_ResetPerfStats(session: OvrSession): OvrResult


/** Gets the time of the specified frame midpoint.
 *
 *  Predicts the time at which the given frame will be displayed. The predicted time is the middle of the time period during which the corresponding eye images
 *  will be displayed.
 *
 *  The application should increment frameIndex for each successively targeted frame, and pass that index to any relevant OVR functions that need to apply to the
 *  frame identified by that index.
 *
 *  This function is thread-safe and allows for multiple application threads to target their processing to the same displayed frame.
 *
 *  In the even that prediction fails due to various reasons (e.g. the display being off or app has yet to present any frames), the return value will be current
 *  CPU time.
 *
 *  \param[in] session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[in] frameIndex Identifies the frame the caller wishes to target.
 *             A value of zero returns the next frame index.
 *  \return Returns the absolute frame midpoint time for the given frameIndex.
 *  \see ovr_GetTimeInSeconds   */
external fun ovr_GetPredictedDisplayTime(session: OvrSession, frameIndex: Long): Double


/** Returns global, absolute high-resolution time in seconds.
 *
 *  The time frame of reference for this function is not specified and should not be depended upon.
 *
 *  \return Returns seconds as a floating point value.
 *  \see ovrPoseStatef, ovrFrameTiming  */
external fun ovr_GetTimeInSeconds(): Double

/** Performance HUD enables the HMD user to see information critical to the real-time operation of the VR application such as latency timing, and CPU & GPU
 *  performance metrics
 *
 *  App can toggle performance HUD modes as such:
 *     \code{.cpp}
 *         ovrPerfHudMode PerfHudMode = ovrPerfHud_LatencyTiming;
 *         ovr_SetInt(session, OVR_PERF_HUD_MODE, (int)PerfHudMode);
 *     \endcode */
enum class OvrPerfHudMode(@JvmField val i: Int) {

    ovrPerfHud_Off(0), ///              < Turns off the performance HUD
    ovrPerfHud_PerfSummary(1), ///      < Shows performance summary and headroom
    ovrPerfHud_LatencyTiming(2), ///    < Shows latency related timing info
    ovrPerfHud_AppRenderTiming(3), ///  < Shows render timing info for application
    ovrPerfHud_CompRenderTiming(4), /// < Shows render timing info for OVR compositor
    ovrPerfHud_VersionInfo(5), ///      < Shows SDK & HMD version Info
    ovrPerfHud_Count(6)///              < \internal Count of enumerated elements.
}

/** Layer HUD enables the HMD user to see information about a layer
 *
 *  App can toggle layer HUD modes as such:
 *     \code{.cpp}
 *         ovrLayerHudMode LayerHudMode (ovrLayerHud_Info;
 *         ovr_SetInt(session, OVR_LAYER_HUD_MODE, (int)LayerHudMode);
 *     \endcode */
enum class OvrLayerHudMode(@JvmField val i: Int) {

    ovrLayerHud_Off(0), /// < Turns off the layer HUD
    ovrLayerHud_Info(1), ///< Shows info about a specific layer
}

/** Debug HUD is provided to help developers gauge and debug the fidelity of their app's stereo rendering characteristics. Using the provided quad and crosshair
 *  guides, the developer can verify various aspects such as VR tracking units (e.g. meters), stereo camera-parallax properties (e.g. making sure objects at
 *  infinity are rendered with the proper separation), measuring VR geometry sizes and distances and more.
 *
 *  App can toggle the debug HUD modes as such:
 *     \code{.cpp}
 *         ovrDebugHudStereoMode DebugHudMode (ovrDebugHudStereo_QuadWithCrosshair;
 *         ovr_SetInt(session, OVR_DEBUG_HUD_STEREO_MODE, (int)DebugHudMode);
 *     \endcode
 * The app can modify the visual properties of the stereo guide (i.e. quad, crosshair) using the ovr_SetFloatArray function. For a list of tweakable properties,
 * see the OVR_DEBUG_HUD_STEREO_GUIDE_* keys in the OVR_CAPI_Keys.h header file.    */
enum class OvrDebugHudStereoMode(@JvmField val i: Int) {

    ovrDebugHudStereo_Off(0), ///                   < Turns off the Stereo Debug HUD
    ovrDebugHudStereo_Quad(1), ///                  < Renders Quad in world for Stereo Debugging
    ovrDebugHudStereo_QuadWithCrosshair(2), ///     < Renders Quad+crosshair in world for Stereo Debugging
    ovrDebugHudStereo_CrosshairAtInfinity(3), ///   < Renders screen-space crosshair at infinity for Stereo Debugging
    ovrDebugHudStereo_Count(4)                  /// < \internal Count of enumerated elements
}

/** ---------------------------------------------------------------------------------------------------------------------------------------------------------------
 *  @name Property Access
 *
 *  These functions read and write OVR properties. Supported properties are defined in OVR_CAPI_Keys.h  */

/** Reads a boolean property.
 *
 *  \param[in] session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[in] propertyName The name of the property, which needs to be valid for only the call.
 *  \param[in] defaultVal specifes the value to return if the property couldn't be read.
 *  \return Returns the property interpreted as a boolean value. Returns defaultVal if the property doesn't exist.  */
fun ovr_GetBool(session: OvrSession, propertyName: String, defaultVal: Boolean)
        = ovr_GetBool(session, propertyName, if (defaultVal) ovrTrue else ovrFalse) == ovrTrue

external fun ovr_GetBool(session: OvrSession, propertyName: String, defaultVal: OvrBool): OvrBool

/** Writes or creates a boolean property.
 *  If the property wasn't previously a boolean property, it is changed to a boolean property.
 *
 *  \param[in ] session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[in ] propertyName The name of the property, which needs to be valid only for the call.
 *  \param[in ] value The value to write.
 *  \return Returns true if successful, otherwise false. A false result should only occur if the property name is empty or if the property is read-only. */
fun ovr_SetBool(session: OvrSession, propertyName: Structure, value: Boolean) = ovr_SetBool(session, propertyName, if (value) ovrTrue else ovrFalse) == ovrTrue

external fun ovr_SetBool(session: OvrSession, propertyName: Structure, value: OvrBool): OvrBool


/** Reads an integer property.
 *
 *  \param[in ] session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[in ] propertyName The name of the property, which needs to be valid only for the call.
 *  \param[in ] defaultVal Specifes the value to return if the property couldn't be read.
 *  \return Returns the property interpreted as an integer value. Returns defaultVal if the property doesn't exist.  */
external fun ovr_GetInt(session: OvrSession, propertyName: String, defaultVal: Int): Int

/** Writes or creates an integer property.
 *
 *  If the property wasn't previously a boolean property, it is changed to an integer property.
 *
 *  \param[in ] session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[in ] propertyName The name of the property, which needs to be valid only for the call.
 *  \param[in ] value The value to write.
 *  \return Returns true if successful, otherwise false. A false result should only occur if the property name is empty or if the property is read-only. */
fun ovr_SetInt_(session: OvrSession, propertyName: String, value: Int) = ovr_SetInt(session, propertyName, value) == ovrTrue

external fun ovr_SetInt(session: OvrSession, propertyName: String, value: Int): OvrBool


/** Reads a float property.
 *
 *  \param[in ] session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[in ] propertyName The name of the property, which needs to be valid only for the call.
 *  \param[in ] defaultVal specifes the value to return if the property couldn't be read.
 *  \return Returns the property interpreted as an float value. Returns defaultVal if the property doesn't exist.    */
external fun ovr_GetFloat(session: OvrSession, propertyName: String, defaultVal: Float): Float

/** Writes or creates a float property.
 *  If the property wasn't previously a float property, it's changed to a float property.
 *
 *  \param[in ] session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[in ] propertyName The name of the property, which needs to be valid only for the call.
 *  \param[in ] value The value to write.
 *  \return Returns true if successful, otherwise false. A false result should only occur if the property name is empty or if the property is read-only. */
fun ovr_SetFloat_(session: OvrSession, propertyName: String, value: Float) = ovr_SetFloat(session, propertyName, value) == ovrTrue

external fun ovr_SetFloat(session: OvrSession, propertyName: String, value: Float): OvrBool


/** Reads a float array property.
 *
 *  \param[in ] session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[in ] propertyName The name of the property, which needs to be valid only for the call.
 *  \param[in ] values An array of float to write to.
 *  \param[in ] valuesCapacity Specifies the maximum number of elements to write to the values array.
 *  \return Returns the number of elements read, or 0 if property doesn't exist or is empty. */
external fun ovr_GetFloatArray(session: OvrSession, propertyName: String, values: FloatArray, valuesCapacity: Int): Int

/** Writes or creates a float array property.
 *
 *  \param[in ] session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[in ] propertyName The name of the property, which needs to be valid only for the call.
 *  \param[in ] values An array of float to write from.
 *  \param[in ] valuesSize Specifies the number of elements to write.
 *  \return Returns true if successful, otherwise false. A false result should only occur if the property name is empty or if the property is read-only. */
fun ovr_SetFloatArray_(session: OvrSession, propertyName: String, values: FloatArray, valuesSize: Int) =
        ovr_SetFloatArray(session, propertyName, values, valuesSize) == ovrTrue

external fun ovr_SetFloatArray(session: OvrSession, propertyName: String, values: FloatArray, valuesSize: Int): OvrBool


/** Reads a string property.
 *  Strings are UTF8-encoded and null-terminated.
 *
 *  \param[in ] session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[in ] propertyName The name of the property, which needs to be valid only for the call.
 *  \param[in ] defaultVal Specifes the value to return if the property couldn't be read.
 *  \return Returns the string property if it exists. Otherwise returns defaultVal, which can be specified as NULL.
 *          The return memory is guaranteed to be valid until next call to ovr_GetString or until the session is destroyed, whichever occurs first.  */
external fun ovr_GetString(session: OvrSession, propertyName: String, defaultVal: String): String

/** Writes or creates a string property.
 *  Strings are UTF8-encoded and null-terminated.
 *
 *  \param[in ] session Specifies an ovrSession previously returned by ovr_Create.
 *  \param[in ] propertyName The name of the property, which needs to be valid only for the call.
 *  \param[in ] value The string property, which only needs to be valid for the duration of the call.
 *  \return Returns true if successful, otherwise false. A false result should only occur if the property name is empty or if the property is read-only. */
fun ovr_SetString_(session: OvrSession, propertyName: String, value: String) = ovr_SetString(session, propertyName, value) == ovrTrue

external fun ovr_SetString(session: OvrSession, propertyName: String, value: String): OvrBool




























