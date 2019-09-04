package com.hepexta.jaxrs.bank.service;

import com.hepexta.jaxrs.bank.ex.TransferException;
import com.hepexta.jaxrs.bank.model.Account;
import com.hepexta.jaxrs.bank.model.Transaction;
import com.hepexta.jaxrs.bank.repository.db.LockRepository;
import com.hepexta.jaxrs.bank.repository.Repository;
import com.hepexta.jaxrs.bank.repository.db.TransRepository;
import com.hepexta.jaxrs.util.AppConstants;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path(AppConstants.PATH_TRANSACTION)
@Produces(MediaType.APPLICATION_JSON)
public class TransactionService {

    private final Repository<Account> accountRepository;
    private final LockRepository<Account> accountLockRepository;
    private final TransRepository<Transaction> transactionRepository;

    public TransactionService(Repository<Account> accountRepository, LockRepository<Account> accountLockRepository, TransRepository<Transaction> transactionRepository) {
        this.accountRepository = accountRepository;
        this.accountLockRepository = accountLockRepository;
        this.transactionRepository = transactionRepository;
    }

    @GET
    @Path(AppConstants.PATH_FIND_BY_ACCOUNT_ID)
    public List<Transaction> findByAccountId(@PathParam("id") String id) {
        return transactionRepository.findByAccountId(id);
    }

    @POST
    @Path(AppConstants.PATH_EXECUTE)
    public Response executeTransaction(Transaction transfer) {
        final Account sourceAccount = accountLockRepository.findByIdAndLock(transfer.getSourceAccountId());
        final Account destAccount = accountLockRepository.findByIdAndLock(transfer.getDestAccountId());

        checkTransaction(transfer, sourceAccount, destAccount);

        transactionRepository.insert(transfer);

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(transfer.getAmount()));
        destAccount.setBalance(destAccount.getBalance().add(transfer.getAmount()));

        accountRepository.modify(sourceAccount);
        accountRepository.modify(destAccount);

        return Response.status(Response.Status.OK).build();
    }

    private void checkTransaction(Transaction transfer, Account sourceAccount, Account destAccount) {
        if (sourceAccount == null) {
            throw new TransferException(String.format("Source account not found by ID %s",transfer.getSourceAccountId()));
        }

        if (destAccount == null) {
            throw new TransferException(String.format("Destination account not found ID %s", transfer.getSourceAccountId()));
        }

        if (transfer.getAmount().compareTo(sourceAccount.getBalance())>0){
            throw new TransferException("Not enough funds for transfer");
        }
    }
}
