# Dapla Secret Manager Client

[SecretManagerClient](https://github.com/statisticsnorway/dapla-secrets-client-api/blob/master/src/main/java/no/ssb/dapla/secrets/api/SecretManagerClient.java) is a provider based API for reading secrets.

## Dependencies

Add `dapla-secrets-client-api` as a compile time dependency and choose the provider(s) you want to use.
Please note that `dapla-secrets-provider-dynamic-configuration` is only offered as a convenience library and should not be used in production,
because secrets are loaded as Strings in heap.

```xml
<dependency>
    <groupId>no.ssb.dapla.secrets</groupId>
    <artifactId>dapla-secrets-client-api</artifactId>
    <version>0.2.0</version>
</dependency>

<dependency>
    <groupId>no.ssb.dapla.secrets</groupId>
    <artifactId>dapla-secrets-provider-safe-configuration</artifactId>
    <version>0.4.0</version>
    <scope>runtime</scope>
</dependency>

<dependency>
    <groupId>no.ssb.dapla.secrets</groupId>
    <artifactId>dapla-secrets-provider-dynamic-configuration</artifactId>
    <version>0.3.0</version>
    <scope>runtime</scope>
</dependency>

<dependency>
    <groupId>no.ssb.dapla.secrets</groupId>
    <artifactId>dapla-secrets-provider-google-secret-manager</artifactId>
    <version>0.2.0</version>
    <scope>runtime</scope>
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
     * Convenience method that converts byte-array to char-array as UTF-8.
     *
     * Please notice: The input byte-array will be cleared after conversion.
     *
     * @param bytes input buffer
     * @return characters array as utf8. Return an empty char-array if input is null or empty
     *         the user is responsible for clearing a char-array copy.
     */
    static char[] safeCharArrayAsUTF8(final byte[] bytes);

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

* Safe Configuration (`SafeConfigurationClient`)
* Dynamic Configuration (`DynamicSecretConfigurationClient`) - test pruposes only
* Google Secret Manager (`GoogleSecretManagerClient`)

The `GoogleSecretManagerClient` supports `service-account` and `compute-engine` (default) authentication.

## Usage

1) google secret manager specific provider configuration

```java
Map<String, String> providerConfiguration = Map.of(
        "secrets.provider", "google-secret-manager",
        "secrets.projectId", "ssb-team-dapla",
        "secrets.serviceAccountKeyPath", "FULL_PATH_TO_SERVICE_ACCOUNT.json") // local testing only
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

Please refer to [SecretManagerTest](https://github.com/statisticsnorway/dapla-secrets-provider-google-rest-api/blob/master/src/test/java/no/ssb/dapla/secrets/google/secretmanager/restapi/GoogleSecretManagerRestApiTest.java) for further details.
