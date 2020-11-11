# Dapla Secret Manager Client

[SecretManagerClient](https://github.com/statisticsnorway/dapla-secrets-client-api/blob/master/src/main/java/no/ssb/dapla/secrets/api/SecretManagerClient.java) is a provider based API for reading secrets.

## Dependencies

Add `dapla-secrets-client-api` as a compile time dependency and choose the provider(s) you want to use.

```xml
<dependency>
    <groupId>no.ssb.dapla.secrets</groupId>
    <artifactId>dapla-secrets-client-api</artifactId>
    <version>0.4.0</version>
</dependency>

<dependency>
    <groupId>no.ssb.dapla.secrets</groupId>
    <artifactId>dapla-secrets-provider-safe-configuration</artifactId>
    <version>0.4.0</version>
    <scope>runtime</scope>
</dependency>

<dependency>
    <groupId>no.ssb.dapla.secrets</groupId>
    <artifactId>dapla-secrets-provider-google-secret-manager</artifactId>
    <version>0.2.0</version>
    <scope>runtime</scope>
</dependency>

<!-- dynamic-configuration is unsafe and should only be used for migration or test purposes -->
<dependency>
    <groupId>no.ssb.dapla.secrets</groupId>
    <artifactId>dapla-secrets-provider-dynamic-configuration</artifactId>
    <version>0.3.0</version>
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
     * @return a char buffer as utf8. If the input is null or empty, an empty char-array is returned.
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

### Dynamic configuration

Please note: only use this provider for migration or test purposes

Configuration for [Dynamic Configuration](https://github.com/statisticsnorway/dapla-secrets-provider-dynamic-configuration) provider:

Property                      | Description
------------------------------|----------------------------------------------
secrets.providerId            | dynamic-configuration (required)
secrets.propertyResourcePath  | full filepath to property resource (required)

### Safe configuration

Configuration for [Safe Configuration](https://github.com/statisticsnorway/dapla-secrets-provider-safe-configuration) provider:

Property                      | Description
------------------------------|----------------------------------------------
secrets.providerId            | safe-configuration (required)
secrets.propertyResourcePath  | full filepath to property resource (required)

### Google Secret Manager

Configuration for [Google Secret Manager](https://github.com/statisticsnorway/dapla-secrets-provider-google-secret-manager) provider:

Property                      | Description
------------------------------|----------------------------------------------
secrets.providerId            | google-secret-manager (required)
secrets.projectId             | team projectId (required)
secrets.serviceAccountKeyPath | (optional) full filepath to service-account file enables `service-account`, otherwise it defaults to `compute-engine`. When running on a cluster you SHOULD NOT use a json-key-file!


## Example of use:

1) google secret manager specific provider configuration

```java
Map<String, String> providerConfiguration = Map.of(
        "secrets.provider", "google-secret-manager",
        "secrets.projectId", "ssb-team-dapla",
        "secrets.serviceAccountKeyPath", "FULL_PATH_TO_SERVICE_ACCOUNT.json") // local testing only
);
```

When running on a cluster you SHOULD NOT add the property `secrets.serviceAccountKeyPath`!

2) create client and read secret

```java
try (SecretManagerClient client = SecretManagerClient.create(providerConfiguration)) {
    assertEquals("42\n", client.readString("AN_ANSWER"));
    assertArrayEquals("42\n".getBytes(), client.readBytes("AN_ANSWER"));
}
```

Please refer to [SecretManagerTest](https://github.com/statisticsnorway/dapla-secrets-provider-google-rest-api/blob/master/src/test/java/no/ssb/dapla/secrets/google/secretmanager/restapi/GoogleSecretManagerRestApiTest.java) for further details.
