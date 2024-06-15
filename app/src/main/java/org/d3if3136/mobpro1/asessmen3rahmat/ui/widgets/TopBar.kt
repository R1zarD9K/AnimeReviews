package org.d3if3136.mobpro1.asessmen3rahmat.ui.widgets

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import org.d3if3136.mobpro1.asessmen3rahmat.R
import org.d3if3136.mobpro1.asessmen3rahmat.system.database.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWidget(
    title: String,
    user: User,
    appTheme: Boolean,
    showDialog: Boolean,
    onShowDialogChange: (Boolean) -> Unit,
    onAppThemeChange: (Boolean) -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(title, style = MaterialTheme.typography.titleMedium)
        },
        navigationIcon = {
            IconButton(onClick = { onAppThemeChange(!appTheme) }) {
                Icon(
                    painter = if (appTheme) painterResource(id = R.drawable.dark_mode) else painterResource(
                        id = R.drawable.light_mode
                    ),
                    contentDescription = ""
                )
            }
        },
        actions = {
            IconButton(
                onClick = { onShowDialogChange(!showDialog) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.login),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(user.photoUrl)
                        .crossfade(true)
                        .build(),
                    error = {painterResource(id = R.drawable.profile_circle)},
                    contentDescription = null
                )
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
        )
    )
}