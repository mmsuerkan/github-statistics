# GitHub Statistics Service

Bu proje, belirli bir GitHub organizasyonundaki ve deposundaki en iyi 10 katkıda bulunan geliştiricilerin istatistiklerini toplamak için kullanılan bir servisi içermektedir.

## Kurulum

1. **Projeyi Klonlayın:**
    ```bash
    git clone https://github.com/mmsuerkan/github-statistics.git
    cd github-statistics
    ```

2. **Maven ile Derleme:**
    ```bash
    mvn clean install
    ```

3. **Uygulamayı Başlatma:**
    ```bash
    mvn spring-boot:run
    ```

## Kullanım

Uygulama başlatıldıktan sonra, aşağıdaki API'yi kullanarak en iyi 10 katkıda bulunan geliştiricilerin istatistiklerini alabilirsiniz:

```http
GET /getTop10Contributors/{organization}/{repository}

Parametreler:

{organization}: GitHub organizasyon adı örneğin apache
{repository}: GitHub reposu adı örneğin commons-lang
```

# Örnek Kullanım:
GET http://localhost:8080/getTop10Contributors/apache/commons-lang

## Konfigürasyon
Projenin çalışması için gerekli olan GitHub kullanıcı adı, token ve kaynak dosyaların bulunduğu dizin bilgileri, application.properties dosyasında bulunmaktadır.

github.username=mmsuerkan
github.token=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
github.resources.path=src/main/resources/



