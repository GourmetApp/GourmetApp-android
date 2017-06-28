package com.jugarte.gourmet.di.component;

import com.jugarte.gourmet.Application;
import com.jugarte.gourmet.di.module.ApplicationModule;


import dagger.Component;

@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(Application application);

}
