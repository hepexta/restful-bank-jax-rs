package com.hepexta.jaxrs.bank.ex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class TransactionExceptionHandler implements ExceptionMapper<TransferException> {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionExceptionHandler.class);

    @Override
    public Response toResponse(TransferException ex){
        LOG.error("Error", ex);
        return Response
                .status(ex.getErrorCode(), ex.getLocalizedMessage())
                .entity(ex.getLocalizedMessage())
                .header("Message", ex.getLocalizedMessage())
                .build();
    }
}
