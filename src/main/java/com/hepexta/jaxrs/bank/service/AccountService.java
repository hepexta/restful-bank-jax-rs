package com.hepexta.jaxrs.bank.service;

import com.hepexta.jaxrs.bank.ex.ErrorMessage;
import com.hepexta.jaxrs.bank.ex.TransferException;
import com.hepexta.jaxrs.bank.model.Account;
import com.hepexta.jaxrs.bank.model.Client;
import com.hepexta.jaxrs.bank.model.OperationAmount;
import com.hepexta.jaxrs.bank.repository.Repository;
import com.hepexta.jaxrs.util.AppConstants;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;

@Path(AppConstants.PATH_ACCOUNT)
@Produces(MediaType.APPLICATION_JSON)
public class AccountService {

    private final Repository<Account> accountRepository;
    private final Repository<Client> clientRepository;

    public AccountService(Repository<Account> accountRepository, Repository<Client> clientRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
    }

    @GET
    @Path(AppConstants.PATH_LIST)
    public List<Account> getAccounts() {
        return accountRepository.getList();
    }

    @GET
    @Path(AppConstants.PATH_FIND_BY_ID)
    public Account getClientById(@PathParam("id") String id) {
        return accountRepository.findById(id);
    }

    @DELETE
    @Path(AppConstants.PATH_DELETE)
    public Response deleteClient(@PathParam("id") String id) {
        Account account = accountRepository.findById(id);
        TransferException.shootIf(account == null, ErrorMessage.ERROR_520, id);
        return accountRepository.delete(id)
                ? Response.status(Response.Status.OK).build()
                : Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @POST
    @Path(AppConstants.PATH_INSERT)
    public Account insertAccount(Account account) {
        Client client = clientRepository.findById(account.getClient().getId());
        TransferException.shootIf(client == null, ErrorMessage.ERROR_522, account.getClient().getId());
        String accountId = accountRepository.insert(account);
        return accountRepository.findById(accountId);
    }

    @POST
    @Path(AppConstants.PATH_DEPOSIT)
    public Response deposit(OperationAmount operationAmount) {

        checkAmount(operationAmount.getAmount());
        Account account = accountRepository.findById(operationAmount.getId());

        account.setBalance(account.getBalance().add(operationAmount.getAmount()));
        accountRepository.modify(account);
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path(AppConstants.PATH_WITHDRAWAL)
    public Response withdrawal(OperationAmount operationAmount) {

        checkAmount(operationAmount.getAmount());
        Account account = accountRepository.findById(operationAmount.getId());
        checkBalance(operationAmount, account);

        account.setBalance(account.getBalance().subtract(operationAmount.getAmount()));
        accountRepository.modify(account);
        return Response.status(Response.Status.OK).build();
    }

    private void checkAmount(BigDecimal operationAmount) {
        TransferException.shootIf(operationAmount.compareTo(BigDecimal.ZERO)<=0, ErrorMessage.ERROR_521);
    }

    private void checkBalance(OperationAmount operationAmount, Account account) {
        TransferException.shootIf(account.getBalance().compareTo(operationAmount.getAmount())<0, ErrorMessage.ERROR_524, account.getId());
    }
}
