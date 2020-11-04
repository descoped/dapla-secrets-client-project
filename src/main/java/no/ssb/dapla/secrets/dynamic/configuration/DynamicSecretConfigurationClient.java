package no.ssb.dapla.secrets.dynamic.configuration;

import no.ssb.config.DynamicConfiguration;
import no.ssb.config.StoreBasedDynamicConfiguration;
import no.ssb.dapla.secrets.api.SecretManagerClient;

public class DynamicSecretConfigurationClient implements SecretManagerClient {

    private final DynamicConfiguration configuration;

    public DynamicSecretConfigurationClient(String propertyResourcePath) {
        configuration = new StoreBasedDynamicConfiguration.Builder()
                .propertiesResource(propertyResourcePath)
                .build();
    }

    @Override
    public String readString(String secretName) {
        return readString(secretName, null);
    }

    @Override
    public String readString(String secretName, String secretVersion) {
        return configuration.evaluateToString(secretName);
    }

    @Override
    public byte[] readBytes(String secretName) {
        return readBytes(secretName, null);
    }

    @Override
    public byte[] readBytes(String secretName, String secretVersion) {
        return configuration.evaluateToString(secretName) != null ? configuration.evaluateToString(secretName).getBytes() : null;
    }

    @Override
    public void close() {

    }
}
