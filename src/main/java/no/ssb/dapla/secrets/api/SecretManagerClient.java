package no.ssb.dapla.secrets.api;

import no.ssb.service.provider.api.ProviderConfigurator;

import java.util.Map;

/**
 * SecretManagerClient is a provider based API for reading secrets.
 */
public interface SecretManagerClient extends AutoCloseable {

    /**
     * Read secret
     *
     * @param secretName
     * @return secret value as string
     */
    String readString(String secretName);

    /**
     * Read secret for a specific version
     *
     * @param secretName
     * @param secretVersion
     * @return secret value as string
     */
    String readString(String secretName, String secretVersion);

    /**
     * Read secret
     *
     * @param secretName
     * @return secret value as byte-array
     */
    byte[] readBytes(String secretName);

    /**
     * Read secret for a specific version
     *
     * @param secretName
     * @param secretVersion
     * @return secret value as byte-array
     */
    byte[] readBytes(String secretName, String secretVersion);

    /**
     * Close provider
     */
    @Override
    void close();

    /**
     * Create SecretManagerClient for a 'secrets.provider'
     *
     * @param configuration Each provider requires their own properties
     * @return SecretManagerClient instance
     */
    static SecretManagerClient create(Map<String, String> configuration) {
        return ProviderConfigurator.configure(configuration, configuration.get("secrets.provider"), SecretManagerClientInitializer.class);
    }

}
