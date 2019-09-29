package com.hepexta.jaxrs.bank.repository.db;

import com.hepexta.jaxrs.bank.ex.ErrorMessage;
import com.hepexta.jaxrs.bank.ex.TransferException;
import com.hepexta.jaxrs.bank.model.Account;
import com.hepexta.jaxrs.bank.repository.Repository;
import com.hepexta.jaxrs.bank.repository.dao.mapper.AccountMapper;
import com.hepexta.jaxrs.bank.repository.dao.mapper.ResultSetMapper;
import com.hepexta.jaxrs.util.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.hepexta.jaxrs.bank.repository.db.Queries.QUERY_CBS_ACCOUNT_DELETE;
import static com.hepexta.jaxrs.bank.repository.db.Queries.QUERY_CBS_ACCOUNT_FIND_BY_ID;
import static com.hepexta.jaxrs.bank.repository.db.Queries.QUERY_CBS_ACCOUNT_GET_LIST;
import static com.hepexta.jaxrs.bank.repository.db.Queries.QUERY_CBS_ACCOUNT_INSERT;
import static com.hepexta.jaxrs.bank.repository.db.Queries.QUERY_CBS_ACCOUNT_UPDATE;
import static com.hepexta.jaxrs.util.DBUtils.getId;

public class AccountRepositoryDBImpl implements Repository<Account> {

    private static final Logger LOG = LoggerFactory.getLogger(AccountRepositoryDBImpl.class);
    private ResultSetMapper<Account> mapper = new AccountMapper();
    private static AccountRepositoryDBImpl instance;

    private AccountRepositoryDBImpl() {
    }

    public static AccountRepositoryDBImpl getInstance(){
        if (instance ==null){
            instance = new AccountRepositoryDBImpl();
        }
        return instance;
    }

    @Override
    public List<Account> getList() {
        List<Account> accounts = new ArrayList<>();
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(QUERY_CBS_ACCOUNT_GET_LIST);
             ResultSet resultSet =  stmt.executeQuery()){
            while (resultSet.next()) {
                accounts.add(mapper.map(resultSet));
            }
        } catch (SQLException e) {
            LOG.error("Error getting data", e);
        }
        return accounts;
    }

    @Override
    public Account findById(String id) {
        Account accounts = null;
        long timestamp = new Date().getTime();
        LOG.info("Account findById started:{} {}", id, timestamp);
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = prepareFindByIdStmnt(conn, id);
             ResultSet resultSet =  stmt.executeQuery()){
            if (resultSet.next()) {
                accounts = mapper.map(resultSet);
            }
        } catch (SQLException e) {
            LOG.error("Error getting data", e);
        }
        LOG.info("Account findById finishes:{} {}", id, timestamp);
        return accounts;
    }

    private PreparedStatement prepareFindByIdStmnt(Connection conn, String id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(QUERY_CBS_ACCOUNT_FIND_BY_ID);
        stmt.setString(1, id);
        return stmt;
    }

    @Override
    public String insert(Account model) {
        String result;
        try (Connection conn = DBUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(QUERY_CBS_ACCOUNT_INSERT, Statement.RETURN_GENERATED_KEYS)){
            stmt.setString(1, model.getNumber());
            stmt.setString(2, model.getClient().getId());
            stmt.setBigDecimal(3, model.getBalance());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                LOG.error("Error inserting account {}", model);
                throw new TransferException(ErrorMessage.ERROR_525);
            }
            result = getId(stmt);
        } catch (SQLException e) {
            LOG.error("Error inserting account {}", model);
            throw new TransferException(ErrorMessage.ERROR_525, e);
        }

        return result;
    }

    @Override
    public boolean modify(Account...accounts) {
        boolean result = false;
        LOG.info("Account modify started:{}", accounts);
        try (Connection conn = DBUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(QUERY_CBS_ACCOUNT_UPDATE)){
            conn.setAutoCommit(false);
            for (Account account : accounts) {
                if (Math.random()*10>9){
                    throw new SQLException("Fake Error");
                }
                stmt.setString(1, account.getNumber());
                stmt.setString(2, account.getClient().getId());
                stmt.setBigDecimal(3, account.getBalance());
                stmt.setString(4, account.getId());
                result = stmt.executeUpdate() == 1;
            }
            conn.commit();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            throw new TransferException(ErrorMessage.ERROR_534, e);
        }
        LOG.info("Account modify finish:{}", accounts);
        return result;
    }

    @Override
    public boolean delete(String id) {
        boolean result = false;
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = prepareDeleteStmnt(conn, id)){
            result = stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            LOG.error("Error deleting account", e);
        }
        return result;
    }

    private PreparedStatement prepareDeleteStmnt(Connection conn, String id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(QUERY_CBS_ACCOUNT_DELETE);
        stmt.setString(1, id);
        return stmt;
    }

}
