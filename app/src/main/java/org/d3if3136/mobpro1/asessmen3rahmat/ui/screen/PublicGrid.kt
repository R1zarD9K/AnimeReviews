package org.d3if3136.mobpro1.asessmen3rahmat.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.d3if3136.mobpro1.asessmen3rahmat.R
import org.d3if3136.mobpro1.asessmen3rahmat.system.database.mainViewModel
import org.d3if3136.mobpro1.asessmen3rahmat.system.database.model.User
import org.d3if3136.mobpro1.asessmen3rahmat.system.network.AnimeStatus
import org.d3if3136.mobpro1.asessmen3rahmat.ui.component.ListItem

@Composable
fun PublicGrid(name: String, viewModel: mainViewModel, modifier: Modifier = Modifier, user: User) {
    val status by viewModel._status.collectAsState()
    val anime by viewModel.animeData.observeAsState()

    when (status) {
        AnimeStatus.LOADING -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Loading...",
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        AnimeStatus.SUCCESS -> {
            val filteredAnime = anime?.filter { it.email == user.email }
            LazyVerticalGrid(
                modifier = modifier
                    .fillMaxSize()
                    .padding(4.dp),
                columns = GridCells.Fixed(2)
            ) {
                items(filteredAnime!!) { outfit ->
                    ListItem(outfit, onDelete = { viewModel.deleteAnime(it) })
                }
            }
        }

        AnimeStatus.FAILED -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = stringResource(id = R.string.error))
                Button(
                    onClick = { viewModel.getAllAnime() },
                    modifier = Modifier.padding(top = 16.dp),
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
                ) {
                    Text(text = stringResource(id = R.string.try_again))
                }
            }

        }

        null -> TODO()
    }

}