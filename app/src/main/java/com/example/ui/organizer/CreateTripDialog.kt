package com.example.ui.organizer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTripDialog(
  onDismiss: () -> Unit,
  onCreate: (
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
  ) -> Unit
) {
  var title by remember { mutableStateOf("رحلة عمرة إجازة الخريف – الرياض") }
  var slug by remember { mutableStateOf("riyadh-fall2026") }
  var departureDate by remember { mutableStateOf("29 أكتوبر 2026 (الخميس)") }
  var returnDate by remember { mutableStateOf("01 نوفمبر 2026 (الأحد)") }
  var gatheringPoint by remember { mutableStateOf("مخرج 15 - جامع الراجحي بالرياض") }
  var gatheringAddress by remember { mutableStateOf("المواقف الغربية - بوابة 4") }
  var gatheringTime by remember { mutableStateOf("الخميس 01:30 ظهراً") }
  var hotelName by remember { mutableStateOf("فندق موڤنبيك أبراج البيت (5 نجوم)") }
  var hotelDistance by remember { mutableStateOf("مطل على ساحة الحرم مباشرة") }
  var busModel by remember { mutableStateOf("حافلة مرسيدس VIP موديل 2026") }
  var totalSeatsStr by remember { mutableStateOf("48") }

  var singlePriceStr by remember { mutableStateOf("1850") }
  var doublePriceStr by remember { mutableStateOf("1350") }
  var triplePriceStr by remember { mutableStateOf("1100") }
  var quadPriceStr by remember { mutableStateOf("950") }

  var supervisorName by remember { mutableStateOf("الشيخ فهد العتيبي") }
  var supervisorPhone by remember { mutableStateOf("+966501234567") }

  val textFieldColors = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = DarkSurfaceElevated,
    unfocusedContainerColor = DarkSurfaceElevated,
    focusedBorderColor = Amber500,
    unfocusedBorderColor = DarkBorderHighlight,
    focusedTextColor = DarkTextPrimary,
    unfocusedTextColor = DarkTextPrimary
  )

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.94f)
        .fillMaxHeight(0.92f),
      shape = RoundedCornerShape(20.dp),
      color = DarkSurface,
      border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorderHighlight),
      shadowElevation = 8.dp
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(16.dp)
      ) {
        // Title Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(Icons.Default.AddBusiness, contentDescription = null, tint = Amber400)
            Text(
              text = "إنشاء رحلة عمرة جديدة",
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

        Divider(color = DarkBorderHighlight, modifier = Modifier.padding(vertical = 8.dp))

        // Form Fields
        Column(
          modifier = Modifier
            .weight(1f)
            .verticalScroll(rememberScrollState()),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Text(
            text = "1. البيانات الأساسية للرحلة والرابط:",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = Amber400)
          )

          OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("عنوان الرحلة (مثال: رحلة عمرة – 15 سبتمبر 2026)", color = DarkTextMuted) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = textFieldColors,
            singleLine = true
          )

          OutlinedTextField(
            value = slug,
            onValueChange = { slug = it },
            label = { Text("رمز الرابط المختصر (مثال: riyadh-150926)", color = DarkTextMuted) },
            supportingText = { Text("سيصبح الرابط: umrah-app.com/trip/$slug", color = DarkTextMuted) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = textFieldColors,
            singleLine = true
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            OutlinedTextField(
              value = departureDate,
              onValueChange = { departureDate = it },
              label = { Text("تاريخ الذهاب", color = DarkTextMuted) },
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(10.dp),
              colors = textFieldColors,
              singleLine = true
            )
            OutlinedTextField(
              value = returnDate,
              onValueChange = { returnDate = it },
              label = { Text("تاريخ العودة", color = DarkTextMuted) },
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(10.dp),
              colors = textFieldColors,
              singleLine = true
            )
          }

          Text(
            text = "2. الفندق والباص ومكان التجمع:",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = Amber400)
          )

          OutlinedTextField(
            value = hotelName,
            onValueChange = { hotelName = it },
            label = { Text("اسم فندق السكن بمكة المكرمة", color = DarkTextMuted) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = textFieldColors,
            singleLine = true
          )

          OutlinedTextField(
            value = hotelDistance,
            onValueChange = { hotelDistance = it },
            label = { Text("المسافة من ساحة الحرم", color = DarkTextMuted) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = textFieldColors,
            singleLine = true
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            OutlinedTextField(
              value = busModel,
              onValueChange = { busModel = it },
              label = { Text("نوع وموديل الباص", color = DarkTextMuted) },
              modifier = Modifier.weight(2f),
              shape = RoundedCornerShape(10.dp),
              colors = textFieldColors,
              singleLine = true
            )
            OutlinedTextField(
              value = totalSeatsStr,
              onValueChange = { totalSeatsStr = it },
              label = { Text("المقاعد", color = DarkTextMuted) },
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(10.dp),
              colors = textFieldColors,
              singleLine = true
            )
          }

          OutlinedTextField(
            value = gatheringPoint,
            onValueChange = { gatheringPoint = it },
            label = { Text("مكان التجمع بالرياض", color = DarkTextMuted) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = textFieldColors,
            singleLine = true
          )

          OutlinedTextField(
            value = gatheringTime,
            onValueChange = { gatheringTime = it },
            label = { Text("موعد وساعة التجمع", color = DarkTextMuted) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = textFieldColors,
            singleLine = true
          )

          Text(
            text = "3. أسعار الغرف (للفرد بالريال السعودي):",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = Amber400)
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            OutlinedTextField(
              value = singlePriceStr,
              onValueChange = { singlePriceStr = it },
              label = { Text("الفردية", color = DarkTextMuted) },
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(8.dp),
              colors = textFieldColors,
              singleLine = true
            )
            OutlinedTextField(
              value = doublePriceStr,
              onValueChange = { doublePriceStr = it },
              label = { Text("الثنائية", color = DarkTextMuted) },
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(8.dp),
              colors = textFieldColors,
              singleLine = true
            )
            OutlinedTextField(
              value = triplePriceStr,
              onValueChange = { triplePriceStr = it },
              label = { Text("الثلاثية", color = DarkTextMuted) },
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(8.dp),
              colors = textFieldColors,
              singleLine = true
            )
            OutlinedTextField(
              value = quadPriceStr,
              onValueChange = { quadPriceStr = it },
              label = { Text("الرباعية", color = DarkTextMuted) },
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(8.dp),
              colors = textFieldColors,
              singleLine = true
            )
          }

          Text(
            text = "4. مسؤول ومطوف الرحلة:",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = Amber400)
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            OutlinedTextField(
              value = supervisorName,
              onValueChange = { supervisorName = it },
              label = { Text("اسم المشرف", color = DarkTextMuted) },
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(10.dp),
              colors = textFieldColors,
              singleLine = true
            )
            OutlinedTextField(
              value = supervisorPhone,
              onValueChange = { supervisorPhone = it },
              label = { Text("رقم التواصل", color = DarkTextMuted) },
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(10.dp),
              colors = textFieldColors,
              singleLine = true
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Create Button
        Button(
          onClick = {
            onCreate(
              title,
              slug,
              departureDate,
              returnDate,
              gatheringPoint,
              gatheringAddress,
              gatheringTime,
              hotelName,
              hotelDistance,
              busModel,
              totalSeatsStr.toIntOrNull() ?: 48,
              singlePriceStr.toIntOrNull() ?: 1850,
              doublePriceStr.toIntOrNull() ?: 1350,
              triplePriceStr.toIntOrNull() ?: 1100,
              quadPriceStr.toIntOrNull() ?: 950,
              supervisorName,
              supervisorPhone
            )
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = Amber500)
        ) {
          Icon(Icons.Default.Check, contentDescription = null, tint = DarkBg)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "نشر الرحلة وتوليد الرابط ورمز QR",
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.Bold,
              color = DarkBg
            )
          )
        }
      }
    }
  }
}
