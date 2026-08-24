package com.example.data.model

import androidx.annotation.DrawableRes
import com.example.R

enum class RoomType(
  val id: String,
  val titleAr: String,
  val subtitleAr: String,
  val capacity: Int,
  val descriptionAr: String,
  val badgeAr: String
) {
  SINGLE(
    id = "single",
    titleAr = "غرفة فردية",
    subtitleAr = "خصوصية تامة لمعتمر واحد",
    capacity = 1,
    descriptionAr = "سرير كينج فاخر، حمام خاص، إطلالة هادئة، خصوصية وراحة متكاملة",
    badgeAr = "VIP لشخص واحد"
  ),
  DOUBLE(
    id = "double",
    titleAr = "غرفة ثنائية",
    subtitleAr = "مثالية للأزواج أو شخصين",
    capacity = 2,
    descriptionAr = "سريران منفصلان أو سرير مزدوج، حمام خاص، ومساحة واسعة للحقائب",
    badgeAr = "الأكثر طلباً"
  ),
  TRIPLE(
    id = "triple",
    titleAr = "غرفة ثلاثية",
    subtitleAr = "خيار اقتصادي لـ 3 أفراد",
    capacity = 3,
    descriptionAr = "3 أسرّة مريحة مع إطلالة جميلة ومرفقات فندقية متكاملة",
    badgeAr = "اقتصادي للعائلات"
  ),
  QUAD(
    id = "quad",
    titleAr = "غرفة رباعية",
    subtitleAr = "أفضل سعر للمجموعات والعائلات",
    capacity = 4,
    descriptionAr = "4 أسرّة مفردة واسعة، تكلفة موفرة ومثالية للمجموعات الكبيرة",
    badgeAr = "أوفر سعر للمعتمر"
  )
}

data class RoomPricing(
  val singlePrice: Int = 1850,
  val doublePrice: Int = 1350,
  val triplePrice: Int = 1100,
  val quadPrice: Int = 950
) {
  fun getPriceFor(type: RoomType): Int = when (type) {
    RoomType.SINGLE -> singlePrice
    RoomType.DOUBLE -> doublePrice
    RoomType.TRIPLE -> triplePrice
    RoomType.QUAD -> quadPrice
  }
}

data class BusDetails(
  val modelName: String = "حافلة مرسيدس VIP موديل 2026",
  val busNumber: String = "VIP-502",
  val features: List<String> = listOf(
    "مقاعد جلدية مريحة وفخمة قابلة للإمالة",
    "تكييف مركزي متطور وموزع بكفاءة",
    "واي فاي وشواحن USB لكل مقعد",
    "ثلاجة مياه وعصائر وضيافة مستمرة",
    "شاشات عرض وإرشاد ومكبرات صوت واضحة"
  ),
  @DrawableRes val exteriorDrawable: Int = R.drawable.bus_exterior_1787484681624,
  @DrawableRes val interiorDrawable: Int = R.drawable.bus_interior_1787484695705
)

data class HotelDetails(
  val name: String = "فندق موڤنبيك أبراج البيت (5 نجوم)",
  val stars: Int = 5,
  val distanceFromHaram: String = "مطل مباشرة على ساحة الحرم المكي",
  val locationDescription: String = "مجمع أبراج البيت - وقف الملك عبدالعزيز، مكة المكرمة",
  val features: List<String> = listOf(
    "إفطار بوفيه مفتوح عالمي مشمول",
    "مصاعد مباشرة إلى مصليات الحرم",
    "خدمة استقبال وغرف على مدار 24 ساعة",
    "إطلالات مباشرة على الكعبة المشرفة",
    "خدمة حمل الحقائب والتنظيف اليومي"
  ),
  @DrawableRes val hotelDrawable: Int = R.drawable.hotel_makkah_1787484711606,
  @DrawableRes val roomDrawable: Int = R.drawable.hotel_room_1787484726861
)

data class ItineraryStep(
  val stepNumber: Int,
  val timeAr: String,
  val titleAr: String,
  val descriptionAr: String,
  val iconTag: String = "bus" // "bus", "kaaba", "hotel", "food", "dua"
)

data class GatheringLocation(
  val city: String = "الرياض",
  val pointName: String = "مخرج 15 - ساحة جامع الراجحي",
  val addressDetails: String = "المواقف الشرقية للجامع، أمام بوابة رقم 4 الرئيسية",
  val timeAr: String = "الخميس 01:30 ظهراً",
  val departureTimeAr: String = "الانطلاق 02:30 ظهراً تماماً",
  val latitude: Double = 24.6914,
  val longitude: Double = 46.7865,
  val mapQuery: String = "Al Rajhi Grand Mosque Riyadh"
)

data class SupervisorContact(
  val name: String = "الشيخ فهد العتيبي",
  val role: String = "المشرف العام ومطوف الحملة",
  val phone: String = "+966501234567",
  val whatsapp: String = "966501234567"
)

enum class BookingStatus(val titleAr: String) {
  CONFIRMED("مؤكد"),
  PENDING_PAYMENT("بانتظار الدفع"),
  CANCELLED("ملغي")
}

data class PilgrimBooking(
  val id: String,
  val tripId: String,
  val pilgrimName: String,
  val phone: String,
  val nationalId: String,
  val gender: String, // "ذكر" or "أنثى"
  val roomType: RoomType,
  val pilgrimCount: Int,
  val unitPrice: Int,
  val totalPrice: Int,
  val status: BookingStatus,
  val registeredAt: String,
  val notes: String = ""
)

data class UmrahTrip(
  val id: String,
  val title: String,
  val shareSlug: String,
  val departureDateAr: String,
  val returnDateAr: String,
  val durationAr: String,
  val totalSeats: Int,
  val gatheringLocation: GatheringLocation,
  val busDetails: BusDetails,
  val hotelDetails: HotelDetails,
  val roomPricing: RoomPricing,
  val itinerary: List<ItineraryStep>,
  val supervisor: SupervisorContact,
  @DrawableRes val bannerDrawable: Int = R.drawable.umrah_hero_banner_1787484666263,
  val notesAr: String = "يشمل البرنامج: النقل بحافلات VIP حديثة، الإقامة الفندقية 5 نجوم، الإشراف والمطوف، المزارات النبوية والضيافة.",
  val isRegistrationOpen: Boolean = true
) {
  val fullShareUrl: String
    get() = "https://umrah-app.com/trip/$shareSlug"
}
