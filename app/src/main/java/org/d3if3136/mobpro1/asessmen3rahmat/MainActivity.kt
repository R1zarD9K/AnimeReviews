package org.d3if3136.mobpro1.asessmen3rahmat

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3136.mobpro1.asessmen3rahmat.ui.widgets.ProfilDialog
import org.d3if3136.mobpro1.asessmen3rahmat.ui.widgets.TopAppBarWidget
import org.d3if3136.mobpro1.asessmen3rahmat.system.database.mainViewModel
import org.d3if3136.mobpro1.asessmen3rahmat.system.database.model.User
import org.d3if3136.mobpro1.asessmen3rahmat.system.navigation.NavigationGraph
import org.d3if3136.mobpro1.asessmen3rahmat.system.network.UserDataStore
import org.d3if3136.mobpro1.asessmen3rahmat.system.network.signIn
import org.d3if3136.mobpro1.asessmen3rahmat.system.network.signOut
import org.d3if3136.mobpro1.asessmen3rahmat.system.utils.SettingsDataStore
import org.d3if3136.mobpro1.asessmen3rahmat.ui.component.getCroppedImage
import org.d3if3136.mobpro1.asessmen3rahmat.ui.theme.Asessmen3RahmatTheme
import org.d3if3136.mobpro1.asessmen3rahmat.ui.widgets.AddForm

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BaseApp()
        }
    }
}

@Composable
fun BaseApp(
    navController: NavHostController = rememberNavController(),
    context: Context = LocalContext.current,
    systemViewModel: mainViewModel = viewModel()
) {
    val dataStore = SettingsDataStore(context)
    val userStore = UserDataStore(context)
    val appTheme by dataStore.layoutFlow.collectAsState(true)
    var showDialog by remember { mutableStateOf(false) }
    val user by userStore.userFlow.collectAsState(User())

    var shownImage by rememberSaveable { mutableStateOf(false) }
    var bitmap: Bitmap? by remember { mutableStateOf(null) }
    val launcher = rememberLauncherForActivityResult(contract = CropImageContract()) {
        bitmap = getCroppedImage(context.contentResolver, it)
        if (bitmap != null) shownImage = true
    }

    Asessmen3RahmatTheme(darkTheme = appTheme) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            topBar = {
                TopAppBarWidget(
                    title = stringResource(id = R.string.app_name),
                    user = user,
                    appTheme = appTheme,
                    showDialog = showDialog,
                    onShowDialogChange = { showDialog = it },
                    onAppThemeChange = { newTheme ->
                        CoroutineScope(Dispatchers.IO).launch {
                            dataStore.saveLayout(!appTheme)
                        }
                    }
                )
            },
            bottomBar = {
                //BottomBarWidget(navController)
            },
            ////////////////////////////////////
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    text = { Text(stringResource(id = R.string.addReview)) },
                    icon = {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = stringResource(id = R.string.addReview)
                        )
                    },
                    onClick = {
                        val options = CropImageContractOptions(
                            null, CropImageOptions(
                                imageSourceIncludeGallery = true,
                                imageSourceIncludeCamera = true,
                                fixAspectRatio = true
                            )
                        )
                        launcher.launch(options)
                    }
                )
            }
            ////////////////////////////////////

        ) { paddingValues ->
            Modifier.padding(paddingValues)
            //NavigationGraph(navController, apiProfile, modifier = Modifier.padding(paddingValues))
            NavigationGraph(navController, Modifier.padding(paddingValues), systemViewModel, user)

            // LaunchedEffect to handle sign-in if needed
            LaunchedEffect(showDialog) {
                if (showDialog && user.email.isEmpty()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        signIn(context, userStore)
                    }
                }
            }

            // Display the dialog if showDialog is true
            if (showDialog && user.email.isNotEmpty()) {
                ProfilDialog(user = user, onDismissRequest = { showDialog = false }) {
                    CoroutineScope(Dispatchers.IO).launch {
                        signOut(context, userStore)
                    }
                    showDialog = false
                }
            }

            if (shownImage) {
                AddForm(bitmap = bitmap, onDismissRequest = { shownImage = false }, onConfirmation = { animeTitle, animeReview  ->
                    // Do something
                    systemViewModel.addAnime(user.email, animeTitle, animeReview, bitmap!!)
                    shownImage = false
                })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Asessmen3RahmatTheme {
        BaseApp()
    }
}