package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AppHeader
import com.example.ui.components.FullscreenPhotoDialog
import com.example.ui.organizer.BroadcastDialog
import com.example.ui.organizer.CreateTripDialog
import com.example.ui.organizer.OrganizerDashboardScreen
import com.example.ui.pilgrim.JoinTripDialog
import com.example.ui.pilgrim.PilgrimTripScreen
import com.example.ui.pilgrim.ShareTripDialog
import com.example.ui.poster.UmrahPosterStudioDialog
import com.example.ui.theme.LuxuryTheme

@Composable
fun UmrahApp(viewModel: UmrahViewModel = viewModel()) {
  // Force Right-to-Left (RTL) Layout Direction for genuine Arabic experience
  CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
    val colors = LuxuryTheme.colors
    val trips by viewModel.trips.collectAsStateWithLifecycle()
    val selectedTrip by viewModel.selectedTrip.collectAsStateWithLifecycle()
    val activeTab by viewModel.activeTab.collectAsStateWithLifecycle()
    val stats by viewModel.stats.collectAsStateWithLifecycle()
    val currentThemePreset by viewModel.currentThemePreset.collectAsStateWithLifecycle()

    val selectedRoomType by viewModel.selectedRoomType.collectAsStateWithLifecycle()
    val selectedPilgrimCount by viewModel.selectedPilgrimCount.collectAsStateWithLifecycle()

    val filteredBookings by viewModel.filteredBookings.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val statusFilter by viewModel.statusFilter.collectAsStateWithLifecycle()
    val roomFilter by viewModel.roomFilter.collectAsStateWithLifecycle()

    val showJoinDialog by viewModel.showJoinDialog.collectAsStateWithLifecycle()
    val showShareQrDialog by viewModel.showShareQrDialog.collectAsStateWithLifecycle()
    val showCreateTripDialog by viewModel.showCreateTripDialog.collectAsStateWithLifecycle()
    val showBroadcastDialog by viewModel.showBroadcastDialog.collectAsStateWithLifecycle()
    val showPosterStudioDialog by viewModel.showPosterStudioDialog.collectAsStateWithLifecycle()
    val photoViewerState by viewModel.photoViewerState.collectAsStateWithLifecycle()
    val lastCreatedBooking by viewModel.lastCreatedBooking.collectAsStateWithLifecycle()
    val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(userMessage) {
      userMessage?.let {
        snackbarHostState.showSnackbar(it)
        viewModel.clearUserMessage()
      }
    }

    Scaffold(
      modifier = Modifier
        .fillMaxSize()
        .background(colors.background),
      snackbarHost = { SnackbarHost(snackbarHostState) },
      topBar = {
        AppHeader(
          activeTab = activeTab,
          onTabSelected = { viewModel.setActiveTab(it) },
          trips = trips,
          selectedTrip = selectedTrip,
          onTripSelected = { viewModel.selectTrip(it) },
          onShareClick = { viewModel.openShareQrDialog() },
          onPosterStudioClick = { viewModel.openPosterStudioDialog() }
        )
      }
    ) { innerPadding ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
          .background(colors.background)
      ) {
        selectedTrip?.let { currentTrip ->
          when (activeTab) {
            AppTab.PILGRIM_VIEW -> {
              PilgrimTripScreen(
                trip = currentTrip,
                stats = stats,
                selectedRoomType = selectedRoomType,
                selectedPilgrimCount = selectedPilgrimCount,
                activeThemePreset = currentThemePreset,
                onThemePresetChanged = { viewModel.setThemePreset(it) },
                onRoomTypeSelected = { viewModel.selectRoomType(it) },
                onPilgrimCountChanged = { viewModel.setPilgrimCount(it) },
                onJoinClicked = { viewModel.openJoinDialog() },
                onShareQrClicked = { viewModel.openShareQrDialog() },
                onOpenPosterStudio = { viewModel.openPosterStudioDialog() },
                onOpenPhotoViewer = { title, sub, res ->
                  viewModel.openPhotoViewer(title, sub, res)
                }
              )
            }

            AppTab.ORGANIZER_DASHBOARD -> {
              OrganizerDashboardScreen(
                trip = currentTrip,
                stats = stats,
                bookings = filteredBookings,
                searchQuery = searchQuery,
                statusFilter = statusFilter,
                roomFilter = roomFilter,
                onSearchQueryChanged = { viewModel.setSearchQuery(it) },
                onStatusFilterChanged = { viewModel.setStatusFilter(it) },
                onRoomFilterChanged = { viewModel.setRoomFilter(it) },
                onUpdateBookingStatus = { id, status -> viewModel.updateBookingStatus(id, status) },
                onDeleteBooking = { id -> viewModel.deleteBooking(id) },
                onCreateNewTripClick = { viewModel.openCreateTripDialog() },
                onBroadcastClick = { viewModel.openBroadcastDialog() },
                onDeleteTripClick = { viewModel.deleteTrip(currentTrip.id) }
              )
            }
          }

          // Dialogs
          if (showJoinDialog) {
            JoinTripDialog(
              trip = currentTrip,
              selectedRoomType = selectedRoomType,
              selectedPilgrimCount = selectedPilgrimCount,
              lastCreatedBooking = lastCreatedBooking,
              onDismiss = { viewModel.closeJoinDialog() },
              onSubmit = { name, phone, natId, gender, notes ->
                viewModel.submitBooking(name, phone, natId, gender, notes)
              }
            )
          }

          if (showShareQrDialog) {
            ShareTripDialog(
              trip = currentTrip,
              onDismiss = { viewModel.closeShareQrDialog() }
            )
          }

          if (showPosterStudioDialog) {
            UmrahPosterStudioDialog(
              trip = currentTrip,
              activeThemePreset = currentThemePreset,
              onThemePresetChanged = { viewModel.setThemePreset(it) },
              onDismiss = { viewModel.closePosterStudioDialog() }
            )
          }

          if (showCreateTripDialog) {
            CreateTripDialog(
              onDismiss = { viewModel.closeCreateTripDialog() },
              onCreate = { title, slug, dep, ret, pt, addr, time, hotel, dist, bus, seats, sP, dP, tP, qP, sName, sPhone ->
                viewModel.createTrip(title, slug, dep, ret, pt, addr, time, hotel, dist, bus, seats, sP, dP, tP, qP, sName, sPhone)
              }
            )
          }

          if (showBroadcastDialog) {
            BroadcastDialog(
              trip = currentTrip,
              registeredCount = stats.registeredPilgrims,
              onDismiss = { viewModel.closeBroadcastDialog() }
            )
          }

          FullscreenPhotoDialog(
            state = photoViewerState,
            onDismiss = { viewModel.closePhotoViewer() }
          )
        }
      }
    }
  }
}
