package ro.unibuc.hello.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    public MetricsConfig(MeterRegistry meterRegistry) {
        meterRegistry.config().commonTags("application", "fakemag");
    }
}