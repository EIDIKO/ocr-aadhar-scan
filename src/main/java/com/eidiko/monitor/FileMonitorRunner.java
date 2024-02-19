package com.eidiko.monitor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileMonitorRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        Path inputFolder = Paths.get("C:\\Users\\tonda\\Desktop\\SAMPLE\\INPUT");
        Path outputFolder = Paths.get("C:\\Users\\tonda\\Desktop\\SAMPLE\\PROCESS");

        FolderMonitor folderMonitor = new FolderMonitor(inputFolder, outputFolder);
        folderMonitor.startMonitoring();
    }
}

