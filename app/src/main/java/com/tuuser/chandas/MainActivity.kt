package com.tuuser.chandas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.annotation.DrawableRes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.launch
import com.tuuser.chandas.ui.theme.CHANDASTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CHANDASTheme {
                DogsApp()
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DogsApp() {
	val listState = rememberLazyListState()
	val scope = rememberCoroutineScope()
	val dogs = remember {
		mutableStateListOf(
			Dog("Luna", true, R.drawable.perro1, R.drawable.dog_golden_body),
			Dog("Max", false, R.drawable.perro2, R.drawable.dog_golden_body),
			Dog("Rocky", true, R.drawable.perro3, R.drawable.dog_puppy),
			Dog("Bella", false, R.drawable.perro4, R.drawable.dog_pastor),
			Dog("Toby", false, R.drawable.perro5, R.drawable.dog_pug),
			Dog("Charlie", true, R.drawable.perro6, R.drawable.dog_bulldog)
		)
	}
	var currentIndex by remember { mutableIntStateOf(0) }
	var showOnlyFavorites by remember { mutableStateOf(false) }
	var currentScreen by remember { mutableStateOf(Screen.List) }

	Scaffold(
		modifier = Modifier.fillMaxSize(),
		containerColor = MaterialTheme.colorScheme.surface,
		topBar = {
			when (currentScreen) {
				Screen.List -> {
					CenterAlignedTopAppBar(
						title = { Text(text = "Petagram") },
						navigationIcon = {
							Icon(
								imageVector = Icons.Filled.Pets,
								contentDescription = "Inicio"
							)
						},
						actions = {
							IconButton(onClick = { showOnlyFavorites = !showOnlyFavorites }) {
								Icon(
									imageVector = if (showOnlyFavorites) Icons.Filled.Star else Icons.Outlined.Star,
									contentDescription = if (showOnlyFavorites) "Mostrar todos" else "Ver favoritos"
								)
							}
							IconButton(onClick = { currentScreen = Screen.Manage }) {
								Icon(
									imageVector = Icons.Filled.Edit,
									contentDescription = "Gestionar favoritos"
								)
							}
						}
					)
				}
				Screen.Manage -> {
					CenterAlignedTopAppBar(
						title = { Text(text = "Gestionar favoritos") },
						navigationIcon = {
							IconButton(onClick = { currentScreen = Screen.List }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
									contentDescription = "Volver"
								)
							}
						}
					)
				}
			}
		},

	) { innerPadding ->
		when (currentScreen) {
			Screen.List -> {
				Column(
					modifier = Modifier
						.fillMaxSize()
						.padding(innerPadding)
						.padding(16.dp),
					verticalArrangement = Arrangement.spacedBy(16.dp)
				) {
					val visibleDogs = if (showOnlyFavorites) dogs.filter { it.isFavorite } else dogs
					if (currentIndex >= visibleDogs.size) {
						currentIndex = maxOf(0, visibleDogs.size - 1)
					}

                    LazyColumn(state = listState, modifier = Modifier.weight(1f)) {
                        items(visibleDogs) { dog ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                colors = androidx.compose.material3.CardDefaults.cardColors(
                                    containerColor = androidx.compose.ui.graphics.Color.Transparent
                                )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            color = when (dog.backgroundResId) {
                                                R.drawable.dog_golden_body -> androidx.compose.ui.graphics.Color(0xFFFFE0B2)
                                                R.drawable.dog_puppy -> androidx.compose.ui.graphics.Color(0xFFFFCC80)
                                                R.drawable.dog_pastor -> androidx.compose.ui.graphics.Color(0xFFE3F2FD)
                                                R.drawable.dog_pug -> androidx.compose.ui.graphics.Color(0xFFF3E5F5)
                                                R.drawable.dog_bulldog -> androidx.compose.ui.graphics.Color(0xFFFFCDD2)
                                                else -> androidx.compose.ui.graphics.Color.Transparent
                                            }
                                        )
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                    Image(
                                        painter = painterResource(id = dog.imageResId),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(1f)
                                            .clip(RoundedCornerShape(12.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = dog.name,
                                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                                        )
                                        Icon(
                                            imageVector = if (dog.isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                                            contentDescription = null,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    }

					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.SpaceBetween,
						verticalAlignment = Alignment.CenterVertically
					) {
						Button(
							onClick = {
								if (currentIndex > 0) {
									currentIndex -= 1
									scope.launch { listState.animateScrollToItem(currentIndex) }
								}
							},
							shape = CircleShape,
							modifier = Modifier.size(64.dp)
						) { Text("🐾") }

						Button(
							onClick = {
								if (currentIndex < maxOf(0, visibleDogs.size - 1)) {
									currentIndex += 1
									scope.launch { listState.animateScrollToItem(currentIndex) }
								}
							},
							shape = RoundedCornerShape(50),
							modifier = Modifier
								.fillMaxWidth()
								.height(56.dp)
						) { Text("🦴 Avanzar") }
					}
				}
			}
			Screen.Manage -> {
				Column(
					modifier = Modifier
						.fillMaxSize()
						.padding(innerPadding)
						.padding(16.dp),
					verticalArrangement = Arrangement.spacedBy(8.dp)
				) {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(dogs) { dog ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = dog.imageResId),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(1f)
                                            .clip(RoundedCornerShape(12.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = dog.name,
                                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                                        )
                                        IconButton(onClick = {
                                            val index = dogs.indexOfFirst { it.name == dog.name }
                                            if (index != -1) {
                                                dogs[index] = dogs[index].copy(isFavorite = !dogs[index].isFavorite)
                                            }
                                        }) {
                                            Icon(
                                                imageVector = if (dog.isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                                                contentDescription = if (dog.isFavorite) "Quitar de favoritos" else "Agregar a favoritos",
                                                modifier = Modifier.size(32.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
				}
			}
		}
	}
}

private data class Dog(
    val name: String,
    val isFavorite: Boolean,
    @param:DrawableRes val imageResId: Int,
    @param:DrawableRes val backgroundResId: Int
)

private enum class Screen { List, Manage }

@Preview(showBackground = true)
@Composable
fun DogsAppPreview() {
	CHANDASTheme {
		DogsApp()
	}
}