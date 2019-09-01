package com.hepexta.jaxrs.bank.service;

import com.hepexta.jaxrs.bank.ex.TransferException;
import com.hepexta.jaxrs.bank.model.Client;
import com.hepexta.jaxrs.bank.repository.Repository;
import com.hepexta.jaxrs.util.AppConstants;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path(AppConstants.PATH_CLIENT)
@Produces(MediaType.APPLICATION_JSON)
public class ClientService {

    private Repository<Client> clientRepository;

    public ClientService(Repository<Client> clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GET
    @Path(AppConstants.PATH_LIST)
    public List<Client> getClients() {
        return clientRepository.getList();
    }

    @GET
    @Path(AppConstants.PATH_FIND_BY_ID)
    public Client getClientById(@PathParam("id") String id) {
        return clientRepository.findById(id);
    }

    @DELETE
    @Path(AppConstants.PATH_DELETE)
    public Response deleteClient(@PathParam("id") String clientId) {
        return clientRepository.delete(clientId)
                ? Response.status(Response.Status.OK).build()
                : Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @POST
    @Path(AppConstants.PATH_INSERT)
    @Consumes(MediaType.APPLICATION_JSON)
    public Client insertClient(Client client) {
        String insertedClientId = clientRepository.insert(client);
        return clientRepository.findById(insertedClientId);
    }

    @PUT
    @Path(AppConstants.PATH_MODIFY)
    public Response modifyClient(@PathParam("id") String id, Client newClient) {
        Client client = clientRepository.findById(id);
        if (client == null){
            throw new TransferException(String.format(TransferException.CLIENT_NOT_FOUND_BY_ID, id));
        }
        newClient.setId(id);
        return clientRepository.modify(newClient)
                ? Response.status(Response.Status.OK).build()
                : Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
