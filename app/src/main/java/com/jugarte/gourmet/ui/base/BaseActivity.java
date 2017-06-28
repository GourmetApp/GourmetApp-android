package com.jugarte.gourmet.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jugarte.gourmet.Application;
import com.jugarte.gourmet.di.component.ActivityComponent;
import com.jugarte.gourmet.di.component.DaggerActivityComponent;
import com.jugarte.gourmet.di.module.ActivityModule;

import butterknife.Unbinder;

public class BaseActivity extends AppCompatActivity {

    private Unbinder unBinder;
    private ActivityComponent activityComponent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(((Application) getApplication()).getComponent())
                .build();

    }

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }

    public void setUnBinder(Unbinder unBinder) {
        this.unBinder = unBinder;
    }

    @Override
    protected void onDestroy() {
        if (unBinder != null) {
            unBinder.unbind();
        }

        super.onDestroy();
    }
}
