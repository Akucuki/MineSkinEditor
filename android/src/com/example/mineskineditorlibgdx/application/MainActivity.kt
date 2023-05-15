package com.example.mineskineditorlibgdx.application

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.dropbox.core.v2.DbxClientV2
import com.example.mineskineditorlibgdx.R
import com.example.mineskineditorlibgdx.application.theme.GrayColor
import com.example.mineskineditorlibgdx.databinding.ActivityMainBinding
import com.example.mineskineditorlibgdx.utils.NavigationDispatcher
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileOutputStream
import javax.inject.Inject

typealias onDestinationChanged = NavController.OnDestinationChangedListener

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), AndroidFragmentApplication.Callbacks {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val destinationChangedListener = onDestinationChanged { _, destination, arguments ->
        destination.toString()
        arguments.toString()
        // TODO place some stuff here
    }

    @Inject
    lateinit var navigationDispatcher: NavigationDispatcher

    @Inject
    lateinit var dbxClient: DbxClientV2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        with(binding.bottomNavigationView) {
            setBackgroundColor(GrayColor.toArgb())
            itemIconTintList = null
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_create -> {
                        navigationDispatcher.emit {
                            it.navigate(
                                resId = R.id.fragmentSkinEditor,
                                navOptions = NavOptions.Builder()
                                    .setPopUpTo(R.id.main_graph, true)
                                    .build(),
                                args = null
                            )
                        }
                        true
                    }
                    R.id.nav_content -> {
                        navigationDispatcher.emit {
                            it.navigate(
                                R.id.fragmentContent,
                                navOptions = NavOptions.Builder()
                                    .setPopUpTo(R.id.main_graph, true)
                                    .build(),
                                args = null
                            )
                        }
                        true
                    }
                    else -> false
                }
            }

        }
        setContentView(binding.root)
        applyEdgeToEdge()
        lifecycleScope.apply {
            launch {
                withContext(Dispatchers.IO) {
                    // TODO remove
                    try {
                        val result =
                            dbxClient.files().listFolderBuilder("/maps, addons, skins")
                                .withLimit(100).start()
                        result.entries.forEach {
                            println(it.name)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    val startDestination = provideStartDestination()
                    withContext(Dispatchers.Main) {
                        initNavigation(startDestination)
                    }
                    launchWhenResumed { observeNavigationCommands() }
                }
            }
        }
    }

    private fun provideStartDestination(): Int = R.id.fragmentHome

    private fun initNavigation(startDestination: Int) {
        (supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment).also { navHost ->
            val navInflater = navHost.navController.navInflater
            val navGraph = navInflater.inflate(R.navigation.main_graph)
            navGraph.setStartDestination(startDestination)
            navHost.navController.graph = navGraph
            navController = navHost.navController
            navController.addOnDestinationChangedListener(destinationChangedListener)
        }
    }

    private suspend fun observeNavigationCommands() {
        for (command in navigationDispatcher.navigationEmitter) command.invoke(navController)
    }

    private fun applyEdgeToEdge() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    override fun exit() {}
}