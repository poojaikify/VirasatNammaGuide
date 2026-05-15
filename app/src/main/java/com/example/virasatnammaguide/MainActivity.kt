package com.example.virasatnammaguide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.virasatnammaguide.data.heritageSites

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            com.example.virasatnammaguide.ui.theme.VirasatNammaGuideTheme {
                VirasatApp()
            }
        }
    }
}

@Composable
fun VirasatApp() {

    var selectedScreen by remember { mutableStateOf("SPLASH") }

    var selectedSite by remember {
        mutableStateOf<com.example.virasatnammaguide.data.HeritageSite?>(null)
    }

    var savedSites by remember {
        mutableStateOf(setOf<Int>())
    }

    var checkedInSites by remember {
        mutableStateOf(setOf<Int>())
    }

    Scaffold(
        containerColor = Color(0xFFF8F4EF),
        bottomBar = {
            if (selectedScreen != "SPLASH") {
                BottomNavigationBar(selectedScreen) {
                    selectedScreen = it
                }
            }
        }
    ) { padding ->

        Box(modifier = Modifier.padding(padding)) {

            when (selectedScreen) {

                "SPLASH" -> SplashScreen {
                    selectedScreen = "HOME"
                }

                "HOME" -> HomeScreen {
                    selectedSite = it
                    selectedScreen = "DETAILS"
                }

                "EXPLORE" -> ExploreScreen {
                    selectedSite = it
                    selectedScreen = "DETAILS"
                }

                "DETAILS" -> {
                    selectedSite?.let { site ->
                        DetailsScreen(
                            site = site,
                            isSaved = savedSites.contains(site.id),
                            isCheckedIn = checkedInSites.contains(site.id),
                            onSaveClick = {
                                savedSites =
                                    if (savedSites.contains(site.id)) {
                                        savedSites - site.id
                                    } else {
                                        savedSites + site.id
                                    }
                            },
                            onCheckInClick = {
                                checkedInSites =
                                    if (checkedInSites.contains(site.id)) {
                                        checkedInSites - site.id
                                    } else {
                                        checkedInSites + site.id
                                    }
                            }
                        )
                    }
                }

                "SCAN" -> ScanScreen()

                "PASSPORT" -> PassportScreen(
                    checkedInCount = checkedInSites.size
                )

                "PROFILE" -> ProfileScreen(
                    savedCount = savedSites.size,
                    checkedInCount = checkedInSites.size
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    selected: String,
    onSelected: (String) -> Unit
) {
    NavigationBar {

        NavigationBarItem(
            selected = selected == "HOME",
            onClick = { onSelected("HOME") },
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Home") }
        )

        NavigationBarItem(
            selected = selected == "EXPLORE",
            onClick = { onSelected("EXPLORE") },
            icon = { Icon(Icons.Default.TravelExplore, contentDescription = null) },
            label = { Text("Explore") }
        )

        NavigationBarItem(
            selected = selected == "SCAN",
            onClick = { onSelected("SCAN") },
            icon = { Icon(Icons.Default.QrCodeScanner, contentDescription = null) },
            label = { Text("Scan") }
        )

        NavigationBarItem(
            selected = selected == "PASSPORT",
            onClick = { onSelected("PASSPORT") },
            icon = { Icon(Icons.Default.WorkspacePremium, contentDescription = null) },
            label = { Text("Passport") }
        )

        NavigationBarItem(
            selected = selected == "PROFILE",
            onClick = { onSelected("PROFILE") },
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
            label = { Text("Profile") }
        )
    }
}

@Composable
fun SplashScreen(
    onStartClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF8B1E12)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Virasat-Namma Guide",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Discover Karnataka’s Hidden Heritage",
                color = Color(0xFFFFE8C2)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = onStartClick,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                )
            ) {
                Text(
                    text = "Get Started",
                    color = Color(0xFF8B1E12),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun HomeScreen(
    onSiteClick: (com.example.virasatnammaguide.data.HeritageSite) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F4EF))
            .padding(16.dp)
    ) {

        item {
            Text(
                text = "Virasat-Namma Guide",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF8B1E12)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Discover Karnataka’s Hidden Heritage",
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(20.dp))
        }

        items(heritageSites) { site ->
            HeritageCard(
                title = site.name,
                image = site.imageRes,
                distance = site.distance,
                onClick = {
                    onSiteClick(site)
                }
            )
        }
    }
}

@Composable
fun ExploreScreen(
    onSiteClick: (com.example.virasatnammaguide.data.HeritageSite) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("ALL") }

    val categories = listOf("ALL", "TEMPLE", "MONUMENT")

    val filteredSites = heritageSites.filter {
        val matchesSearch =
            it.name.contains(searchText, ignoreCase = true)

        val matchesCategory =
            selectedCategory == "ALL" ||
                    it.category.uppercase() == selectedCategory

        matchesSearch && matchesCategory
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F4EF))
            .padding(16.dp)
    ) {

        item {
            Text(
                text = "Discovery",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF8B1E12)
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Search Heritage Sites")
                },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                shape = RoundedCornerShape(20.dp),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                categories.forEach { category ->

                    val isSelected = selectedCategory == category

                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (isSelected) Color(0xFF8B1E12)
                                else Color.White
                            )
                            .border(
                                1.dp,
                                Color(0xFF8B1E12),
                                RoundedCornerShape(16.dp)
                            )
                            .clickable {
                                selectedCategory = category
                            }
                            .padding(
                                horizontal = 20.dp,
                                vertical = 12.dp
                            )
                    ) {
                        Text(
                            text = category,
                            color =
                                if (isSelected) Color.White
                                else Color(0xFF8B1E12),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        if (filteredSites.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No Heritage Sites Found",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF8B1E12)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Try another keyword or category.",
                            color = Color.DarkGray
                        )
                    }
                }
            }
        } else {
            items(filteredSites) { site ->
                HeritageCard(
                    title = site.name,
                    image = site.imageRes,
                    distance = site.distance,
                    onClick = {
                        onSiteClick(site)
                    }
                )
            }
        }
    }
}

@Composable
fun HeritageCard(
    title: String,
    image: Int,
    distance: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(image),
                contentDescription = null,
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D1B16)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = distance)
            }
        }
    }
}

@Composable
fun ScanScreen() {
    var qrText by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F4EF))
            .padding(16.dp)
    ) {
        Text(
            text = "QR Heritage Scan",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF8B1E12)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Enter QR Code Content",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D1B16)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "For emulator demo, enter the heritage site name shown inside the QR code.",
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(18.dp))

                OutlinedTextField(
                    value = qrText,
                    onValueChange = {
                        qrText = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("QR Code Text")
                    },
                    placeholder = {
                        Text("Example: Virupaksha Temple, Hampi")
                    },
                    shape = RoundedCornerShape(18.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        result =
                            if (qrText.isNotBlank()) {
                                "QR Verified Successfully\n\nHeritage Site:\n$qrText\n\nHidden heritage information unlocked."
                            } else {
                                "Please enter QR code text."
                            }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8B1E12)
                    )
                ) {
                    Text("Scan QR")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (result.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Text(
                    text = result,
                    modifier = Modifier.padding(20.dp),
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2D1B16)
                )
            }
        }
    }
}

@Composable
fun PassportScreen(
    checkedInCount: Int
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F4EF))
            .padding(16.dp)
    ) {

        item {
            Text(
                text = "Heritage Passport",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B1E12)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF8B1E12)
                ),
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Explorer Tier",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Visited",
                                color = Color.White
                            )

                            Text(
                                text = "$checkedInCount Sites",
                                color = Color.White,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column {
                            Text(
                                text = "Badges",
                                color = Color.White
                            )

                            Text(
                                text = "04 Won",
                                color = Color.White,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Unlocked Badges",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D1B16)
            )

            Spacer(modifier = Modifier.height(12.dp))

            BadgeCard(
                title = "Heritage Explorer",
                description = "Visited multiple heritage places"
            )

            BadgeCard(
                title = "Temple Trail",
                description = "Explored famous temple architecture"
            )

            BadgeCard(
                title = "Culture Learner",
                description = "Unlocked historical facts and legends"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Recent Activity",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D1B16)
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        items(heritageSites.take(3)) { site ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(site.imageRes),
                        contentDescription = null,
                        modifier = Modifier
                            .size(58.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = site.name,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Visited",
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(
    savedCount: Int,
    checkedInCount: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F4EF))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Pooja Explorer",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF8B1E12)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Historical Virtuoso",
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text("Saved Sites: $savedCount")

                Spacer(modifier = Modifier.height(16.dp))

                Text("Visited Sites: $checkedInCount")

                Spacer(modifier = Modifier.height(16.dp))

                Text("Language: English")

                Spacer(modifier = Modifier.height(16.dp))

                Text("App Settings")
            }
        }
    }
}

@Composable
fun DetailsScreen(
    site: com.example.virasatnammaguide.data.HeritageSite,
    isSaved: Boolean,
    isCheckedIn: Boolean,
    onSaveClick: () -> Unit,
    onCheckInClick: () -> Unit
) {
    var isKannada by remember { mutableStateOf(false) }
    var isAudioPlaying by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F4EF))
    ) {

        item {
            Box {
                Image(
                    painter = rememberAsyncImagePainter(site.imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Crop
                )

                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomStart),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xDDFFFFFF)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = site.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF8B1E12)
                        )

                        Text(
                            text = site.location,
                            color = Color.DarkGray
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AssistChip(
                        onClick = { },
                        label = { Text(site.category) }
                    )

                    AssistChip(
                        onClick = { },
                        label = { Text(site.distance) }
                    )

                    AssistChip(
                        onClick = {
                            isKannada = !isKannada
                        },
                        label = {
                            Text(
                                if (isKannada) "English" else "ಕನ್ನಡ"
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF8B1E12)
                    )
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Digital Heritage Guide",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Explore history, architecture, legends and cultural importance of this heritage site.",
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                DetailSection(
                    title = "History",
                    description =
                        if (isKannada)
                            site.kannadaHistory
                        else
                            site.history
                )

                DetailSection(
                    title = "Architecture",
                    description = site.architecture
                )

                DetailSection(
                    title = "Local Legend",
                    description = site.legend
                )

                DetailSection(
                    title = "Visitor Information",
                    description = "Suggested visit time: 1–2 hours\nBest time to visit: Morning or evening\nRecommended for: Tourists, students and heritage lovers"
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        onSaveClick()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8B1E12)
                    )
                ) {
                    Text(
                        if (isSaved)
                            "Saved to Heritage Passport"
                        else
                            "Save Site"
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = {
                        onCheckInClick()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(
                        if (isCheckedIn)
                            "Checked-In"
                        else
                            "Check-In at Site"
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = {
                        isAudioPlaying = !isAudioPlaying
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(
                        if (isAudioPlaying)
                            "Pause Audio Guide"
                        else
                            "Play Audio Guide"
                    )
                }

                if (isAudioPlaying) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Audio guide is playing. Listen to the history, architecture and local legends of ${site.name}.",
                            modifier = Modifier.padding(16.dp),
                            color = Color.DarkGray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
fun DetailSection(
    title: String,
    description: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(modifier = Modifier.padding(18.dp)) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D1B16)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
fun BadgeCard(
    title: String,
    description: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B1E12)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = description,
                color = Color.DarkGray
            )
        }
    }
}