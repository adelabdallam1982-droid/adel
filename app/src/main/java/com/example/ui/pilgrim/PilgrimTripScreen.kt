package com.example.ui.pilgrim

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.*
import com.example.ui.TripStats
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun PilgrimTripScreen(
  trip: UmrahTrip,
  stats: TripStats,
  selectedRoomType: RoomType,
  selectedPilgrimCount: Int,
  activeThemePreset: LuxuryThemePreset,
  onThemePresetChanged: (LuxuryThemePreset) -> Unit,
  onRoomTypeSelected: (RoomType) -> Unit,
  onPilgrimCountChanged: (Int) -> Unit,
  onJoinClicked: () -> Unit,
  onShareQrClicked: () -> Unit,
  onOpenPosterStudio: () -> Unit,
  onOpenPhotoViewer: (title: String, subtitle: String, resId: Int) -> Unit
) {
  val context = LocalContext.current
  val clipboardManager = LocalClipboardManager.current
  val colors = LuxuryTheme.colors
  val unitPrice = trip.roomPricing.getPriceFor(selectedRoomType)
  val totalPrice = unitPrice * selectedPilgrimCount

  Box(modifier = Modifier.fillMaxSize()) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .background(colors.background),
      contentPadding = PaddingValues(bottom = 120.dp)
    ) {
      // 1. Hero Banner with Kaaba & Quick Badges
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(230.dp)
        ) {
          Image(
            painter = painterResource(id = R.drawable.img_umrah_royal_banner_1787552186824),
            contentDescription = "المسجد الحرام والكعبة المشرفة",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )

          // Dynamic luxury gradient overlay
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(
                Brush.verticalGradient(
                  colors = listOf(
                    Color.Transparent,
                    colors.background.copy(alpha = 0.60f),
                    colors.background.copy(alpha = 0.98f)
                  )
                )
              )
          )

          // Overlay Content
          Column(
            modifier = Modifier
              .fillMaxSize()
              .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
          ) {
            // Top Pills: VIP & Registration status
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Surface(
                color = colors.primary,
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 4.dp
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = colors.textOnPrimary,
                    modifier = Modifier.size(14.dp)
                  )
                  Text(
                    text = "رحلة VIP فاخرة 5 نجوم",
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontWeight = FontWeight.Bold,
                      color = colors.textOnPrimary
                    )
                  )
                }
              }

              Surface(
                color = colors.surfaceElevated.copy(alpha = 0.92f),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, colors.borderHighlight)
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Box(
                    modifier = Modifier
                      .size(8.dp)
                      .clip(CircleShape)
                      .background(if (stats.availableSeats > 0) WhatsAppGreen else Color.Red)
                  )
                  Text(
                    text = if (stats.availableSeats > 0) "التسجيل متاح (${stats.availableSeats} مقعد)" else "اكتملت المقاعد",
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontWeight = FontWeight.SemiBold,
                      color = colors.textPrimary
                    )
                  )
                }
              }
            }

            // Bottom Title & Duration
            Column {
              Text(
                text = trip.title,
                style = MaterialTheme.typography.headlineSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = colors.textPrimary
                )
              )
              Spacer(modifier = Modifier.height(4.dp))
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier.size(14.dp)
                  )
                  Text(
                    text = trip.departureDateAr,
                    style = MaterialTheme.typography.bodySmall.copy(color = colors.textSecondary)
                  )
                }

                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier.size(14.dp)
                  )
                  Text(
                    text = trip.durationAr,
                    style = MaterialTheme.typography.bodySmall.copy(color = colors.textSecondary)
                  )
                }
              }
            }
          }
        }
      }

      // 2. Share Link & QR Quick Bar
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .offset(y = (-14).dp),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = colors.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, colors.borderHighlight)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "رابط التسجيل ومشاركة الرحلة:",
                style = MaterialTheme.typography.labelSmall.copy(color = colors.textMuted)
              )
              Text(
                text = trip.shareSlug,
                style = MaterialTheme.typography.bodyMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = colors.primary
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              OutlinedButton(
                onClick = {
                  clipboardManager.setText(AnnotatedString(trip.fullShareUrl))
                  android.widget.Toast.makeText(context, "تم نسخ رابط الرحلة", android.widget.Toast.LENGTH_SHORT).show()
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                  containerColor = colors.surfaceElevated,
                  contentColor = colors.textPrimary
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, colors.borderHighlight),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                modifier = Modifier.height(36.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.ContentCopy,
                  contentDescription = null,
                  tint = colors.primary,
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("نسخ", fontSize = 12.sp, color = colors.textPrimary)
              }

              Button(
                onClick = onShareQrClicked,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                modifier = Modifier.height(36.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.QrCode2,
                  contentDescription = null,
                  tint = colors.textOnPrimary,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("QR", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = colors.textOnPrimary)
              }
            }
          }
        }
      }

      // 3. NEW: Interactive Luxury Poster & Color Theme Showcase Studio Banner
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = colors.surfaceSubtle),
          border = androidx.compose.foundation.BorderStroke(1.5.dp, colors.primary.copy(alpha = 0.5f)),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(colors.primary.copy(alpha = 0.2f))
                    .border(1.dp, colors.primary, CircleShape),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Palette,
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier.size(18.dp)
                  )
                }

                Column {
                  Text(
                    text = "🎨 ألوان ملكية وتصميم البوستر الإعلاني",
                    style = MaterialTheme.typography.titleSmall.copy(
                      fontWeight = FontWeight.Bold,
                      color = colors.textPrimary
                    )
                  )
                  Text(
                    text = "اختر الطابع اللوني المفضل للتطبيق والبوستر",
                    style = MaterialTheme.typography.labelSmall.copy(
                      color = colors.textMuted,
                      fontSize = 10.sp
                    )
                  )
                }
              }

              Button(
                onClick = onOpenPosterStudio,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.height(34.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Visibility,
                  contentDescription = null,
                  tint = colors.textOnPrimary,
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "عرض البوستر",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = colors.textOnPrimary
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick live theme chips row
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              LuxuryThemePreset.entries.forEach { preset ->
                val isSelected = activeThemePreset == preset

                Surface(
                  modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .border(
                      width = if (isSelected) 2.dp else 1.dp,
                      color = if (isSelected) preset.previewPrimary else colors.borderHighlight,
                      shape = RoundedCornerShape(10.dp)
                    )
                    .clickable { onThemePresetChanged(preset) },
                  color = if (isSelected) colors.surfaceElevated else colors.surface,
                  shape = RoundedCornerShape(10.dp)
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                  ) {
                    Box(
                      modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(preset.previewPrimary)
                    )
                    Text(
                      text = preset.nameAr,
                      style = MaterialTheme.typography.labelSmall.copy(
                        color = if (isSelected) colors.textPrimary else colors.textSecondary,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 10.sp
                      )
                    )
                  }
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))
      }

      // 4. Key Trip Highlights (Dates, Riyadh Gathering, Available Seats)
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            // Departure Date Card
            Card(
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = colors.surface),
              border = androidx.compose.foundation.BorderStroke(1.dp, colors.border)
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Icon(Icons.Default.FlightTakeoff, contentDescription = null, tint = colors.secondary, modifier = Modifier.size(16.dp))
                  Text("تاريخ الذهاب", style = MaterialTheme.typography.labelSmall, color = colors.textMuted)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(trip.departureDateAr, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                Text(trip.gatheringLocation.departureTimeAr, style = MaterialTheme.typography.labelSmall, color = colors.primary)
              }
            }

            // Return Date Card
            Card(
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = colors.surface),
              border = androidx.compose.foundation.BorderStroke(1.dp, colors.border)
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Icon(Icons.Default.FlightLand, contentDescription = null, tint = colors.primary, modifier = Modifier.size(16.dp))
                  Text("تاريخ العودة", style = MaterialTheme.typography.labelSmall, color = colors.textMuted)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(trip.returnDateAr, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                Text("الأحد بعد صلاة الظهر", style = MaterialTheme.typography.labelSmall, color = colors.textSecondary)
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))
        }
      }

      // 5. Bus Section (صور وتفاصيل الباص VIP من الداخل والخارج)
      item {
        SectionCard(
          title = "حافلة الرحلة VIP (مرسيدس 2026)",
          subtitle = "صور الباص الحقيقية من الداخل والخارج ووسائل الراحة",
          icon = Icons.Default.DirectionsBus,
          accentColor = colors.primary
        ) {
          Column {
            // Bus Photos (Exterior & Interior)
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              // Exterior Image
              Box(
                modifier = Modifier
                  .weight(1f)
                  .height(120.dp)
                  .clip(RoundedCornerShape(12.dp))
                  .border(1.dp, colors.borderHighlight, RoundedCornerShape(12.dp))
                  .clickable {
                    onOpenPhotoViewer("الباص من الخارج", trip.busDetails.modelName, trip.busDetails.exteriorDrawable)
                  }
              ) {
                Image(
                  painter = painterResource(id = trip.busDetails.exteriorDrawable),
                  contentDescription = "صورة الباص من الخارج",
                  contentScale = ContentScale.Crop,
                  modifier = Modifier.fillMaxSize()
                )
                Surface(
                  color = colors.background.copy(alpha = 0.85f),
                  shape = RoundedCornerShape(topStart = 8.dp),
                  modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                  Text(
                    text = "من الخارج 🔍",
                    style = MaterialTheme.typography.labelSmall.copy(color = colors.textPrimary, fontSize = 10.sp),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }

              // Interior Image
              Box(
                modifier = Modifier
                  .weight(1f)
                  .height(120.dp)
                  .clip(RoundedCornerShape(12.dp))
                  .border(1.dp, colors.borderHighlight, RoundedCornerShape(12.dp))
                  .clickable {
                    onOpenPhotoViewer("الباص من الداخل", "مقاعد جلدية مريحة وواي فاي", trip.busDetails.interiorDrawable)
                  }
              ) {
                Image(
                  painter = painterResource(id = trip.busDetails.interiorDrawable),
                  contentDescription = "صورة الباص من الداخل",
                  contentScale = ContentScale.Crop,
                  modifier = Modifier.fillMaxSize()
                )
                Surface(
                  color = colors.background.copy(alpha = 0.85f),
                  shape = RoundedCornerShape(topStart = 8.dp),
                  modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                  Text(
                    text = "من الداخل 🔍",
                    style = MaterialTheme.typography.labelSmall.copy(color = colors.textPrimary, fontSize = 10.sp),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bus Features Checklist
            Text(
              text = "مميزات الحافلة والتنقل:",
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = colors.textPrimary)
            )
            Spacer(modifier = Modifier.height(6.dp))

            trip.busDetails.features.forEach { feat ->
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(vertical = 2.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.CheckCircle,
                  contentDescription = null,
                  tint = colors.secondary,
                  modifier = Modifier.size(16.dp)
                )
                Text(
                  text = feat,
                  style = MaterialTheme.typography.bodySmall.copy(color = colors.textSecondary)
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))
      }

      // 6. Hotel & Room Photos Section (اسم الفندق وصور الفندق والغرف الحقيقية)
      item {
        SectionCard(
          title = trip.hotelDetails.name,
          subtitle = trip.hotelDetails.distanceFromHaram,
          icon = Icons.Default.Hotel,
          accentColor = colors.primary
        ) {
          Column {
            // Hotel & Room Photos
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              // Hotel Exterior/Lobby
              Box(
                modifier = Modifier
                  .weight(1f)
                  .height(130.dp)
                  .clip(RoundedCornerShape(12.dp))
                  .border(1.dp, colors.borderHighlight, RoundedCornerShape(12.dp))
                  .clickable {
                    onOpenPhotoViewer("فندق السكن", trip.hotelDetails.name, trip.hotelDetails.hotelDrawable)
                  }
              ) {
                Image(
                  painter = painterResource(id = trip.hotelDetails.hotelDrawable),
                  contentDescription = "صورة الفندق",
                  contentScale = ContentScale.Crop,
                  modifier = Modifier.fillMaxSize()
                )
                Surface(
                  color = colors.background.copy(alpha = 0.85f),
                  shape = RoundedCornerShape(topStart = 8.dp),
                  modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                  Text(
                    text = "واجهة الفندق 🔍",
                    style = MaterialTheme.typography.labelSmall.copy(color = colors.textPrimary, fontSize = 10.sp),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }

              // Real Room Photo
              Box(
                modifier = Modifier
                  .weight(1f)
                  .height(130.dp)
                  .clip(RoundedCornerShape(12.dp))
                  .border(1.dp, colors.borderHighlight, RoundedCornerShape(12.dp))
                  .clickable {
                    onOpenPhotoViewer("الغرف الفندقية الحقيقية", "أسرّة فاخرة ومرافق متكاملة", trip.hotelDetails.roomDrawable)
                  }
              ) {
                Image(
                  painter = painterResource(id = trip.hotelDetails.roomDrawable),
                  contentDescription = "صورة الغرفة الحقيقية",
                  contentScale = ContentScale.Crop,
                  modifier = Modifier.fillMaxSize()
                )
                Surface(
                  color = colors.background.copy(alpha = 0.85f),
                  shape = RoundedCornerShape(topStart = 8.dp),
                  modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                  Text(
                    text = "الغرف الحقيقية 🔍",
                    style = MaterialTheme.typography.labelSmall.copy(color = colors.textPrimary, fontSize = 10.sp),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Hotel Amenities
            trip.hotelDetails.features.forEach { item ->
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(vertical = 2.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Done,
                  contentDescription = null,
                  tint = colors.primary,
                  modifier = Modifier.size(16.dp)
                )
                Text(
                  text = item,
                  style = MaterialTheme.typography.bodySmall.copy(color = colors.textSecondary)
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))
      }

      // 7. Interactive Room Selection & Price Calculator (اختيار نوع الغرفة والأسعار)
      item {
        SectionCard(
          title = "اختيار نوع الغرفة وسعر الرحلة",
          subtitle = "حدد نوع الغرفة المناسب لمعرفة تكلفة المعتمر والإجمالي",
          icon = Icons.Default.KingBed,
          accentColor = colors.primary
        ) {
          Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            RoomType.entries.forEach { roomType ->
              val isSelected = selectedRoomType == roomType
              val price = trip.roomPricing.getPriceFor(roomType)

              Card(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(12.dp))
                  .border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) colors.primary else colors.borderHighlight,
                    shape = RoundedCornerShape(12.dp)
                  )
                  .clickable { onRoomTypeSelected(roomType) },
                colors = CardDefaults.cardColors(
                  containerColor = if (isSelected) colors.surfaceElevated else colors.surface
                )
              ) {
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                  ) {
                    RadioButton(
                      selected = isSelected,
                      onClick = { onRoomTypeSelected(roomType) },
                      colors = RadioButtonDefaults.colors(
                        selectedColor = colors.primary,
                        unselectedColor = colors.textMuted
                      )
                    )

                    Column {
                      Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                      ) {
                        Text(
                          text = roomType.titleAr,
                          style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) colors.primary else colors.textPrimary
                          )
                        )
                        Surface(
                          color = if (isSelected) colors.primary.copy(alpha = 0.15f) else colors.surfaceElevated,
                          shape = RoundedCornerShape(4.dp),
                          border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) colors.primary.copy(alpha = 0.5f) else colors.borderHighlight)
                        ) {
                          Text(
                            text = roomType.badgeAr,
                            style = MaterialTheme.typography.labelSmall.copy(
                              fontSize = 10.sp,
                              color = if (isSelected) colors.primary else colors.textMuted,
                              fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                          )
                        }
                      }
                      Text(
                        text = roomType.descriptionAr,
                        style = MaterialTheme.typography.bodySmall.copy(
                          color = colors.textSecondary,
                          fontSize = 11.sp
                        ),
                        maxLines = 2
                      )
                    }
                  }

                  // Price
                  Column(horizontalAlignment = Alignment.End) {
                    Text(
                      text = "$price ر.س",
                      style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = colors.primary
                      )
                    )
                    Text(
                      text = "للفرد شامل الرحلة",
                      style = MaterialTheme.typography.labelSmall.copy(
                        color = colors.textMuted,
                        fontSize = 10.sp
                      )
                    )
                  }
                }
              }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Number of Pilgrims Selector
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = colors.surfaceElevated),
              border = androidx.compose.foundation.BorderStroke(1.dp, colors.borderHighlight)
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column {
                  Text(
                    text = "عدد المعتمرين المسجلين:",
                    style = MaterialTheme.typography.labelMedium.copy(
                      fontWeight = FontWeight.Bold,
                      color = colors.textPrimary
                    )
                  )
                  Text(
                    text = "سعر الفرد: $unitPrice ر.س",
                    style = MaterialTheme.typography.bodySmall.copy(color = colors.primary)
                  )
                }

                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  IconButton(
                    onClick = { onPilgrimCountChanged((selectedPilgrimCount - 1).coerceAtLeast(1)) },
                    modifier = Modifier
                      .size(36.dp)
                      .clip(CircleShape)
                      .background(colors.surface)
                      .border(1.dp, colors.borderHighlight, CircleShape)
                  ) {
                    Icon(Icons.Default.Remove, contentDescription = "تقليل", tint = colors.textPrimary, modifier = Modifier.size(16.dp))
                  }

                  Text(
                    text = "$selectedPilgrimCount",
                    style = MaterialTheme.typography.titleMedium.copy(
                      fontWeight = FontWeight.Bold,
                      color = colors.primary
                    )
                  )

                  IconButton(
                    onClick = { onPilgrimCountChanged((selectedPilgrimCount + 1).coerceAtMost(stats.availableSeats.coerceAtLeast(1))) },
                    modifier = Modifier
                      .size(36.dp)
                      .clip(CircleShape)
                      .background(colors.primary)
                  ) {
                    Icon(Icons.Default.Add, contentDescription = "زيادة", tint = colors.textOnPrimary, modifier = Modifier.size(16.dp))
                  }
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))
      }

      // 8. Gathering Time & Location in Riyadh (موعد ومكان التجمع بالرياض مع الخريطة)
      item {
        SectionCard(
          title = "موعد ومكان التجمع في الرياض",
          subtitle = trip.gatheringLocation.pointName,
          icon = Icons.Default.LocationOn,
          accentColor = colors.primary
        ) {
          Column {
            Row(
              verticalAlignment = Alignment.Top,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(40.dp)
                  .clip(RoundedCornerShape(10.dp))
                  .background(colors.primary.copy(alpha = 0.15f))
                  .border(1.dp, colors.primary.copy(alpha = 0.4f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.AccessTimeFilled,
                  contentDescription = null,
                  tint = colors.primary,
                  modifier = Modifier.size(22.dp)
                )
              }

              Column {
                Text(
                  text = "وقت التجمع والانطلاق:",
                  style = MaterialTheme.typography.labelSmall.copy(color = colors.textMuted)
                )
                Text(
                  text = "${trip.gatheringLocation.timeAr} • ${trip.gatheringLocation.departureTimeAr}",
                  style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                  )
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Map Preview Card
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
              colors = CardDefaults.cardColors(containerColor = colors.surfaceElevated),
              border = androidx.compose.foundation.BorderStroke(1.dp, colors.borderHighlight)
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    tint = Color(0xFFEF4444),
                    modifier = Modifier.size(18.dp)
                  )
                  Text(
                    text = trip.gatheringLocation.addressDetails,
                    style = MaterialTheme.typography.bodySmall.copy(
                      fontWeight = FontWeight.SemiBold,
                      color = colors.textPrimary
                    )
                  )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Interactive Button: Open in Google Maps
                Button(
                  onClick = {
                    openGoogleMaps(
                      context = context,
                      lat = trip.gatheringLocation.latitude,
                      lng = trip.gatheringLocation.longitude,
                      label = trip.gatheringLocation.pointName
                    )
                  },
                  modifier = Modifier.fillMaxWidth(),
                  shape = RoundedCornerShape(8.dp),
                  colors = ButtonDefaults.buttonColors(
                    containerColor = colors.surface,
                    contentColor = colors.primary
                  ),
                  border = androidx.compose.foundation.BorderStroke(1.dp, colors.primary.copy(alpha = 0.6f))
                ) {
                  Icon(
                    imageVector = Icons.Default.Map,
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "فتح موقع التجمع على خرائط Google",
                    style = MaterialTheme.typography.labelMedium.copy(color = colors.primary, fontWeight = FontWeight.Bold)
                  )
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))
      }

      // 9. Trip Itinerary (تفاصيل برنامج الرحلة)
      item {
        SectionCard(
          title = "برنامج الرحلة خطوة بخطوة",
          subtitle = "جدول زمني متكامل من انطلاق الرياض حتى العودة",
          icon = Icons.Default.Timeline,
          accentColor = colors.primary
        ) {
          Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            trip.itinerary.forEachIndexed { index, step ->
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.Top
              ) {
                // Number / Icon Circle
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Box(
                    modifier = Modifier
                      .size(32.dp)
                      .clip(CircleShape)
                      .background(colors.surfaceElevated)
                      .border(1.dp, colors.primary, CircleShape),
                    contentAlignment = Alignment.Center
                  ) {
                    Text(
                      text = "${step.stepNumber}",
                      style = MaterialTheme.typography.labelMedium.copy(
                        color = colors.primary,
                        fontWeight = FontWeight.Bold
                      )
                    )
                  }
                  if (index < trip.itinerary.size - 1) {
                    Box(
                      modifier = Modifier
                        .width(2.dp)
                        .height(38.dp)
                        .background(colors.borderHighlight)
                    )
                  }
                }

                // Step content
                Column(modifier = Modifier.weight(1f)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(
                      text = step.titleAr,
                      style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                      )
                    )
                    Surface(
                      color = colors.surfaceElevated,
                      shape = RoundedCornerShape(4.dp),
                      border = androidx.compose.foundation.BorderStroke(1.dp, colors.borderHighlight)
                    ) {
                      Text(
                        text = step.timeAr,
                        style = MaterialTheme.typography.labelSmall.copy(
                          fontSize = 10.sp,
                          color = colors.primary,
                          fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                      )
                    }
                  }
                  Spacer(modifier = Modifier.height(2.dp))
                  Text(
                    text = step.descriptionAr,
                    style = MaterialTheme.typography.bodySmall.copy(
                      color = colors.textSecondary,
                      fontSize = 12.sp,
                      lineHeight = 17.sp
                    )
                  )
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))
      }

      // 10. Trip Supervisor & Direct Contact (رقم مسؤول الرحلة)
      item {
        SectionCard(
          title = "مسؤول ومطوف الرحلة",
          subtitle = "للاستفسارات والمساعدة قبل وأثناء الرحلة",
          icon = Icons.Default.SupportAgent,
          accentColor = colors.primary
        ) {
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = colors.surfaceElevated),
            border = androidx.compose.foundation.BorderStroke(1.dp, colors.borderHighlight)
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(colors.primary),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = colors.textOnPrimary,
                    modifier = Modifier.size(26.dp)
                  )
                }

                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = trip.supervisor.name,
                    style = MaterialTheme.typography.titleSmall.copy(
                      fontWeight = FontWeight.Bold,
                      color = colors.textPrimary
                    )
                  )
                  Text(
                    text = trip.supervisor.role,
                    style = MaterialTheme.typography.bodySmall.copy(color = colors.primary)
                  )
                  Text(
                    text = trip.supervisor.phone,
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontWeight = FontWeight.SemiBold,
                      color = colors.textSecondary
                    )
                  )
                }
              }

              Spacer(modifier = Modifier.height(12.dp))

              // Action Buttons: WhatsApp & Call
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Button(
                  onClick = {
                    openWhatsApp(
                      context = context,
                      phone = trip.supervisor.whatsapp,
                      message = "السلام عليكم ورحمة الله، أود الاستفسار بخصوص ${trip.title}"
                    )
                  },
                  modifier = Modifier.weight(1f),
                  shape = RoundedCornerShape(8.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen)
                ) {
                  Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("محادثة WhatsApp", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                  onClick = { dialPhoneNumber(context, trip.supervisor.phone) },
                  modifier = Modifier.weight(1f),
                  shape = RoundedCornerShape(8.dp),
                  colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = colors.surface,
                    contentColor = colors.textPrimary
                  ),
                  border = androidx.compose.foundation.BorderStroke(1.dp, colors.borderHighlight)
                ) {
                  Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("اتصال مباشر", color = colors.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))
      }
    }

    // 11. Sticky Bottom Bar: Room Choice Summary + «انضم إلى الرحلة» Button
    Surface(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .fillMaxWidth(),
      color = colors.headerBackground,
      shadowElevation = 16.dp,
      border = androidx.compose.foundation.BorderStroke(1.dp, colors.border)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .navigationBarsPadding()
          .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "قيمة الرحلة الإجمالية:",
            style = MaterialTheme.typography.labelSmall.copy(color = colors.textMuted)
          )
          Row(verticalAlignment = Alignment.Bottom) {
            Text(
              text = "$totalPrice",
              style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = colors.primary
              )
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "ر.س",
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = colors.primary
              ),
              modifier = Modifier.padding(bottom = 2.dp)
            )
          }
          Text(
            text = "${selectedRoomType.titleAr} ($selectedPilgrimCount أفراد)",
            style = MaterialTheme.typography.labelSmall.copy(
              color = colors.textSecondary,
              fontSize = 11.sp
            )
          )
        }

        Button(
          onClick = onJoinClicked,
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
          contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
          modifier = Modifier.height(52.dp)
        ) {
          Icon(
            imageVector = Icons.Default.HowToReg,
            contentDescription = null,
            tint = colors.textOnPrimary,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "انضم إلى الرحلة",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = colors.textOnPrimary
            )
          )
        }
      }
    }
  }
}

@Composable
fun SectionCard(
  title: String,
  subtitle: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  accentColor: Color,
  content: @Composable () -> Unit
) {
  val colors = LuxuryTheme.colors
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = colors.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, colors.border)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(colors.surfaceElevated)
            .border(1.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(10.dp)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = accentColor,
            modifier = Modifier.size(20.dp)
          )
        }

        Column {
          Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = colors.textPrimary
            )
          )
          if (subtitle.isNotBlank()) {
            Text(
              text = subtitle,
              style = MaterialTheme.typography.bodySmall.copy(color = colors.textMuted)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))
      Divider(color = colors.borderHighlight.copy(alpha = 0.6f))
      Spacer(modifier = Modifier.height(14.dp))

      content()
    }
  }
}
