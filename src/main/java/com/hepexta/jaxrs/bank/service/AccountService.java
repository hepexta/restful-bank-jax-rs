package com.hepexta.jaxrs.bank.service;

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
    public List<Account> getClients() {
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
        if (account == null){
            throw new TransferException(String.format(TransferException.ACCOUNT_NOT_FOUND_BY_ID, id));
        }
        return accountRepository.delete(id)
                ? Response.status(Response.Status.OK).build()
                : Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @POST
    @Path(AppConstants.PATH_INSERT)
    public Account insertAccount(Account account) {
        Client client = clientRepository.findById(account.getClient().getId());
        if (client == null){
            throw new TransferException(String.format(TransferException.CLIENT_NOT_FOUND_BY_ID, account.getClient().getId()));
        }
        String accountId = accountRepository.insert(account);
        return accountRepository.findById(accountId);
    }

    @POST
    @Path(AppConstants.PATH_DEPOSIT)
    public Response deposit(OperationAmount operationAmount) {
        Account account = accountRepository.findById(operationAmount.getId());
        account.deposit(operationAmount.getAmount());
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path(AppConstants.PATH_WITHDRAWAL)
    public Response withdrawal(OperationAmount operationAmount) {
        Account account = accountRepository.findById(operationAmount.getId());
        account.withdraw(operationAmount.getAmount());
        return Response.status(Response.Status.OK).build();
    }
}
