package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.StudySessionDao;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class StudySessionService {

    private StudySessionDao sessionDao;
    private ExecutorService executor;
    static final Logger log = LogManager.getLogger();

    public StudySessionService(StudySessionDao sessionDao) {
        this.sessionDao = sessionDao;
        this.executor = Executors.newCachedThreadPool();  //is this needed for ours???
    }


}
