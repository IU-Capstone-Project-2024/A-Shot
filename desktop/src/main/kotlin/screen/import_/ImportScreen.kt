package screen.import_

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import screen.import_.component.Container
import screen.import_.component.Failure
import screen.import_.component.Loading
import screen.import_.component.SelectDir
import shot.ShotCollection
import java.io.File

@Composable
fun ImportScreen(
	onImported: (File, ShotCollection) -> Unit,
) {
	val model = remember { ImportModel(onImported) }
	val state by model.stateFlow.collectAsState()

	Container(state) { current ->
		when (current) {
			ImportState.SelectDir -> {
				SelectDir { dir ->
					model.import(dir)
				}
			}

			is ImportState.Loading -> {
				Loading(
					dirName = current.dir.name,
					progress = current.progress,
					onCancel = {
						model.reset()
					}
				)
			}

			is ImportState.Failure -> {
				Failure(
					reason = current.reason,
					onOk = {
						model.reset()
					}
				)
			}
		}
	}
}
