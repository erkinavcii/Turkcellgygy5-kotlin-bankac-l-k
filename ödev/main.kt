import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)
    var balance = 1000.0 // Başlangıç bakiyesi
    var pin = "1234"
    var IsRunning = true

    println("--- Turkcell GYGY Bankasına Hoşgeldiniz ---")
    
    // Basit bir güvenlik kontrolü
    print("Lütfen PIN Kodunuzu Girin: ")
    val enteredPin = scanner.next()
    
    if (enteredPin != pin) {
        println("Hatalı PIN! Uygulama kapatılıyor.")
        return
    }

    while (IsRunning) {
        println("\n=== İŞLEM MENÜSÜ (Bakiye: $balance TL) ===")
        println("1. Bakiyeyi Sorgula")
        println("2. Para Yatır")
        println("3. Para Çek")
        println("4. Para Transferi (IBAN)")
        println("5. Döviz Hesapla (USD/EUR)")
        println("6. Kredi Hesapla")
        println("0. Çıkış")
        print("Seçiminiz: ")

        when (scanner.next()) {
            "1" -> println("\n[!] Güncel Bakiyeniz: $balance TL")
            "2" -> {
                print("Yatırılacak Tutar: ")
                val amount = scanner.nextDouble()
                balance += amount
                println("[OK] $amount TL yatırıldı. Yeni bakiye: $balance TL")
            }
            "3" -> {
                print("Çekilecek Tutar: ")
                val amount = scanner.nextDouble()
                if (amount <= balance) {
                    balance -= amount
                    println("[OK] $amount TL çekildi. Kalan bakiye: $balance TL")
                } else println("[HATA] Yetersiz bakiye!")
            }
            "4" -> {
                print("Alıcı IBAN (TR...): ")
                val iban = scanner.next()
                print("Gönderilecek Tutar: ")
                val amount = scanner.nextDouble()
                if (amount <= balance) {
                    balance -= amount
                    println("[OK] $amount TL tutarı $iban adresine gönderildi.")
                } else println("[HATA] Yetersiz bakiye!")
            }
            "5" -> {
                println("1. TL -> USD (Kur: 50)\n2. TL -> EUR (Kur: 40)")
                print("Seçim: ")
                val choice = scanner.next()
                print("TL Tutarı: ")
                val tl = scanner.nextDouble()
                if (choice == "1") println("[!] Sonuç: ${tl / 40} USD")
                else println("[!] Sonuç: ${tl / 50} EUR")
            }
            "6" -> {
                print("Çekilecek Kredi Tutarı: ")
                val loan = scanner.nextDouble()
                print("Vade (Ay): ")
                val months = scanner.nextInt()
                val total = loan * 1.2 // %20 sabit faiz
                println("[!] Toplam Geri Ödeme: $total TL (Aylık: ${total / months} TL)")
            }
            "0" -> {
                println("Bizi tercih ettiğiniz için teşekkürler!")
                IsRunning = false
            }
            else -> println("Geçersiz seçim!")
        }
    }
}