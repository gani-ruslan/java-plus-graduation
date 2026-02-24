package ru.practicum.ewm.stats.client.props;

import java.time.Duration;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * Класс конфигурации для клиента статистики (StatsClient).
 * 
 * Содержит параметры настройки HTTP-клиента для взаимодействия с сервисом статистики,
 * включая базовый URL, таймауты соединения и параметры механизма повторных попыток.
 * 
 * Параметры загружаются из конфигурационных файлов приложения (application.properties/yml)
 * с префиксом "stats" и автоматически валидируются при старте приложения.
 * 
 * Основные группы параметров:
 * 
 * 1. Базовая конфигурация подключения:
 *    - baseUrl - обязательный параметр, указывающий адрес сервиса статистики
 *    - connectTimeout - таймаут установления TCP-соединения
 *    - readTimeout - таймаут ожидания ответа от сервера
 * 
 * 2. Параметры механизма повторных попыток для операции hit:
 *    - hitMaxAttempts - максимальное количество попыток отправки
 *    - hitBackoffMillis - базовая задержка для экспоненциального backoff
 *    - hitBackoffCapMillis - максимальная задержка между попытками
 * 
 * Механизм повторных попыток реализует стратегию exponential backoff с ограничением:
 * - Задержка между попытками рассчитывается как: delay = hitBackoffMillis * 2^(attempt-1)
 * - Задержка не может превышать hitBackoffCapMillis
 * - Общее количество попыток ограничено hitMaxAttempts
 * 
 * Пример конфигурации в application.yml:
 * 
 * stats:
 *   base-url: http://stats-server:9090
 *   connect-timeout: 500ms
 *   read-timeout: 1000ms
 *   hit-max-attempts: 3
 *   hit-backoff-millis: 200
 *   hit-backoff-cap-millis: 2000
 * 
 * Валидация:
 * - baseUrl не может быть пустым или null
 * - hitMaxAttempts должен быть >= 1
 * - hitBackoffMillis и hitBackoffCapMillis должны быть >= 0
 * 
 * Класс является Spring-компонентом и автоматически создаётся при старте приложения.
 * Используется StatsClient для настройки RestTemplate и поведения при отправке запросов.
 * 
 * @see ru.practicum.ewm.stats.client.StatsClient
 */
@Component
@ConfigurationProperties(prefix = "stats")
@Validated
@Getter
@Setter
public class ClientProperties {
    @NotBlank
    private String baseUrl;

    /** Таймаут установления соединения (по умолчанию 500 мс). */
    private Duration connectTimeout = Duration.ofMillis(500);

    /** Таймаут чтения ответа (по умолчанию 1000 мс). */
    private Duration readTimeout = Duration.ofMillis(1000);

    /** Максимум попыток для hit (включая первую), по умолчанию 3. */
    @Min(1)
    private int hitMaxAttempts = 3;

    /** Базовая задержка между повторами в миллисекундах (экспоненциальная), по умолчанию 200 мс. */
    @Min(0)
    private long hitBackoffMillis = 200;

    /** Верхний предел задержки между повторами, чтобы не зависать (по умолчанию 2000 мс). */
    @Min(0)
    private long hitBackoffCapMillis = 2000;
}
