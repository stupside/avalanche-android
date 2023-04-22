
import android.content.Context
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.avalanche.core.qr.AvalancheQrAnalyzer
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.common.Barcode
import java.util.concurrent.ExecutorService

@Composable
@ExperimentalGetImage
fun AvalancheQrScanner(
    context: Context,
    executor: ExecutorService,
    scanner: BarcodeScanner,
    onDiscovered: (code: Barcode) -> Unit
) {

    val analyzer = remember {
        val analyzer = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        analyzer.setAnalyzer(executor, AvalancheQrAnalyzer(scanner) { code ->
            if(code != null)
                onDiscovered(code)
        })

        analyzer
    }

    val lifecycle = LocalLifecycleOwner.current

    val view = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER

            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        }
    }

    SideEffect {

        val future = ProcessCameraProvider.getInstance(context)

        future.addListener({

            val provider = future.get()

            val preview = Preview.Builder().build()

            provider.bindToLifecycle(
                lifecycle,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                analyzer
            )

            preview.setSurfaceProvider(view.surfaceProvider)

        }, ContextCompat.getMainExecutor(context))
    }

    AndroidView({ view }, modifier = Modifier.fillMaxSize())
}