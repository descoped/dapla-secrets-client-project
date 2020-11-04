package no.ssb.dapla.secrets.dynamic.configuration;

import no.ssb.dapla.secrets.api.SecretManagerClient;
import no.ssb.dapla.secrets.api.SecretManagerClientInitializer;
import no.ssb.service.provider.api.ProviderName;

import java.util.Map;
import java.util.Set;

@ProviderName("dynamic-secret-configuration")
public class DynamicSecretConfigurationClientInitializer implements SecretManagerClientInitializer {

    @Override
    public String providerId() {
        return "dynamic-secret-configuration";
    }

    @Override
    public Set<String> configurationKeys() {
        return Set.of(
                "secrets.propertyResourcePath"
        );
    }

    @Override
    public SecretManagerClient initialize(Map<String, String> configuration) {
        String propertyResourcePath = configuration.get("secrets.propertyResourcePath");
        return new DynamicSecretConfigurationClient(propertyResourcePath);
    }
}
