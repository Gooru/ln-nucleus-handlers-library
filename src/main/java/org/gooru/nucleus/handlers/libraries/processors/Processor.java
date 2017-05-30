package org.gooru.nucleus.handlers.libraries.processors;

import org.gooru.nucleus.handlers.libraries.processors.responses.MessageResponse;

/**
 * @author szgooru Created On: 26-May-2017
 */
public interface Processor {
    MessageResponse process();
}
