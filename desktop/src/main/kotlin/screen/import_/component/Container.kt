package screen.import_.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun <T> Container(
    state: T,
    importButtonWidth: Dp? = null,
    importButtonHeight: Dp? = null,
    content: @Composable BoxScope.(T) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
    ) {
        Card(
            // TODO: determine the size in a normal way
            modifier = Modifier
            .let {
                if (importButtonWidth != null && importButtonHeight != null) {
                    it.size(importButtonWidth, importButtonHeight)
                } else {
                    it.fillMaxSize()
                }
            }
            .align(Alignment.Center)
        ) {
        Box(modifier = Modifier.fillMaxSize()) {
            content(state)
        }
    }
    }
}

@Composable
@Preview
fun ContainerPreview() {
    Container(
        state = Unit,
        importButtonWidth = 1000.dp,
        importButtonHeight = 430.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center)
        {
            // todo
        }
    }
}
