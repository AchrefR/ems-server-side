package com.ppg.ems_server_side_v0.config.migration;

import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

@Component
public class Notifier extends ApplicationEvent{

    public Notifier()
    {
        super(true);
    }

}
