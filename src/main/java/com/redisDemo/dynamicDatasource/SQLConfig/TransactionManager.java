package com.redisDemo.dynamicDatasource.SQLConfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.*;

import java.util.List;

/**
 * @author lovepreetsingh
 */
@Slf4j
@Component
public class TransactionManager implements PlatformTransactionManager {

    @Autowired
    @Qualifier("DataSourceTransactionManagerList")
    List<Pair<String, DataSourceTransactionManager>> managerList;

    @Autowired
    @Qualifier("dynamicDataSource")
    DynamicDataSource dynamicDataSource;

    private PlatformTransactionManager getCurrentManager() {
//        log.info("Getting Transaction manager ... ");
//        String dataSourceType = ShardConfigurator.getDataSourceType();
//        for(Pair<String, DataSourceTransactionManager> managerName : managerList) {
//            log.info("Getting Transaction manager ... " + managerName.getFirst());
//            if(managerName.getFirst().equalsIgnoreCase(dataSourceType)) {
//                return managerName.getSecond();
//            }
//        }
//        throw new IllegalStateException("No tx manager for id " + dataSourceType);
        return new DataSourceTransactionManager(dynamicDataSource);
    }

    @Override
    public TransactionStatus getTransaction(TransactionDefinition transactionDefinition) throws TransactionException {
        log.info("TransactionManager.getTransaction transactionDefinition : " + transactionDefinition);
        TransactionStatus transaction = this.getCurrentManager().getTransaction(transactionDefinition);
        return transaction;
    }

    @Override
    public void commit(TransactionStatus transactionStatus) throws TransactionException {
        log.info("TransactionManager.commit transactionDefinition : " + transactionStatus);
        this.getCurrentManager().commit(transactionStatus);
    }

    @Override
    public void rollback(TransactionStatus transactionStatus) throws TransactionException {
        log.info("TransactionManager.rollback transactionDefinition : " + transactionStatus);
        try {
            this.getCurrentManager().rollback(transactionStatus);
        } catch(IllegalTransactionStateException illegalTransactionStateException) {
            log.error("TransactionManager has already commited the txn");
        }
    }
}
