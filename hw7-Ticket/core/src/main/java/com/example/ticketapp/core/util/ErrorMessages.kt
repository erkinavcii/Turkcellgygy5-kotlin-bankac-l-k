package com.example.ticketapp.core.util

fun Throwable.toUserMessage(): String {
    val msg = this.message ?: ""
    return when {
        msg.contains("email_taken", ignoreCase = true) || (msg.contains("409", ignoreCase = true) && msg.contains("email", ignoreCase = true)) -> {
            "Bu e-posta adresi zaten kayıtlı."
        }
        msg.contains("capacity_exceeded", ignoreCase = true) -> {
            "Stok yetersiz, lütfen sayfayı yenileyip tekrar deneyin."
        }
        msg.contains("already_paid", ignoreCase = true) -> {
            "Bu bilet zaten ödenmiş."
        }
        msg.contains("not_purchase_owner", ignoreCase = true) -> {
            "Bu satın alım size ait değil."
        }
        msg.contains("401", ignoreCase = true) || msg.contains("unauthorized", ignoreCase = true) -> {
            "Oturumunuzun süresi doldu, lütfen tekrar giriş yapın."
        }
        msg.contains("403", ignoreCase = true) -> {
            "Bu işlem için yetkiniz bulunmamaktadır."
        }
        else -> {
            if (msg.contains("Giriş başarısız", ignoreCase = true)) {
                "Giriş başarısız, lütfen e-posta ve şifrenizi kontrol edin."
            } else {
                msg.replace("Exception:", "").trim().ifBlank { "Bilinmeyen bir hata oluştu." }
            }
        }
    }
}
