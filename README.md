# Reactive Notes API

## Proje Amacı
* Quarkus framework'ünü ve Reactive PostgreSQL Client API'sini kullanarak basit bir
CRUD (Create, Read, Update, Delete) REST servisi geliştirildi.
* Servis bir Note nesnesini yönetiyor ve bu nesne içinde metadata adlı bir alan JSONB formatında saklanıyor.

## Teknoloji Yığını

* Java 17
* Quarkus 3.8.1
* PostgreSQL
* REST (JAX-RS)
* quarkus-reactive-pg-client
* io.vertx.core.json.JsonObject
* quarkus-junit5
* rest-assured

## Gereksinimler

* PostgreSQL veritabanı kurulu olmalı
* notesdb veritabanı oluşturulmalı
* notes tablosu oluşturulmalı
* application.properties'deki veritabanı bağlantı bilgileri doğru olmalı

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

## Reactive Programlama Yaklaşımı

Proje, reactive programlama paradigmasını kullanarak geliştirilmiştir:

### Özellikler
- Non-blocking işlemler ile yüksek performans
- Asenkron veri işleme
- Verimli kaynak kullanımı
- Yüksek ölçeklenebilirlik

### Örnek Non-blocking Metot
```java
public Uni<Note> findById(UUID id) {
    return client.preparedQuery("SELECT * FROM notes WHERE id = $1")
                .execute(Tuple.of(id))
                .onItem().transform(rows -> fromRow(rows.iterator().next()));
}
```

### Faydaları
1. **Yüksek Performans**
   - İşlemler bloklanmadan devam eder
   - Sistem kaynakları daha verimli kullanılır
   - Daha fazla eşzamanlı istek işlenebilir

2. **Ölçeklenebilirlik**
   - Daha az thread kullanımı
   - Daha az bellek kullanımı
   - Daha iyi kaynak yönetimi

## Geliştirme Adımları

### 1. Quarkus Projesi Oluşturma

`pom.xml` dosyasına aşağıdaki bağımlılıklar eklendi:

```xml
<dependencies>
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-resteasy-reactive</artifactId>
    </dependency>
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-reactive-pg-client</artifactId>
    </dependency>
    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-resteasy-reactive-jackson</artifactId>
    </dependency>
</dependencies>
```

### 2. Note Model Sınıfı

```java
public class Note {
    private UUID id;
    private String title;
    private Map<String, Object> metadata;
    // Getter ve Setter metodları
}
```

### 3. Repository Katmanı

```java
@ApplicationScoped
public class NoteRepository {
    public Uni<Void> create(Note note);
    public Uni<Note> findById(UUID id);
    public Uni<List<Note>> findAll();
    public Uni<Void> deleteById(UUID id);
}
```

### 4. REST Endpoint'leri

```java
@Path("/notes")
public class NoteResource {
    @POST
    public Uni<Response> create(Note note); //Yeni bir not oluştur.

    @GET
    @Path("/{id}")
    public Uni<Response> getById(@PathParam("id") UUID id); //id ile not getir.

    @GET
    public Uni<List<Note>> getAll(); //Tüm notları getir.

    @DELETE
    @Path("/{id}")
    public Uni<Response> delete(@PathParam("id") UUID id); //Notu sil.
}
```

### 5. Veritabanı Yapılandırması

```properties
quarkus.datasource.db-kind=postgresql
quarkus.datasource.reactive.url=postgresql://localhost:5432/notesdb
quarkus.datasource.username=postgres
quarkus.datasource.password=postgres
```

## API Endpoint'leri

| Metod  | Endpoint      | Açıklama           |
|--------|--------------|-------------------|
| POST   | `/notes`     | Yeni not oluştur  |
| GET    | `/notes/{id}`| ID ile not getir  |
| GET    | `/notes`     | Tüm notları listele|
| DELETE | `/notes/{id}`| Notu sil          |

## Veri Modeli: Note

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

## Test Senaryoları

### 1. Not Oluşturma ve Getirme Testi (`testCreateAndGetNote`)
- POST isteği ile yeni not oluşturma
- 201 Created durum kodu kontrolü
- GET isteği ile notu getirme
- Not içeriğinin doğruluğunu kontrol etme

### 2. Not Silme Testi (`testDeleteNote`)
- Var olan notu silme
- 204 No Content durum kodu kontrolü
- Var olmayan notu silmeye çalışma
- 404 Not Found durum kodu kontrolü

### 3. Tüm Notları Getirme Testi (`testGetAllNotes`)
- Birden fazla not oluşturma
- Tüm notları getirme
- Not sayısı ve içeriklerinin doğruluğunu kontrol etme

## Uygulamayı Çalıştırma

1. **Geliştirme Modu**:
   ```bash
   ./mvnw quarkus:dev
   ```

2. **Testleri Çalıştırma**:
   ```bash
   ./mvnw test
   ```

## Geliştirilebilecek Noktalar

1. **Validation**
   - Giriş verilerinin doğrulanması
   - Hata mesajlarının iyileştirilmesi

2. **Error Handling**
   - Özel hata sınıfları
   - Global exception handling

3. **Pagination**
   - Sayfalama desteği
   - Sıralama ve filtreleme

4. **Dokümantasyon**
   - Swagger/OpenAPI entegrasyonu
   - API dokümantasyonu

5. **Güvenlik**
   - Authentication
   - Authorization
   - Rate limiting

## Sorun Giderme

1. **Maven Wrapper Sorunları**:
   - Eğer `mvnw` komutu bulunamıyorsa veya çalışmıyorsa:
     ```bash
     mvn wrapper:wrapper
     ```

2. **İzin Sorunları** (macOS/Linux):
   - Eğer `mvnw` için izin hatası alıyorsanız:
     ```bash
     chmod +x mvnw
     ```

