# Metin2 ile OOP (Nesne Yönelimli Programlama) Ders Notları

Selam! Bu dökümanda Programlamanın 4 büyük temel taşını (Encapsulation, Inheritance, Polymorphism, Abstraction) bir AI gibi değil, herkesin bildiği **Metin2** üzerinden anlatmaya çalıştım. 

Neden Metin2? Çünkü sınıflar, karakterler ve skiller tam olarak bu iş için biçilmiş kaftan.

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
