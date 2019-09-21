package com.hepexta.jaxrs.bank.repository.db;

import com.hepexta.jaxrs.bank.ex.ErrorMessage;
import com.hepexta.jaxrs.bank.ex.TransferException;
import com.hepexta.jaxrs.bank.model.Transaction;
import com.hepexta.jaxrs.bank.repository.dao.mapper.ResultSetMapper;
import com.hepexta.jaxrs.bank.repository.dao.mapper.TransactionMapper;
import com.hepexta.jaxrs.util.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.hepexta.jaxrs.bank.repository.db.Queries.QUERY_CBS_TRANSACTION_FIND_BY_ACCOUNT_ID;
import static com.hepexta.jaxrs.bank.repository.db.Queries.QUERY_CBS_TRANSACTION_INSERT;
import static com.hepexta.jaxrs.util.DBUtils.getId;

public class TransactionRepositoryDBImpl implements TransRepository<Transaction> {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionRepositoryDBImpl.class);
    private ResultSetMapper<Transaction> mapper = new TransactionMapper();
    private static TransactionRepositoryDBImpl instance;

    private TransactionRepositoryDBImpl() {
    }

    public static TransactionRepositoryDBImpl getInstance(){
        if (instance ==null){
            instance = new TransactionRepositoryDBImpl();
        }
        return instance;
    }

    @Override
    public List<Transaction> findByAccountId(String id) {
        List<Transaction> transactionList = new ArrayList<>();
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = prepareFindByIdStmnt(conn, id);
             ResultSet resultSet =  stmt.executeQuery()){
            while (resultSet.next()) {
                transactionList.add(mapper.map(resultSet));
            }
        } catch (SQLException e) {
            LOG.error("Error getting data", e);
        }
        return transactionList;
    }

    @Override
    public String insert(Transaction model) {
        String result;
        try (Connection conn = DBUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(QUERY_CBS_TRANSACTION_INSERT, Statement.RETURN_GENERATED_KEYS)){
            stmt.setString(1, model.getSourceAccountId());
            stmt.setString(2, model.getDestAccountId());
            stmt.setDate(3, java.sql.Date.valueOf(model.getOperDate()));
            stmt.setBigDecimal(4, model.getAmount());
            stmt.setString(5, model.getComment());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                LOG.error("Error inserting transaction {}", model);
                throw new TransferException(ErrorMessage.ERROR_527);
            }

            result = getId(stmt);
        } catch (SQLException e) {
            LOG.error("Error inserting transaction {}", model);
            throw new TransferException(ErrorMessage.ERROR_527, e);
        }

        return result;
    }

    private PreparedStatement prepareFindByIdStmnt(Connection conn, String id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(QUERY_CBS_TRANSACTION_FIND_BY_ACCOUNT_ID);
        stmt.setString(1, id);
        stmt.setString(2, id);
        return stmt;
    }


}
