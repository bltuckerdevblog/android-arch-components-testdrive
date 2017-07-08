package com.abnormallydriven.architecturecomponentspost.addmeasurements;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableFloat;
import android.support.annotation.NonNull;

import com.abnormallydriven.architecturecomponentspost.common.data.MeasurementDao;
import com.abnormallydriven.architecturecomponentspost.common.data.entities.Measurement;
import com.abnormallydriven.architecturecomponentspost.common.data.entities.User;
import com.abnormallydriven.architecturecomponentspost.common.di.Disk;
import com.abnormallydriven.architecturecomponentspost.common.di.UI;

import java.util.Date;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

//TODO tests

@Singleton
public class AddMeasurementsViewModel extends ViewModel {

    @NonNull
    private final Executor uiExecutor;

    @NonNull
    private final Executor diskExecutor;

    @NonNull
    private final MeasurementDao measurementDao;

    @NonNull
    public final ObservableBoolean shouldShowProgressSpinner;

    public final ObservableBoolean shouldShowSaveSuccessMessage;

    @Inject
    public AddMeasurementsViewModel(@UI @NonNull Executor uiExecutor,
                                    @Disk @NonNull Executor diskExecutor,
                                    MeasurementDao measurementDao) {
        this.uiExecutor = uiExecutor;
        this.diskExecutor = diskExecutor;
        this.measurementDao = measurementDao;

        shouldShowProgressSpinner = new ObservableBoolean(false);
        shouldShowSaveSuccessMessage = new ObservableBoolean(false);
    }

    void onSaveMeasurementForUserClick(final Measurement measurement) {
        shouldShowProgressSpinner.set(true);

        diskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                measurementDao.insertMeasurements(measurement);
                uiExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        shouldShowProgressSpinner.set(false);
                        shouldShowSaveSuccessMessage.set(true);
                    }
                });

            }
        });
    }
}
