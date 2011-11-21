package de.zib.gndms.stuff.threading;
/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import de.zib.gndms.stuff.misc.LogProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author try ma ik jo rr a zib
 * @date 12.04.11  11:11
 * @brief
 */
public class TimedForkable<T> implements Callable<T> {

    private Callable<T> tCallable;
    private long timeout; // ms
    private final long wakeUp;
    private final TimeUnit wakeUpUnit;
    private LogProvider logger;


    public TimedForkable( @NotNull Callable<T> tCallable, long timeout, long wakeup, @NotNull TimeUnit wakeupUnit, LogProvider logger ) {
        this.tCallable = tCallable;
        this.timeout = timeout;
        this.wakeUp = wakeup;
        this.wakeUpUnit = wakeupUnit;
        this.logger = logger;
    }


    public TimedForkable( @NotNull Callable<T> tCallable, long timeout, LogProvider logger ) {
        this( tCallable, timeout, -1, TimeUnit.MILLISECONDS, logger );
    }


    public T call() throws Exception {

        long deadline = adjustDeadline( );
        if ( logger != null )
            logger.getLog().debug( "setting deadline to: "+ new Date( deadline ) );

        Forkable<T> fork;
        if( wakeUp == -1 )
            fork = new Forkable<T>( tCallable, deadline );
        else
            fork = new Forkable<T>( tCallable, deadline, wakeUp, wakeUpUnit );

        return fork.call();
    }


    private long adjustDeadline() {
        return System.currentTimeMillis() + timeout;
    }
}
