package tm.bent.dinle.ui.util.recorder

import java.io.File

interface AudioRecorder {
    fun start(outputFile: File)
    fun stop()
}