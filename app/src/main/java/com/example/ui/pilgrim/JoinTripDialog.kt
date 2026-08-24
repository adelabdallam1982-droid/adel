package com.example.ui.pilgrim

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.*
import com.example.ui.components.QrCodeCanvas
import com.example.ui.components.openWhatsApp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinTripDialog(
  trip: UmrahTrip,
  selectedRoomType: RoomType,
  selectedPilgrimCount: Int,
  lastCreatedBooking: PilgrimBooking?,
  onDismiss: () -> Unit,
  onSubmit: (name: String, phone: String, nationalId: String, gender: String, notes: String) -> Unit
) {
  val context = LocalContext.current
  val colors = LuxuryTheme.colors
  var fullName by remember { mutableStateOf("") }
  var phone by remember { mutableStateOf("05") }
  var nationalId by remember { mutableStateOf("") }
  var selectedGender by remember { mutableStateOf("ذكر") }
  var notes by remember { mutableStateOf("") }

  var nameError by remember { mutableStateOf(false) }
  var phoneError by remember { mutableStateOf(false) }

  val unitPrice = trip.roomPricing.getPriceFor(selectedRoomType)
  val totalPrice = unitPrice * selectedPilgrimCount

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.94f)
        .fillMaxHeight(0.88f),
      shape = RoundedCornerShape(20.dp),
      color = colors.surface,
      border = androidx.compose.foundation.BorderStroke(1.dp, colors.borderHighlight),
      shadowElevation = 8.dp
    ) {
      if (lastCreatedBooking != null) {
        // Success Screen with Booking Details & WhatsApp Confirmation
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.SpaceBetween
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
          ) {
            Box(
              modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(colors.primary.copy(alpha = 0.15f))
                .border(1.dp, colors.primary.copy(alpha = 0.4f), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = colors.primary,
                modifier = Modifier.size(40.dp)
              )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
              text = "تم استلام طلب انضمامك بنجاح! 🎉",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
              ),
              textAlign = TextAlign.Center
            )
            Text(
              text = "تقبل الله طاعتكم وعمرة مباركة وميسرة بإذن الله",
              style = MaterialTheme.typography.bodySmall.copy(color = colors.textMuted),
              textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Booking Details Card
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(14.dp),
              colors = CardDefaults.cardColors(containerColor = colors.surfaceElevated),
              border = androidx.compose.foundation.BorderStroke(1.dp, colors.borderHighlight)
            ) {
              Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text("رقم الحجز:", style = MaterialTheme.typography.labelSmall, color = colors.textMuted)
                  Text(lastCreatedBooking.id, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = colors.primary)
                }

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text("اسم المعتمر:", style = MaterialTheme.typography.labelSmall, color = colors.textMuted)
                  Text(lastCreatedBooking.pilgrimName, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = colors.textPrimary)
                }

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text("نوع الغرفة:", style = MaterialTheme.typography.labelSmall, color = colors.textMuted)
                  Text("${lastCreatedBooking.roomType.titleAr} (${lastCreatedBooking.pilgrimCount} أفراد)", style = MaterialTheme.typography.labelMedium, color = colors.primary)
                }

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text("الإجمالي المستحق:", style = MaterialTheme.typography.labelSmall, color = colors.textMuted)
                  Text("${lastCreatedBooking.totalPrice} ر.س", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = colors.primary)
                }

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text("حالة الحجز:", style = MaterialTheme.typography.labelSmall, color = colors.textMuted)
                  Text("بانتظار التحويل والتأكيد", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = StatusPendingText)
                }
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // QR Code of Booking
            QrCodeCanvas(
              data = "UMRAH-BOOKING:${lastCreatedBooking.id}:${lastCreatedBooking.phone}",
              pixelColor = Color(0xFF0F172A),
              modifier = Modifier
                .size(110.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, colors.borderHighlight, RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "امسح الكود لتأكيد الحجز عند ركوب الباص",
              style = MaterialTheme.typography.labelSmall.copy(color = colors.textMuted, fontSize = 11.sp)
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Actions
          Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Button(
              onClick = {
                val msg = """
                  السلام عليكم ورحمة الله، تم التسجيل في رحلة العمرة:
                  • اسم المعتمر: ${lastCreatedBooking.pilgrimName}
                  • رقم الحجز: ${lastCreatedBooking.id}
                  • نوع الغرفة: ${lastCreatedBooking.roomType.titleAr} (${lastCreatedBooking.pilgrimCount} أفراد)
                  • المبلغ الإجمالي: ${lastCreatedBooking.totalPrice} ر.س
                  • الرحلة: ${trip.title}
                  أرجو تزويدي بحساب التحويل لتأكيد الحجز.
                """.trimIndent()
                openWhatsApp(context, trip.supervisor.whatsapp, msg)
              },
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen)
            ) {
              Icon(Icons.Default.Chat, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("إرسال تفاصيل الحجز للمشرف عبر WhatsApp", color = Color.White, fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
              onClick = onDismiss,
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.outlinedButtonColors(
                containerColor = colors.surfaceElevated,
                contentColor = colors.textPrimary
              ),
              border = androidx.compose.foundation.BorderStroke(1.dp, colors.borderHighlight)
            ) {
              Text("تم، العودة لصفحة الرحلة", color = colors.textPrimary)
            }
          }
        }
      } else {
        // Registration Form
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
        ) {
          // Top Title & Close
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
                imageVector = Icons.Default.HowToReg,
                contentDescription = null,
                tint = colors.primary,
                modifier = Modifier.size(24.dp)
              )
              Text(
                text = "الانضمام إلى رحلة العمرة",
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

          Divider(color = colors.borderHighlight, modifier = Modifier.padding(vertical = 8.dp))

          // Scrollable Form Body
          Column(
            modifier = Modifier
              .weight(1f)
              .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            // Selected Room Summary Card
            Surface(
              color = colors.surfaceElevated,
              shape = RoundedCornerShape(10.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, colors.borderHighlight),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column {
                  Text(
                    text = "الغرفة المختارة: ${selectedRoomType.titleAr}",
                    style = MaterialTheme.typography.labelMedium.copy(
                      fontWeight = FontWeight.Bold,
                      color = colors.primary
                    )
                  )
                  Text(
                    text = "$unitPrice ر.س × $selectedPilgrimCount أفراد",
                    style = MaterialTheme.typography.bodySmall.copy(color = colors.textSecondary)
                  )
                }

                Text(
                  text = "$totalPrice ر.س",
                  style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = colors.primary
                  )
                )
              }
            }

            // 1. Full Name
            OutlinedTextField(
              value = fullName,
              onValueChange = {
                fullName = it
                nameError = it.isBlank()
              },
              label = { Text("الاسم الرباعي للمعتمر (المسؤول عن الحجز)", color = colors.textMuted) },
              leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = colors.primary) },
              isError = nameError,
              supportingText = if (nameError) { { Text("يرجى إدخال الاسم الرباعي", color = Color(0xFFF87171)) } } else null,
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(10.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colors.surfaceElevated,
                unfocusedContainerColor = colors.surfaceElevated,
                focusedBorderColor = colors.primary,
                unfocusedBorderColor = colors.borderHighlight,
                focusedTextColor = colors.textPrimary,
                unfocusedTextColor = colors.textPrimary
              ),
              singleLine = true
            )

            // 2. Mobile Phone Number
            OutlinedTextField(
              value = phone,
              onValueChange = {
                phone = it
                phoneError = it.length < 10
              },
              label = { Text("رقم الجوال (واتساب)", color = colors.textMuted) },
              leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = colors.primary) },
              isError = phoneError,
              supportingText = if (phoneError) { { Text("يرجى إدخال رقم جوال صحيح مكون من 10 أرقام", color = Color(0xFFF87171)) } } else null,
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(10.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colors.surfaceElevated,
                unfocusedContainerColor = colors.surfaceElevated,
                focusedBorderColor = colors.primary,
                unfocusedBorderColor = colors.borderHighlight,
                focusedTextColor = colors.textPrimary,
                unfocusedTextColor = colors.textPrimary
              ),
              singleLine = true
            )

            // 3. National ID / Iqama
            OutlinedTextField(
              value = nationalId,
              onValueChange = { nationalId = it },
              label = { Text("رقم الهوية الوطنية أو الإقامة (لإصدار التصاريح)", color = colors.textMuted) },
              leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = colors.primary) },
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(10.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colors.surfaceElevated,
                unfocusedContainerColor = colors.surfaceElevated,
                focusedBorderColor = colors.primary,
                unfocusedBorderColor = colors.borderHighlight,
                focusedTextColor = colors.textPrimary,
                unfocusedTextColor = colors.textPrimary
              ),
              singleLine = true
            )

            // 4. Gender Selector
            Column {
              Text(
                text = "الجنس:",
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.SemiBold,
                  color = colors.textPrimary
                )
              )
              Spacer(modifier = Modifier.height(4.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                listOf("ذكر", "أنثى").forEach { gender ->
                  val isSel = selectedGender == gender
                  OutlinedButton(
                    onClick = { selectedGender = gender },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                      containerColor = if (isSel) colors.primary else colors.surfaceElevated
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                      1.dp,
                      if (isSel) colors.primary else colors.borderHighlight
                    )
                  ) {
                    Text(
                      text = gender,
                      fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                      color = if (isSel) colors.textOnPrimary else colors.textPrimary
                    )
                  }
                }
              }
            }

            // 5. Special Notes
            OutlinedTextField(
              value = notes,
              onValueChange = { notes = it },
              label = { Text("ملاحظات خاصة (أعمار المعتمرين، كراسي متحركة، الخ)", color = colors.textMuted) },
              leadingIcon = { Icon(Icons.Default.Notes, contentDescription = null, tint = colors.primary) },
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(10.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colors.surfaceElevated,
                unfocusedContainerColor = colors.surfaceElevated,
                focusedBorderColor = colors.primary,
                unfocusedBorderColor = colors.borderHighlight,
                focusedTextColor = colors.textPrimary,
                unfocusedTextColor = colors.textPrimary
              ),
              minLines = 2
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Submit Button
          Button(
            onClick = {
              if (fullName.isBlank()) {
                nameError = true
                return@Button
              }
              if (phone.length < 10) {
                phoneError = true
                return@Button
              }
              onSubmit(fullName, phone, nationalId, selectedGender, notes)
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
          ) {
            Icon(Icons.Default.Check, contentDescription = null, tint = colors.textOnPrimary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "تأكيد وإرسال طلب الانضمام",
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = colors.textOnPrimary
              )
            )
          }
        }
      }
    }
  }
}
