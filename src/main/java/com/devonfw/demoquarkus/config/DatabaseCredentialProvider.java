package com.devonfw.demoquarkus.config;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.arc.Unremovable;
import io.quarkus.credentials.CredentialsProvider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@Unremovable
// @Named("my-credentials-provider")
public class DatabaseCredentialProvider implements CredentialsProvider {

  @ConfigProperty(name = "quarkus.datasource.password")
  String password;

  @ConfigProperty(name = "quarkus.datasource.username")
  String username;

  @ConfigProperty(name = "foo.bar")
  String test;

  // @Inject
  // VaultKVSecretEngine kv;

  @Override
  public Map<String, String> getCredentials(String credentialsProviderName) {

    log.info("call DatabaseCredentialProvider.getCredentials");
    log.info(this.username + ", " + this.password + "," + this.test + ", "
        + ConfigProvider.getConfig().getConfigValue("foo.bar").getValue() + ", "
        + ConfigProvider.getConfig().getConfigValue("path").getValue());
    // log.info(this.kv.readSecret("myapps/vault-quickstart/db").get("a-private-key").toString());
    Map<String, String> properties = new HashMap<>();
    properties.put(USER_PROPERTY_NAME, this.test);
    properties.put(PASSWORD_PROPERTY_NAME, this.test);
    return properties;
  }

}
