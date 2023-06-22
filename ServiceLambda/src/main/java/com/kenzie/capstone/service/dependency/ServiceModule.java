package com.kenzie.capstone.service.dependency;

import com.kenzie.capstone.service.LambdaService;
import com.kenzie.capstone.service.StudySessionService;
import com.kenzie.capstone.service.dao.ExampleDao;

import com.kenzie.capstone.service.dao.StudySessionDao;
import dagger.Module;
import dagger.Provides;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Module(
    includes = DaoModule.class
)
public class ServiceModule {

    @Singleton
    @Provides
    @Inject
    public StudySessionService provideStudySessionService(@Named("StudySessionDao")StudySessionDao sessionDao) {
        return new StudySessionService(sessionDao);
    }
}

