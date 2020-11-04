package no.ssb.dapla.secrets.api;

import no.ssb.config.DynamicConfiguration;
import no.ssb.config.StoreBasedDynamicConfiguration;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SecretManagerTest {

    @Test
    void readDynamicSecret() {
        Map<String, String> providerConfiguration = Map.of(
                "secrets.provider", "dynamic-secret-configuration",
                "secrets.propertyResourcePath", "application-secret.properties"
        );

        try (SecretManagerClient client = SecretManagerClient.create(providerConfiguration)) {
            assertEquals("42", client.readString("AN_ANSWER"));
            assertArrayEquals("42".getBytes(), client.readBytes("AN_ANSWER"));
        }
    }

    @Disabled
    @Test
    void readGoogleSecret() {
        DynamicConfiguration configuration = new StoreBasedDynamicConfiguration.Builder()
                .propertiesResource("application-override.properties")
                .build();

        Map<String, String> providerConfiguration = Map.of(
                "secrets.provider", "google-secret-manager",
                "secrets.projectId", "ssb-team-dapla",
                "secrets.serviceAccountKeyPath", getServiceAccountFile(configuration)
        );

        try (SecretManagerClient client = SecretManagerClient.create(providerConfiguration)) {
            assertEquals("42\n", client.readString("AN_ANSWER"));
            assertArrayEquals("42\n".getBytes(), client.readBytes("AN_ANSWER"));
        }
    }

    String getServiceAccountFile(DynamicConfiguration configuration) {
        String path = configuration.evaluateToString("gcp.service-account.file");
        if (!Files.isReadable(Paths.get(path))) {
            throw new RuntimeException("Missing 'application-override.properties'-file with required property 'gcp.service-account.file'");
        }
        return path;
    }

}
