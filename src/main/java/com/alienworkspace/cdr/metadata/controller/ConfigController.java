package com.alienworkspace.cdr.metadata.controller;

import static com.alienworkspace.cdr.metadata.helpers.Constants.CONFIG_BASE_URL;

import com.alienworkspace.cdr.metadata.config.AppConfig;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * MetadataController is a REST controller that handles requests related to metadata.
 * It is responsible for exposing endpoints under the base URL "/api/metadata".
 *
 * <p>
 * This class defines entry points to interact with metadata operations.
 */
@Tag(name = "Metadata", description = "Metadata Operations")
@RestController
@RequestMapping(CONFIG_BASE_URL)
public class ConfigController {

    private static final Logger logger = LoggerFactory.getLogger(ConfigController.class);

    private final AppConfig appConfig;

    /**
     * Constructor for MetadataController.
     *
     * @param appConfig The application configuration.
     */
    public ConfigController(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    /**
     * Retrieves the application configuration.
     *
     * @return A ResponseEntity containing the application configuration.
     */
    @GetMapping()
    @Retry(name = "getAppConfig", fallbackMethod = "getAppConfigFallback")
    public ResponseEntity<AppConfig> getAppConfig() {
        logger.info("Getting app config");
        return ResponseEntity.ok(new AppConfig(appConfig.getContactDetails(),
                appConfig.getDescription(), appConfig.getVersion(), appConfig.getWorkDays(), appConfig.getEmail()));
    }

    /**
     * Fallback method for the getAppConfig method.
     *
     * @param throwable The exception that occurred.
     * @return A ResponseEntity containing the application configuration.
     */
    public ResponseEntity<AppConfig> getAppConfigFallback(Throwable throwable) {
        logger.error("Error getting config", throwable);
        return ResponseEntity.ok(null);
    }
}
