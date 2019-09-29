package com.hepexta.jaxrs.bank.service;

import com.hepexta.jaxrs.bank.ex.ErrorMessage;
import com.hepexta.jaxrs.bank.ex.TransferException;
import com.hepexta.jaxrs.bank.model.Account;
import com.hepexta.jaxrs.bank.model.Transfer;
import com.hepexta.jaxrs.bank.repository.Repository;
import com.hepexta.jaxrs.bank.repository.db.LockRepository;
import com.hepexta.jaxrs.bank.repository.db.TransRepository;
import com.hepexta.jaxrs.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;

@Path(AppConstants.PATH_TRANSACTION)
@Produces(MediaType.APPLICATION_JSON)
public class TransferService {

    private static final Logger LOG = LoggerFactory.getLogger(TransferService.class);
    private final Repository<Account> accountRepository;
    private final LockRepository<Account> accountLockRepository;
    private final TransRepository<Transfer> transactionRepository;

    public TransferService(Repository<Account> accountRepository, LockRepository<Account> accountLockRepository, TransRepository<Transfer> transactionRepository) {
        this.accountRepository = accountRepository;
        this.accountLockRepository = accountLockRepository;
        this.transactionRepository = transactionRepository;
    }

    @GET
    @Path(AppConstants.PATH_FIND_BY_ACCOUNT_ID)
    public List<Transfer> findByAccountId(@PathParam("id") String id) {
        return transactionRepository.findByAccountId(id);
    }

    @POST
    @Path(AppConstants.PATH_EXECUTE)
    public Response executeTransfer(Transfer transfer) {

        LOG.info("executeTransfer started:{}", transfer);
        checkInput(transfer);

        final Account sourceAccount = accountLockRepository.findByIdAndLock(transfer.getSourceAccountId());
        final Account destAccount = accountLockRepository.findByIdAndLock(transfer.getDestAccountId());
        try {
            TransferException.throwIf(sourceAccount == null, ErrorMessage.ERROR_529, transfer.getSourceAccountId());
            TransferException.throwIf(transfer.getAmount().compareTo(sourceAccount.getBalance()) > 0, ErrorMessage.ERROR_531, sourceAccount.getId());
            TransferException.throwIf(destAccount == null, ErrorMessage.ERROR_530, transfer.getDestAccountId());

            transactionRepository.insert(transfer);

            sourceAccount.setBalance(sourceAccount.getBalance().subtract(transfer.getAmount()));
            destAccount.setBalance(destAccount.getBalance().add(transfer.getAmount()));

            accountRepository.modify(sourceAccount);
            accountRepository.modify(destAccount);
        }
        finally {
            accountLockRepository.unlock(transfer.getSourceAccountId());
            accountLockRepository.unlock(transfer.getDestAccountId());
        }
        LOG.info("executeTransfer finished:{}", transfer);
        return Response.status(Response.Status.OK).build();
    }

    private void checkInput(Transfer transfer) {
        TransferException.throwIf(transfer.getAmount().compareTo(BigDecimal.ZERO)<=0, ErrorMessage.ERROR_521);
        TransferException.throwIf(transfer.getSourceAccountId().equals(transfer.getDestAccountId()), ErrorMessage.ERROR_528);
    }

}
