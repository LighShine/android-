package com.lizhiheng.myapp.screen

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.solver.state.State
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lizhiheng.myapp.util.ConvertImage
import com.lizhiheng.myapp.viewmodel.AuthViewModel
import com.lizhiheng.myapp.viewmodel.MyImagesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun Record(viewModel: MyImagesViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main_screen") {
        composable("main_screen") {
            MainScreen(navController, viewModel)
        }
        composable("add_screen") {
            AddScreen(navController, viewModel)
        }
        composable(
            "edit_screen/{imageId}",
            arguments = listOf(navArgument("imageId") { type = NavType.IntType })
        ) { backStackEntry ->
            EditScreen(navController, viewModel, backStackEntry.arguments!!.getInt("imageId"))
        }
    }
}
fun formatTimestamp(timestamp: Long): String {
    val dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
    val formatter = DateTimeFormatter.ofPattern("M月d日 E", Locale.CHINESE)
    return dateTime.format(formatter)
}
@Composable
fun ImageDialogg(bitmap: Bitmap, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .wrapContentSize(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp)
            ) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(bitmap.width.toFloat() / bitmap.height.toFloat())
                )
            }
        }
    }
}


@Composable
fun MainScreen(navController: NavController, viewModel: MyImagesViewModel) {
    val images by viewModel.images.observeAsState(emptyList())
    val coroutineScope = rememberCoroutineScope()
    var selectedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "手记",
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF456D1C)
                        )
                    )
                },
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add_screen") },
                modifier= androidx.compose.ui.Modifier.padding(56.dp)) {
                Image(
                    painter = painterResource(id = com.lizhiheng.myapp.R.drawable.ic_add2),
                    contentDescription = null,
                    modifier = androidx.compose.ui.Modifier
                        .size(40.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF0F0F0))
        ) {
            items(images, key = { it.id }) { image ->
                var offsetX by remember { mutableStateOf(0f) }
                Box(
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                        .fillMaxWidth()
                ) {
                    // 删除按钮
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                viewModel.deleteImage(image)
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .offset(x = offsetX.dp + 100.dp)
                    ) {
                        Image(
                            painter = painterResource(id = com.lizhiheng.myapp.R.drawable.ic_delete),
                            contentDescription = null,
                            modifier = androidx.compose.ui.Modifier
                                .size(40.dp)
                        )
                    }

                    // 编辑按钮
                    IconButton(
                        onClick = {
                            navController.navigate("edit_screen/${image.id}")
                        },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .offset(x = offsetX.dp + 150.dp)
                    ) {
                        Image(
                            painter = painterResource(id = com.lizhiheng.myapp.R.drawable.ic_edit),
                            contentDescription = null,
                            modifier = androidx.compose.ui.Modifier
                                .size(40.dp)
                        )
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(x = offsetX.dp)
                            .pointerInput(Unit) {
                                detectHorizontalDragGestures { _, dragAmount ->
                                    val newOffset = (offsetX + dragAmount).coerceIn(-200f, 0f)
                                    offsetX = newOffset
                                }
                            },
                        elevation = 4.dp,
                        shape = RoundedCornerShape(12.dp),
                        backgroundColor = Color(0xFFF3EBDF),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 200.dp)
                            ) {
                                items(image.imageData) { imageData ->
                                    val bitmap = ConvertImage.convertToBitmap(imageData)
                                    Card(
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .size(100.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .clickable { selectedBitmap = bitmap },
                                        elevation = 2.dp
                                    ) {
                                        Image(
                                            bitmap = bitmap.asImageBitmap(),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = image.title,
                                style = androidx.compose.ui.text.TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = image.description,
                                style = androidx.compose.ui.text.TextStyle(fontSize = 14.sp),
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = formatTimestamp(image.timestamp),
                                style = androidx.compose.ui.text.TextStyle(fontSize = 12.sp),
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }

    selectedBitmap?.let { bitmap ->
        ImageDialog(bitmap = bitmap, onDismiss = { selectedBitmap = null })
    }
}
@Composable
fun AddScreen(navController: NavController, viewModel: MyImagesViewModel) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val selectImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        imageUris = uris
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("添加记录") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "取消")
                    }
                },
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            androidx.compose.material.Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp),
                backgroundColor = Color(0xFFC8DDD4)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("标题") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("描述") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { selectImageLauncher.launch("image/*") }
                    ,colors=ButtonDefaults.buttonColors(backgroundColor = Color(0xFFE4CBA3))) {
                        Text("选择图片", fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(128.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(imageUris) { uri ->
                            val bitmap = remember(uri) {
                                if (Build.VERSION.SDK_INT < 28) {
                                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                                } else {
                                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                                    ImageDecoder.decodeBitmap(source)
                                }
                            }
                            Card(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(128.dp)
                            ) {
                                Image(
                                    bitmap = bitmap.asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }
            if (imageUris.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                androidx.compose.material.Button(
                    onClick = {
                        val bitmaps = imageUris.map { uri ->
                            if (Build.VERSION.SDK_INT < 28) {
                                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                            } else {
                                val source = ImageDecoder.createSource(context.contentResolver, uri)
                                ImageDecoder.decodeBitmap(source)
                            }
                        }
                        viewModel.addImage(title, description, bitmaps)
                        navController.popBackStack()
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    colors=ButtonDefaults.buttonColors(backgroundColor = Color(0xFFE4CBA3))
                ) {
                    Text("保存图片")
                }
            }
        }
    }
}

@Composable
fun ImageItem(bitmap: Bitmap, onImageClick: (Bitmap) -> Unit) {
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = null,
        modifier = androidx.compose.ui.Modifier
            .size(128.dp)
            .padding(4.dp)
            .clickable { onImageClick(bitmap) }
    )
}
@Composable
fun ImageDialog(bitmap: Bitmap, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .wrapContentSize(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp)
            ) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(bitmap.width.toFloat() / bitmap.height.toFloat())
                )
            }
        }
    }
}
@Composable
fun EditScreen(navController: NavController, viewModel: MyImagesViewModel, imageId: Int) {
    val context = LocalContext.current
    val image = viewModel.images.observeAsState().value?.find { it.id == imageId }
    var title by remember { mutableStateOf(image?.title ?: "") }
    var description by remember { mutableStateOf(image?.description ?: "") }
    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var oldImageDatas by remember { mutableStateOf(image?.imageData ?: emptyList()) }

    val selectImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        imageUris = uris
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("修改") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "取消")
                    }
                },
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp),
                backgroundColor = Color(0xFFC8DDD4)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("标题") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("描述") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { selectImageLauncher.launch("image/*") }
                        ,colors=ButtonDefaults.buttonColors(backgroundColor = Color(0xFFE4CBA3))) {
                        Text("选择图片")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(128.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (imageUris.isNotEmpty()) {
                            items(imageUris) { uri ->
                                val bitmap by produceState<Bitmap?>(initialValue = null, uri) {
                                    value = loadBitmapFromUri(context, uri)
                                }
                                bitmap?.let {
                                    Card(
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .size(128.dp)
                                    ) {
                                        Image(
                                            bitmap = it.asImageBitmap(),
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }
                            }
                        } else {
                            items(oldImageDatas) { imageData ->
                                val bitmap = ConvertImage.convertToBitmap(imageData)
                                Card(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .size(128.dp)
                                ) {
                                    Image(
                                        bitmap = bitmap.asImageBitmap(),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val finalBitmaps = if (imageUris.isNotEmpty()) {
                        imageUris.mapNotNull { uri ->
                            runBlocking { loadBitmapFromUri(context, uri) }
                        }
                    } else {
                        oldImageDatas.map { ConvertImage.convertToBitmap(it) }
                    }
                    viewModel.updateImage(imageId, title, description, finalBitmaps)
                    navController.popBackStack()
                },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFE4CBA3))
            ) {
                Text("更新图片")
            }
        }
    }
}

suspend fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}