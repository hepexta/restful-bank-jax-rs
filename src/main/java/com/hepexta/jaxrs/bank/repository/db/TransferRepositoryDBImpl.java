package com.hepexta.jaxrs.bank.repository.db;

import com.hepexta.jaxrs.bank.ex.ErrorMessage;
import com.hepexta.jaxrs.bank.ex.TransferException;
import com.hepexta.jaxrs.bank.model.Transfer;
import com.hepexta.jaxrs.bank.model.TransferStatus;
import com.hepexta.jaxrs.bank.repository.dao.mapper.ResultSetMapper;
import com.hepexta.jaxrs.bank.repository.dao.mapper.TransferMapper;
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

import static com.hepexta.jaxrs.bank.repository.db.Queries.QUERY_CBS_TRANSACTION_FIND_BY_ACCOUNT_ID;
import static com.hepexta.jaxrs.bank.repository.db.Queries.QUERY_CBS_TRANSACTION_INSERT;
import static com.hepexta.jaxrs.bank.repository.db.Queries.QUERY_CBS_TRANSACTION_UPDATE_STATUS;
import static com.hepexta.jaxrs.util.DBUtils.getId;

public class TransferRepositoryDBImpl implements TransRepository<Transfer> {

    private static final Logger LOG = LoggerFactory.getLogger(TransferRepositoryDBImpl.class);
    private ResultSetMapper<Transfer> mapper = new TransferMapper();
    private static TransferRepositoryDBImpl instance;

    private TransferRepositoryDBImpl() {
    }

    public static TransferRepositoryDBImpl getInstance(){
        if (instance ==null){
            instance = new TransferRepositoryDBImpl();
        }
        return instance;
    }

    @Override
    public List<Transfer> findByAccountId(String accountId) {
        List<Transfer> transferList = new ArrayList<>();
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement stmt = prepareFindByIdStmnt(conn, accountId);
             ResultSet resultSet =  stmt.executeQuery()){
            while (resultSet.next()) {
                transferList.add(mapper.map(resultSet));
            }
        } catch (SQLException e) {
            LOG.error("Error getting data", e);
        }
        return transferList;
    }

    @Override
    public String insert(Transfer model) {
        String result;
        try (Connection conn = DBUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(QUERY_CBS_TRANSACTION_INSERT, Statement.RETURN_GENERATED_KEYS)){
            stmt.setString(1, model.getSourceAccountId());
            stmt.setString(2, model.getDestAccountId());
            stmt.setDate(3, java.sql.Date.valueOf(model.getOperDate()));
            stmt.setBigDecimal(4, model.getAmount());
            stmt.setString(5, model.getComment());
            stmt.setString(6, TransferStatus.NEW.getStatus());
            stmt.setString(7, String.format(TransferStatus.NEW.getMessage(), new Date()));
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

    @Override
    public void updateStatus(String id, TransferStatus status, String... params) {
        LOG.info("UpdateStatus started:{}", id);
        try (Connection conn = DBUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(QUERY_CBS_TRANSACTION_UPDATE_STATUS)){
            stmt.setString(1, status.getStatus());
            stmt.setString(2, String.format(status.getMessage(), params));
            stmt.setString(3, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new TransferException(ErrorMessage.ERROR_534, e);
        }
        LOG.info("UpdateStatus finish:{}", id);
    }

    private PreparedStatement prepareFindByIdStmnt(Connection conn, String id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(QUERY_CBS_TRANSACTION_FIND_BY_ACCOUNT_ID);
        stmt.setString(1, id);
        stmt.setString(2, id);
        return stmt;
    }


}
