@file:OptIn(ExperimentalFoundationApi::class)

package edu.farmingdale.draganddropanim_demo

import android.content.ClipData
import android.content.ClipDescription
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalAnimationGraphicsApi::class, ExperimentalFoundationApi::class)
@Composable
fun DragAndDropBoxes(modifier: Modifier = Modifier) {
    val boxCount = 4
    var dragBoxIndex by remember {
        mutableIntStateOf(0)
    }

    var animateBox by remember {
        mutableStateOf(false)
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Row(
            modifier = modifier
                .fillMaxWidth()
                .weight(0.2f)
        ) {

            repeat(boxCount) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(10.dp)
                        .border(1.dp, Color.Black)
                        .dragAndDropTarget(
                            shouldStartDragAndDrop = { event ->
                                event
                                    .mimeTypes()
                                    .contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                            },
                            target = remember {
                                object : DragAndDropTarget {
                                    override fun onDrop(event: DragAndDropEvent): Boolean {
                                        dragBoxIndex = index
                                        animateBox = true
                                        return true
                                    }
                                }
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    this@Row.AnimatedVisibility(
                        visible = index == dragBoxIndex,
                        enter = scaleIn() + fadeIn(),
                        exit = scaleOut() + fadeOut()
                    ) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .dragAndDropSource {
                                    detectTapGestures(
                                        onLongPress = { offset ->
                                            startTransfer(
                                                transferData = DragAndDropTransferData(
                                                    clipData = ClipData.newPlainText(
                                                        "text",
                                                        ""
                                                    )
                                                )
                                            )
                                        }
                                    )
                                }
                        )
                    }
                }
            }
        }
        val rotateAnimation by animateFloatAsState(
            targetValue = if (animateBox) 360f else 0f,
            animationSpec = tween(
                durationMillis = 1000
            ),
            label = ""
        )

        val moveHorizontallyAnimation by animateFloatAsState(
            targetValue = if (animateBox) 100f else 0f,
            animationSpec = tween(
                durationMillis = 1000
            ),
            label = ""
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f)
                .background(Color.Red)
                .offset(x = 100.dp, y = 100.dp)
        ) {

            Box(
                modifier = Modifier.offset(x = moveHorizontallyAnimation.dp, y = -moveHorizontallyAnimation.dp)
            ){
                Box(
                    modifier = Modifier
                        .rotate(rotateAnimation)
                        .size(70.dp)
                        .background(Color.Blue)


                )
            }
            Row{
                Button(onClick = {
                    animateBox = true
                }) {
                    Text(text = "Animate")
                }

                Button(onClick = {
                    animateBox = false
                }) {
                    Text(text = "Reset")
                }
            }


        }/**/
        /*Canvas(
        modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f)
                .background(Color.Red)
                //.offset(x = 100.dp, y = 100.dp)

        ) {

            rotate(rotateAnimation) {
                drawRect(
                    Color.Green, size = Size(70f, 70f)
                )
            }

        }*/
    }
}

