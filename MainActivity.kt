package com.example.nammametrosahaya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nammametrosahaya.ui.theme.NammaMetroSahayaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NammaMetroSahayaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF5F7FA)
                ) {
                    MainAppController()
                }
            }
        }
    }
}

// DATA MODELS
data class Station(val name: String, val line: String)

val purple = listOf(
    "Whitefield (Kadugodi)", "Hopefarm Channasandra", "Kadugodi Tree Park", "Pattandur Agrahara",
    "Sri Sathya Sai Hospital", "Nallurhalli", "Kundalahalli", "Seetharampalya", "Hoodi Junction",
    "Garudacharpalya", "Mahadevapura", "Singayyanapalya", "Krishnarajapura", "Benniganahalli",
    "Baiyappanahalli", "Swami Vivekananda Road", "Indiranagar", "Halasuru", "Trinity", "MG Road",
    "Cubbon Park", "Dr. B. R. Ambedkar Station, Vidhana Soudha", "Sir M. Visvesvaraya Station",
    "Majestic", "City Railway Station", "Magadi Road", "Hosahalli", "Vijayanagar", "Attiguppe",
    "Deepanjali Nagar", "Mysuru Road", "Nayandahalli", "Jnanabharathi", "Rajarajeshwari Nagar",
    "Pattanagere", "Kengeri Bus Terminal", "Kengeri", "Challaghatta"
)

val green = listOf(
    "Madavara", "Chikkabidarakallu", "Manjunathanagar", "Nagasandra", "Dasarahalli", "Jalahalli",
    "Peenya Industry", "Peenya", "Goraguntepalya", "Yeshwanthpur", "Sandal Soap Factory",
    "Mahalakshmi", "Rajajinagar", "Mahakavi Kuvempu Road", "Srirampura", "Mantri Square Sampige Road",
    "Majestic", "Chickpete", "Krishna Rajendra Market", "National College", "Lalbagh",
    "South End Circle", "Jayanagar", "Rashtreeya Vidyalaya Road", "Banashankari", "Jaya Prakash Nagar",
    "Yelachenahalli", "Konanakunte Cross", "Doddakallasandra", "Vajarahalli", "Talaghattapura", "Silk Institute"
)

val allStationsSorted = (purple + green).distinct().sorted()

// HELPERS
fun getLineKannada(line: String): String {
    return when (line) {
        "Purple" -> "ನೇರಳೆ"
        "Green" -> "ಹಸಿರು"
        "Interchange" -> "ಬದಲಾವಣೆ"
        else -> line
    }
}

@Composable
fun DualText(k: String, e: String, big: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = k, fontSize = if (big) 24.sp else 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text(text = e, fontSize = if (big) 16.sp else 14.sp, color = Color.DarkGray)
    }
}

// EXIT DATA ENGINE
fun getExitData(stationName: String): List<String> {
    return when (stationName) {
        "Majestic" -> listOf("Gate A: Majestic Bus Stand (KSRTC/BMTC)", "Gate B: Sangolli Rayanna Railway Station", "Gate C: Upparpet/City Market Side", "Gate D: Dhanvantari Road")
        "MG Road" -> listOf("Gate 1: Brigade Road / Church Street", "Gate 2: MG Road Boulevard / Rangoli Center")
        "Indiranagar" -> listOf("Gate A: 100 Feet Road Side", "Gate B: CMH Road / Binnamangala Side")
        "KR Puram (Krishnarajapura)" -> listOf("Gate 1: KR Puram Railway Station Bridge", "Gate 2: Tin Factory / Outer Ring Road")
        "Yeshwanthpur" -> listOf("Gate A: Yeshwanthpur Railway Station (PF 1/6)", "Gate B: Tumkur Road / APMC Yard")
        "Vidhana Soudha" -> listOf("Gate 1: Vidhana Soudha / High Court", "Gate 2: Post Office / Cubbon Park Side")
        else -> listOf("Gate 1: Main Road / Entry Side", "Gate 2: Bus Stop / Auto Stand Side")
    }
}

// MAIN CONTROLLER
@Composable
fun MainAppController() {
    var screen by remember { mutableStateOf("selection") }
    var start by remember { mutableStateOf("Whitefield (Kadugodi)") }
    var end by remember { mutableStateOf("Majestic") }
    var route by remember { mutableStateOf(listOf<Station>()) }

    when (screen) {
        "selection" -> MetroSelectionScreen(
            start, end, { start = it }, { end = it },
            onNext = { route = generateRoute(start, end); screen = "route" },
            onExitFinder = { screen = "exit" }
        )
        "route" -> RouteScreen(route) { screen = "guide" }
        "guide" -> VisualGuideScreen(route) { screen = "selection" }
        "exit" -> ExitFinderScreen { screen = "selection" }
    }
}

// SCREENS
@Composable
fun MetroSelectionScreen(
    start: String, end: String, onStart: (String) -> Unit, onEnd: (String) -> Unit,
    onNext: () -> Unit, onExitFinder: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Spacer(Modifier.height(20.dp))
        Text("🚇 Namma Metro Sahaya", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4A148C))
        Text("ಸ್ಮಾರ್ಟ್ ಮೆಟ್ರೋ ಮಾರ್ಗದರ್ಶಿ", fontSize = 16.sp, color = Color.Gray)

        Spacer(Modifier.height(20.dp))
        Text("📍 FROM STATION", color = Color.Gray, fontWeight = FontWeight.Bold)
        SimpleDropdown(start, allStationsSorted, onStart)

        Text("🏁 TO STATION", color = Color.Gray, fontWeight = FontWeight.Bold)
        SimpleDropdown(end, allStationsSorted, onEnd)

        Spacer(Modifier.height(30.dp))
        Button(onClick = onNext, modifier = Modifier.fillMaxWidth().height(55.dp), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A))) {
            Text("🚇 FIND ROUTE", color = Color.White, fontWeight = FontWeight.Bold)
        }
        Button(onClick = onExitFinder, modifier = Modifier.fillMaxWidth().height(55.dp), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B))) {
            Text("🚪 EXIT GATE FINDER", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun RouteScreen(route: List<Station>, onNext: () -> Unit) {
    val totalStations = route.size
    val fare = 10 + (if (totalStations > 1) (totalStations - 1) * 2 else 0)

    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("Route Summary", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)

        LazyColumn(Modifier.weight(1f).padding(vertical = 10.dp)) {
            // Explicitly defining (index: Int, station: Station) helps the compiler
            itemsIndexed(route) { index: Int, station: Station ->
                Text(
                    text = "${index + 1}. ${station.name}",
                    color = Color.DarkGray,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Total Stations: $totalStations", fontWeight = FontWeight.Bold)
                Text("Estimated Fare: ₹$fare", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A))
        ) {
            Text("START VISUAL GUIDE", color = Color.White)
        }
    }
}

@Composable
fun VisualGuideScreen(
    route: List<Station>,
    onFinish: () -> Unit
) {

    val steps = generateDynamicSteps(route)

    var index by remember {
        mutableIntStateOf(0)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val image = when {

            // SCAN STEP
            steps[index].second.contains("Scan", true) ->
                R.drawable.scan_ticket

            // INTERCHANGE STEP
            steps[index].second.contains("Interchange", true) ||
                    steps[index].second.contains("Majestic", true) ->
                R.drawable.majestic_interchange_new

            // PLATFORM STEP
            steps[index].second.contains("Go to Platform", true) ->
                R.drawable.station_entry

            // BOARD TRAIN STEP
            steps[index].second.contains("Board", true) ->
                R.drawable.platform_entry

            // TRAVEL STEP
            steps[index].second.contains("Travel", true) ->
                R.drawable.metro_inside

            // DESTINATION ARRIVAL
            steps[index].second.contains("reached", true) ->
                R.drawable.station_arrival

            // EXIT GATE STEP
            steps[index].second.contains("Use one of these exits", true) ->
                R.drawable.exit_gates

            else ->
                R.drawable.station_entry
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),

            shape = RoundedCornerShape(20.dp),

            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {

            Image(
                painter = painterResource(id = image),

                contentDescription = null,

                modifier = Modifier.fillMaxSize(),

                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        DualText(
            steps[index].first,
            steps[index].second,
            true
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),

            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            OutlinedButton(
                onClick = {

                    if (index > 0)
                        index--
                    else
                        onFinish()
                }
            ) {

                Text("BACK")
            }

            Button(
                onClick = {

                    if (index < steps.size - 1)
                        index++
                    else
                        onFinish()
                },

                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E7D32)
                )
            ) {

                Text(
                    if (index == steps.size - 1)
                        "FINISH"
                    else
                        "NEXT"
                )
            }
        }
    }
}
@Composable
fun ExitFinderScreen(onBack: () -> Unit) {
    var selectedStation by remember { mutableStateOf(allStationsSorted[0]) }
    var expanded by remember { mutableStateOf(false) }
    val currentExits = getExitData(selectedStation)
    Column(Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("🚪 Exit Gate Finder", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00796B))
        Spacer(Modifier.height(20.dp))
        Box {
            OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) { Text(selectedStation, color = Color.Black) }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                allStationsSorted.forEach { station ->
                    DropdownMenuItem(text = { Text(station) }, onClick = { selectedStation = station; expanded = false })
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        LazyColumn(Modifier.weight(1f)) {
            items(currentExits) { exit ->
                Card(Modifier.fillMaxWidth().padding(vertical = 4.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Text(exit, Modifier.padding(16.dp), color = Color.DarkGray)
                }
            }
        }
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth().height(50.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B))) {
            Text("BACK TO HOME", color = Color.White)
        }
    }
}

@Composable
fun SimpleDropdown(selected: String, list: List<String>, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth().height(55.dp), shape = RoundedCornerShape(12.dp)) {
            Text(selected, color = Color.Black)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            list.forEach { DropdownMenuItem(text = { Text(it) }, onClick = { onSelect(it); expanded = false }) }
        }
    }
}

// LOGIC
fun generateRoute(start: String, end: String): List<Station> {
    val startInPurple = purple.contains(start)
    val endInPurple = purple.contains(end)
    val startInGreen = green.contains(start)
    val endInGreen = green.contains(end)

    fun getPath(list: List<String>, s: String, e: String): List<String> {
        val startIndex = list.indexOf(s)
        val endIndex = list.indexOf(e)
        return if (startIndex <= endIndex) {
            list.subList(startIndex, endIndex + 1)
        } else {
            list.subList(endIndex, startIndex + 1).reversed()
        }
    }

    val finalNames = when {
        // Case 1: Both on Purple Line
        startInPurple && endInPurple -> getPath(purple, start, end)

        // Case 2: Both on Green Line
        startInGreen && endInGreen -> getPath(green, start, end)

        // Case 3: Interchange (Purple to Green)
        startInPurple && endInGreen -> {
            getPath(purple, start, "Majestic") + getPath(green, "Majestic", end).drop(1)
        }

        // Case 4: Interchange (Green to Purple)
        startInGreen && endInPurple -> {
            getPath(green, start, "Majestic") + getPath(purple, "Majestic", end).drop(1)
        }

        else -> listOf(start, end)
    }

    return finalNames.map { name ->
        val line = when {
            name == "Majestic" -> "Interchange"
            purple.contains(name) -> "Purple"
            else -> "Green"
        }
        Station(name, line)
    }
}

fun generateDynamicSteps(
    route: List<Station>
): List<Pair<String, String>> {

    if (route.size < 2)
        return listOf(
            "ದೋಷ" to "No route found"
        )

    val steps = mutableListOf<Pair<String, String>>()

    val first = route.first()

    val second = route[1]

    // PLATFORM NUMBER LOGIC

    val firstPlatform = when (first.line) {

        "Purple" -> {

            val startIndex = purple.indexOf(first.name)

            val endIndex = purple.indexOf(route.last().name)

            if (startIndex < endIndex)
                "Platform 2"
            else
                "Platform 1"
        }

        "Green" -> {

            val startIndex = green.indexOf(first.name)

            val endIndex = green.indexOf(route.last().name)

            if (startIndex < endIndex)
                "Platform 2"
            else
                "Platform 1"
        }

        else -> "Platform"
    }

    // STEP 1 — SCAN ENTRY

    steps.add(
        "ಹಂತ 1: ಟಿಕೆಟ್ ಅಥವಾ ಸ್ಮಾರ್ಟ್ ಕಾರ್ಡ್ ಸ್ಕ್ಯಾನ್ ಮಾಡಿ"
                to
                "Step 1: Scan your ticket or smart card"
    )

    // STEP 2 — PLATFORM

    steps.add(
        "ಹಂತ 2: $firstPlatform ಗೆ ಹೋಗಿ (${second.name} ದಿಕ್ಕು)"
                to
                "Step 2: Go to $firstPlatform (towards ${second.name})"
    )

    // STEP 3 — BOARD TRAIN

    steps.add(
        "ಹಂತ 3: ಮೆಟ್ರೋ ರೈಲಿಗೆ ಹತ್ತಿ"
                to
                "Step 3: Board the metro train"
    )

    // STEP 4 — TRAVEL

    steps.add(
        "ಹಂತ 4: ನಿಲ್ದಾಣಗಳನ್ನು ದಾಟಿ ಪ್ರಯಾಣಿಸಿ"
                to
                "Step 4: Travel through the stations"
    )

    // INTERCHANGE DETECTION

    val interchangeIndex = route.indexOfFirst {

        it.name == "Majestic"
    }

    if (
        interchangeIndex != -1 &&
        interchangeIndex < route.size - 1
    ) {

        val nextStation = route[interchangeIndex + 1]

        val nextLine = nextStation.line

        val nextPlatform = when (nextLine) {

            "Purple" -> {

                val majesticIndex = purple.indexOf("Majestic")

                val nextIndex = purple.indexOf(nextStation.name)

                if (nextIndex > majesticIndex)
                    "Platform 2"
                else
                    "Platform 1"
            }

            "Green" -> {

                val majesticIndex = green.indexOf("Majestic")

                val nextIndex = green.indexOf(nextStation.name)

                if (nextIndex > majesticIndex)
                    "Platform 2"
                else
                    "Platform 1"
            }

            else -> "Platform"
        }

        steps.add(
            "ಹಂತ ${steps.size + 1}: ಮೆಜೆಸ್ಟಿಕ್ ಬದಲಾವಣೆ ನಿಲ್ದಾಣಕ್ಕೆ ತಲುಪಿದ್ದೀರಿ. ${getLineKannada(nextLine)} ಲೈನ್‌ನ $nextPlatform ಗೆ ಹೋಗಿ (${nextStation.name} ದಿಕ್ಕು)"
                    to
                    "Step ${steps.size + 1}: You have reached Majestic Interchange Station. Go to $nextPlatform of the $nextLine Line (towards ${nextStation.name})"
        )
    }

    // ARRIVAL STEP

    val destination = route.last()

    steps.add(
        "ಹಂತ ${steps.size + 1}: ${destination.name} ನಿಲ್ದಾಣಕ್ಕೆ ತಲುಪಿದ್ದೀರಿ"
                to
                "Step ${steps.size + 1}: You have reached ${destination.name} station"
    )

    // EXIT SCAN STEP

    steps.add(
        "ಹಂತ ${steps.size + 1}: ನಿರ್ಗಮನಕ್ಕಾಗಿ ಟಿಕೆಟ್ ಅಥವಾ ಕಾರ್ಡ್ ಸ್ಕ್ಯಾನ್ ಮಾಡಿ"
                to
                "Step ${steps.size + 1}: Scan your ticket/card for exit"
    )

    // EXIT GATES

    val destinationExits = getExitData(destination.name)

    val exitTextEnglish = destinationExits.joinToString("\n")

    val exitTextKannada = destinationExits.joinToString("\n")

    steps.add(
        "ಹಂತ ${steps.size + 1}: ಈ ದ್ವಾರಗಳಲ್ಲಿ ಒಂದನ್ನು ಬಳಸಿ:\n$exitTextKannada"
                to
                "Step ${steps.size + 1}: Use one of these exits:\n$exitTextEnglish"
    )

    return steps
}