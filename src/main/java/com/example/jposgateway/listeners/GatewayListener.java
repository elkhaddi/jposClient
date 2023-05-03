package com.example.jposgateway.listeners;

import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOSource;
import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;
import org.jpos.transaction.Context;
import org.jpos.transaction.ContextConstants;

public class GatewayListener implements ISORequestListener, Configurable {
    private Configuration configuration;
    @Override
    public void setConfiguration(Configuration configuration) throws ConfigurationException {
        this.configuration = configuration;
    }

    @Override
    public boolean process(ISOSource isoSource, ISOMsg isoMsg) {
        String space = configuration.get("space");
        String queue = configuration.get("queue");
        Long timeout = configuration.getLong("spaceTimeout");
        Space Qspace = SpaceFactory.getSpace(space);
        Context context = new Context();
        context.put(ContextConstants.REQUEST,isoMsg);
        context.put(ContextConstants.SOURCE,isoSource);
        Qspace.out(queue,context,timeout);
        return false;
    }
}
