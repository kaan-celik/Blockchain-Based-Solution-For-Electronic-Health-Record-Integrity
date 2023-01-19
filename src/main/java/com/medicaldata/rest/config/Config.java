package com.medicaldata.rest.config;

import com.algorand.algosdk.v2.client.common.AlgodClient;
import com.algorand.algosdk.v2.client.common.IndexerClient;
import com.medicaldata.rest.business.SmartContractFactory;
import com.medicaldata.rest.business.service.*;
import com.medicaldata.rest.data.repository.AlgorandRepository;
import com.medicaldata.rest.data.repository.AlgorandRepositoryImp;
import com.medicaldata.rest.data.repository.DatabaseRepository;
import com.medicaldata.rest.data.repository.PostgresDatabaseRepository;
import com.medicaldata.rest.middleware.handler.RequestHandler;
import com.medicaldata.rest.middleware.converter.RequestConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class Config {

  @Bean
  public PostgresDatabaseRepository postgresDatabaseRepository(DataSource algoproject){
    return new PostgresDatabaseRepository(new NamedParameterJdbcTemplate(algoproject));
  }

  @Bean
  public RequestConverter requestConverter() {
    return new RequestConverter(new AccountCreatorService());
  }

  @Bean
  public AccountCreatorService accountCreatorService() {
    return new AccountCreatorService();
  }

  @Bean
  public TransactionService transactionService(AlgodClient algodClient,
                                               SmartContractUtilService smartContractUtilService) {
    return new TransactionService(algodClient,
            new SmartContractFactory(algodClient,new SmartContractGenerator()),
            new TransactionBuilder(smartContractUtilService));
  }

  @Bean
  public RequestHandler requestHandler(AlgorandRepository algorandRepository, DatabaseRepository databaseRepository) {
    return new RequestHandler(algorandRepository, databaseRepository);
  }

  @Bean
  public AlgodClient algodClient() {

    return new AlgodClient("https://testnet-algorand.api.purestake.io/ps2", 443, "");
//    AlgodClient algodClient = new AlgodClient("https://localhost", 8080, "5a645468dffe417d4ea0682b4ded3a58d2984dcef199a6bb7a70316ba42ac9f5");
  }

  @Bean
  public IndexerClient indexerClient() {
    return new IndexerClient("https://testnet-algorand.api.purestake.io/idx2", 443, "");
  }

  @Bean
  public AlgorandRepository algorandRepository(TransactionBuilder transactionBuilder,
                                               TransactionService transactionService,
                                               AccountCreatorService accountCreatorService,
                                               SmartContractUtilService smartContractUtilService,
                                               IndexerClient indexerClient) {
    return new AlgorandRepositoryImp(transactionBuilder,transactionService,
            accountCreatorService, smartContractUtilService, indexerClient);
  }


  @Bean
  public TransactionBuilder transactionBuilder (SmartContractUtilService smartContractUtilService){
    return new TransactionBuilder(smartContractUtilService);
  }

  @Bean
  public SmartContractUtilService algorandApplicationService (AlgodClient algodClient) {
    return new SmartContractUtilService(algodClient);
  }
}
