package com.example.ui.pilgrim

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.UmrahTrip
import com.example.ui.components.QrCodeCanvas
import com.example.ui.components.shareText
import com.example.ui.theme.*

@Composable
fun ShareTripDialog(
  trip: UmrahTrip,
  onDismiss: () -> Unit
) {
  val context = LocalContext.current
  val clipboardManager = LocalClipboardManager.current
  val colors = LuxuryTheme.colors

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.92f)
        .wrapContentHeight(),
      shape = RoundedCornerShape(20.dp),
      color = colors.surface,
      border = androidx.compose.foundation.BorderStroke(1.dp, colors.borderHighlight),
      shadowElevation = 10.dp
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(
              imageVector = Icons.Default.QrCode2,
              contentDescription = null,
              tint = colors.primary,
              modifier = Modifier.size(24.dp)
            )
            Text(
              text = "رابط ورمز QR للرحلة",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
              )
            )
          }

          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = colors.textMuted)
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Printable Poster Preview Container
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = colors.surfaceElevated),
          border = androidx.compose.foundation.BorderStroke(1.dp, colors.borderHighlight)
        ) {
          Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "📱 امسح الكود للتسجيل في رحلة العمرة",
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
              ),
              textAlign = TextAlign.Center
            )
            Text(
              text = trip.title,
              style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = colors.primary
              ),
              textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            // QR Code Element
            Surface(
              color = Color.White,
              shape = RoundedCornerShape(12.dp),
              shadowElevation = 2.dp,
              modifier = Modifier.padding(4.dp)
            ) {
              QrCodeCanvas(
                data = trip.fullShareUrl,
                pixelColor = Color(0xFF0F172A),
                modifier = Modifier
                  .size(180.dp)
                  .padding(12.dp)
              )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Direct URL Pill
            Surface(
              color = colors.surface,
              shape = RoundedCornerShape(8.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, colors.borderHighlight)
            ) {
              Text(
                text = trip.fullShareUrl,
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = colors.primary,
                  fontSize = 11.sp
                ),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Buttons
        Column(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // Share via WhatsApp / Apps
          Button(
            onClick = {
              val shareMsg = """
                🕋 انضم معنا في: ${trip.title}
                📅 موعد الرحلة: ${trip.departureDateAr}
                🏨 السكن: ${trip.hotelDetails.name} (${trip.hotelDetails.distanceFromHaram})
                🚌 النقل: ${trip.busDetails.modelName}
                
                🔗 اضغط على الرابط التالي للاطلاع على صور الباص والغرف والأسعار والتسجيل المباشر:
                ${trip.fullShareUrl}
              """.trimIndent()
              shareText(context, "مشاركة رحلة العمرة", shareMsg)
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
          ) {
            Icon(Icons.Default.Share, contentDescription = null, tint = colors.textOnPrimary)
            Spacer(modifier = Modifier.width(8.dp))
            Text("نشر ومشاركة الرابط عبر WhatsApp", color = colors.textOnPrimary, fontWeight = FontWeight.Bold)
          }

          // Copy Link Button
          OutlinedButton(
            onClick = {
              clipboardManager.setText(AnnotatedString(trip.fullShareUrl))
              Toast.makeText(context, "تم نسخ رابط الرحلة إلى الحافظة", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
              containerColor = colors.surfaceElevated,
              contentColor = colors.textPrimary
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, colors.borderHighlight)
          ) {
            Icon(Icons.Default.ContentCopy, contentDescription = null, tint = colors.primary)
            Spacer(modifier = Modifier.width(8.dp))
            Text("نسخ الرابط المباشر", color = colors.textPrimary)
          }
        }
      }
    }
  }
}
