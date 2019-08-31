package com.hepexta.jaxrs.bank.service;

import com.hepexta.jaxrs.bank.ex.TransferException;
import com.hepexta.jaxrs.bank.model.Account;
import com.hepexta.jaxrs.bank.model.Client;
import com.hepexta.jaxrs.bank.repository.AccountFactory;
import com.hepexta.jaxrs.bank.repository.ClientFactory;
import com.hepexta.jaxrs.bank.repository.Repository;
import com.hepexta.jaxrs.util.AppConstants;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
    public Response deleteClient(@PathParam("id") String number) {
        return accountRepository.delete(number)
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
