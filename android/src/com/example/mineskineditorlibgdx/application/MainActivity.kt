package com.example.mineskineditorlibgdx.application

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.example.mineskineditorlibgdx.R
import com.example.mineskineditorlibgdx.databinding.ActivityMainBinding
import com.example.mineskineditorlibgdx.utils.NavigationDispatcher
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyEdgeToEdge()
        lifecycleScope.apply {
            launch {
                withContext(Dispatchers.IO) {
                    val startDestination = provideStartDestination()
                    withContext(Dispatchers.Main) {
                        initNavigation(startDestination)
                    }
                    launchWhenResumed { observeNavigationCommands() }
                }
            }
        }
    }

    private fun provideStartDestination(): Int = R.id.fragmentSkinEditor

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