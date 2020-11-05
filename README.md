# Dapla Secret Manager Client

[SecretManagerClient](src/main/java/no/ssb/dapla/secrets/api/SecretManagerClient.java) is a provider based API for reading secrets.

## Dependency

```xml
<dependency>
    <groupId>no.ssb.dapla.secrets</groupId>
    <artifactId>dapla-secrets-client-api</artifactId>
    <version>0.1.0</version>
</dependency>

<dependency>
    <groupId>no.ssb.dapla.secrets</groupId>
    <artifactId>dapla-secrets-provider-google-rest-api</artifactId>
    <version>0.1.0</version>
</dependency>
```

## API

```java
interface SecretManagerClient extends AutoCloseable {

    /**
     * Read secret
     */
    String readString(String secretName);

    /**
     * Read secret for a specific version
     */
    String readString(String secretName, String secretVersion);

    /**
     * Read secret
     */
    byte[] readBytes(String secretName);

    /**
     * Read secret for a specific version
     */
    byte[] readBytes(String secretName, String secretVersion);

    /**
     * Create SecretManagerClient for a 'secrets.provider'
     *
     * @param configuration Each provider requires their own properties
     * @return SecretManagerClient instance
     */
    static SecretManagerClient create(Map<String, String> configuration);
}
```

## Supported providers

* Dynamic Secret Provider (`DynamicSecretConfigurationClient`)
* Google Secret Manager Provider (`GoogleSecretManagerClient`)

The `GoogleSecretManagerClient` supports `service-account` and `compute-engine` authentication.

## Usage

1) google secret manager specific provider configuration

```java
Map<String, String> providerConfiguration = Map.of(
        "secrets.provider", "google-secret-manager",
        "secrets.projectId", "ssb-team-dapla",
        "secrets.serviceAccountKeyPath", "FULL_PATH_TO_SERVICE_ACCOUNT.json")
);
```

Please note: if you skip the `secrets.serviceAccountKeyPath` property, the SecretManagerClient will default to Workload Identity.

2) create client and read secret

```java
try (SecretManagerClient client = SecretManagerClient.create(providerConfiguration)) {
    assertEquals("42\n", client.readString("AN_ANSWER"));
    assertArrayEquals("42\n".getBytes(), client.readBytes("AN_ANSWER"));
}
```

Please refer to [SecretManagerTest](src/test/java/no/ssb/dapla/secrets/api/SecretManagerTest.java) for further details.
