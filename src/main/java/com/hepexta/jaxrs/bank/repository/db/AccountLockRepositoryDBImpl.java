package com.hepexta.jaxrs.bank.repository.db;

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

import static com.hepexta.jaxrs.bank.repository.db.Queries.QUERY_CBS_ACCOUNT_FIND_BY_ID_AND_LOCK;

public class AccountLockRepositoryDBImpl implements LockRepository<Account> {

    private final static Logger LOG = LoggerFactory.getLogger(AccountLockRepositoryDBImpl.class);
    private ResultSetMapper<Account> mapper = new AccountMapper();
    private static AccountLockRepositoryDBImpl INSTANCE;

    private AccountLockRepositoryDBImpl() {
    }

    public static AccountLockRepositoryDBImpl getINSTANCE(){
        if (INSTANCE==null){
            INSTANCE = new AccountLockRepositoryDBImpl();
        }
        return INSTANCE;
    }

    @Override
    public Account findByIdAndLock(String id) {
        Account accounts = null;
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = prepareFindByIdStmnt(conn, id);
             ResultSet resultSet =  stmt.executeQuery()){
            if (resultSet.next()) {
                accounts = mapper.map(resultSet);
            }
        } catch (SQLException e) {
            LOG.error("Error getting data", e);
        }
        return accounts;
    }

    private PreparedStatement prepareFindByIdStmnt(Connection conn, String id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(QUERY_CBS_ACCOUNT_FIND_BY_ID_AND_LOCK);
        stmt.setString(1, id);
        return stmt;
    }
}
