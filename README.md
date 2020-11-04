# Dapla Secret Manager Client

`SecretManagerClient` is a provider based API for reading secrets.

Supported providers are:

* Dynamic Secret Provider (`DynamicSecretConfigurationClient`)
* Google Secret Manager Provider (`GoogleSecretManagerClient`)

The `GoogleSecretManagerClient` supports `service-account` and `compute-engine` authentication.

Usage:

1) load configuration with property 'gcp.service-account.file'
```
DynamicConfiguration configuration = new StoreBasedDynamicConfiguration.Builder()
        .propertiesResource("application-override.properties")
        .build();
```

2) google secret manager specific provider configuration
```
Map<String, String> providerConfiguration = Map.of(
        "secrets.provider", "google-secret-manager",
        "secrets.projectId", "ssb-team-dapla",
        "secrets.serviceAccountKeyPath", configuration.evaluateToString("gcp.service-account.file")
);
```

3) create client and read secret
```
try (SecretManagerClient client = SecretManagerClient.create(providerConfiguration)) {
    assertEquals("42\n", client.readString("AN_ANSWER"));
    assertArrayEquals("42\n".getBytes(), client.readBytes("AN_ANSWER"));
}
```

Please refer to `SecretManagerTest` for further details.
