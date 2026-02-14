package net.engineeringdigest.journalApp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.api.response.WeatherResponse;
import net.engineeringdigest.journalApp.cache.AppCache;
import net.engineeringdigest.journalApp.constants.Placeholders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class WeatherService {

    @Value("${weather.api.key}")
    private String API_KEY;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisService redisService;

    @Autowired
    private AppCache appCache;

    public WeatherResponse getWeather(String city) {
        String redisKey = "weather_of_" + city;

        try {
            // =========================
            // 1️⃣ GET FROM REDIS
            // =========================
            WeatherResponse cachedWeather = redisService.get(redisKey, WeatherResponse.class);

            if (cachedWeather != null) {
                log.info("✅ Weather fetched from Redis for city: {}", city);
                return cachedWeather;
            }

            log.info("⚠️ No data in Redis for city: {}, calling API...", city);

            // =========================
            // 2️⃣ BUILD API URL
            // =========================
            String apiTemplate = appCache.appCache.get(AppCache.key.WEATHER_API.toString());

            if (apiTemplate == null) {
                log.error("❌ WEATHER_API URL not found in AppCache");
                return null;
            }

            String finalAPI = apiTemplate
                    .replace(Placeholders.CITY, city)
                    .replace(Placeholders.API_KEY, API_KEY);

            log.info("🌍 Calling Weather API: {}", finalAPI);

            // =========================
            // 3️⃣ CALL WEATHER API
            // =========================
            ResponseEntity<WeatherResponse> response =
                    restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);

            WeatherResponse body = response.getBody();

            if (body == null) {
                log.error("❌ Weather API returned NULL body for city: {}", city);
                return null;
            }

            log.info("🌦️ Weather API success for city: {}", city);

            // =========================
            // 4️⃣ SAVE TO REDIS CLOUD
            // =========================
            redisService.set(redisKey, body, 300L);

            log.info("💾 Weather saved in Redis for city: {} with TTL 5 min", city);

            return body;

        } catch (JsonProcessingException e) {
            log.error("❌ JSON Processing error while saving to Redis", e);
        } catch (Exception e) {
            log.error("❌ General error in getWeather()", e);
        }

        return null;
    }
}
