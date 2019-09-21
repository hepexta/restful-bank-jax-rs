package com.hepexta.jaxrs.bank.ex;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class TransactionExceptionHandler implements ExceptionMapper<TransferException> {
    @Override
    public Response toResponse(TransferException ex){
        return Response
                .status(ex.getErrorCode(), ex.getLocalizedMessage())
                .entity(ex.getLocalizedMessage())
                .header("Message", ex.getLocalizedMessage())
                .build();
    }
}
