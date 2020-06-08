package com.chan.compose.components

import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.core.*
import androidx.ui.focus.FocusModifier
import androidx.ui.foundation.Text
import androidx.ui.foundation.TextField
import androidx.ui.foundation.TextFieldValue
import androidx.ui.foundation.currentTextStyle
import androidx.ui.graphics.Color
import androidx.ui.text.TextStyle
import androidx.ui.text.style.TextDecoration
import androidx.ui.unit.ipx

@Composable
fun HintEditText(
    hintText: String = "",
    modifier: Modifier = Modifier,
    textStyle: TextStyle = currentTextStyle()
) {
    val state = state { TextFieldValue("") }
    val inputField = @Composable {
        val focusModifier = FocusModifier()
        TextField(
            value = state.value,
            modifier = modifier + focusModifier + Modifier.onPositioned {
                focusModifier.requestFocus()
            },
            onValueChange = { state.value = it },
            textStyle = textStyle.merge(TextStyle(textDecoration = TextDecoration.None))
        )
    }

    Layout(
        children = @Composable {
            inputField()
            Text(
                text = hintText,
                modifier = modifier,
                style = textStyle.merge(TextStyle(color = Color.Gray))
            )
            //Divider(color = Color.Black, thickness = 2.dp)
        },
        measureBlock = { measurables: List<Measurable>, constraints: Constraints, _ ->
            val inputFieldPlace = measurables[0].measure(constraints)
            val hintEditPlace = measurables[1].measure(constraints)
            /*val dividerEditPlace = measurables[2].measure(
                Constraints(constraints.minWidth, constraints.maxWidth, 2.ipx, 2.ipx)
            )*/
            layout(
                inputFieldPlace.width,
                inputFieldPlace.height //+ dividerEditPlace.height
            ) {
                inputFieldPlace.place(0.ipx, 0.ipx)
                if (state.value.text.isEmpty())
                    hintEditPlace.place(0.ipx, 0.ipx)
                //dividerEditPlace.place(0.ipx, inputFieldPlace.height)
            }
        })
}