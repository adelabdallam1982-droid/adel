package com.example.ui.organizer

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.TripStats
import com.example.ui.components.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrganizerDashboardScreen(
  trip: UmrahTrip,
  stats: TripStats,
  bookings: List<PilgrimBooking>,
  searchQuery: String,
  statusFilter: BookingStatus?,
  roomFilter: RoomType?,
  onSearchQueryChanged: (String) -> Unit,
  onStatusFilterChanged: (BookingStatus?) -> Unit,
  onRoomFilterChanged: (RoomType?) -> Unit,
  onUpdateBookingStatus: (bookingId: String, newStatus: BookingStatus) -> Unit,
  onDeleteBooking: (bookingId: String) -> Unit,
  onCreateNewTripClick: () -> Unit,
  onBroadcastClick: () -> Unit,
  onDeleteTripClick: () -> Unit
) {
  val context = LocalContext.current

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(DarkBg),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // 1. Dashboard Top Header & Quick Actions
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "لوحة تحكم الحملة والرحلات",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              color = DarkTextPrimary
            )
          )
          Text(
            text = "إدارة ${trip.title}",
            style = MaterialTheme.typography.bodySmall.copy(color = DarkTextMuted),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }

        FilledTonalButton(
          onClick = onCreateNewTripClick,
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = Amber500,
            contentColor = DarkBg
          ),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
        ) {
          Icon(Icons.Default.Add, contentDescription = null, tint = DarkBg, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("رحلة جديدة", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkBg)
        }
      }
    }

    // 2. Overview Stats Grid (عدد المسجلين، المقاعد المتاحة، الغرف، الإيرادات)
    item {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // Registered Pilgrims Stat
          MetricCard(
            title = "المعتمرين المسجلين",
            value = "${stats.registeredPilgrims} / ${stats.totalSeats}",
            subtitle = "متبقي ${stats.availableSeats} مقاعد",
            icon = Icons.Default.Groups,
            color = Amber400,
            modifier = Modifier.weight(1f)
          )

          // Revenue Stat
          MetricCard(
            title = "إجمالي الحجوزات",
            value = "${stats.totalRevenueSar} ر.س",
            subtitle = "المؤكد: ${stats.confirmedRevenueSar} ر.س",
            icon = Icons.Default.AttachMoney,
            color = Emerald400,
            modifier = Modifier.weight(1f)
          )
        }

        // Room Occupancy Breakdown
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Text(
              text = "توزيع الغرف المحجوزة:",
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = DarkTextPrimary
              )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceAround
            ) {
              RoomStatItem("فردية", "${stats.singleRoomsCount}", Amber400)
              RoomStatItem("ثنائية", "${stats.doubleRoomsCount}", Emerald400)
              RoomStatItem("ثلاثية", "${stats.tripleRoomsCount}", Amber300)
              RoomStatItem("رباعية", "${stats.quadRoomsCount}", Emerald300)
            }
          }
        }
      }
    }

    // 3. Organizer Action Bar (تصدير الكشف، إرسال تعميم، رابط التسجيل)
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // Export List Button
        OutlinedButton(
          onClick = {
            val listExport = buildString {
              appendLine("📋 كشف معتمري ${trip.title}")
              appendLine("📅 تاريخ الرحلة: ${trip.departureDateAr}")
              appendLine("----------------------------------------")
              bookings.forEachIndexed { i, b ->
                appendLine("${i + 1}. ${b.pilgrimName} | ${b.phone} | ${b.roomType.titleAr} (${b.pilgrimCount} أفراد) | ${b.totalPrice} ر.س | [${b.status.titleAr}]")
              }
              appendLine("----------------------------------------")
              appendLine("إجمالي المعتمرين: ${stats.registeredPilgrims} | الإجمالي: ${stats.totalRevenueSar} ر.س")
            }
            shareText(context, "كشف أسماء المعتمرين", listExport)
          },
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.outlinedButtonColors(
            containerColor = DarkSurfaceElevated,
            contentColor = DarkTextPrimary
          ),
          border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderHighlight),
          contentPadding = PaddingValues(vertical = 8.dp)
        ) {
          Icon(Icons.Default.FileDownload, contentDescription = null, tint = Amber400, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("تصدير الكشف", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkTextPrimary)
        }

        // Broadcast Message Button
        Button(
          onClick = onBroadcastClick,
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = Amber500),
          contentPadding = PaddingValues(vertical = 8.dp)
        ) {
          Icon(Icons.Default.Campaign, contentDescription = null, tint = DarkBg, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("إرسال تعميم", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkBg)
        }
      }
    }

    // 4. Search and Filter Bar
    item {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = onSearchQueryChanged,
          placeholder = { Text("ابحث باسم المعتمر أو رقم الجوال أو رقم الحجز...", color = DarkTextMuted) },
          leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Amber400) },
          trailingIcon = {
            if (searchQuery.isNotEmpty()) {
              IconButton(onClick = { onSearchQueryChanged("") }) {
                Icon(Icons.Default.Clear, contentDescription = "مسح", tint = DarkTextMuted)
              }
            }
          },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = DarkSurface,
            unfocusedContainerColor = DarkSurface,
            focusedBorderColor = Amber500,
            unfocusedBorderColor = DarkBorderHighlight,
            focusedTextColor = DarkTextPrimary,
            unfocusedTextColor = DarkTextPrimary
          ),
          singleLine = true
        )

        // Status Filter Chips
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          item {
            FilterChip(
              selected = statusFilter == null,
              onClick = { onStatusFilterChanged(null) },
              label = { Text("الكل (${bookings.size})", fontSize = 11.sp) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = Amber500,
                selectedLabelColor = DarkBg,
                containerColor = DarkSurfaceElevated,
                labelColor = DarkTextSecondary
              ),
              border = FilterChipDefaults.filterChipBorder(
                borderColor = DarkBorderHighlight,
                selectedBorderColor = Amber500,
                enabled = true,
                selected = statusFilter == null
              )
            )
          }

          BookingStatus.entries.forEach { status ->
            item {
              FilterChip(
                selected = statusFilter == status,
                onClick = { onStatusFilterChanged(if (statusFilter == status) null else status) },
                label = { Text(status.titleAr, fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = Amber500,
                  selectedLabelColor = DarkBg,
                  containerColor = DarkSurfaceElevated,
                  labelColor = DarkTextSecondary
                ),
                border = FilterChipDefaults.filterChipBorder(
                  borderColor = DarkBorderHighlight,
                  selectedBorderColor = Amber500,
                  enabled = true,
                  selected = statusFilter == status
                )
              )
            }
          }
        }
      }
    }

    // 5. Registered Pilgrims List Title
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "قائمة المعتمرين المسجلين (${bookings.size}):",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = DarkTextPrimary
          )
        )
      }
    }

    // 6. Pilgrim Cards
    if (bookings.isEmpty()) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = DarkSurface),
          border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(Icons.Default.PersonSearch, contentDescription = null, tint = DarkTextMuted, modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "لا توجد حجوزات مطابقة للبحث",
              style = MaterialTheme.typography.bodyMedium.copy(color = DarkTextMuted)
            )
          }
        }
      }
    } else {
      items(bookings, key = { it.id }) { booking ->
        PilgrimCard(
          booking = booking,
          onUpdateStatus = { newStatus -> onUpdateBookingStatus(booking.id, newStatus) },
          onDelete = { onDeleteBooking(booking.id) }
        )
      }
    }
  }
}

@Composable
fun MetricCard(
  title: String,
  value: String,
  subtitle: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  color: Color,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = DarkSurface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = title,
          style = MaterialTheme.typography.labelSmall.copy(color = DarkTextMuted)
        )
        Box(
          modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(DarkSurfaceElevated)
            .border(1.dp, color.copy(alpha = 0.4f), CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
        }
      }

      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = value,
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          color = DarkTextPrimary
        )
      )
      Text(
        text = subtitle,
        style = MaterialTheme.typography.bodySmall.copy(
          color = DarkTextSecondary,
          fontSize = 10.sp
        )
      )
    }
  }
}

@Composable
fun RoomStatItem(title: String, count: String, color: Color) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(
      text = count,
      style = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.Bold,
        color = color
      )
    )
    Text(
      text = title,
      style = MaterialTheme.typography.labelSmall.copy(
        color = DarkTextMuted,
        fontSize = 11.sp
      )
    )
  }
}

@Composable
fun PilgrimCard(
  booking: PilgrimBooking,
  onUpdateStatus: (BookingStatus) -> Unit,
  onDelete: () -> Unit
) {
  val context = LocalContext.current
  var showMenu by remember { mutableStateOf(false) }

  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = DarkSurface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      // Top Row: Name + Status Badge + Menu
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = booking.pilgrimName,
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.Bold,
              color = DarkTextPrimary
            )
          )
          Text(
            text = "رقم الحجز: ${booking.id} • ${booking.registeredAt}",
            style = MaterialTheme.typography.labelSmall.copy(
              color = DarkTextMuted,
              fontSize = 10.sp
            )
          )
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          StatusBadge(status = booking.status)

          Box {
            IconButton(
              onClick = { showMenu = true },
              modifier = Modifier.size(30.dp)
            ) {
              Icon(Icons.Default.MoreVert, contentDescription = "خيارات الحجز", tint = DarkTextMuted)
            }

            DropdownMenu(
              expanded = showMenu,
              onDismissRequest = { showMenu = false },
              modifier = Modifier
                .background(DarkSurface)
                .border(1.dp, DarkBorderHighlight, RoundedCornerShape(8.dp))
            ) {
              Text(
                text = "تغيير حالة الحجز:",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = DarkTextMuted
                ),
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
              )

              BookingStatus.entries.forEach { status ->
                DropdownMenuItem(
                  text = { Text("تغيير إلى: ${status.titleAr}", color = DarkTextPrimary) },
                  leadingIcon = {
                    Icon(
                      imageVector = if (booking.status == status) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                      contentDescription = null,
                      tint = if (booking.status == status) Amber500 else DarkTextMuted
                    )
                  },
                  onClick = {
                    onUpdateStatus(status)
                    showMenu = false
                  }
                )
              }

              Divider(
                modifier = Modifier.padding(vertical = 4.dp),
                color = DarkBorderHighlight
              )

              DropdownMenuItem(
                text = { Text("حذف هذا الحجز", color = Color(0xFFF87171)) },
                leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFF87171)) },
                onClick = {
                  onDelete()
                  showMenu = false
                }
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Middle Row: Room Type + Count + Total SAR
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          RoomBadge(roomType = booking.roomType)
          Surface(
            color = DarkSurfaceElevated,
            shape = RoundedCornerShape(6.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderHighlight)
          ) {
            Text(
              text = "${booking.pilgrimCount} أفراد (${booking.gender})",
              style = MaterialTheme.typography.labelSmall.copy(
                color = DarkTextSecondary,
                fontSize = 11.sp
              ),
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }

        Text(
          text = "${booking.totalPrice} ر.س",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = Amber400
          )
        )
      }

      if (booking.notes.isNotBlank()) {
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "ملاحظات: ${booking.notes}",
          style = MaterialTheme.typography.bodySmall.copy(
            color = DarkTextSecondary,
            fontSize = 11.sp
          )
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Action Row: Quick WhatsApp & Call
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedButton(
          onClick = {
            val whatsappMsg = "السلام عليكم ورحمة الله أخي ${booking.pilgrimName}، بخصوص حجزك لرحلة العمرة (${booking.id}) نوع الغرفة: ${booking.roomType.titleAr}..."
            openWhatsApp(context, booking.phone, whatsappMsg)
          },
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(vertical = 4.dp),
          colors = ButtonDefaults.outlinedButtonColors(
            containerColor = DarkSurfaceElevated,
            contentColor = WhatsAppGreen
          ),
          border = androidx.compose.foundation.BorderStroke(1.dp, WhatsAppGreen.copy(alpha = 0.6f))
        ) {
          Icon(Icons.Default.Chat, contentDescription = null, tint = WhatsAppGreen, modifier = Modifier.size(15.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("مراسلة WhatsApp", color = WhatsAppGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }

        OutlinedButton(
          onClick = { dialPhoneNumber(context, booking.phone) },
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(vertical = 4.dp),
          colors = ButtonDefaults.outlinedButtonColors(
            containerColor = DarkSurfaceElevated,
            contentColor = DarkTextPrimary
          ),
          border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderHighlight)
        ) {
          Icon(Icons.Default.Phone, contentDescription = null, tint = Amber400, modifier = Modifier.size(15.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(booking.phone, color = DarkTextPrimary, fontSize = 11.sp)
        }
      }
    }
  }
}
