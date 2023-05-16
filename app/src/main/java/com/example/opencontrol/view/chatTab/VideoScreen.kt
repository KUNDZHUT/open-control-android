package com.example.opencontrol.view.chatTab

import android.Manifest
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.agora.agorauikit_android.AgoraConnectionData
import io.agora.agorauikit_android.AgoraVideoViewer
import io.agora.rtc2.RtcEngine
import timber.log.Timber

const val AGORA_APP_ID = "efe9f81f09164d32955260ccc3143b7d"

data class VideoScreen(
    val roomName: String
) : Screen {
    @Composable
    override fun Content() {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            VideoScreenContent(roomName = roomName)
        }
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
@Composable
private fun VideoScreenContent(
    roomName: String,
    viewModel: VideoScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    Timber.d("@@@ VideoScreenContent (roomName = $roomName)")
    val navigator = LocalNavigator.currentOrThrow
    var agoraView: AgoraVideoViewer? = null
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            viewModel.onPermissionsResult(
                acceptedAudioPermission = perms[Manifest.permission.RECORD_AUDIO] == true,
                acceptedCameraPermission = perms[Manifest.permission.CAMERA] == true
            )
        })

    LaunchedEffect(key1 = true) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
            )
        )
    }

    BackHandler {
        agoraView?.leaveChannel()
        RtcEngine.destroy()
        navigator.pop()
    }

    if (viewModel.hasAudioPermission.value && viewModel.hasCameraPermission.value) {
        AndroidView(
            factory = { context ->
                AgoraVideoViewer(
                    context = context,
                    connectionData = AgoraConnectionData(appId = AGORA_APP_ID)
                ).also {
                    it.join(roomName)
                    agoraView = it
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}