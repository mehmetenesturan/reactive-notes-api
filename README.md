# Reactive Notes API

Quarkus ve Reactive PostgreSQL kullanarak metadata içeren notları yöneten basit bir REST API.

## Teknoloji Altyapısı

* Java 17
* Quarkus 3.8.1
* PostgreSQL
* Reactive Programming

## Ön Gereksinimler

1. **Java 17 Kurulumu**
   - [Oracle](https://www.oracle.com/java/technologies/downloads/#java17) veya [OpenJDK](https://jdk.java.net/17/)'dan Java 17'yi indirin ve kurun
   - Kurulumu doğrulayın:
     ```bash
     java -version
     ```

2. **PostgreSQL Kurulumu**
   - **Windows**:
     - [PostgreSQL İndirme Sayfası](https://www.postgresql.org/download/windows/)'ndan indirin ve kurun
     - Varsayılan port: 5432
     - Varsayılan kullanıcı adı: postgres
     - Kurulum sırasında şifre belirleyin
   
   - **macOS**:
     ```bash
     brew install postgresql@14
     brew services start postgresql@14
     ```
   
   - **Linux (Ubuntu/Debian)**:
     ```bash
     sudo apt update
     sudo apt install postgresql postgresql-contrib
     sudo systemctl start postgresql
     sudo systemctl enable postgresql
     ```

## Veritabanı Kurulumu

Veritabanını iki şekilde kurabilirsiniz:

1. **notes.sql Kullanarak**:
   ```bash
   # PostgreSQL'e bağlanın
   psql -U postgres
   
   # SQL scriptini çalıştırın
   \i notes.sql
   ```

2. **Manuel Kurulum**:
   ```sql
   -- PostgreSQL'e bağlanın ve bu komutları çalıştırın:
   CREATE DATABASE notesdb;
   
   \c notesdb;
   
   CREATE TABLE IF NOT EXISTS notes (
       id UUID PRIMARY KEY,
       title TEXT NOT NULL,
       metadata JSONB
   );
   ```

## Proje Kurulumu

1. **Repository'yi Klonlayın**:
   ```bash
   git clone <repository-url>
   cd reactive-notes-api
   ```

2. **Maven Wrapper Kurulumu** (eğer mvnw çalışmıyorsa):
   ```bash
   mvn wrapper:wrapper
   ```

3. **Maven Wrapper İzinlerini Ayarlayın** (macOS/Linux):
   ```bash
   chmod +x mvnw
   ```

4. **Veritabanı Bağlantı Ayarları**:
   - `src/main/resources/application.properties` dosyasını açın
   - Gerekirse aşağıdaki özellikleri güncelleyin:
     ```properties
     quarkus.datasource.db-kind=postgresql
     quarkus.datasource.reactive.url=postgresql://localhost:5432/notesdb
     quarkus.datasource.username=postgres
     quarkus.datasource.password=your_password
     ```

## Uygulamayı Çalıştırma

1. **Geliştirme Modu**:
   ```bash
   # Windows
   .\mvnw quarkus:dev
   
   # macOS/Linux
   ./mvnw quarkus:dev
   ```

2. **Testleri Çalıştırma**:
   ```bash
   # Windows
   .\mvnw test
   
   # macOS/Linux
   ./mvnw test
   ```

## API Uç Noktaları

| Metod  | Uç Nokta      | Açıklama           |
|--------|--------------|-------------------|
| POST   | `/notes`     | Yeni not oluştur  |
| GET    | `/notes/{id}`| ID ile not getir  |
| GET    | `/notes`     | Tüm notları listele|
| DELETE | `/notes/{id}`| Notu sil          |

## Örnek Not Yapısı

```json
{
  "id": "UUID",
  "title": "string",
  "metadata": {
    "tags": ["work", "urgent"],
    "color": "yellow"
  }
}
```

## Sorun Giderme

1. **Maven Wrapper Sorunları**:
   - Eğer `mvnw` komutu bulunamıyorsa veya çalışmıyorsa:
     ```bash
     mvn wrapper:wrapper
     ```

2. **Veritabanı Bağlantı Sorunları**:
   - PostgreSQL'in çalıştığından emin olun
   - `application.properties` dosyasındaki veritabanı bilgilerini kontrol edin
   - Veritabanı ve tablonun oluşturulduğundan emin olun

3. **İzin Sorunları** (macOS/Linux):
   - Eğer `mvnw` için izin hatası alıyorsanız:
     ```bash
     chmod +x mvnw
     ```

4. **Port Çakışmaları**:
   - Varsayılan port 8081'dir
   - Port kullanımdaysa, `application.properties` dosyasında değiştirin:
     ```properties
     quarkus.http.port=8082
     ```
