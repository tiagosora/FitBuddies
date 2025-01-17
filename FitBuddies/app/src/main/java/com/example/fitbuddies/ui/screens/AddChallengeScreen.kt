package com.example.fitbuddies.ui.screens

import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.Image
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Group
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.example.fitbuddies.ui.components.NavigationItem
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fitbuddies.ai.AiClient.generateFitnessChallenge
import com.example.fitbuddies.viewmodels.AddChallengeViewModel
import com.example.fitbuddies.viewmodels.ProfileViewModel
import com.example.fitbuddies.data.models.User
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddChallengeScreen(
    navController: NavHostController,
    addChallengeViewModel: AddChallengeViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("Running") }
    var duration by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var expandedImage by remember { mutableStateOf(false) }
    var goal by remember { mutableStateOf("") }
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    val context = LocalContext.current
    var showModal by remember { mutableStateOf(false) }
    val selectedFriends = remember { mutableStateListOf<User>() } // Alterado para `User`
    val scrollState = rememberScrollState()
    val user by profileViewModel.user.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                    val bitmap = ImageDecoder.decodeBitmap(source)
                    imageBitmap = bitmap.asImageBitmap()
                } else {
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream?.close()
                    imageBitmap = bitmap?.asImageBitmap()
                }
            }
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { bitmap ->
            bitmap?.let {
                imageBitmap = it.asImageBitmap()
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .aspectRatio(1f)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.small
                    )
                    .clickable { expandedImage = true },
                contentAlignment = Alignment.Center
            ) {
                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap!!,
                        contentDescription = "Selected Image",
                        modifier = Modifier.size(64.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = "Add Image",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                DropdownMenu(
                    expanded = expandedImage,
                    onDismissRequest = { expandedImage = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Choose from Gallery") },
                        onClick = {
                            expandedImage = false
                            launcher.launch("image/*")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Take a Photo") },
                        onClick = {
                            expandedImage = false
                            cameraLauncher.launch(null)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "Create New Challenge",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "by ${user?.firstName ?: "Guest"} ${user?.lastName ?: ""}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Challenge Title") },
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedType,
                onValueChange = {},
                readOnly = true,
                label = { Text("Challenge Type") },
                shape = MaterialTheme.shapes.medium,
                trailingIcon = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Expand"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                listOf("Running", "Cycling", "Weightlifting", "Yoga", "Other").forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            selectedType = type
                            expanded = false
                        }
                    )
                }
            }
        }

        Button(
            onClick = {
                generateFitnessChallenge(selectedType) { aiResponse ->
                    if (aiResponse == null) {
                        println("Failed to generate challenge.")
                        return@generateFitnessChallenge
                    }

                    title = aiResponse["title"] as String
                    description = aiResponse["description"] as String
                    duration = aiResponse["duration"].toString().removeSuffix(".0")
                    goal = aiResponse["goal"].toString().removeSuffix(".0")
                }
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
        ) {
            Icon(Icons.Default.AddTask, contentDescription = "Generate Challenge Using AI")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Generate Challenge Using AI")
        }

        OutlinedTextField(
            value = duration,
            onValueChange = { duration = it },
            label = { Text("Duration (days)") },
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = goal,
            onValueChange = { goal = it },
            label = {
                Text(
                    if (selectedType == "Running" || selectedType == "Cycling")
                        "Goal (km)"
                    else
                        "Goal (hours)"
                )
            },
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                showModal = true
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
        ) {
            Icon(Icons.Default.Group, contentDescription = "Invite Friends")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Invite Friends")
        }

        if (showModal) {
            InviteFriendsModal(
                onDismiss = { showModal = false },
                onConfirm = { friends ->
                    selectedFriends.clear()
                    selectedFriends.addAll(friends) // Recebe `User` diretamente
                    showModal = false
                },
                viewModel = addChallengeViewModel
            )
        }

        Button(
            onClick = {
                if (title.isEmpty() || description.isEmpty() || duration.isEmpty() || goal.isEmpty()) {
                    duration = 0.toString()
                    goal = 0.toString()
                }
                addChallengeViewModel.createChallenge(
                    title = title,
                    description = description,
                    type = selectedType,
                    selectedFriends = selectedFriends,
                    duration = duration.toInt(),
                    goal = goal.toInt(),
                    photoBitmap = imageBitmap
                )
                navController.navigate("main") {
                    popUpTo("main") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            Text("Create Challenge")
        }
    }
}


@Composable
fun InviteFriendsModal(
    onDismiss: () -> Unit,
    onConfirm: (List<User>) -> Unit,
    viewModel: AddChallengeViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    val friends = remember { mutableStateListOf<User>() }
    val selectedFriends = remember { mutableStateListOf<User>() }

    // Buscar amigos ao abrir o modal
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val result = viewModel.fetchFriends()
            friends.clear()
            friends.addAll(result)
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            tonalElevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Invite Friends",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                // Campo de busca
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 16.sp)
                )

                // Lista de amigos filtrados
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    friends.filter { "${it.firstName} ${it.lastName}".contains(searchQuery, ignoreCase = true) }
                        .forEach { friend ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (selectedFriends.contains(friend)) {
                                            selectedFriends.remove(friend)
                                        } else {
                                            selectedFriends.add(friend)
                                        }
                                    }
                                    .padding(8.dp)
                            ) {
                                Checkbox(
                                    checked = selectedFriends.contains(friend),
                                    onCheckedChange = null
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("${friend.firstName} ${friend.lastName}", fontSize = 16.sp)
                            }
                        }
                }

                // Botões de Cancelar e Confirmar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    TextButton(onClick = { onConfirm(selectedFriends) }) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}
