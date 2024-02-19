package com.eidiko.monitor;

import java.io.IOException;
import java.nio.file.*;


public class FolderMonitor {

    private final Path inputFolder;
    private final Path outputFolder;

    public FolderMonitor(Path inputFolder, Path outputFolder) {
        this.inputFolder = inputFolder;
        this.outputFolder = outputFolder;
    }

    public void startMonitoring() throws IOException, InterruptedException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        inputFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

        while (true) {
            WatchKey key = watchService.take();
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                if (kind == StandardWatchEventKinds.OVERFLOW) {
                    continue;
                }
                @SuppressWarnings("unchecked")
				WatchEvent<Path> ev = (WatchEvent<Path>) event;
                Path fileName = ev.context();
                Path inputFile = inputFolder.resolve(fileName);
                Path outputFile = outputFolder.resolve(fileName);

                try {
                    Files.copy(inputFile, outputFile, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("File copied: " + inputFile);
                } catch (IOException e) {
                    System.err.println("Error copying file: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            key.reset();
        }
    }
}

