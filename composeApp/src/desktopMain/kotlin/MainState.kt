import shot.ShotCollection
import java.io.File

data class MainState(
	val dir: File? = null,
	val shots: ShotCollection = ShotCollection(),
	val currentGroup: Int = 0,
)
