package de.zib.gndms.stuff;
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

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;import java.lang.Object;

/**
 * @author try ma ik jo rr a zib
 * @date 06.07.11  15:22
 * @brief
 */
public class GNDMSInjectorSpring implements GNDMSInjector {

    AutowireCapableBeanFactory factory;


    public GNDMSInjectorSpring( BeanFactory factory ) {
        setBeanFactory( factory );
    }


    public void injectMembers( Object existingBean ) {
        factory.autowireBean( existingBean );
    }


    @Override
    public <T> T getInstance( Class<T> clazz ) {
        return factory.getBean( clazz );
    }


    public void setBeanFactory( BeanFactory beanFactory ) throws BeansException {
        factory = AutowireCapableBeanFactory.class.cast( beanFactory );
    }
}
