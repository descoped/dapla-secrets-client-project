# Dapla Secret Manager Client

`SecretManagerClient` is a provider based API for reading secrets.

Supported providers are:

* Dynamic Secret Provider (`DynamicSecretConfigurationClient`)
* Google Secret Manager Provider (`GoogleSecretManagerClient`)

The `GoogleSecretManagerClient` supports `service-account` and `compute-engine` authentication.

Usage:

1) google secret manager specific provider configuration
```
Map<String, String> providerConfiguration = Map.of(
        "secrets.provider", "google-secret-manager",
        "secrets.projectId", "ssb-team-dapla",
        "secrets.serviceAccountKeyPath", "FULL_PATH_TO_SERVICE_ACCOUNT.json")
);
```

2) create client and read secret
```
try (SecretManagerClient client = SecretManagerClient.create(providerConfiguration)) {
    assertEquals("42\n", client.readString("AN_ANSWER"));
    assertArrayEquals("42\n".getBytes(), client.readBytes("AN_ANSWER"));
}
```

Please refer to [`SecretManagerTest`](src/test/java/no/ssb/dapla/secrets/api/SecretManagerTest.java) for further details.
