package com.leadwoods.dissertation.ui.ar_view

import androidx.lifecycle.DefaultLifecycleObserver
import com.google.ar.core.Anchor
import com.google.ar.core.Trackable
import com.leadwoods.dissertation.MainActivity
import com.leadwoods.dissertation.samplerender.Framebuffer
import com.leadwoods.dissertation.samplerender.Mesh
import com.leadwoods.dissertation.samplerender.SampleRender
import com.leadwoods.dissertation.samplerender.Shader
import com.leadwoods.dissertation.samplerender.Texture
import com.leadwoods.dissertation.samplerender.VertexBuffer
import com.leadwoods.dissertation.samplerender.arcore.BackgroundRenderer
import com.leadwoods.dissertation.samplerender.arcore.PlaneRenderer
import com.leadwoods.dissertation.samplerender.arcore.SpecularCubemapFilter
import com.leadwoods.dissertation.support.DisplayRotationHelper
import com.leadwoods.dissertation.support.TrackingStateHelper

private data class WrappedAnchor(
    val anchor: Anchor,
    val trackable: Trackable,
)

class ARRenderer(private val activity: MainActivity) : SampleRender.Renderer, DefaultLifecycleObserver {

    companion object {

        private val sphericalHarmonicFactors =
            floatArrayOf(
                0.282095f,
                -0.325735f,
                0.325735f,
                -0.325735f,
                0.273137f,
                -0.273137f,
                0.078848f,
                -0.273137f,
                0.136569f
            )

        private val Z_NEAR = 0.1f
        private val Z_FAR = 100f

        val APPROXIMATE_DISTANCE_METERS = 1.0f

        val CUBEMAP_RESOLUTION = 16
        val CUBEMAP_NUMBER_OF_IMPORTANCE_SAMPLES = 32
    }

    lateinit var reder: SampleRender
    lateinit var planeRenderer: PlaneRenderer
    lateinit var backgroundRenderer: BackgroundRenderer
    lateinit var virtualSceneFrameBuffer: Framebuffer
    var hasSetTextureNames = false

    lateinit var pointCloudVertextBuffer: VertexBuffer
    lateinit var pointCloudMesh: Mesh
    lateinit var pointCloudShader: Shader

    var lastPointCloudTimestamp: Long = 0

    lateinit var virtualObjectMesh: Mesh
    lateinit var virtualObjectShader: Shader
    lateinit var virtualObjectAlbedoTexture: Texture
    lateinit var virtualObjectAlbedoInstantPlacementTexture: Texture

    private val wrappedAnchors = mutableListOf<WrappedAnchor>()

    lateinit var dfgTexture: Texture
    lateinit var cubemapFilter: SpecularCubemapFilter

    val modelMatrix = FloatArray(16)
    val viewMatrix = FloatArray(16)
    val projectionMatrix = FloatArray(16)
    val modelViewMatrix = FloatArray(16)

    val modelViewProjectionMatrix = FloatArray(16)

    val sphericalHarmonicsCoeffecients = FloatArray(9 * 3)
    val viewInverseMatrix = FloatArray(16)
    val worldLightDirection = floatArrayOf(0.0f, 0.0f, 0.0f, 0.0f)
    val viewLightDirection = FloatArray(4)

    val session
        get() = activity.arCoreHelper.session

    val displayRotationHelper = DisplayRotationHelper(activity)
    val trackingStateHelper = TrackingStateHelper(activity)


    override fun onSurfaceCreated(render: SampleRender?) {
        TODO("Not yet implemented")
    }

    override fun onSurfaceChanged(render: SampleRender?, width: Int, height: Int) {
        TODO("Not yet implemented")
    }

    override fun onDrawFrame(render: SampleRender?) {
        TODO("Not yet implemented")
    }
}