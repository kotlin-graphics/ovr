package ovr

/**
 * Created by GBarbieri on 03.11.2016.
 */

//-----------------------------------------------------------------------------------
// ***** Stereo Enumerations

/** StereoEye specifies which eye we are rendering for; it is used to retrieve StereoEyeParams. */
enum class StereoEye(@JvmField val i:Int){

    StereoEye_Left(0),
    StereoEye_Right(1),
    StereoEye_Center(2)
}



// TODO ? because they are not native
//-----------------------------------------------------------------------------------
// ***** Propjection functions
//
//Matrix4f            CreateProjection ( bool rightHanded, bool isOpenGL, FovPort fov, StereoEye eye,
//float zNear = 0.01f, float zFar = 10000.0f,
//bool flipZ = false, bool farAtInfinity = false);
//
//Matrix4f            CreateOrthoSubProjection ( bool rightHanded, StereoEye eyeType,
//float tanHalfFovX, float tanHalfFovY,
//float unitsX, float unitsY, float distanceFromCamera,
//float interpupillaryDistance, Matrix4f const &projection,
//float zNear = 0.0f, float zFar = 0.0f,
//bool flipZ = false, bool farAtInfinity = false);
//
//ScaleAndOffset2D    CreateNDCScaleAndOffsetFromFov ( FovPort fov );
