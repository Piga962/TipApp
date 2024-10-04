package com.example.tipapp_starter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tipapp_starter.components.InputField
import com.example.tipapp_starter.components.RoundedIconButton
import com.example.tipapp_starter.ui.theme.TipAppStarterTheme
import com.example.tipapp_starter.utils.calculateTotalPerPerson
import com.example.tipapp_starter.utils.calculateTotalTip

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipAppStarterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TipApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Topheader(totalPerPerson: Double = 0.0) {
    Surface(modifier = Modifier
        .fillMaxWidth()
        .height(150.dp)
        .clip(shape=RoundedCornerShape(corner = CornerSize(12.dp))),color = Color(0xFFE9D7F7)
    ){
        Column(modifier = Modifier.padding(12.dp)
        ,horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Text(text = "Total Per Person",style = MaterialTheme.typography.headlineSmall)
            Text(text = "$$totalPerPerson",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold)

        }
    }
}

@Preview(showBackground = true)
@Composable
fun TipApp(modifier: Modifier = Modifier) {

    val totalBillState = remember { mutableStateOf("") }
    val totalPerPersonState = remember { mutableStateOf(0.0) }
    val splitNumber = remember { mutableStateOf(1) }
    val tipPercentage = remember { mutableStateOf(0) }
    val sliderPositionState = remember { mutableStateOf(0f) }
    val tipAmountState = remember { mutableStateOf(0.0) }

        Surface(modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(),
            shape = RoundedCornerShape(corner = CornerSize(8.dp)),
            border = BorderStroke(width = 3.dp, color = Color.LightGray)
        )
        {
            Column(modifier = Modifier.padding(6.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top)
            {
                Topheader(totalPerPerson = totalPerPersonState.value)
                InputField(valueState = totalBillState, labelId = "Ingresar Costo")

                Row(modifier = Modifier.padding(3.dp),
                    horizontalArrangement = Arrangement.Start
                )
                {
                    Text("Split", modifier = Modifier.align(alignment = Alignment.CenterVertically))
                    Spacer(modifier = Modifier.width(120.dp))
                    RoundedIconButton(
                        imageVector = Icons.Default.Remove,
                        onClick = {splitNumber.value--
                            totalPerPersonState.value = calculateTotalPerPerson(
                                totalBillState.value.toDouble(),
                                splitNumber.value,
                                tipPercentage.value)
                                  },
                        borderColor = Color.LightGray
                    )
                    Text("${splitNumber.value}", modifier = Modifier.align(alignment = Alignment.CenterVertically))
                    RoundedIconButton(
                        imageVector = Icons.Default.Add,
                        onClick = {splitNumber.value++
                                totalPerPersonState.value = calculateTotalPerPerson(
                                totalBillState.value.toDouble(),
                                splitNumber.value,
                                tipPercentage.value)
                                  },
                        borderColor = Color.LightGray
                    )
                }
                Row(modifier = Modifier.padding(3.dp),
                    horizontalArrangement = Arrangement.Start
                ){
                    Text("Tip")
                    Spacer(modifier = Modifier.width(200.dp))
                    Text("${tipPercentage.value}%")
                }
                Slider(
                    value = sliderPositionState.value,
                    onValueChange = {
                        sliderPositionState.value = it

                        tipPercentage.value = sliderPositionState.value.toInt()
                        tipAmountState.value = calculateTotalTip(
                            totalBillState.value.toDouble(),
                            tipPercentage.value
                        )
                        totalPerPersonState.value = calculateTotalPerPerson(
                            totalBillState.value.toDouble(),
                            splitNumber.value,
                            tipPercentage.value
                        )
                                    },
                    valueRange = 0f..25f,
                )
            }
        }
}