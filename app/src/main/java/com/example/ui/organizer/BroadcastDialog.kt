package com.example.ui.organizer

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.UmrahTrip
import com.example.ui.components.shareText
import com.example.ui.theme.*

@Composable
fun BroadcastDialog(
  trip: UmrahTrip,
  registeredCount: Int,
  onDismiss: () -> Unit
) {
  val context = LocalContext.current
  val clipboardManager = LocalClipboardManager.current

  val templates = listOf(
    "تذكير بموعد ومكان التجمع" to """
      السلام عليكم ورحمة الله ضيوف الرحمن معتمري ${trip.title}،
      نود تذكيركم بموعد التجمع:
      📍 المكان: ${trip.gatheringLocation.pointName} (${trip.gatheringLocation.addressDetails})
      🕐 الموعد: ${trip.gatheringLocation.timeAr}
      🚌 انطلاق الحافلة: ${trip.gatheringLocation.departureTimeAr} تماماً.
      يرجى الحضور في الموعد المحدد والالتزام ببطاقات الحقائب.
      رافقتكم السلامة.
    """.trimIndent(),
    "تذكير بلباس الإحرام والتصاريح" to """
      ضيوف الرحمن الكرام في ${trip.title}،
      نذكركم بالتأكد من:
      1. إحضار ملابس الإحرام والتأكد من شحن الأجهزة.
      2. إحضار بطاقة الهوية الوطنية أو الإقامة الأصلية.
      3. وجود تصريح العمرة عبر تطبيق نسك.
      تقبل الله منا ومنكم صالح الأعمال.
    """.trimIndent(),
    "إشعار الوصول والتسكين بالفندق" to """
      الحمد لله على سلامتكم ضيوف الرحمن في ${trip.title}،
      وصلنا بحمد الله إلى ${trip.hotelDetails.name}.
      يرجى من جميع الإخوة والأخوات التوجه للاستقبال لاستلام بطاقات الغرف، والتجمع بعد ساعة في بهو الفندق للانطلاق لأداء مناسك العمرة جماعة مع المطوف.
    """.trimIndent()
  )

  var selectedTemplateIndex by remember { mutableStateOf(0) }
  var messageText by remember { mutableStateOf(templates[0].second) }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.92f)
        .wrapContentHeight(),
      shape = RoundedCornerShape(20.dp),
      color = DarkSurface,
      border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderHighlight),
      shadowElevation = 8.dp
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
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
            Icon(Icons.Default.Campaign, contentDescription = null, tint = Amber400)
            Text(
              text = "إرسال تعميم / إشعار للمعتمرين",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = DarkTextPrimary
              )
            )
          }

          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = DarkTextMuted)
          }
        }

        Surface(
          color = DarkSurfaceElevated,
          shape = RoundedCornerShape(8.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderHighlight),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = "سيتم توجيه هذه الرسالة إلى $registeredCount معتمر مسجل في ${trip.title}",
            style = MaterialTheme.typography.bodySmall.copy(
              color = Amber400,
              fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.padding(10.dp)
          )
        }

        // Templates selection
        Text(
          text = "نماذج رسائل جاهزة:",
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = DarkTextMuted)
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          templates.forEachIndexed { idx, item ->
            val isSel = selectedTemplateIndex == idx
            OutlinedButton(
              onClick = {
                selectedTemplateIndex = idx
                messageText = item.second
              },
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(8.dp),
              colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (isSel) Amber500 else DarkSurfaceElevated
              ),
              border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isSel) Amber500 else DarkBorderHighlight
              ),
              contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
            ) {
              Text(
                text = item.first.take(12) + "..",
                fontSize = 10.sp,
                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                color = if (isSel) DarkBg else DarkTextPrimary,
                maxLines = 1
              )
            }
          }
        }

        OutlinedTextField(
          value = messageText,
          onValueChange = { messageText = it },
          label = { Text("نص الرسالة أو التعميم", color = DarkTextMuted) },
          modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
          shape = RoundedCornerShape(10.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = DarkSurfaceElevated,
            unfocusedContainerColor = DarkSurfaceElevated,
            focusedBorderColor = Amber500,
            unfocusedBorderColor = DarkBorderHighlight,
            focusedTextColor = DarkTextPrimary,
            unfocusedTextColor = DarkTextPrimary
          )
        )

        // Actions
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Button(
            onClick = {
              shareText(context, "تعميم لرحلة العمرة", messageText)
            },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Amber500)
          ) {
            Icon(Icons.Default.Share, contentDescription = null, tint = DarkBg, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("مشاركة وإرسال", color = DarkBg, fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }

          OutlinedButton(
            onClick = {
              clipboardManager.setText(AnnotatedString(messageText))
              Toast.makeText(context, "تم نسخ نص التعميم", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(
              containerColor = DarkSurfaceElevated,
              contentColor = DarkTextPrimary
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderHighlight)
          ) {
            Icon(Icons.Default.ContentCopy, contentDescription = null, tint = Amber400, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("نسخ النص", fontSize = 12.sp, color = DarkTextPrimary)
          }
        }
      }
    }
  }
}
