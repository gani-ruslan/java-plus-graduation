package ru.practicum.ewm.stats.client.props;

import java.time.Duration;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "stats-client")
@Validated
@Getter
@Setter
public class ClientProperties {
    @NotBlank
    private String baseUrl;

    private Duration connectTimeout = Duration.ofMillis(500);

    private Duration readTimeout = Duration.ofMillis(1000);

    @Min(1)
    private int hitMaxAttempts = 3;

    @Min(0)
    private long hitBackoffMillis = 200;

    @Min(0)
    private long hitBackoffCapMillis = 2000;
}
