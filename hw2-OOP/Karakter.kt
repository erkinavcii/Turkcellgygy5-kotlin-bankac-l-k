/**
 * METIN2 OOP ÖDEVİ - BÖLÜM 1: TEMEL KARAKTER SINIFI
 * 
 * Burada "Abstraction" (Soyutlama) ve "Encapsulation" (Kapsülleme) temellerini atıyoruz.
 */

// Abstraction: Karakter sınıfı 'abstract' yani soyuttur. 
// Oyunda "Karakter" adında bir nesne yoktur, o sadece bir şablondur.
abstract class Karakter(val isim: String, val sinif: String) {

    // Encapsulation: HP (Can) değerini private yapıyoruz. 
    // Dışarıdan birisi karakterin canını kafasına göre -500 yapamasın.
    private var hp: Int = 100
    private var seviye: Int = 1

    // Can değerini güvenli bir şekilde okumak için fonksiyon
    fun hpGoster(): Int = hp

    // Can azaltma işlemi (Zarar alma mantığı)
    fun hasarAl(miktar: Int) {
        hp -= miktar
        if (hp < 0) hp = 0
        println("$isim $miktar hasar aldı! Kalan Can: $hp")
    }

    // Her karakterin bir "Saldırı" yeteneği vardır ama her biri farklı yapar.
    // Bu yüzden bu fonksiyonu 'abstract' yapıyoruz.
    abstract fun duryeSaldir()
    
    // Her karakterin ortak bir selamlama fonksiyonu olabilir.
    fun selamVer() {
        println("Merhaba, ben $isim! ($sinif - Seviye: $seviye)")
    }
}

// ---------------------------------------------------------
// 3. Inheritance (Kalıtım): Savaşçı ve Sura, Karakter'den miras alır.
// ---------------------------------------------------------

class Savasci(isim: String) : Karakter(isim, "Savaşçı") {
    // Polymorphism (Çok Biçimlilik): Savaşçı kendine has saldırır.
    override fun duryeSaldir() {
        println("$isim: Hava Kılıcı yaktı ve kılıcıyla seri vuruşlar yapıyor! ⚔️")
    }
}

class Sura(isim: String) : Karakter(isim, "Sura") {
    // Polymorphism (Çok Biçimlilik): Sura büyüyle saldırır.
    override fun duryeSaldir() {
        println("$isim: Karanlık Koruma bastı ve Ateş Hayaleti ile saldırıyor! 🔥")
    }
}

// ---------------------------------------------------------
// TEST / MAIN FONKSİYONU
// ---------------------------------------------------------

fun main() {
    println("=== METIN2 OOP DÜNYASINA GİRİŞ ===\n")

    val kahraman1 = Savasci("Kahraman1")
    val kahraman2 = Sura("Sura1")

    // Selam verme (Ortak fonksiyon kullanımı)
    kahraman1.selamVer()
    kahraman2.selamVer()

    println("\n--- SAVAŞ BAŞLIYOR! ---")

    // Çok Biçimlilik örneği: Bir liste içine farklı karakterleri koyuyoruz
    val karakterler = listOf(kahraman1, kahraman2)

    for (k in karakterler) {
        // Hepsi duryeSaldir diyor ama hepsi KENDİNE HAS saldırıyor!
        k.duryeSaldir()
    }

    println("\n--- HASAR ALMA VE KAPSÜLLEME ---")
    kahraman1.hasarAl(30)
    println("${kahraman1.isim} adlı karakterin güncel canı: ${kahraman1.hpGoster()}")
}
