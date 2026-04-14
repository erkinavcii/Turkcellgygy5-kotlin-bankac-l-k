# Metin2 Temalı OOP (Nesne Yönelimli Programlama) Ödevi

Bu ödevde, Nesne Yönelimli Programlamanın (OOP) 4 temel prensibi olan **Encapsulation, Inheritance, Polymorphism ve Abstraction** kavramlarını, popüler MMORPG oyunu **Metin2** karakterleri üzerinden açıkladım. 

Neden Metin2? Çünkü sınıflar, karakter tipleri ve yetenekler bu konseptleri anlamak için en somut örnekleri sunuyor.

---

## 1. Abstraction (Soyutlama) - "Şablon Çıkarmak"

**Soru:** Neden `Karakter` adında bir sınıfımız var?
**Cevap:** Çünkü oyunda tek başına bir "Karakter" nesnesi yok. Ya Savaşçısın, ya Surasın ya da Ninjasın. Karakter sınıfı sadece hepsinde ortak olması gereken (HP, İsim, Seviye gibi) özellikleri barındıran bir **şablondur**.

Kodda `abstract class Karakter` yaparak şunu diyoruz: "Hocam, kimse doğrudan bir Karakter oluşturamasın, bu sadece bir taslak."

## 2. Encapsulation (Kapsülleme) - "Korumaya Almak"

**Soru:** Neden HP (Can) değerini `private` yapıyoruz?
**Cevap:** Diyelim ki karakterin canını dışarıdan bir fonksiyonla değiştirebiliyoruz. Eğer `private` yapmazsak, kötü niyetli biri gelip karakterin canını tek satırda `hp = -500` yapabilir.
Biz canı kapsülleyerek (saklayarak) diyoruz ki: "Canı sadece benim yazdığım `hasarAl` fonksiyonu ile değiştirebilirsin. Böylece canın 0'ın altına düşmesini de ben kontrol ederim."

## 3. Inheritance (Kalıtım) - "Huyunu Suyunu Almak"

**Soru:** Savaşçı ve Sura'nın her ikisi için de HP, İsim ve Seviye kodlarını tekrar mı yazacağız?
**Cevap:** Hayır, bu çok gereksiz olur. `Savasci` ve `Sura` sınıflarını `Karakter` sınıfından türeterek (miras alarak) tüm bu özellikleri otomatik olarak devralmalarını sağlıyoruz. Kodda bunu `class Savasci : Karakter` şeklinde görüyorsun.

## 4. Polymorphism (Çok Biçimlilik) - "Aynı Komut, Farklı Hareket"

**Soru:** Herkese "Saldır!" dendiğinde ne olur?
**Cevap:** İşte sihir burada! Bir listeye hem Savaşçıyı hem Surayı koyup hepsine tek bir döngüde `duryeSaldir()` dersen; Savaşçı kılıcını savururken, Sura ateş topu atar. 
Yani üst sınıf üzerinden (`Karakter` tipinde değişkenlerle) alt sınıfların kendilerine has davranışlarını tetikleyebiliyoruz.

---

### Sonuç olarak;
Bu ödevde gördüğümüz gibi OOP, karmaşık sistemleri (bir oyun dünyası gibi) daha düzenli, güvenli ve esnek bir şekilde kodlamamıza olanak tanıyor. 

*Bu doküman, bir Metin2 oyuncusunun gözünden Nesne Yönelimli Programlamayı anlatmak için hazırlanmıştır.*
