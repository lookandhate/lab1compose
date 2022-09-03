package ru.lookandhate.lab1compose

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.lookandhate.lab1compose.ui.theme.Lab1composeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab1composeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    CalculatorPage()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Lab1composeTheme {
//        Greeting("Android")
        CalculatorPage()

    }

}

fun onButtonClick(
    firstOperand: String,
    secondOperand: String,
    callback: (Double, Double) -> Double,
    context: Context
) {
    val firstOperandDigit = firstOperand.toDoubleOrNull()
    val secondOperandDigit = secondOperand.toDoubleOrNull()
    if (firstOperandDigit == null || secondOperandDigit == null) {
        Toast.makeText(
            context,
            "One or both operands are not a digit! Check your input",
            Toast.LENGTH_LONG
        ).show()
        return
    }
    val intent = Intent(context, ResultActivity::class.java)
    intent.putExtra("result", callback(firstOperandDigit, secondOperandDigit))
    context.startActivity(intent)
}

@Composable
fun CalculatorPage() {
    var firstOperandValue by remember { mutableStateOf("0.0") }
    var secondOperandValue by remember { mutableStateOf("0.0") }
    val context = LocalContext.current
    Column() {
        // Values input column
        Column(
            Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OperandComponent(
                operandNumber = 1,
                operandValue = firstOperandValue,
                onNumberChange = {
                    firstOperandValue = it
                })
            OperandComponent(
                operandNumber = 2,
                operandValue = secondOperandValue,
                onNumberChange = {
                    secondOperandValue = it
                })
        }

        // Action buttons
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            Button(onClick = {
                onButtonClick(
                    firstOperandValue,
                    secondOperandValue,
                    { first: Double, second: Double -> first + second },
                    context
                )
            }) { Text("Add") }
            Button(onClick = {
                onButtonClick(
                    firstOperandValue,
                    secondOperandValue,
                    { first: Double, second: Double -> first - second },
                    context
                )
            }) { Text("Subtract") }
            Button(onClick = {
                onButtonClick(
                    firstOperandValue,
                    secondOperandValue,
                    { first: Double, second: Double ->
                        first / second
                    },
                    context
                )
            }) { Text("Divide") }
            Button(onClick = {
                onButtonClick(
                    firstOperandValue,
                    secondOperandValue,
                    { first: Double, second: Double -> first * second },
                    context
                )
            }) { Text("Multiply") }

        }
    }

}

@Composable
fun OperandComponent(
    operandNumber: Number,
    operandValue: String,
    onNumberChange: (String) -> Unit
) {
    TextField(
        value = operandValue,
        label = { Text("Operand $operandNumber") },
        onValueChange = onNumberChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )

}