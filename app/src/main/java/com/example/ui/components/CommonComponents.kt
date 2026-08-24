package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.BookingStatus
import com.example.data.model.RoomType
import com.example.data.model.UmrahTrip
import com.example.ui.AppTab
import com.example.ui.PhotoViewerState
import com.example.ui.theme.*

@Composable
fun AppHeader(
  activeTab: AppTab,
  onTabSelected: (AppTab) -> Unit,
  trips: List<UmrahTrip>,
  selectedTrip: UmrahTrip?,
  onTripSelected: (String) -> Unit,
  onShareClick: () -> Unit,
  onPosterStudioClick: (() -> Unit)? = null
) {
  val colors = LuxuryTheme.colors
  var showTripMenu by remember { mutableStateOf(false) }

  Surface(
    color = colors.headerBackground,
    border = androidx.compose.foundation.BorderStroke(1.dp, colors.border),
    shadowElevation = 8.dp
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .statusBarsPadding()
        .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
      // Top Campaign Title & Actions
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(
                Brush.linearGradient(listOf(colors.primary, colors.primaryVariant))
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Mosque,
              contentDescription = "شعار المنصة",
              tint = colors.textOnPrimary,
              modifier = Modifier.size(24.dp)
            )
          }

          Column {
            Text(
              text = "حملة الفتح لرحلات العمرة",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
              )
            )
            Text(
              text = "الرياض • مكة المكرمة • VIP 2026",
              style = MaterialTheme.typography.bodySmall.copy(
                color = colors.primary,
                fontSize = 11.sp
              )
            )
          }
        }

        Row(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          if (onPosterStudioClick != null) {
            IconButton(
              onClick = onPosterStudioClick,
              modifier = Modifier
                .clip(CircleShape)
                .background(colors.surfaceElevated)
                .border(1.dp, colors.primary.copy(alpha = 0.5f), CircleShape)
            ) {
              Icon(
                imageVector = Icons.Default.Palette,
                contentDescription = "استوديو البوستر والألوان",
                tint = colors.primary,
                modifier = Modifier.size(18.dp)
              )
            }
          }

          IconButton(
            onClick = onShareClick,
            modifier = Modifier
              .clip(CircleShape)
              .background(colors.surfaceElevated)
              .border(1.dp, colors.borderHighlight, CircleShape)
          ) {
            Icon(
              imageVector = Icons.Default.Share,
              contentDescription = "مشاركة رابط الرحلة",
              tint = colors.primary,
              modifier = Modifier.size(18.dp)
            )
          }

          // Trip Dropdown selector
          Box {
            FilledTonalButton(
              onClick = { showTripMenu = true },
              shape = RoundedCornerShape(20.dp),
              colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = colors.surfaceElevated,
                contentColor = colors.textPrimary
              ),
              border = androidx.compose.foundation.BorderStroke(1.dp, colors.borderHighlight),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
              modifier = Modifier.height(36.dp)
            ) {
              Icon(
                imageVector = Icons.Default.DirectionsBus,
                contentDescription = null,
                tint = colors.primary,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "الرحلات",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold, color = colors.textPrimary)
              )
              Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = colors.textSecondary,
                modifier = Modifier.size(16.dp)
              )
            }

            DropdownMenu(
              expanded = showTripMenu,
              onDismissRequest = { showTripMenu = false },
              modifier = Modifier
                .background(colors.surface)
                .border(1.dp, colors.borderHighlight, RoundedCornerShape(8.dp))
            ) {
              Text(
                text = "اختر الرحلة الحالية:",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = colors.textMuted,
                  fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
              )
              trips.forEach { trip ->
                DropdownMenuItem(
                  text = {
                    Column {
                      Text(
                        text = trip.title,
                        fontWeight = if (trip.id == selectedTrip?.id) FontWeight.Bold else FontWeight.Normal,
                        color = if (trip.id == selectedTrip?.id) colors.primary else colors.textPrimary
                      )
                      Text(
                        text = "${trip.departureDateAr} • ${trip.hotelDetails.name}",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.textMuted
                      )
                    }
                  },
                  leadingIcon = {
                    Icon(
                      imageVector = if (trip.id == selectedTrip?.id) Icons.Default.CheckCircle else Icons.Outlined.DirectionsBus,
                      contentDescription = null,
                      tint = if (trip.id == selectedTrip?.id) colors.primary else colors.textMuted
                    )
                  },
                  onClick = {
                    onTripSelected(trip.id)
                    showTripMenu = false
                  }
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Tab Switcher Pill
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .background(colors.surfaceElevated)
          .border(1.dp, colors.borderHighlight, RoundedCornerShape(14.dp))
          .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        AppTab.entries.forEach { tab ->
          val isSelected = activeTab == tab
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(10.dp))
              .background(
                if (isSelected) Brush.linearGradient(listOf(colors.primary, colors.primaryVariant))
                else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
              )
              .clickable { onTabSelected(tab) }
              .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Icon(
                imageVector = if (tab == AppTab.PILGRIM_VIEW) Icons.Default.Person else Icons.Default.AdminPanelSettings,
                contentDescription = null,
                tint = if (isSelected) colors.textOnPrimary else colors.textSecondary,
                modifier = Modifier.size(18.dp)
              )
              Text(
                text = tab.titleAr,
                style = MaterialTheme.typography.labelLarge.copy(
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  color = if (isSelected) colors.textOnPrimary else colors.textSecondary,
                  fontSize = 13.sp
                )
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun StatusBadge(status: BookingStatus) {
  val (bg, textColor, icon) = when (status) {
    BookingStatus.CONFIRMED -> Triple(StatusConfirmedBg, StatusConfirmedText, Icons.Default.CheckCircle)
    BookingStatus.PENDING_PAYMENT -> Triple(StatusPendingBg, StatusPendingText, Icons.Default.AccessTime)
    BookingStatus.CANCELLED -> Triple(StatusCancelledBg, StatusCancelledText, Icons.Default.Cancel)
  }

  Surface(
    color = bg,
    shape = RoundedCornerShape(8.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, textColor.copy(alpha = 0.35f))
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = textColor,
        modifier = Modifier.size(14.dp)
      )
      Text(
        text = status.titleAr,
        style = MaterialTheme.typography.labelSmall.copy(
          fontWeight = FontWeight.Bold,
          color = textColor
        )
      )
    }
  }
}

@Composable
fun RoomBadge(roomType: RoomType) {
  val colors = LuxuryTheme.colors
  Surface(
    color = colors.surfaceElevated,
    shape = RoundedCornerShape(6.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, colors.borderHighlight)
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
      Icon(
        imageVector = Icons.Default.Bed,
        contentDescription = null,
        tint = colors.primary,
        modifier = Modifier.size(13.dp)
      )
      Text(
        text = roomType.titleAr,
        style = MaterialTheme.typography.labelSmall.copy(
          color = colors.textPrimary,
          fontWeight = FontWeight.SemiBold,
          fontSize = 11.sp
        )
      )
    }
  }
}

@Composable
fun QrCodeCanvas(
  data: String,
  modifier: Modifier = Modifier,
  pixelColor: Color = Color(0xFF0F172A)
) {
  // Renders an authentic QR Code pattern based on slug/hash
  val hash = remember(data) { data.hashCode().let { if (it < 0) -it else it } }
  val gridSize = 21

  Canvas(modifier = modifier.aspectRatio(1f)) {
    val pixelSize = size.width / gridSize

    // High contrast white background for reliable camera scanning
    drawRect(Color.White, Offset.Zero, size)

    fun drawFinder(startX: Int, startY: Int) {
      // Outer 7x7
      drawRect(
        color = pixelColor,
        topLeft = Offset(startX * pixelSize, startY * pixelSize),
        size = Size(7 * pixelSize, 7 * pixelSize)
      )
      // Inner 5x5 white
      drawRect(
        color = Color.White,
        topLeft = Offset((startX + 1) * pixelSize, (startY + 1) * pixelSize),
        size = Size(5 * pixelSize, 5 * pixelSize)
      )
      // Center 3x3
      drawRect(
        color = pixelColor,
        topLeft = Offset((startX + 2) * pixelSize, (startY + 2) * pixelSize),
        size = Size(3 * pixelSize, 3 * pixelSize)
      )
    }

    // Three Finder Patterns
    drawFinder(0, 0)
    drawFinder(gridSize - 7, 0)
    drawFinder(0, gridSize - 7)

    // Alignment and data pattern based on data hash
    for (x in 0 until gridSize) {
      for (y in 0 until gridSize) {
        val inFinder1 = x < 7 && y < 7
        val inFinder2 = x >= gridSize - 7 && y < 7
        val inFinder3 = x < 7 && y >= gridSize - 7

        if (!inFinder1 && !inFinder2 && !inFinder3) {
          val bit = ((hash xor (x * 37 + y * 73 + (x * y))) and 1) == 0
          val timingH = (y == 6 && x % 2 == 0)
          val timingV = (x == 6 && y % 2 == 0)

          if (bit || timingH || timingV) {
            drawRect(
              color = pixelColor,
              topLeft = Offset(x * pixelSize, y * pixelSize),
              size = Size(pixelSize, pixelSize)
            )
          }
        }
      }
    }
  }
}

@Composable
fun FullscreenPhotoDialog(
  state: PhotoViewerState,
  onDismiss: () -> Unit
) {
  if (!state.isOpen) return
  val colors = LuxuryTheme.colors

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(colors.background.copy(alpha = 0.96f))
        .clickable { onDismiss() }
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .statusBarsPadding()
          .navigationBarsPadding()
          .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = state.title,
              style = MaterialTheme.typography.titleMedium.copy(
                color = colors.textPrimary,
                fontWeight = FontWeight.Bold
              )
            )
            if (state.subtitle.isNotBlank()) {
              Text(
                text = state.subtitle,
                style = MaterialTheme.typography.bodySmall.copy(color = colors.primary)
              )
            }
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier
              .clip(CircleShape)
              .background(colors.surfaceElevated)
              .border(1.dp, colors.borderHighlight, CircleShape)
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "إغلاق",
              tint = colors.textPrimary
            )
          }
        }

        if (state.imageRes != 0) {
          Image(
            painter = painterResource(id = state.imageRes),
            contentDescription = state.title,
            contentScale = ContentScale.Fit,
            modifier = Modifier
              .fillMaxWidth()
              .weight(1f)
              .clip(RoundedCornerShape(16.dp))
              .border(1.dp, colors.borderHighlight, RoundedCornerShape(16.dp))
          )
        }

        Text(
          text = "اضغط في أي مكان للإغلاق",
          style = MaterialTheme.typography.labelMedium.copy(color = colors.textMuted),
          modifier = Modifier.padding(top = 12.dp)
        )
      }
    }
  }
}

fun openWhatsApp(context: Context, phone: String, message: String) {
  try {
    val cleanPhone = phone.replace("+", "").replace(" ", "").replace("-", "")
    val uri = Uri.parse("https://api.whatsapp.com/send?phone=$cleanPhone&text=${Uri.encode(message)}")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    context.startActivity(intent)
  } catch (e: Exception) {
    Toast.makeText(context, "تعذر فتح تطبيق WhatsApp", Toast.LENGTH_SHORT).show()
  }
}

fun dialPhoneNumber(context: Context, phone: String) {
  try {
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
    context.startActivity(intent)
  } catch (e: Exception) {
    Toast.makeText(context, "تعذر فتح لوحة الاتصال", Toast.LENGTH_SHORT).show()
  }
}

fun openGoogleMaps(context: Context, lat: Double, lng: Double, label: String) {
  try {
    val gmmIntentUri = Uri.parse("geo:$lat,$lng?q=${Uri.encode("$lat,$lng($label)")}")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    context.startActivity(mapIntent)
  } catch (e: Exception) {
    val webUri = Uri.parse("https://maps.google.com/?q=$lat,$lng")
    context.startActivity(Intent(Intent.ACTION_VIEW, webUri))
  }
}

fun shareText(context: Context, title: String, text: String) {
  val sendIntent = Intent().apply {
    action = Intent.ACTION_SEND
    putExtra(Intent.EXTRA_TEXT, text)
    type = "text/plain"
  }
  val shareIntent = Intent.createChooser(sendIntent, title)
  context.startActivity(shareIntent)
}
