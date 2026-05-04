package ru.practicum.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.EndpointHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
public class StatsClient {

    private final RestTemplate rest;
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public StatsClient(@Value("${stats.server.url}") String serverUrl,
                       RestTemplateBuilder builder) {
        this.rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .build();
    }

    public void hit(String app, String uri, String ip, LocalDateTime timestamp) {
        EndpointHitDto hitDto = new EndpointHitDto();
        hitDto.setApp(app);
        hitDto.setUri(uri);
        hitDto.setIp(ip);
        hitDto.setTimestamp(timestamp);
        rest.postForEntity("/hit", hitDto, Object.class);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end,
                                           List<String> uris, boolean unique) {
        Map<String, Object> params = Map.of(
                "start", start.format(FORMATTER),
                "end", end.format(FORMATTER),
                "uris", uris != null ? String.join(",", uris) : "",
                "unique", unique
        );
        return rest.getForEntity(
                "/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                Object.class,
                params
        );
    }
}