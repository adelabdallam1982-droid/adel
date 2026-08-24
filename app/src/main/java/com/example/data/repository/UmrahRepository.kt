package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class UmrahRepository {

  private val _trips = MutableStateFlow<List<UmrahTrip>>(createInitialTrips())
  val trips: StateFlow<List<UmrahTrip>> = _trips.asStateFlow()

  private val _bookings = MutableStateFlow<List<PilgrimBooking>>(createInitialBookings())
  val bookings: StateFlow<List<PilgrimBooking>> = _bookings.asStateFlow()

  private val _selectedTripId = MutableStateFlow<String>("riyadh-150926")
  val selectedTripId: StateFlow<String> = _selectedTripId.asStateFlow()

  fun selectTrip(tripId: String) {
    _selectedTripId.value = tripId
  }

  fun getSelectedTrip(): UmrahTrip? {
    return _trips.value.find { it.id == _selectedTripId.value } ?: _trips.value.firstOrNull()
  }

  fun addBooking(
    tripId: String,
    pilgrimName: String,
    phone: String,
    nationalId: String,
    gender: String,
    roomType: RoomType,
    pilgrimCount: Int,
    notes: String
  ): PilgrimBooking {
    val trip = _trips.value.find { it.id == tripId } ?: _trips.value.first()
    val unitPrice = trip.roomPricing.getPriceFor(roomType)
    val totalPrice = unitPrice * pilgrimCount
    val dateFormat = SimpleDateFormat("yyyy/MM/dd - hh:mm a", Locale("ar"))

    val newBooking = PilgrimBooking(
      id = "UMR-" + (1000..9999).random(),
      tripId = tripId,
      pilgrimName = pilgrimName,
      phone = phone,
      nationalId = nationalId,
      gender = gender,
      roomType = roomType,
      pilgrimCount = pilgrimCount,
      unitPrice = unitPrice,
      totalPrice = totalPrice,
      status = BookingStatus.PENDING_PAYMENT,
      registeredAt = dateFormat.format(Date()),
      notes = notes
    )

    _bookings.value = listOf(newBooking) + _bookings.value
    return newBooking
  }

  fun updateBookingStatus(bookingId: String, newStatus: BookingStatus) {
    _bookings.value = _bookings.value.map {
      if (it.id == bookingId) it.copy(status = newStatus) else it
    }
  }

  fun deleteBooking(bookingId: String) {
    _bookings.value = _bookings.value.filter { it.id != bookingId }
  }

  fun addTrip(trip: UmrahTrip) {
    _trips.value = listOf(trip) + _trips.value
    _selectedTripId.value = trip.id
  }

  fun updateTrip(updatedTrip: UmrahTrip) {
    _trips.value = _trips.value.map {
      if (it.id == updatedTrip.id) updatedTrip else it
    }
  }

  fun deleteTrip(tripId: String) {
    _trips.value = _trips.value.filter { it.id != tripId }
    _bookings.value = _bookings.value.filter { it.tripId != tripId }
    if (_selectedTripId.value == tripId) {
      _selectedTripId.value = _trips.value.firstOrNull()?.id ?: ""
    }
  }

  private fun createInitialTrips(): List<UmrahTrip> {
    val mainTrip = UmrahTrip(
      id = "riyadh-150926",
      title = "رحلة عمرة – 15 سبتمبر 2026",
      shareSlug = "riyadh-150926",
      departureDateAr = "15 سبتمبر 2026 (الخميس)",
      returnDateAr = "18 سبتمبر 2026 (الأحد)",
      durationAr = "4 أيام / 3 ليالٍ في مكة المكرمة",
      totalSeats = 48,
      gatheringLocation = GatheringLocation(
        city = "الرياض",
        pointName = "مخرج 15 - ساحة جامع الراجحي",
        addressDetails = "المواقف الغربية المقابلة لبوابة رقم 4 (طريق الدائري الشرقي)",
        timeAr = "الخميس 01:30 ظهراً",
        departureTimeAr = "الانطلاق 02:30 ظهراً تماماً",
        latitude = 24.6914,
        longitude = 46.7865,
        mapQuery = "Al Rajhi Grand Mosque Riyadh"
      ),
      busDetails = BusDetails(
        modelName = "حافلة مرسيدس VIP موديل 2026",
        busNumber = "VIP-770",
        features = listOf(
          "مقاعد جلدية مريحة وفخمة قابلة للإمالة بدرجة 140°",
          "تكييف مركزي متطور وموزع بكفاءة مع إضاءة ليد هادئة",
          "واي فاي سريع ومنافذ شحن USB و Type-C لكل راكب",
          "ثلاجة مياه زمزم وعصائر وضيافة وتمر وسناك مجاني",
          "مكبرات صوت وشاشات إرشاد وتوجيه ومطوف مرافق"
        )
      ),
      hotelDetails = HotelDetails(
        name = "فندق موڤنبيك أبراج البيت (5 نجوم)",
        stars = 5,
        distanceFromHaram = "مطل مباشرة على ساحة الحرم وباب الملك فهد",
        locationDescription = "مجمع أبراج البيت، وقف الملك عبدالعزيز - مكة المكرمة",
        features = listOf(
          "بوفيه إفطار فاخر يومياً مشمول في جميع الغرف",
          "مصاعد مباشرة إلى مصليات الحرم في دقائق معدودة",
          "خدمة استقبال وغرف وحمل حقائب على مدار 24 ساعة",
          "إطلالات مباشرة ومميزة على الكعبة المشرفة",
          "واي فاي فائق السرعة وخدمة غسيل وكوي الملابس"
        )
      ),
      roomPricing = RoomPricing(
        singlePrice = 1850,
        doublePrice = 1350,
        triplePrice = 1100,
        quadPrice = 950
      ),
      itinerary = listOf(
        ItineraryStep(
          stepNumber = 1,
          timeAr = "الخميس 01:30 م",
          titleAr = "التجمع والانطلاق من الرياض",
          descriptionAr = "التجمع في مواقف جامع الراجحي بالرياض مخرج 15، واستلام بطاقات الأمتعة والمقاعد، ثم الانطلاق بتكبيرات التلبية مع ضيافة فاخرة.",
          iconTag = "bus"
        ),
        ItineraryStep(
          stepNumber = 2,
          timeAr = "الخميس 08:30 م",
          titleAr = "الوصول إلى الميقات (قرن المنازل - السيل الكبير)",
          descriptionAr = "التوقف بالميقات للاغتسال والتطيب ولبس الإحرام وصلاة ركعتي الإحرام وعقد نية العمرة بمرافقة المرشد الديني.",
          iconTag = "dua"
        ),
        ItineraryStep(
          stepNumber = 3,
          timeAr = "الجمعة 12:30 ص",
          titleAr = "الوصول إلى مكة وتسكين الفندق",
          descriptionAr = "الوصول إلى فندق موڤنبيك أبراج البيت، استلام بطاقات الغرف وتوزيع الحقائب، وأخذ قسط من الراحة.",
          iconTag = "hotel"
        ),
        ItineraryStep(
          stepNumber = 4,
          timeAr = "الجمعة 03:30 ص",
          titleAr = "أداء مناسك العمرة جماعة مع المطوف",
          descriptionAr = "الانطلاق الجماعي للحرم المكي لأداء الطواف والسعي والتحلل بصحبة المشرف والمطوف مع أجهزة صوتية للأدعية.",
          iconTag = "kaaba"
        ),
        ItineraryStep(
          stepNumber = 5,
          timeAr = "السبت 08:00 ص",
          titleAr = "جولة المزارات والمعالم التاريخية (اختياري)",
          descriptionAr = "زيارة جبل ثور، جبل النور (غار حراء)، عرفات، ومزدلفة ومنى برفقة باص الحملة مع شرح تاريخي متكامل.",
          iconTag = "bus"
        ),
        ItineraryStep(
          stepNumber = 6,
          timeAr = "الأحد 01:00 م",
          titleAr = "طواف الوداع والعودة إلى الرياض",
          descriptionAr = "أداء طواف الوداع بعد صلاة الظهر، تسجيل الخروج من الفندق، والانطلاق سالمين إلى مدينة الرياض.",
          iconTag = "bus"
        )
      ),
      supervisor = SupervisorContact(
        name = "الشيخ فهد العتيبي",
        role = "المشرف العام ومطوف الحملة",
        phone = "+966501234567",
        whatsapp = "966501234567"
      ),
      notesAr = "تشمل الرحلة: النقل بحافلة VIP، السكن 5 نجوم، الإفطار، المزارات، مياه زمزم وضيافة مستمرة طوال الرحلة."
    )

    val weekendTrip = UmrahTrip(
      id = "riyadh-220926",
      title = "رحلة عمرة نهاية سبتمبر VIP – الرياض",
      shareSlug = "riyadh-220926",
      departureDateAr = "22 سبتمبر 2026 (الخميس)",
      returnDateAr = "25 سبتمبر 2026 (الأحد)",
      durationAr = "4 أيام / 3 ليالٍ",
      totalSeats = 45,
      gatheringLocation = GatheringLocation(
        city = "الرياض",
        pointName = "محطة قطار سار - مخرج 9",
        addressDetails = "مواقف الباصات السياحية - البوابة الجنوبية",
        timeAr = "الخميس 02:00 ظهراً",
        departureTimeAr = "الانطلاق 03:00 عصراً",
        latitude = 24.7688,
        longitude = 46.7275,
        mapQuery = "SAR Train Station Riyadh"
      ),
      busDetails = BusDetails(
        modelName = "حافلة مرسيدس سوبر VIP 2026",
        busNumber = "VIP-900"
      ),
      hotelDetails = HotelDetails(
        name = "فندق أنجم مكة المكرمة (5 نجوم)",
        stars = 5,
        distanceFromHaram = "يبعد 120 متر عن ساحة الحرم الشمالية"
      ),
      roomPricing = RoomPricing(
        singlePrice = 1750,
        doublePrice = 1250,
        triplePrice = 1050,
        quadPrice = 890
      ),
      itinerary = mainTrip.itinerary,
      supervisor = SupervisorContact(
        name = "أبو عبدالله الغامدي",
        role = "مسؤول التسجيل والرحلات",
        phone = "+966555987654",
        whatsapp = "966555987654"
      )
    )

    return listOf(mainTrip, weekendTrip)
  }

  private fun createInitialBookings(): List<PilgrimBooking> {
    return listOf(
      PilgrimBooking(
        id = "UMR-1082",
        tripId = "riyadh-150926",
        pilgrimName = "عبدالرحمن بن خالد السبيعي",
        phone = "0551122334",
        nationalId = "1098765432",
        gender = "ذكر",
        roomType = RoomType.DOUBLE,
        pilgrimCount = 2,
        unitPrice = 1350,
        totalPrice = 2700,
        status = BookingStatus.CONFIRMED,
        registeredAt = "2026/08/20 - 10:15 ص",
        notes = "برفقة الوالد، يرجى تخصيص مقاعد في مقدمة الحافلة"
      ),
      PilgrimBooking(
        id = "UMR-1095",
        tripId = "riyadh-150926",
        pilgrimName = "سارة بنت محمد الدوسري",
        phone = "0504455667",
        nationalId = "1087654321",
        gender = "أنثى",
        roomType = RoomType.QUAD,
        pilgrimCount = 4,
        unitPrice = 950,
        totalPrice = 3800,
        status = BookingStatus.CONFIRMED,
        registeredAt = "2026/08/21 - 04:30 م",
        notes = "عائلة 4 أفراد (أم وبناتها)"
      ),
      PilgrimBooking(
        id = "UMR-1104",
        tripId = "riyadh-150926",
        pilgrimName = "محمد بن عبدالله القحطاني",
        phone = "0569988776",
        nationalId = "1076543210",
        gender = "ذكر",
        roomType = RoomType.SINGLE,
        pilgrimCount = 1,
        unitPrice = 1850,
        totalPrice = 1850,
        status = BookingStatus.PENDING_PAYMENT,
        registeredAt = "2026/08/22 - 08:20 م",
        notes = "سيتم التحويل البنكي خلال 24 ساعة"
      ),
      PilgrimBooking(
        id = "UMR-1118",
        tripId = "riyadh-150926",
        pilgrimName = "فيصل بن أحمد الزهراني",
        phone = "0543322110",
        nationalId = "1065432109",
        gender = "ذكر",
        roomType = RoomType.TRIPLE,
        pilgrimCount = 3,
        unitPrice = 1100,
        totalPrice = 3300,
        status = BookingStatus.CONFIRMED,
        registeredAt = "2026/08/23 - 09:00 ص",
        notes = "3 شباب أصدقاء"
      ),
      PilgrimBooking(
        id = "UMR-1125",
        tripId = "riyadh-150926",
        pilgrimName = "إبراهيم بن صالح المنصور",
        phone = "0531234889",
        nationalId = "1054321098",
        gender = "ذكر",
        roomType = RoomType.DOUBLE,
        pilgrimCount = 2,
        unitPrice = 1350,
        totalPrice = 2700,
        status = BookingStatus.PENDING_PAYMENT,
        registeredAt = "2026/08/23 - 11:45 ص",
        notes = "طلب غرفة مطلة على الحرم"
      )
    )
  }
}
