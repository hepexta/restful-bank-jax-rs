package com.hepexta.jaxrs.bank.repository.db;

import com.hepexta.jaxrs.bank.ex.ErrorMessage;
import com.hepexta.jaxrs.bank.ex.TransferException;
import com.hepexta.jaxrs.bank.model.Account;
import com.hepexta.jaxrs.bank.repository.dao.mapper.AccountLockMapper;
import com.hepexta.jaxrs.bank.repository.dao.mapper.ResultSetMapper;
import com.hepexta.jaxrs.util.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.hepexta.jaxrs.bank.repository.db.Queries.QUERY_CBS_ACCOUNT_FIND_BY_ID_AND_LOCK;

public class AccountLockRepositoryDBImpl implements LockRepository<Account> {

    private static final Logger LOG = LoggerFactory.getLogger(AccountLockRepositoryDBImpl.class);
    private ResultSetMapper<Account> mapper = new AccountLockMapper();
    private static AccountLockRepositoryDBImpl instance;
    private final Set<String> localCache = new CopyOnWriteArraySet<>();

    private AccountLockRepositoryDBImpl() {
    }

    public static AccountLockRepositoryDBImpl getInstance(){
        if (instance ==null){
            instance = new AccountLockRepositoryDBImpl();
        }
        return instance;
    }

    @Override
    public Account findByIdAndLock(String id) {
        Account account = null;
        long timestamp = new Date().getTime();
        LOG.info("Account findById and Lock started:{} {}", id, timestamp);
        TransferException.throwIf(putIfNotPresent(id), ErrorMessage.ERROR_533, id);
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = prepareFindByIdStmnt(conn, id);
             ResultSet resultSet =  stmt.executeQuery()){
            if (resultSet.next()) {
                account = mapper.map(resultSet);
            }
        } catch (SQLException e) {
            LOG.error("Error getting data", e);
        }
        LOG.info("Account findById and Lock finished:{} {}", account, timestamp);
        return account;
    }

    @Override
    public void unlock(String id) {
        localCache.remove(id);
    }

    private boolean putIfNotPresent(String id) {
        LOG.info("LocalCache {}", localCache);
        boolean b = localCache.contains(id);
        if (!b){
            localCache.add(id);
        }
        return b;
    }

    private PreparedStatement prepareFindByIdStmnt(Connection conn, String id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(QUERY_CBS_ACCOUNT_FIND_BY_ID_AND_LOCK);
        stmt.setString(1, id);
        return stmt;
    }
}
