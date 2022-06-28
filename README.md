# GastroPay-SDK-Android

[![MavenCentral](https://img.shields.io/maven-central/v/tr.com.inventiv/gastropaysdk)](https://search.maven.org/artifact/tr.com.inventiv/gastropaysdk)
[![API](https://img.shields.io/badge/api-21%2B-brightgreen)](https://developer.android.com/studio/releases/platforms#5.0)
[![AndroidX](https://img.shields.io/badge/androidX-✓-blueviolet)](#)
[![GitHub license](https://img.shields.io/github/license/multinetinventiv/GastroPay-SDK-Android)](https://github.com/multinetinventiv/GastroPay-SDK-Android/blob/main/LICENSE)

Resmi GastroPay Sdk kütüphanesidir. GastroPay sistemi üzerinden ödeme almak ve gastropay ile anlaşmalı
restoranları listelemek için kullanılır.

## Sistem Gereksinimleri

Entegre edecek uygulamada;

- Minimum SDK versyionu 21 ve üzeri olmalı

- AndroidX desteği olmalı

## Gradle Entegrasyonu

**build.gradle(:app)** altına aşağıdaki dependency eklenmeli

```Groovy
dependencies {
    implementation "tr.com.inventiv:gastropaysdk:$latest_version"
}
```

Eğer projede ekli değilse **build.gradle(:project)** altına **mavenCentral** reposunun eklenmesi gerekir.

```Groovy
buildscript {
    repositories {
        google()
        mavenCentral()
                ..
    }
}
        ..
        allprojects {
            repositories {
                google()
                mavenCentral()
                        ..
            }
        }
```

## Doküman

Entegrasyon için gerekli servisler aşağıda belirtildiği şekildedir.

### Init Servisi

Uygulama ilk ayağa kalktığı anda **GastroPaySdk**‘nın initialize edilmesi gerekir.

> Initialize işlemi yapılmadan sdk'nin diğer servisleri çalışmaz.

Bu işlem için en ideal yöntem `Application` sınıfından türetilmiş bir sınıf
içerisindeki `onCreate()`metodunun altında sdk **init** fonksiyonunun çağırılmasıdır.

```kotlin
class SdkSampleApp : Application() {
    override fun onCreate() {
        super.onCreate()

        try {
            GastroPaySdk.init(
                application = this,
                environment = Environment.TEST,
                obfuscationKey = "obfuscationKey",
                language = Language.TR,
                logging = true,
                listener = object : GastroPaySdkListener {
                    override fun onInitialized(isInitialized: Boolean) {
                        Log.d("isInitialized", isInitialized.toString())
                    }

                    override fun onAuthTokenReceived(authToken: String) {
                        Log.d("onAuthTokenReceived", authToken)
                    }

                    override fun onPaymentSuccess() {
                        Log.d("GastroPaySdk", "onPaymentSuccess")
                    }

                    override fun onSDKClosed() {
                        Log.d("onSDKClosed", "sdk closed")
                    }
                })
        } catch (e: GastroPaySdkException) {
            // Sdk couldn't be loaded successfully, check private key if its correct 
        }
    }
}
```

Metodun değişkenleri:

- `application`: applicationContext değeri

- `obfuscationKey`: değeri implementasyon aşamasında tarafınıza özel olarak iletilecektir. Bu değer
  sdk'nın çalışması için doğru olarak girilmek zorundadır.

- `environment`: **DEV**, **TEST**, **PILOT** ve **PRODUCTION** olmak üzere 4 çeşit ortam vardır.
  Sdk’nın hangi ortamda çalışması isteniliyorsa, o ortamın buradan ayarlanması gerekir.

- `language`: SDK’nın **TR** ve **EN** olmak üzere 2 farklı dil desteği vardır. Dil seçeneği `init`
  metodunda ayarlanabildiği gibi, ayrı bir metod ile de tanımlanabilir. Bu parametre *opsiyonel* olup
  değer girilmediğinde sdk cihazın diline uygun çalışacaktır. (İngilizce dil desteği henüz aktif değildir.)

- `logging` : sdk çalışma loglarını açıp kapamayı sağlayan değerdir. Herhangi bir değer girilmezse
  **DEBUG** modda loglar aktif olarak ayarlanacaktır.

- `listener`: SDK ile alakalı işlemler hakkında geri bildirim almak için kullanılacak değerdir.
  İstenildiği takdirde burada set edilebileceği gibi ayrı bir metod ile uygulamanın farklı
  yerlerinde de set edilebilir. Uygulama sadece son set edilen callback üzerinden dönüş alabilecektir.

### Set Language Servisi

Uygulama içerisinde dil değişikliği yapılması durumunda sdk’nın da dilinin güncellenmesi gerekir.
Bunun için `setLanguage()` metodu kullanılmalıdır.

Language parametresi için **TR** ve **EN** değerleri mevcuttur.

```kotlin
GastroPaySdk.setLanguage(Language.EN)
```

### Set Global GastroPay SDK Listener Servisi

Uygulama içerisinde SDK’dan geri dönüş almak istenilen yerde bu değer set edilip callback’leri
üzerinden sdk ile ilgili dönüşler alınabilir. Uygulama 4 durum ile ilgili bilgilendirilir :

- **onInitialized** : SDK başarılı bir şekilde initialize edildikten sonra tetiklenir.

- **onPaymentSuccess** : Kullanıcı SDK üzerinden başarılı bir ödeme işlemi gerçekleştirdikten sonra
  tetiklenir.

- **onSDKClosed** : Kullanıcı SDK’yı kapattığı zaman (genel ekranlardaki çarpı butonu ile sdk’yı
  sonlandırma durumu) tetiklenir.

- **onAuthTokenReceived**: Bu durum henüz netleşmiş değildir, son hali ile doküman güncellenecektir.

```kotlin
GastroPaySdk.setGlobalGastroPaySdkListener(object : GastroPaySdkListener {
    override fun onInitialized(isInitialized: Boolean) {
        Log.d("isInitialized", isInitialized.toString())
    }

    override fun onAuthTokenReceived(authToken: String) {
        Log.d("onAuthTokenReceived", authToken)
    }

    override fun onPaymentSuccess() {
        Log.d("GastroPaySdk", "onPaymentSuccess")
    }

    override fun onSDKClosed() {
        Log.d("onSDKClosed", "sdk closed")
    }
})
```

### Start Servisi

Uygulama üzerinden sdk’nın başlatılması ve GastroPay ile ödeme yapma işleminin gerçekleşmesi için
SDK’nın `start` metodu kullanılmalıdır.

Bu metod ile kullanıcı sdk’nın ekranlarına yönlendirilir. Kullanıcı, sdk ekranları üzerinden
GastroPay hesabına giriş yapabilir ya da yeni hesap oluşturabilir.

Kullanıcı giriş yaptıktan sonra sdk’nın ana ekranına yönlendirilir. Buradan ödeme yapma,
restoranları listeleme-detaylarını görme, ödeme geçmişini görme aksiyonlarını alabilir.

Start metodunun çağrısı ise aşağıdaki örnekteki gibidir :

```kotlin
GastroPaySdk.start(
    context = this,
    authToken = null
)
```

Metodun değişkenleri:

- `context` : değeri olarak activity context değeri gönderilmelidir.

- `authToken` : değeri ise daha önceden giriş yapmış bir kullanıcının login ekranını görmeden sdk’yı
  açabilmesi için kullanılan opsiyonel alandır. Geçerli bir token değeri girilirse kullanıcı direk
  anasayfaya yönlendirilir, aksi takdirde login ekranı üzerinden giriş yapması gerekir. 
