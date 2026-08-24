package com.example.ui.poster

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.data.model.RoomType
import com.example.data.model.UmrahTrip
import com.example.ui.components.QrCodeCanvas
import com.example.ui.components.openWhatsApp
import com.example.ui.components.shareText
import com.example.ui.theme.*

@Composable
fun UmrahPosterStudioDialog(
  trip: UmrahTrip,
  activeThemePreset: LuxuryThemePreset,
  onThemePresetChanged: (LuxuryThemePreset) -> Unit,
  onDismiss: () -> Unit
) {
  val context = LocalContext.current
  val clipboardManager = LocalClipboardManager.current

  // Local preset state to preview instantly in poster studio
  var selectedPreset by remember(activeThemePreset) { mutableStateOf(activeThemePreset) }
  val colors = getLuxuryColors(selectedPreset)

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.96f)
        .fillMaxHeight(0.94f),
      shape = RoundedCornerShape(24.dp),
      color = colors.surface,
      border = BorderStroke(1.5.dp, colors.borderHighlight),
      shadowElevation = 16.dp
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(16.dp)
      ) {
        // Modal Header
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
                .size(36.dp)
                .clip(CircleShape)
                .background(colors.primary.copy(alpha = 0.2f))
                .border(1.dp, colors.primary, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Palette,
                contentDescription = null,
                tint = colors.primary,
                modifier = Modifier.size(20.dp)
              )
            }

            Column {
              Text(
                text = "استوديو وتصميم بوستر العمرة الفاخر",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = colors.textPrimary
                )
              )
              Text(
                text = "اختر من بين 5 لوحات ألوان ملكية تتنفس روعة وجمالاً",
                style = MaterialTheme.typography.bodySmall.copy(
                  color = colors.primary,
                  fontSize = 11.sp
                )
              )
            }
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(colors.surfaceElevated)
              .border(1.dp, colors.borderHighlight, CircleShape)
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "إغلاق",
              tint = colors.textPrimary,
              modifier = Modifier.size(18.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Color Theme Palettes Selector Bar
        Text(
          text = "لوحة الألوان الملكية المخصصة للبوستر والتطبيق:",
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            color = colors.textSecondary
          )
        )
        Spacer(modifier = Modifier.height(6.dp))

        // Theme Pills Row
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          LuxuryThemePreset.entries.forEach { preset ->
            val isSelected = selectedPreset == preset
            val animBorderColor by animateColorAsState(
              targetValue = if (isSelected) preset.previewPrimary else Color.Transparent,
              label = "border"
            )

            Surface(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .border(
                  width = if (isSelected) 2.dp else 1.dp,
                  color = if (isSelected) preset.previewPrimary else colors.borderHighlight,
                  shape = RoundedCornerShape(12.dp)
                )
                .clickable {
                  selectedPreset = preset
                  onThemePresetChanged(preset)
                },
              color = if (isSelected) colors.surfaceElevated else colors.surfaceSubtle,
              shape = RoundedCornerShape(12.dp)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                // Color dots pair
                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                  Box(
                    modifier = Modifier
                      .size(14.dp)
                      .clip(CircleShape)
                      .background(preset.previewPrimary)
                  )
                  Box(
                    modifier = Modifier
                      .size(14.dp)
                      .clip(CircleShape)
                      .background(preset.previewSecondary)
                  )
                }

                Text(
                  text = preset.nameAr,
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) colors.textPrimary else colors.textSecondary,
                    fontSize = 11.sp
                  )
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Divider(color = colors.borderHighlight)
        Spacer(modifier = Modifier.height(10.dp))

        // Poster Canvas Preview (Scrollable Luxury Flyer)
        Column(
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          LuxuryFlyerCard(trip = trip, colors = colors)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Action Buttons Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // WhatsApp Share Button
          Button(
            onClick = {
              val shareMsg = buildLuxuryWhatsAppMessage(trip)
              openWhatsApp(context, trip.supervisor.whatsapp, shareMsg)
            },
            modifier = Modifier
              .weight(1.3f)
              .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen)
          ) {
            Icon(
              imageVector = Icons.Default.Chat,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "نشر عبر WhatsApp",
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp
            )
          }

          // Copy Message Text
          OutlinedButton(
            onClick = {
              val shareMsg = buildLuxuryWhatsAppMessage(trip)
              clipboardManager.setText(AnnotatedString(shareMsg))
              Toast.makeText(context, "تم نسخ نص الإعلان والروابط بالكامل 📋", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
              .weight(1f)
              .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
              containerColor = colors.surfaceElevated,
              contentColor = colors.textPrimary
            ),
            border = BorderStroke(1.dp, colors.borderHighlight)
          ) {
            Icon(
              imageVector = Icons.Default.ContentCopy,
              contentDescription = null,
              tint = colors.primary,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "نسخ الإعلان",
              color = colors.textPrimary,
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }
    }
  }
}

@Composable
fun LuxuryFlyerCard(
  trip: UmrahTrip,
  colors: LuxuryColors,
  modifier: Modifier = Modifier
) {
  // Main Flyer Container styled like a royal Islamic campaign flyer
  Card(
    modifier = modifier
      .fillMaxWidth()
      .wrapContentHeight(),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = colors.surfaceSubtle),
    border = BorderStroke(2.dp, colors.primary.copy(alpha = 0.6f)),
    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // 1. Ornate Top Islamic Arch & Bismillah
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .weight(1f)
            .height(1.dp)
            .background(
              Brush.horizontalGradient(
                listOf(Color.Transparent, colors.primary)
              )
            )
        )
        Text(
          text = " ﷽ ",
          style = MaterialTheme.typography.titleMedium.copy(
            color = colors.primary,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
          ),
          modifier = Modifier.padding(horizontal = 8.dp)
        )
        Box(
          modifier = Modifier
            .weight(1f)
            .height(1.dp)
            .background(
              Brush.horizontalGradient(
                listOf(colors.primary, Color.Transparent)
              )
            )
        )
      }

      Spacer(modifier = Modifier.height(4.dp))

      // Subtitle Pill
      Surface(
        color = colors.primary.copy(alpha = 0.15f),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, colors.primary.copy(alpha = 0.4f))
      ) {
        Text(
          text = "✨ بشرى سارة لضيوف الرحمن • رحلات عمرة VIP 2026 ✨",
          style = MaterialTheme.typography.labelSmall.copy(
            color = colors.primary,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp
          ),
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Campaign Name in Golden Typography
      Text(
        text = "حملة الفتح لرحلات العمرة",
        style = MaterialTheme.typography.headlineSmall.copy(
          fontWeight = FontWeight.Black,
          color = colors.textPrimary,
          textAlign = TextAlign.Center
        )
      )

      Text(
        text = trip.title,
        style = MaterialTheme.typography.titleSmall.copy(
          fontWeight = FontWeight.Bold,
          color = colors.primaryVariant,
          textAlign = TextAlign.Center
        )
      )

      Spacer(modifier = Modifier.height(10.dp))

      // 2. Hero Image Banner with Golden Border & Arch
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(170.dp)
          .clip(RoundedCornerShape(14.dp))
          .border(1.5.dp, colors.primary.copy(alpha = 0.7f), RoundedCornerShape(14.dp))
      ) {
        Image(
          painter = painterResource(id = R.drawable.img_umrah_royal_banner_1787552186824),
          contentDescription = "الكعبة المشرفة والمسجد الحرام",
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )

        // Gradient overlay
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                listOf(
                  Color.Transparent,
                  colors.background.copy(alpha = 0.3f),
                  colors.background.copy(alpha = 0.85f)
                )
              )
            )
        )

        // Date Overlay Badge on image
        Surface(
          color = colors.background.copy(alpha = 0.9f),
          shape = RoundedCornerShape(topStart = 10.dp),
          border = BorderStroke(1.dp, colors.primary.copy(alpha = 0.5f)),
          modifier = Modifier.align(Alignment.BottomEnd)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.CalendarMonth,
              contentDescription = null,
              tint = colors.primary,
              modifier = Modifier.size(14.dp)
            )
            Text(
              text = "${trip.departureDateAr} (4 أيام)",
              style = MaterialTheme.typography.labelSmall.copy(
                color = colors.textPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
              )
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // 3. Hotel & Transport Badges Grid
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // Hotel Info Card
        Card(
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = colors.surfaceElevated),
          border = BorderStroke(1.dp, colors.borderHighlight)
        ) {
          Column(modifier = Modifier.padding(10.dp)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Hotel,
                contentDescription = null,
                tint = colors.primary,
                modifier = Modifier.size(16.dp)
              )
              Text(
                text = "السكن 5 نجوم",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = colors.primary
                )
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = trip.hotelDetails.name,
              style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
              ),
              maxLines = 2,
              overflow = TextOverflow.Ellipsis
            )
            Text(
              text = trip.hotelDetails.distanceFromHaram,
              style = MaterialTheme.typography.labelSmall.copy(
                color = colors.textMuted,
                fontSize = 10.sp
              ),
              maxLines = 1
            )
          }
        }

        // Bus Info Card
        Card(
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = colors.surfaceElevated),
          border = BorderStroke(1.dp, colors.borderHighlight)
        ) {
          Column(modifier = Modifier.padding(10.dp)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = Icons.Default.DirectionsBus,
                contentDescription = null,
                tint = colors.primary,
                modifier = Modifier.size(16.dp)
              )
              Text(
                text = "حافلة فاخرة VIP",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = colors.primary
                )
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = trip.busDetails.modelName,
              style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
              ),
              maxLines = 2,
              overflow = TextOverflow.Ellipsis
            )
            Text(
              text = "مقاعد جلدية مريحة وواي فاي",
              style = MaterialTheme.typography.labelSmall.copy(
                color = colors.textMuted,
                fontSize = 10.sp
              ),
              maxLines = 1
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // 4. Room Pricing Table (Grid of 4 Room Types)
      Text(
        text = "🏷️ باقات الأسعار المعتمدة للفرد:",
        style = MaterialTheme.typography.labelMedium.copy(
          fontWeight = FontWeight.Bold,
          color = colors.textPrimary
        ),
        modifier = Modifier.align(Alignment.Start)
      )
      Spacer(modifier = Modifier.height(6.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        RoomType.entries.forEach { type ->
          val price = trip.roomPricing.getPriceFor(type)
          val isPopular = type == RoomType.DOUBLE

          Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(10.dp),
            color = if (isPopular) colors.primary.copy(alpha = 0.15f) else colors.surfaceElevated,
            border = BorderStroke(
              width = if (isPopular) 1.5.dp else 1.dp,
              color = if (isPopular) colors.primary else colors.borderHighlight
            )
          ) {
            Column(
              modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = type.titleAr.replace("غرفة ", ""),
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.SemiBold,
                  color = colors.textSecondary,
                  fontSize = 10.sp
                )
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "$price",
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.Black,
                  color = if (isPopular) colors.primary else colors.textPrimary,
                  fontSize = 14.sp
                )
              )
              Text(
                text = "ريال",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = colors.textMuted,
                  fontSize = 9.sp
                )
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // 5. Package Inclusions Checklist
      Surface(
        color = colors.surfaceElevated,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, colors.borderHighlight),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(10.dp)) {
          Text(
            text = "🌟 مميزات وخدمات الرحلة المشمولة:",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              color = colors.primary
            )
          )
          Spacer(modifier = Modifier.height(4.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text("✓ نقل VIP مرسيدس حديث", style = MaterialTheme.typography.bodySmall.copy(color = colors.textSecondary, fontSize = 11.sp))
              Text("✓ بوفيه إفطار مفتوح يومياً", style = MaterialTheme.typography.bodySmall.copy(color = colors.textSecondary, fontSize = 11.sp))
            }
            Column(modifier = Modifier.weight(1f)) {
              Text("✓ مرشد ومطوف ديني مرافق", style = MaterialTheme.typography.bodySmall.copy(color = colors.textSecondary, fontSize = 11.sp))
              Text("✓ مياه زمزم وضيافة مستمرة", style = MaterialTheme.typography.bodySmall.copy(color = colors.textSecondary, fontSize = 11.sp))
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // 6. QR Code Section for Direct Registration
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(colors.surfaceElevated)
          .border(1.dp, colors.borderHighlight, RoundedCornerShape(12.dp))
          .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // QR Code Box with White Canvas for instant camera scan
        Box(
          modifier = Modifier
            .size(72.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, colors.primary, RoundedCornerShape(8.dp))
            .padding(2.dp)
        ) {
          QrCodeCanvas(
            data = trip.fullShareUrl,
            pixelColor = Color(0xFF0F172A),
            modifier = Modifier.fillMaxSize()
          )
        }

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "📱 امسح الكود للتسجيل الفوري",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              color = colors.textPrimary
            )
          )
          Text(
            text = "أو عبر الرابط: ${trip.shareSlug}",
            style = MaterialTheme.typography.bodySmall.copy(
              color = colors.primary,
              fontWeight = FontWeight.SemiBold,
              fontSize = 11.sp
            )
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "للحجز والاستفسار: ${trip.supervisor.phone}",
            style = MaterialTheme.typography.labelSmall.copy(
              color = colors.textMuted,
              fontSize = 10.sp
            )
          )
        }
      }
    }
  }
}

fun buildLuxuryWhatsAppMessage(trip: UmrahTrip): String {
  return """
    🕋 *حملة الفتح لرحلات العمرة VIP* 🕋
    ✨ _${trip.title}_ ✨
    
    قال ﷺ: «العُمْرَةُ إِلَى العُمْرَةِ كَفَّارَةٌ لِمَا بَيْنَهُمَا»
    
    📅 *موعد الرحلة:* ${trip.departureDateAr}
    🏨 *السكن:* ${trip.hotelDetails.name} (${trip.hotelDetails.distanceFromHaram})
    🚌 *المواصلات:* ${trip.busDetails.modelName}
    
    🏷️ *الأسعار للشخص الواحد:*
    • الغرفة الرباعية: ${trip.roomPricing.quadPrice} ر.س
    • الغرفة الثلاثية: ${trip.roomPricing.triplePrice} ر.س
    • الغرفة الثنائية: ${trip.roomPricing.doublePrice} ر.س
    • الغرفة الفردية: ${trip.roomPricing.singlePrice} ر.س
    
    🌟 *الخدمات المشمولة:*
    ✓ النقل بحافلات حديثة ومكيفة VIP
    ✓ السكن 5 نجوم وبوفيه إفطار مفتوح
    ✓ مرافقة مرشد ديني ومطوف لأداء المناسك
    ✓ مياه زمزم وضيافة خفيفة طوال الرحلة
    ✓ جولة مزارات ومعالم مكة المكرمة
    
    🔗 *للتسجيل واختيار الغرفة مباشرة عبر الرابط:*
    ${trip.fullShareUrl}
    
    📞 *للتواصل والاستفسار:*
    ${trip.supervisor.name} - ${trip.supervisor.phone}
  """.trimIndent()
}
