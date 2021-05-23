package com.laboschqpa.imageconverter.model;

import com.laboschqpa.imageconverter.exceptions.TooManyJobsException;
import lombok.RequiredArgsConstructor;

import java.util.LinkedList;

@RequiredArgsConstructor
public class VariantCreationJobQueue {
    private final int maxQueueLength;

    private final LinkedList<ProcessCreationJobCommand> jobQueue = new LinkedList<>();

    public void enqueue(ProcessCreationJobCommand jobCommand) {
        final boolean acceptJob;
        synchronized (jobQueue) {
            if (jobQueue.size() < maxQueueLength) {
                jobQueue.addLast(jobCommand);
                acceptJob = true;
            } else {
                acceptJob = false;
            }
        }

        if (!acceptJob) {
            throw new TooManyJobsException();
        }
    }

    public ProcessCreationJobCommand pop() {
        synchronized (jobQueue) {
            return jobQueue.pop();
        }
    }

    public int getCurrentLength() {
        synchronized (jobQueue) {
            return jobQueue.size();
        }
    }

    public boolean isEmpty() {
        synchronized (jobQueue) {
            return jobQueue.isEmpty();
        }
    }
}
