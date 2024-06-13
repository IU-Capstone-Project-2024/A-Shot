package screen.main

import PhotoGroup
import java.io.File

sealed class MainState {
	object Loading : MainState()
	class Success(var ok: Array<PhotoGroup>, var nok: Array<File>) : MainState()
	object Failure : MainState()
}
