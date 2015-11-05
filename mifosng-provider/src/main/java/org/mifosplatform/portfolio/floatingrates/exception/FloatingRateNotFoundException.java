package org.mifosplatform.portfolio.floatingrates.exception;

import org.mifosplatform.infrastructure.core.exception.AbstractPlatformResourceNotFoundException;

public class FloatingRateNotFoundException extends AbstractPlatformResourceNotFoundException {

    public FloatingRateNotFoundException(final Long id) {
        super("error.msg.floatingrate.id.invalid", "Floating Rate with identifier " + id + " does not exist", id);
    }

    public FloatingRateNotFoundException(final String globalisationMessageCode) {
        super(globalisationMessageCode, "Floating Rate does not exist");
    }
}
