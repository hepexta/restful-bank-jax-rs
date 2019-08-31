package com.hepexta.jaxrs.bank.service;

import com.hepexta.jaxrs.bank.model.Account;
import com.hepexta.jaxrs.bank.model.Client;
import com.hepexta.jaxrs.bank.repository.AccountFactory;
import com.hepexta.jaxrs.bank.repository.ClientFactory;
import com.hepexta.jaxrs.bank.repository.Repository;
import com.hepexta.jaxrs.util.AppConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;

@Path(AppConstants.PATH_ACCOUNT)
@Produces(MediaType.APPLICATION_JSON)
public class AccountService {

    private Repository<Account> accountRepository = AccountFactory.getAccountRepository();
    private Repository<Client> clientRepository = ClientFactory.getClientRepository();

    @GET
    @Path(AppConstants.PATH_LIST)
    public List<Account> getClients() {
        return accountRepository.getList();
    }

    @GET
    @Path(AppConstants.PATH_FIND_BY_ID)
    public Account getClientById(@QueryParam("id") String id) {
        return accountRepository.findById(id);
    }

    @DELETE
    @Path(AppConstants.PATH_DELETE)
    public Response deleteClient(@QueryParam("id") String number) {
        return accountRepository.delete(number)
                ? Response.status(Response.Status.OK).build()
                : Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @POST
    @Path(AppConstants.PATH_INSERT)
    public Account insertAccount(@QueryParam("clientId") String clientId, @QueryParam("balance") String balance) {
        Client client = clientRepository.findById(clientId);
        String accountId = accountRepository.insert(new Account(client, new BigDecimal(balance)));
        return accountRepository.findById(accountId);
    }

    @POST
    @Path(AppConstants.PATH_DEPOSIT)
    public Response deposit(@QueryParam("number") String number, @QueryParam("amount") BigDecimal amount) {
        Account account = accountRepository.findById(number);
        account.deposit(amount);
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path(AppConstants.PATH_WITHDRAWAL)
    public Response withdrawal(@QueryParam("number") String number, @QueryParam("amount") BigDecimal amount) {
        Account account = accountRepository.findById(number);
        account.withdraw(amount);
        return Response.status(Response.Status.OK).build();
    }
}
