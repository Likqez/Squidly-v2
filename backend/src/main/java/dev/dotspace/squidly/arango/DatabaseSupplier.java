package dev.dotspace.squidly.arango;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.arangodb.DbName;
import com.arangodb.mapping.ArangoJack;
import dev.dotspace.squidly.CredentialPair;

import java.util.function.Supplier;

public class DatabaseSupplier implements Supplier<ArangoDatabase> {

  private String arangoDatabase;
  private String arangoHost;
  private int arangoPort;
  private CredentialPair credentialPair;
  private ArangoDB arangoClient;

  public ArangoDB load() {
    this.arangoHost = System.getenv().getOrDefault("squidly_data_host", "127.0.0.1");
    this.arangoPort = Integer.parseInt(System.getenv().getOrDefault("squidly_data_port", "8529"));
    this.arangoDatabase = System.getenv().getOrDefault("squidly_data_database", "squidly");

    this.credentialPair = new CredentialPair(
        System.getenv().getOrDefault("squidly_data_user", "squidly"),
        System.getenv().getOrDefault("squidly_data_passwd", "")
    );

    this.arangoClient = this.buildClient();
    return this.arangoClient;
  }

  private ArangoDB buildClient() {
    return new ArangoDB.Builder()
        .user(this.credentialPair.user())
        .password(this.credentialPair.auth())
        .host(this.arangoHost, this.arangoPort)
        .serializer(new ArangoJack())
        .build();
  }

  @Override
  public ArangoDatabase get() {
    return this.getClient().db(DbName.normalize(this.arangoDatabase));
  }

  public ArangoDB getClient() {
    return arangoClient != null ? arangoClient : load();
  }

}
