package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.UmrahRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppTab(val titleAr: String, val subtitleAr: String) {
  PILGRIM_VIEW("صفحة الرحلة (المعتمر)", "رابط الرحلة العام"),
  ORGANIZER_DASHBOARD("لوحة تحكم المنظم", "إدارة الحجوزات والمعتمرين")
}

data class TripStats(
  val totalSeats: Int,
  val registeredPilgrims: Int,
  val availableSeats: Int,
  val confirmedCount: Int,
  val pendingCount: Int,
  val cancelledCount: Int,
  val singleRoomsCount: Int,
  val doubleRoomsCount: Int,
  val tripleRoomsCount: Int,
  val quadRoomsCount: Int,
  val totalRevenueSar: Int,
  val confirmedRevenueSar: Int
)

data class PhotoViewerState(
  val isOpen: Boolean = false,
  val title: String = "",
  val subtitle: String = "",
  val imageRes: Int = 0
)

class UmrahViewModel(
  private val repository: UmrahRepository = UmrahRepository()
) : ViewModel() {

  val trips: StateFlow<List<UmrahTrip>> = repository.trips
  val selectedTripId: StateFlow<String> = repository.selectedTripId

  private val _activeTab = MutableStateFlow(AppTab.PILGRIM_VIEW)
  val activeTab: StateFlow<AppTab> = _activeTab.asStateFlow()

  private val _selectedRoomType = MutableStateFlow(RoomType.DOUBLE)
  val selectedRoomType: StateFlow<RoomType> = _selectedRoomType.asStateFlow()

  private val _selectedPilgrimCount = MutableStateFlow(2)
  val selectedPilgrimCount: StateFlow<Int> = _selectedPilgrimCount.asStateFlow()

  // Luxury Theme Palette
  private val _currentThemePreset = MutableStateFlow(com.example.ui.theme.LuxuryThemePreset.ROYAL_MIDNIGHT_GOLD)
  val currentThemePreset: StateFlow<com.example.ui.theme.LuxuryThemePreset> = _currentThemePreset.asStateFlow()

  // Poster Studio State
  private val _showPosterStudioDialog = MutableStateFlow(false)
  val showPosterStudioDialog: StateFlow<Boolean> = _showPosterStudioDialog.asStateFlow()

  // Dialog & Modal states
  private val _showJoinDialog = MutableStateFlow(false)
  val showJoinDialog: StateFlow<Boolean> = _showJoinDialog.asStateFlow()

  private val _showShareQrDialog = MutableStateFlow(false)
  val showShareQrDialog: StateFlow<Boolean> = _showShareQrDialog.asStateFlow()

  private val _showCreateTripDialog = MutableStateFlow(false)
  val showCreateTripDialog: StateFlow<Boolean> = _showCreateTripDialog.asStateFlow()

  private val _showBroadcastDialog = MutableStateFlow(false)
  val showBroadcastDialog: StateFlow<Boolean> = _showBroadcastDialog.asStateFlow()

  private val _photoViewerState = MutableStateFlow(PhotoViewerState())
  val photoViewerState: StateFlow<PhotoViewerState> = _photoViewerState.asStateFlow()

  private val _lastCreatedBooking = MutableStateFlow<PilgrimBooking?>(null)
  val lastCreatedBooking: StateFlow<PilgrimBooking?> = _lastCreatedBooking.asStateFlow()

  private val _userMessage = MutableStateFlow<String?>(null)
  val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

  // Dashboard Filters
  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _statusFilter = MutableStateFlow<BookingStatus?>(null)
  val statusFilter: StateFlow<BookingStatus?> = _statusFilter.asStateFlow()

  private val _roomFilter = MutableStateFlow<RoomType?>(null)
  val roomFilter: StateFlow<RoomType?> = _roomFilter.asStateFlow()

  val selectedTrip: StateFlow<UmrahTrip?> = combine(trips, selectedTripId) { tripList, id ->
    tripList.find { it.id == id } ?: tripList.firstOrNull()
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  val tripBookings: StateFlow<List<PilgrimBooking>> = combine(
    repository.bookings,
    selectedTripId
  ) { allBookings, tripId ->
    allBookings.filter { it.tripId == tripId }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val filteredBookings: StateFlow<List<PilgrimBooking>> = combine(
    tripBookings,
    _searchQuery,
    _statusFilter,
    _roomFilter
  ) { list, query, status, room ->
    list.filter { booking ->
      val matchesQuery = query.isBlank() ||
        booking.pilgrimName.contains(query, ignoreCase = true) ||
        booking.phone.contains(query) ||
        booking.nationalId.contains(query) ||
        booking.id.contains(query, ignoreCase = true)

      val matchesStatus = status == null || booking.status == status
      val matchesRoom = room == null || booking.roomType == room

      matchesQuery && matchesStatus && matchesRoom
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val stats: StateFlow<TripStats> = combine(
    selectedTrip,
    tripBookings
  ) { currentTrip, bookingsList ->
    val totalSeats = currentTrip?.totalSeats ?: 48
    val activeBookings = bookingsList.filter { it.status != BookingStatus.CANCELLED }
    val registered = activeBookings.sumOf { it.pilgrimCount }
    val available = (totalSeats - registered).coerceAtLeast(0)

    val confirmed = bookingsList.count { it.status == BookingStatus.CONFIRMED }
    val pending = bookingsList.count { it.status == BookingStatus.PENDING_PAYMENT }
    val cancelled = bookingsList.count { it.status == BookingStatus.CANCELLED }

    val singleCount = activeBookings.count { it.roomType == RoomType.SINGLE }
    val doubleCount = activeBookings.count { it.roomType == RoomType.DOUBLE }
    val tripleCount = activeBookings.count { it.roomType == RoomType.TRIPLE }
    val quadCount = activeBookings.count { it.roomType == RoomType.QUAD }

    val totalRev = activeBookings.sumOf { it.totalPrice }
    val confirmedRev = bookingsList.filter { it.status == BookingStatus.CONFIRMED }.sumOf { it.totalPrice }

    TripStats(
      totalSeats = totalSeats,
      registeredPilgrims = registered,
      availableSeats = available,
      confirmedCount = confirmed,
      pendingCount = pending,
      cancelledCount = cancelled,
      singleRoomsCount = singleCount,
      doubleRoomsCount = doubleCount,
      tripleRoomsCount = tripleCount,
      quadRoomsCount = quadCount,
      totalRevenueSar = totalRev,
      confirmedRevenueSar = confirmedRev
    )
  }.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    TripStats(48, 12, 36, 3, 2, 0, 1, 2, 1, 1, 14350, 9800)
  )

  fun setActiveTab(tab: AppTab) {
    _activeTab.value = tab
  }

  fun selectTrip(tripId: String) {
    repository.selectTrip(tripId)
  }

  fun selectRoomType(roomType: RoomType) {
    _selectedRoomType.value = roomType
    _selectedPilgrimCount.value = roomType.capacity
  }

  fun setPilgrimCount(count: Int) {
    _selectedPilgrimCount.value = count.coerceIn(1, 10)
  }

  fun setLuxuryTheme(preset: com.example.ui.theme.LuxuryThemePreset) {
    _currentThemePreset.value = preset
  }

  fun setThemePreset(preset: com.example.ui.theme.LuxuryThemePreset) {
    _currentThemePreset.value = preset
  }

  fun openPosterStudio() {
    _showPosterStudioDialog.value = true
  }

  fun openPosterStudioDialog() {
    _showPosterStudioDialog.value = true
  }

  fun closePosterStudio() {
    _showPosterStudioDialog.value = false
  }

  fun closePosterStudioDialog() {
    _showPosterStudioDialog.value = false
  }

  fun openJoinDialog() {
    _lastCreatedBooking.value = null
    _showJoinDialog.value = true
  }

  fun closeJoinDialog() {
    _showJoinDialog.value = false
  }

  fun openShareQrDialog() {
    _showShareQrDialog.value = true
  }

  fun closeShareQrDialog() {
    _showShareQrDialog.value = false
  }

  fun openCreateTripDialog() {
    _showCreateTripDialog.value = true
  }

  fun closeCreateTripDialog() {
    _showCreateTripDialog.value = false
  }

  fun openBroadcastDialog() {
    _showBroadcastDialog.value = true
  }

  fun closeBroadcastDialog() {
    _showBroadcastDialog.value = false
  }

  fun openPhotoViewer(title: String, subtitle: String, imageRes: Int) {
    _photoViewerState.value = PhotoViewerState(
      isOpen = true,
      title = title,
      subtitle = subtitle,
      imageRes = imageRes
    )
  }

  fun closePhotoViewer() {
    _photoViewerState.value = PhotoViewerState(isOpen = false)
  }

  fun submitBooking(
    name: String,
    phone: String,
    nationalId: String,
    gender: String,
    notes: String
  ): Boolean {
    val trip = selectedTrip.value ?: return false
    val newBooking = repository.addBooking(
      tripId = trip.id,
      pilgrimName = name,
      phone = phone,
      nationalId = nationalId,
      gender = gender,
      roomType = _selectedRoomType.value,
      pilgrimCount = _selectedPilgrimCount.value,
      notes = notes
    )
    _lastCreatedBooking.value = newBooking
    _userMessage.value = "تم تسجيل طلبك بنجاح برقم الحجز: ${newBooking.id}"
    return true
  }

  fun updateBookingStatus(bookingId: String, newStatus: BookingStatus) {
    repository.updateBookingStatus(bookingId, newStatus)
    _userMessage.value = "تم تحديث حالة الحجز إلى: ${newStatus.titleAr}"
  }

  fun deleteBooking(bookingId: String) {
    repository.deleteBooking(bookingId)
    _userMessage.value = "تم حذف الحجز بنجاح"
  }

  fun createTrip(
    title: String,
    slug: String,
    departureDate: String,
    returnDate: String,
    gatheringPoint: String,
    gatheringAddress: String,
    gatheringTime: String,
    hotelName: String,
    hotelDistance: String,
    busModel: String,
    totalSeats: Int,
    singlePrice: Int,
    doublePrice: Int,
    triplePrice: Int,
    quadPrice: Int,
    supervisorName: String,
    supervisorPhone: String
  ) {
    val newTrip = UmrahTrip(
      id = slug.ifBlank { "trip-${System.currentTimeMillis() % 10000}" },
      title = title,
      shareSlug = slug.ifBlank { "riyadh-${System.currentTimeMillis() % 10000}" },
      departureDateAr = departureDate,
      returnDateAr = returnDate,
      durationAr = "3 ليالٍ في مكة المكرمة",
      totalSeats = totalSeats.coerceAtLeast(10),
      gatheringLocation = GatheringLocation(
        city = "الرياض",
        pointName = gatheringPoint,
        addressDetails = gatheringAddress,
        timeAr = gatheringTime
      ),
      busDetails = BusDetails(
        modelName = busModel
      ),
      hotelDetails = HotelDetails(
        name = hotelName,
        distanceFromHaram = hotelDistance
      ),
      roomPricing = RoomPricing(
        singlePrice = singlePrice,
        doublePrice = doublePrice,
        triplePrice = triplePrice,
        quadPrice = quadPrice
      ),
      itinerary = repository.trips.value.firstOrNull()?.itinerary ?: emptyList(),
      supervisor = SupervisorContact(
        name = supervisorName,
        phone = supervisorPhone,
        whatsapp = supervisorPhone.replace("+", "").replace(" ", "")
      )
    )

    repository.addTrip(newTrip)
    _showCreateTripDialog.value = false
    _userMessage.value = "تم إنشاء رحلة جديدة بنجاح!"
  }

  fun deleteTrip(tripId: String) {
    repository.deleteTrip(tripId)
    _userMessage.value = "تم حذف الرحلة"
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun setStatusFilter(status: BookingStatus?) {
    _statusFilter.value = status
  }

  fun setRoomFilter(room: RoomType?) {
    _roomFilter.value = room
  }

  fun clearUserMessage() {
    _userMessage.value = null
  }
}
